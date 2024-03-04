package com.epf.rentmanager.ui.cli;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.VehicleService;
import exception.ServiceException;
import model.Vehicle;

import java.util.List;
import java.util.Scanner;

public class VehicleCli {

    private VehicleService vehicleService;
    public VehicleCli(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }
    public void createVehicle() throws ServiceException {
        Scanner sc = new Scanner(System.in);
        Vehicle vehicle;

        try{
            System.out.println("Création d'un nouveau véhicule.");
            System.out.println("Entrez le nom du constructeur du véhicule :");
            String constructeur = sc.nextLine();

            while(constructeur.isEmpty()){
                System.out.println("Le nom du constructeur ne peut être vide.\n");
                System.out.println("Entrez le nom du constructeur du véhicule :");
                constructeur = sc.nextLine();
            }

            System.out.println("Entrez le nom du modèle du véhicule :");
            String modele = sc.nextLine();

            while(modele.isEmpty()){
                System.out.println("Le nom du modèle du véhicule ne peut être vide.\n");
                System.out.println("Entrez le nom du modèle du véhicule :");
                modele = sc.nextLine();
            }

            System.out.println("Entrez le nombre de places du véhicule :");
            String input_nb_places = sc.nextLine();

            while(input_nb_places.isEmpty()){
                System.out.println("Le nombre de places du véhicule ne peut être vide.\n");
                System.out.println("Entrez le nombre de places du véhicule :");
                input_nb_places = sc.nextLine();
            }

            int nb_places  = Integer.parseInt(input_nb_places);
            vehicle = new Vehicle();
            vehicle.setConstructeur(constructeur);
            vehicle.setModele(modele);
            vehicle.setNb_places(nb_places);

            long vehicleId = vehicleService.create(vehicle);
            System.out.println("Le véhicule a été créé avec succès. ID du véhicule : " + vehicleId);

        }catch(ServiceException e){
            throw new ServiceException("Une erreur a eu lieu lors de la création du véhicule.\n");
        }
        sc.close();
    }
    
    public void findAll() throws ServiceException {
        try{
            List<Vehicle> vehicles = vehicleService.findAll();

            if (!vehicles.isEmpty()) {
                for (Vehicle vehicle : vehicles) {
                    System.out.println("Constructeur : " + vehicle.getConstructeur());
                    System.out.println("Modèle : " + vehicle.getModele());
                    System.out.println("Nombre de places : " + vehicle.getNb_places());
                }
            } else {
                System.out.println("Aucun véhicule trouvé.");
            }
        }catch(ServiceException e){
            throw new ServiceException(e.getMessage());
        }
    }
}
