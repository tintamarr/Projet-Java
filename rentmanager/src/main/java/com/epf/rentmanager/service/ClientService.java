package com.epf.rentmanager.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.epf.rentmanager.dao.ClientDao;
import exception.DaoException;
import exception.ServiceException;
import model.Client;
import model.Vehicle;

public class ClientService {

	private ClientDao clientDao;
	public static ClientService instance;
	
	private ClientService() {
		this.clientDao = ClientDao.getInstance();
	}
	
	public static ClientService getInstance() {
		if (instance == null) {
			instance = new ClientService();
		}
		
		return instance;
	}
	
	
	public long create(Client client) throws ServiceException {
		if (client.getNom().isEmpty() || client.getPrenom().isEmpty() ) {
			throw new ServiceException("Le nom et le prénom du client ne peuvent être nuls.\n");
		}
		try {
			return clientDao.create(client);
		} catch (DaoException e) {
			throw new ServiceException("Une erreur a eu lieu lors de la création du client.\n");
		}
	}

	public long delete(Client client) throws ServiceException {
		try {
			return clientDao.delete(client);
		} catch (DaoException e) {
			throw new ServiceException("Une erreur a eu lieu lors de la suppression du client.\n");
		}
	}

	public Client findById(long id) throws ServiceException {
		try {
			Optional<Client> clientOptional = clientDao.findById(id);
			if (clientOptional.isPresent()) {
				return clientOptional.get();
			} else {
				throw new ServiceException("Aucun client ne correspond à l'id: "+id+".\n");
			}
		} catch (DaoException e) {
			throw new ServiceException("Une erreur a eu lieu lors de la récupération du client par l'id.\n");
		}
	}

	public List<Client> findAll() throws ServiceException {
		try {
			Optional<List<Client>> optionalClients = clientDao.findAll();
			if (optionalClients.isPresent()) {
				return optionalClients.get();
			} else {
				throw new ServiceException("Aucun client trouvé dans la liste.\n");
			}
		} catch (DaoException e) {
			throw new ServiceException("Une erreur a eu lieu lors de la récupération des clients.\n");
		}
	}
	
}
