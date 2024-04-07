package com.epf.rentmanager.ui.cli;
import com.epf.rentmanager.service.ClientService;
import exception.AgeValidationException;
import exception.DaoException;
import exception.ServiceException;
import model.Client;
import model.Vehicle;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
public class ClientCli {
    private ClientService clientService;
    public ClientCli(ClientService clientService) {
        this.clientService = clientService;
    }
    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    public void createClient(Scanner sc) throws ServiceException {
        Client client;
        try {

            System.out.println("Création d'un nouveau client.");
            System.out.println("Entrez le nom du client :");
            String nom = sc.nextLine();

            while(nom.isEmpty()){
                System.out.println("Le nom ne peut être vide.\n");
                System.out.println("Entrez le nom du client :");
                nom = sc.nextLine();
            }

            System.out.println("Entrez le prénom du client :");
            String prenom = sc.nextLine();

            while(prenom.isEmpty()){
                System.out.println("Le prenom ne peut être vide.\n");
                System.out.println("Entrez le prenom du client :");
                prenom = sc.nextLine();
            }

            System.out.println("Entrez l'email du client [mailclient@blabla.com] :");
            String email = sc.nextLine();

            while(!EmailCorrect(email)){
                System.out.println("La syntaxe de l'email n'est pas correcte, veuillez reessayer.");
                System.out.println("Entrez l'email du client [mailclient@blabla.com] :");
                email = sc.nextLine();
            }

            client = new Client();
            client.setNom(nom);
            client.setPrenom(prenom);
            client.setEmail(email);
            client.setNaissance(DateDeNaissance());

            long clientId = clientService.create(client);
            System.out.println("Le client a été créé avec succès. ID du client : " + clientId);

        }catch (ServiceException e) {
            throw new ServiceException("Une erreur a eu lieu lors de la création du client.\n");
        } catch (AgeValidationException e) {
            throw new RuntimeException(e);
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }

    }

    public static LocalDate DateDeNaissance() {

        Scanner sc = new Scanner(System.in);

        String date;
        LocalDate dateNaissance = null;
        boolean dateCorrecte = false;

        while (!dateCorrecte) {
            System.out.println("Entrez votre date de naissance JJ/MM/AAAA: ");
            date = sc.nextLine();

            if (date.isEmpty()) {
                System.out.println("Vous devez entrer une date !");
                continue;
            }

            try {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                dateNaissance = LocalDate.parse(date, dtf);
                dateCorrecte = true;
            } catch (Exception e) {
                System.out.println("La date entrée est invalide. Assurez-vous de respecter le format [jj. MMM. AAAA].");
            }
        }
        sc.close();
        return dateNaissance;
    }

    public static boolean EmailCorrect(String email){
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }
    public void findAll() throws ServiceException {
        try{
            List<Client> clients = clientService.findAll();

            if (!clients.isEmpty()) {
                for (Client client : clients) {
                    System.out.println("Id : " + client.getId());
                    System.out.println("Nom : " + client.getNom());
                    System.out.println("Prénom : " + client.getPrenom());
                    System.out.println("Adresse mail : " + client.getEmail());
                    System.out.println("Date de naissance : " + client.getNaissance()+ "\n");
                }
            } else {
                System.out.println("Aucun client trouvé.");
            }
        }catch(ServiceException e){
            throw new ServiceException(e.getMessage());
        }
    }

    public void deleteClient(Scanner sc) throws ServiceException {

        try{
           System.out.println("Liste des clients: \n") ;
           ClientCli clientCli = new ClientCli(clientService);
           clientCli.findAll();
           System.out.println("Saisissez l'id du client que vous voulez supprimer.\n");
           long IdClientASupprimer = sc.nextLong();

            Client client = clientService.findById(IdClientASupprimer);
            System.out.println(client);
            long idClient = clientService.delete(client);
            System.out.println("Le client avec l'id: "+idClient+" a bien été supprimé");
        }catch(ServiceException e){
            throw new ServiceException(e.getMessage());
        }
    }
}

