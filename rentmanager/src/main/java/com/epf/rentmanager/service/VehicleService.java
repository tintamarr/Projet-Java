package com.epf.rentmanager.service;

import java.util.List;

import exception.DaoException;
import exception.ServiceException;
import com.epf.rentmanager.dao.VehicleDao;
import model.Vehicle;
import java.util.Optional;
public class VehicleService {

	private VehicleDao vehicleDao;
	public static VehicleService instance;
	
	private VehicleService() {
		this.vehicleDao = VehicleDao.getInstance();
	}
	
	public static VehicleService getInstance() {
		if (instance == null) {
			instance = new VehicleService();
		}
		
		return instance;
	}
	
	
	public long create(Vehicle vehicle) throws ServiceException {
		if (vehicle.getNb_places()<=1 || vehicle.getConstructeur().isEmpty()) {
			throw new ServiceException("Le véhicule doit avoir un nombre de places supérieures à 1 et un constructeur non vide.\n");
		}

		try {
			return vehicleDao.create(vehicle);
		} catch (DaoException e) {
			throw new ServiceException("Une erreur a eu lieu lors de la création du véhicule.\n");
		}
	}

	public long delete(Vehicle vehicle) throws ServiceException {
		try {
			return vehicleDao.delete(vehicle);
		} catch (DaoException e) {
			throw new ServiceException("Une erreur a eu lieu lors de la suppresion du véhicule.\n");
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
			throw new ServiceException("Une erreur a eu lieu lors de la récupération du véhicule par l'id.\n");
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
