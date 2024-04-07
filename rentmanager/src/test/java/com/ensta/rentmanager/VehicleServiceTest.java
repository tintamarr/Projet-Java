package com.ensta.rentmanager;


import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.epf.rentmanager.dao.VehicleDao;
import com.epf.rentmanager.service.VehicleService;

import exception.ServiceException;

import model.Vehicle;
import org.junit.jupiter.api.Test;


import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;



@ExtendWith(MockitoExtension.class)
public class VehicleServiceTest {
    @InjectMocks
    private VehicleService vehicleService;

    @Mock
    private VehicleDao vehicleDao;

    @Test
    void testCreateVehicleWithInvalidNumberOfSeats() {
        Vehicle vehicle = new Vehicle();
        vehicle.setModele("Modele");
        vehicle.setConstructeur("Constructeur");
        vehicle.setNb_places(11);
        ServiceException exception = assertThrows(ServiceException.class, () -> vehicleService.create(vehicle));
        assertEquals("Le véhicule doit avoir un nombre de places compris entre 2 et 9.\n", exception.getMessage());
    }

}

