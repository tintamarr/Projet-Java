package com.epf.rentmanager.servlet;

import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;
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
import java.util.List;

@WebServlet("/rents/delete")
public class ReservationDeleteServlet extends HttpServlet {
    @Autowired
    private ReservationService reservationService;
    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        this.getServletContext().getRequestDispatcher("/WEB-INF/views/rents/list.jsp").forward(request, response);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        long rentId = Long.parseLong(request.getParameter("rentId"));
        Reservation reservation = new Reservation();
        reservation.setId(rentId);

        try{
            reservationService.delete(reservation);
            response.sendRedirect("/rentmanager/rents");
        } catch (ServiceException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
