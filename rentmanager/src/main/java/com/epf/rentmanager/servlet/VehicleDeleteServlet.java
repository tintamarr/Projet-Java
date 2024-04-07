package com.epf.rentmanager.servlet;

import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.VehicleService;
import exception.ServiceException;
import model.Client;
import model.Reservation;
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

@WebServlet("/cars/delete")
public class VehicleDeleteServlet extends HttpServlet {
    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private ReservationService reservationService;
    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        this.getServletContext().getRequestDispatcher("/WEB-INF/views/cars/list.jsp").forward(request, response);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        long carId = Long.parseLong(request.getParameter("carId"));
        Vehicle vehicle = new Vehicle();
        vehicle.setId(carId);


        try{
            List<Reservation> reservationsVehicle = reservationService.findResaByVehicleId(carId);
            for (Reservation reservation : reservationsVehicle) {
                reservationService.delete(reservation);
            }
            vehicleService.delete(vehicle);
            response.sendRedirect("/rentmanager/cars");
        } catch (ServiceException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
