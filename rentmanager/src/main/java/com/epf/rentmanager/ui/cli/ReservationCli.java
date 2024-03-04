package com.epf.rentmanager.ui.cli;

import com.epf.rentmanager.service.ReservationService;
import java.util.Scanner;
import exception.ServiceException;
public class ReservationCli {
    private ReservationService reservationService;
    public ReservationCli(ReservationService reservationService) {
        this.reservationService = reservationService;
    }


}
