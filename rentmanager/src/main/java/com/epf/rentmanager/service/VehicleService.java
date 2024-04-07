package com.epf.rentmanager.service;

import java.util.List;

import exception.DaoException;
import exception.ServiceException;
import com.epf.rentmanager.dao.VehicleDao;
import model.Vehicle;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class VehicleService {

	private VehicleDao vehicleDao;
	private VehicleService(VehicleDao vehicleDao){
		this.vehicleDao = vehicleDao;
	}
	
	public long countVehicles() throws ServiceException{
		try{
			return vehicleDao.countVehicle();
		}catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}
	public long create(Vehicle vehicle) throws ServiceException {
		if (vehicle.getModele().isEmpty() || vehicle.getConstructeur().isEmpty()) {
			throw new ServiceException("Le véhicule doit avoir un modèle un constructeur.\n");
		}
		if(vehicle.getNb_places()<2 || vehicle.getNb_places()>9){
			throw new ServiceException("Le véhicule doit avoir un nombre de places compris entre 2 et 9.\n");
		}
		try {
			return vehicleDao.create(vehicle);
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	public long delete(Vehicle vehicle) throws ServiceException {
		try {
			return vehicleDao.delete(vehicle);
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	public Vehicle findById(long id) throws ServiceException {
		try {
			Optional<Vehicle> vehicleOptional = vehicleDao.findById(id);
			if (vehicleOptional.isPresent()) {
				return vehicleOptional.get();
			} else {
				throw new ServiceException("Aucun véhicule ne correspond à l'id: "+id+".\n");
			}
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	public List<Vehicle> findAll() throws ServiceException {
		try {
			Optional<List<Vehicle>> optionalVehicles = vehicleDao.findAll();
			if (optionalVehicles.isPresent()) {
				return optionalVehicles.get();
			} else {
				throw new ServiceException("Aucun véhicule trouvé dans la liste..\n");
			}
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
}
