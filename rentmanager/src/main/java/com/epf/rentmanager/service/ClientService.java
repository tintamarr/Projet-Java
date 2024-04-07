package com.epf.rentmanager.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.epf.rentmanager.dao.ClientDao;
import exception.DaoException;
import exception.ServiceException;
import model.Client;
import model.Vehicle;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

	private ClientDao clientDao;
	private ClientService(ClientDao clientDao){
		this.clientDao = clientDao;
	}
	public long countClients() throws ServiceException{
		try{
			return clientDao.countClients();
		}catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	public long create(Client client) throws ServiceException {
		if (client.getNom().isEmpty() || client.getPrenom().isEmpty() ) {
			throw new ServiceException("Le nom et le prénom du client ne peuvent être nuls.\n");
		}
		try {
			return clientDao.create(client);
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	public long delete(Client client) throws ServiceException {
		try {
			return clientDao.delete(client);
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
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
			throw new ServiceException(e.getMessage());
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
			throw new ServiceException(e.getMessage());
		}
	}
	
}
