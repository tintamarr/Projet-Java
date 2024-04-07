package com.epf.rentmanager.servlet;

import com.epf.rentmanager.service.VehicleService;
import exception.ServiceException;
import model.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/cars/create")
public class VehicleCreateServlet extends HttpServlet {
    @Autowired
    private VehicleService vehicleService;
    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        this.getServletContext().getRequestDispatcher("/WEB-INF/views/vehicles/create.jsp").forward(request, response);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Vehicle vehicle = new Vehicle();

        String constructeur = request.getParameter("manufacturer");
        String modele = request.getParameter("modele");
        int nb_places = Integer.parseInt(request.getParameter("seats"));

        if (nb_places < 2 || nb_places >9) {
            String erreur = "Le nombre de places de la voiture doit être compris entre 2 et 9.\n";
            request.setAttribute("erreur", erreur);
            request.getRequestDispatcher("/WEB-INF/views/vehicles/create.jsp").forward(request, response);
            return;
        }

        vehicle.setConstructeur(constructeur);
        vehicle.setModele(modele);
        vehicle.setNb_places(nb_places);

        try{
            vehicleService.create(vehicle);
            response.sendRedirect("/rentmanager/cars");

        } catch (ServiceException e) {
            String erreur = e.getMessage();
            request.setAttribute("erreur", erreur);
            request.getRequestDispatcher("/WEB-INF/views/users/create.jsp").forward(request, response);
        }
    }
}
