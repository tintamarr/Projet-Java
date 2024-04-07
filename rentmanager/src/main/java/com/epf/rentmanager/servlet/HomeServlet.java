package com.epf.rentmanager.servlet;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.VehicleService;
import exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@Autowired
	private VehicleService vehicleService;
	@Autowired
	private ClientService clientService;
	@Autowired
	private ReservationService reservationService;

	@Override
	public void init() throws ServletException {
		super.init();
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		long nb_vehicles = 0;
		long nb_clients = 0;
		long nb_reservations = 0;

		try{
			nb_vehicles = vehicleService.countVehicles();
			nb_clients = clientService.countClients();
			nb_reservations = reservationService.countRents();
		}catch (ServiceException e) {
			throw new RuntimeException(e.getMessage());
		}
		request.setAttribute("nb_vehicles",nb_vehicles);
		request.setAttribute("nb_clients",nb_clients);
		request.setAttribute("nb_reservations",nb_reservations);
		this.getServletContext().getRequestDispatcher("/WEB-INF/views/home.jsp").forward(request, response);
	}

}
