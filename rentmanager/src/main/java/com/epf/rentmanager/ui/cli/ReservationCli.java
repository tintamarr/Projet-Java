package com.epf.rentmanager.ui.cli;
import com.epf.rentmanager.service.ReservationService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import exception.ServiceException;
import model.Client;
import model.Reservation;
import model.Vehicle;

import javax.sound.midi.SysexMessage;


public class ReservationCli {
    private ReservationService reservationService;
    public ReservationCli(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    public void CreateReservation() throws ServiceException {
        Scanner sc = new Scanner(System.in);
        Reservation reservation;

        try{
            System.out.println("Création d'une nouvelle réservation:");
            System.out.println("Entrez l'id du client associé à la réservation que vous souhaitez créer :");
            String idClientSaisi = sc.nextLine();

            while(idClientSaisi.isEmpty()){
                System.out.println("L'id du client ne peut être vide.\n");
                System.out.println("Entrez l'id du client associé à la réservation que vous souhaitez créer:");
                idClientSaisi = sc.nextLine();
            }
            long idClient = Integer.parseInt(idClientSaisi);

            System.out.println("Entrez l'id du véhicule associé à la réservation que vous souhaitez créer :");
            String idVehicleSaisi = sc.nextLine();

            while(idVehicleSaisi.isEmpty()){
                System.out.println("L'id du véhicule ne peut être vide.\n");
                System.out.println("Entrez l'id du véhicule associé à la réservation que vous souhaitez créer:");
                idVehicleSaisi = sc.nextLine();
            }
            long idVehicle = Integer.parseInt(idVehicleSaisi);

            reservation = new Reservation();
            reservation.setClient_id(idClient);
            reservation.setVehicle_id(idVehicle);
            reservation.setDebut(DateDeReservation(true, sc));
            reservation.setFin(DateDeReservation(false, sc));

            long reservationId = reservationService.create(reservation);
            System.out.println("La réservation a été créée avec succès. ID de la réservation : " + reservationId);

        }catch(ServiceException e){
            throw new ServiceException(e.getMessage());
        }
        sc.close();
    }
    public static LocalDate DateDeReservation(boolean DebutOuFin, Scanner sc) {

        String DateSaisie;
        LocalDate dateReservation = null;
        boolean dateCorrecte = false;

        while (!dateCorrecte) {
            if(DebutOuFin){
                System.out.println("Entrez la date de début de la réservation JJ/MM/AAAA: ");
            }else{
                System.out.println("Entrez la date de fin de la réservation JJ/MM/AAAA: ");
            }
            DateSaisie = sc.nextLine();

            if (DateSaisie.isEmpty()) {
                System.out.println("Vous devez entrer une date !");
                continue;
            }
            try {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                dateReservation = LocalDate.parse(DateSaisie, dtf);
                dateCorrecte = true;
            } catch (Exception e) {
                System.out.println("La date entrée est invalide. Assurez-vous de respecter le format [jj/MM/AAAA].");
            }
        }
        return dateReservation;
    }
    public void findAllReservations() throws ServiceException {
        try{
            List<Reservation> reservations = reservationService.findAll();

            if (!reservations.isEmpty()) {
                for (Reservation reservation : reservations) {
                    System.out.println("Id : " + reservation.getId());
                    System.out.println("Id client : " + reservation.getClient_id());
                    System.out.println("Id véhicule : " + reservation.getVehicle_id());
                    System.out.println("Début de la réservation : " + reservation.getDebut());
                    System.out.println("Fin de la réservation : " + reservation.getFin() + "\n");
                }
            } else {
                System.out.println("Aucune réservation trouvée.");
            }
        }catch(ServiceException e){
            throw new ServiceException(e.getMessage());
        }
    }
    public void DeleteReservation() throws ServiceException {

        Scanner sc = new Scanner(System.in);
        try{
            System.out.println("Liste des réservations: \n") ;
            ReservationCli reservationCli = new ReservationCli(reservationService);
            reservationCli.findAllReservations();

            System.out.println("Saisissez l'id de la réservation que vous voulez supprimer.\n");
            long idReservation = sc.nextLong();

            List<Reservation> reservations = reservationService.findAll();

            if (!reservations.isEmpty()) {
                for (Reservation reservation : reservations) {
                    if (reservation.getId() == idReservation){
                        System.out.println(reservation);
                        long idResaSupprimee = reservationService.delete(reservation);
                        System.out.println("La réservation avec l'id: "+idResaSupprimee+" a bien été supprimée");
                    }
                }
            } else {
                System.out.println("Aucune réservation trouvée.");
            }

        }catch(ServiceException e){
            throw new ServiceException(e.getMessage());
        }
        sc.close();
    }

    public void findResaByVehicleId() throws ServiceException {
        Scanner sc = new Scanner(System.in);
        try{
            long idVehicle = sc.nextLong();

            List<Reservation> reservations = reservationService.findResaByVehicleId(idVehicle);

            if (!reservations.isEmpty()) {
                for (Reservation reservation : reservations) {
                    System.out.println("Id : " + reservation.getId());
                    System.out.println("Id client : " + reservation.getClient_id());
                    System.out.println("Id véhicule : " + reservation.getVehicle_id());
                    System.out.println("Début de la réservation : " + reservation.getDebut());
                    System.out.println("Fin de la réservation : " + reservation.getFin() + "\n");
                }
            } else {
                System.out.println("Aucune réservation trouvée.");
            }
        }catch(ServiceException e){
            throw new ServiceException(e.getMessage());
        }
        sc.close();
    }

    public void findResaByClientId() throws ServiceException {
        Scanner sc = new Scanner(System.in);
        try {
            System.out.println("Entrer l'id du client dont vous souhaitez afficher la réservation:");
            long IdClient = sc.nextLong();

            List<Reservation> reservations = reservationService.findResaByClientId(IdClient);

            if (!reservations.isEmpty()) {
                for (Reservation reservation : reservations) {
                    System.out.println("Id : " + reservation.getId());
                    System.out.println("Id client : " + reservation.getClient_id());
                    System.out.println("Id véhicule : " + reservation.getVehicle_id());
                    System.out.println("Début de la réservation : " + reservation.getDebut());
                    System.out.println("Fin de la réservation : " + reservation.getFin() + "\n");
                }
            } else {
                System.out.println("Aucune réservation trouvée.");
            }

        } catch (ServiceException e) {
            throw new ServiceException(e.getMessage());
        } finally {
            sc.close();
        }
    }

}
