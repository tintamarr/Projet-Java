package com.epf.rentmanager.ui.cli;
import com.epf.rentmanager.Configuration.AppConfiguration;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.VehicleService;
import exception.ServiceException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Scanner;
public class Main {

    public static void main(String[] args) throws ServiceException {
        ApplicationContext context = new
                AnnotationConfigApplicationContext(AppConfiguration.class);
        Scanner sc = new Scanner(System.in);
        ClientService clientService = context.getBean(ClientService.class);
        ClientCli clientCli = new ClientCli(clientService);
        VehicleService vehicleService = context.getBean(VehicleService.class);
        VehicleCli vehicleCli = new VehicleCli(vehicleService);
        ReservationService reservationService = context.getBean(ReservationService.class);
        ReservationCli reservationCli = new ReservationCli(reservationService);

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
                "11 - Pour supprimer une réservation\n");

        System.out.println("Entrez le chiffre correspondant à l'action que vous voulez réaliser:\n");
        int action = sc.nextInt();

        while (action>11 || action<1){
            System.out.println("Veuillez rentrer un nombre compris entre 1 et 6 correspondant à l'action à réaliser.\n");
            action = sc.nextInt();
        }
        switch (action) {
            case 1:
                clientCli.createClient(sc);
                break;
            case 2:
                clientCli.findAll();
                break;
            case 3:
                clientCli.deleteClient(sc);
                break;
            case 4:
                vehicleCli.createVehicle(sc);
                break;
            case 5:
                vehicleCli.findAll();
                break;
            case 6:
                vehicleCli.deleteVehicle(sc);
                break;
            case 7:
                reservationCli.CreateReservation(sc);
                break;
            case 8:
                reservationCli.findAllReservations();
                break;
            case 9:
                reservationCli.findResaByClientId(sc);
                break;
            case 10:
                reservationCli.findResaByVehicleId(sc);
                break;
            case 11:
                reservationCli.DeleteReservation(sc);
                break;
        }
    }
}
