package com.epf.rentmanager.ui.cli;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.VehicleService;
import exception.ServiceException;

import java.util.Scanner;
public class Main {

    public static void main(String[] args) throws ServiceException {


        ClientService clientService = ClientService.getInstance();
        ClientCli clientCli = new ClientCli(clientService);
        VehicleService vehicleService = VehicleService.getInstance();
        VehicleCli vehicleCli = new VehicleCli(vehicleService);

        Scanner sc = new Scanner(System.in);
        System.out.println("Quelle action souhaitez-vous réaliser? Entrez:\n" +
                "1 - Pour créer un client\n" +
                "2 - Pour lister tous les clients\n" +
                "3 - Pour supprimer un client\n" +
                "4 - Pour créer un véhicule\n" +
                "5 - Pour lister tous les véhicules\n" +
                "6 - Pour supprimer un véhicule\n");

        int action = sc.nextInt();
        while (action>6 || action<1){
            System.out.println("Veuillez rentrer un nombre compris entre 1 et 6 correspondant à l'action à réaliser.\n");
            action = sc.nextInt();
        }

        switch (action) {
            case 1:
                clientCli.createClient();
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                vehicleCli.createVehicle();
                break;
            case 5:
                vehicleCli.findAll();
                break;
            case 6:
                break;
        }
        sc.close();
    }
}
