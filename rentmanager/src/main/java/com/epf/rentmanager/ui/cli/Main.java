package com.epf.rentmanager.ui.cli;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.VehicleService;
import exception.ServiceException;

import java.util.Scanner;
public class Main {

    public static void main(String[] args) throws ServiceException {
        Scanner sc = new Scanner(System.in);
        ClientService clientService = ClientService.getInstance();
        ClientCli clientCli = new ClientCli(clientService);
        VehicleService vehicleService = VehicleService.getInstance();
        VehicleCli vehicleCli = new VehicleCli(vehicleService);
        ReservationService reservationService = ReservationService.getInstance();
        ReservationCli reservationCli = new ReservationCli(reservationService);

        boolean continuer = true;

        while (continuer) {
            System.out.println("Quelle action souhaitez-vous réaliser? Entrez:\n" +
                    "1 - Pour créer un client\n" +
                    "2 - Pour lister tous les clients\n" +
                    "3 - Pour supprimer un client\n" +
                    "4 - Pour créer un véhicule\n" +
                    "5 - Pour lister tous les véhicules\n" +
                    "6 - Pour supprimer un véhicule\n" +
                    "7 - Pour créer une réservation\n" +
                    "8 - Pour lister toutes les réservations\n" +
                    "9 - Pour lister les réservations associées à un client donné\n" +
                    "10 - Pour lister les réservations associées à un véhicule donné\n" +
                    "11 - Pour supprimer une réservation\n" +
                    "0 - Pour arrêter\n");

            int action = sc.nextInt();
            while (action>11 || action<0){
                System.out.println("Veuillez rentrer un nombre compris entre 1 et 6 correspondant à l'action à réaliser.\n");
                action = sc.nextInt();
            }
            switch (action) {
                case 0:
                    continuer = false;
                    break;
                case 1:
                    clientCli.createClient();
                    break;
                case 2:
                    clientCli.findAll();
                    break;
                case 3:
                    clientCli.deleteClient();
                    break;
                case 4:
                    vehicleCli.createVehicle();
                    break;
                case 5:
                    vehicleCli.findAll();
                    break;
                case 6:
                    vehicleCli.deleteVehicle();
                    break;
                case 7:
                    reservationCli.CreateReservation();
                    break;
                case 8:
                    reservationCli.findAllReservations();
                    break;
                case 9:
                    reservationCli.findResaByClientId();
                    break;
                case 10:
                    reservationCli.findResaByVehicleId();
                    break;
                case 11:
                    reservationCli.DeleteReservation();
                    break;
            }
        }
        sc.close();
    }
}
