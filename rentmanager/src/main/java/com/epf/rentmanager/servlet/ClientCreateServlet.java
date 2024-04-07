package com.epf.rentmanager.servlet;

import com.epf.rentmanager.service.ClientService;
import exception.AgeValidationException;
import exception.DaoException;
import exception.ServiceException;
import model.Client;
import model.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@WebServlet("/users/create")
public class ClientCreateServlet extends HttpServlet {
    @Autowired
    private ClientService clientService;
    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        this.getServletContext().getRequestDispatcher("/WEB-INF/views/users/create.jsp").forward(request, response);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Client client = new Client();

        String nom = request.getParameter("last_name");
        String prenom = request.getParameter("first_name");

        if (nom.length() < 3 || prenom.length() < 3) {
            String erreur = "Le nom et le prénom du client doivent faire au moins 3 caractères.";
            request.setAttribute("erreur", erreur);
            request.getRequestDispatcher("/WEB-INF/views/users/create.jsp").forward(request, response);
            return;
        }
        String email = request.getParameter("email");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dateStr = request.getParameter("birth_date");
        LocalDate naissance = LocalDate.parse(dateStr, formatter);

        client.setNaissance(naissance);
        client.setPrenom(prenom);
        client.setNom(nom);
        client.setEmail(email);
        try{
            clientService.create(client);
            response.sendRedirect("/rentmanager/users");
        } catch (AgeValidationException e) {
            String erreurAge = "Le client doit avoir au moins 18 ans pour être créé.";
            request.setAttribute("erreur", erreurAge);
            request.getRequestDispatcher("/WEB-INF/views/users/create.jsp").forward(request, response);
        } catch (ServiceException | DaoException e) {
            String erreur = e.getMessage();
            request.setAttribute("erreur", erreur);
            request.getRequestDispatcher("/WEB-INF/views/users/create.jsp").forward(request, response);
        }
    }
}
