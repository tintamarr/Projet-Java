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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet("/users/delete")
public class ClientDeleteServlet extends HttpServlet {
    @Autowired
    private ClientService clientService;

    @Autowired
    private ReservationService reservationService;
    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        this.getServletContext().getRequestDispatcher("/WEB-INF/views/users/list.jsp").forward(request, response);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        long userId = Long.parseLong(request.getParameter("userId"));
        Client client = new Client();
        client.setId(userId);


        try{
            List<Reservation> reservationsClient = reservationService.findResaByClientId(userId);
            for (Reservation reservation : reservationsClient) {
                reservationService.delete(reservation);
            }
            clientService.delete(client);
            response.sendRedirect("/rentmanager/users");
        } catch (ServiceException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
