package com.epf.rentmanager.service;
import com.epf.rentmanager.dao.ReservationDao;
import exception.DaoException;
import exception.ServiceException;
import model.Reservation;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.Period;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
@Service
public class ReservationService {
    private ReservationDao reservationDao;
    private ReservationService(ReservationDao reservationDao){
        this.reservationDao = reservationDao;
    }
    public long countRents() throws ServiceException{
        try{
            return reservationDao.countRents();
        }catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }
    public long create(Reservation reservation) throws ServiceException, DaoException {

        Period period = Period.between(reservation.getDebut(), reservation.getFin());

        if(period.getDays() > 7){
           throw new ServiceException("Une voiture ne peux pas être réservée plus de 7 jours de suite par le même utilisateur");
        }

        if(dejaReserver(reservation.getId(), reservation.getDebut(), reservation.getFin())){
            throw new ServiceException("Cette voiture a déjà été réservée sur cette période.\n");
        }
        if(reservation30Jours(reservation.getId(), reservation.getDebut(), reservation.getFin())){
            throw new ServiceException("Une voiture ne peux pas être réservée 30 jours de suite sans pause\n.\n");
        }
        try {
            return reservationDao.create(reservation);
        } catch (DaoException e) {
            throw new ServiceException("Une erreur a eu lieu lors de la création de la réservation.\n");
        }
    }

    public boolean dejaReserver(long carId, LocalDate dateDebut, LocalDate dateFin) throws DaoException {
        try {
            Optional<List<Reservation>> optionalReservations = reservationDao.findResaByVehicleId(carId);

            if (optionalReservations.isPresent()) {
                List<Reservation> reservations = optionalReservations.get();

                for (Reservation reservation : reservations) {
                    LocalDate debut = reservation.getDebut();
                    LocalDate fin = reservation.getFin();
                    if (dateDebut.isBefore(fin) && dateFin.isAfter(debut) || dateDebut.isEqual(debut) || dateFin.isEqual(fin) || dateFin.isEqual(debut) || dateDebut.isEqual(fin)) {
                        return true;
                    }
                }
            }
            return false;
        } catch (DaoException e) {
            throw new DaoException("Une erreur s'est produite lors de la recherche de réservations pour la voiture et la période spécifiées.");
        }
    }

    public class ReservationComparator implements Comparator<Reservation> {
        @Override
        public int compare(Reservation r1, Reservation r2) {
            return r1.getDebut().compareTo(r2.getDebut());
        }
    }

    public boolean reservation30Jours(long carId, LocalDate dateDebut, LocalDate dateFin) throws DaoException {
        try {
            Optional<List<Reservation>> optionalReservations = reservationDao.findResaByVehicleId(carId);

            if (optionalReservations.isPresent()) {
                List<Reservation> reservations = optionalReservations.get();

                reservations.sort(new ReservationComparator());
                LocalDate dateFinPrecedente = null;
                int periodeTotaleReservee = 0;

                for (Reservation reservation : reservations) {
                    LocalDate finReservation = reservation.getFin();
                    LocalDate debutReservation = reservation.getDebut();


                    if (dateFinPrecedente != null && dateFinPrecedente.plusDays(1).isEqual(debutReservation)) {
                        Period period = Period.between(debutReservation, finReservation);
                        periodeTotaleReservee += period.getDays();
                    }
                    dateFinPrecedente = finReservation;
                }
                return periodeTotaleReservee>=30;
            }
            return false;

        } catch (DaoException e) {
            throw new DaoException("Une erreur s'est produite lors de la recherche de réservations pour la voiture et la période spécifiées.");
        }
    }


    public long delete(Reservation reservation) throws ServiceException {
        try {
            return reservationDao.delete(reservation);
        } catch (DaoException e) {
            throw new ServiceException("Une erreur a eu lieu lors de la suppression de la réservation.\n");
        }
    }

    public List<Reservation> findResaByClientId(long clientId) throws ServiceException {
        try {
            Optional<List<Reservation>> reservationOptional = reservationDao.findResaByClientId(clientId);

            if (reservationOptional.isPresent()) {
               return reservationOptional.get();
            } else {
                throw new ServiceException("Aucune réservation ne correspond à l'id client : "+clientId+".\n");
            }
        } catch (DaoException e) {
            throw new ServiceException("Une erreur a eu lieu lors de la récupération de la réservation par l'id du client.\n");
        }
    }
    public List<Reservation> findResaByVehicleId(long vehicleId) throws ServiceException {
        try {
            Optional<List<Reservation>>  reservationOptional = reservationDao.findResaByVehicleId(vehicleId);

            if (reservationOptional.isPresent()) {
                return reservationOptional.get();
            } else {
                throw new ServiceException("Aucune réservation ne correspond à l'id du véhicule : "+vehicleId+".\n");
            }
        } catch (DaoException e) {
            throw new ServiceException("Une erreur a eu lieu lors de la récupération de la réservation par l'id du véhicule.\n");
        }
    }
    public List<Reservation> findAll() throws ServiceException {
        try {
            Optional<List<Reservation>> optionalReservations = reservationDao.findAll();
            if (optionalReservations.isPresent()) {
                return optionalReservations.get();
            } else {
                throw new ServiceException("Aucune réservation trouvée dans la liste.\n");
            }
        } catch (DaoException e) {
            throw new ServiceException("Une erreur a eu lieu lors de la récupération des réservations.\n");
        }
    }
}
