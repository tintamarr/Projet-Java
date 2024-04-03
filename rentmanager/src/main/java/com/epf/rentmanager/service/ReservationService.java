package com.epf.rentmanager.service;
import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.dao.ReservationDao;
import exception.DaoException;
import exception.ServiceException;
import model.Client;
import model.Reservation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class ReservationService {
    private ReservationDao reservationDao;
    private ReservationService(ReservationDao reservationDao){
        this.reservationDao = reservationDao;
    }

    public long create(Reservation reservation) throws ServiceException {
        try {
            return reservationDao.create(reservation);
        } catch (DaoException e) {
            throw new ServiceException("Une erreur a eu lieu lors de la création de la réservation.\n");
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
