package com.epf.rentmanager.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

import com.epf.rentmanager.dao.ClientDao;
import exception.AgeValidationException;
import exception.DaoException;
import exception.ServiceException;
import model.Client;
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
	
	public long create(Client client) throws ServiceException, AgeValidationException, DaoException {
		if (client.getNom().isEmpty() || client.getPrenom().isEmpty() ) {
			throw new ServiceException("Le nom et le prénom du client ne peuvent être nuls.\n");
		}
		if(!clientMajeur(client.getNaissance())){
			throw new AgeValidationException(("Le client doit être agé de minimum 18 ans pour pouvoir être créé.\n"));
		}
		if(client.getNom().length()<3 || client.getPrenom().length()<3){
			throw new ServiceException("Le nom et le prénom du client doivent faire minimum 3 caractères.\n");
		}
		Optional<List<Client>> optionalListeClients = clientDao.findAll();

		if (optionalListeClients.isPresent()) {
			List<Client> listeClients = optionalListeClients.get();

			for (Client clientExistant : listeClients) {
				if (clientExistant.getEmail().equals(client.getEmail())) {
					throw new ServiceException("L'adresse e-mail est déjà utilisée par un autre client.\n");
				}
			}
		} else {
			throw new ServiceException("Aucun client trouvé.\n");
		}

		try {
			return clientDao.create(client);
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}
	public boolean clientMajeur(LocalDate naissance) {

		LocalDate dateActuelle = LocalDate.now();
		Period period = Period.between(naissance, dateActuelle);
		return period.getYears() >= 18;
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
