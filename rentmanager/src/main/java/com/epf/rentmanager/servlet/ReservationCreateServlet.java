package com.epf.rentmanager.servlet;

import com.epf.rentmanager.service.ReservationService;
import exception.DaoException;
import exception.ServiceException;
import model.Client;
import model.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

@WebServlet("/rents/create")
public class ReservationCreateServlet extends HttpServlet {
    @Autowired
    private ReservationService reservationService;
    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        this.getServletContext().getRequestDispatcher("/WEB-INF/views/rents/create.jsp").forward(request, response);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Reservation reservation = new Reservation();

        long vehicle_id = Long.parseLong(request.getParameter("car"));
        long client_id = Long.parseLong(request.getParameter("client"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dateStr = request.getParameter("begin");
        LocalDate debut = LocalDate.parse(dateStr, formatter);

        dateStr = request.getParameter("end");
        LocalDate fin = LocalDate.parse(dateStr, formatter);
        try {
            if (reservationService.dejaReserver(vehicle_id, debut, fin)) {
                String erreur = "La voiture est déjà réservée pour cette période.";
                request.setAttribute("erreur", erreur);
                request.getRequestDispatcher("/WEB-INF/views/rents/create.jsp").forward(request, response);
                return;
            }
        } catch (DaoException e) {
            throw new ServletException("Erreur lors de la vérification des réservations existantes", e);
        }
        try {
            if (reservationService.reservation30Jours(vehicle_id, debut, fin)) {
                String erreur = "Une voiture ne peux pas être réservée 30 jours de suite sans pause.\n";
                request.setAttribute("erreur", erreur);
                request.getRequestDispatcher("/WEB-INF/views/rents/create.jsp").forward(request, response);
                return;
            }
        } catch (DaoException e) {
            throw new ServletException("Erreur lors de la vérification des réservations existantes", e);
        }


        reservation.setVehicle_id(vehicle_id);
        reservation.setClient_id(client_id);
        reservation.setDebut(debut);
        reservation.setFin(fin);

        try{
            reservationService.create(reservation);
            response.sendRedirect("/rentmanager/rents");
        } catch (ServiceException e) {
            String erreur = e.getMessage();
            request.setAttribute("erreur", erreur);
            request.getRequestDispatcher("/WEB-INF/views/rents/create.jsp").forward(request, response);
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }
    }
}
