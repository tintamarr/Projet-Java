package com.epf.rentmanager.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.epf.rentmanager.persistence.ConnectionManager;
import exception.DaoException;
import model.Client;


public class ClientDao {
	
	private static ClientDao instance = null;
	private ClientDao() {
	}
	public static ClientDao getInstance() {
		if(instance == null) {
			instance = new ClientDao();
		}
		return instance;
	}
	
	private static final String CREATE_CLIENT_QUERY = "INSERT INTO Client(nom, prenom, email, naissance) VALUES(?, ?, ?, ?);";
	private static final String DELETE_CLIENT_QUERY = "DELETE FROM Client WHERE id=?;";
	private static final String FIND_CLIENT_QUERY = "SELECT nom, prenom, email, naissance FROM Client WHERE id=?;";
	private static final String FIND_CLIENTS_QUERY = "SELECT id, nom, prenom, email, naissance FROM Client;";
	
	public long create(Client client) throws DaoException {

		ResultSet rs = null;

		try(Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(CREATE_CLIENT_QUERY, Statement.RETURN_GENERATED_KEYS)){

			ps.setString(1, client.getNom());
			ps.setString(2, client.getPrenom());
			ps.setString(3, client.getEmail());
			ps.setDate(4, Date.valueOf(client.getNaissance()));
			ps.executeUpdate();

			rs = ps.getGeneratedKeys();
			long clientId = -1;
			if (rs.next()) {
				clientId = rs.getLong(1);
			}
			return clientId;

		} catch (SQLException e) {
			throw new DaoException("Erreur lors de la création d'un utilisateur.");
		}
		finally{
			if(rs!=null){
				try{
					rs.close();
				}catch(SQLException e){
					System.err.println("Une erreur est survenue lors de la fermeture du ResultSet.");
				}
			}
		}
	}
	
	public long delete(Client client) throws DaoException {

		try(Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(DELETE_CLIENT_QUERY)){

			ps.setLong(1,client.getId());
			ps.executeUpdate();

		}catch (SQLException e){
			throw new DaoException("Une erreur s'est produite lors de la suppression du client.");
		}
		return client.getId();
	}

	public Optional<Client> findById(long id) throws DaoException {

		ResultSet rs = null;
		Client client = null;

		try(Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(FIND_CLIENT_QUERY)){

			ps.setLong(1,id);
			rs = ps.executeQuery();

			if (rs.next()) {
				client = new Client();
				client.setId(rs.getLong("id"));
				client.setNom(rs.getString("nom"));
				client.setPrenom(rs.getString("prenom"));
				client.setEmail(rs.getString("email"));
				client.setNaissance(((java.sql.Date) rs.getObject("naissance")).toLocalDate());
			}

		}catch(SQLException e){
			throw new DaoException("Une erreur s'est produite lors de la recherche du client par son id.");
		}finally{
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					System.err.println("Une erreur est survenue lors de la fermeture du ResultSet.");
				}
			}
		}
		return Optional.ofNullable(client);
	}

	public Optional<List<Client>> findAll() throws DaoException {
		ResultSet rs = null;
		List<Client> clients = new ArrayList<>();

		try(Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(FIND_CLIENTS_QUERY)){

			rs = ps.executeQuery();

			while(rs.next()){
				Client client = new Client();
				client.setId(rs.getLong("id"));
				client.setNom(rs.getString("nom"));
				client.setPrenom(rs.getString("prenom"));
				client.setEmail(rs.getString("email"));
				client.setNaissance(((java.sql.Date) rs.getObject("naissance")).toLocalDate());
				clients.add(client);
			}
		}catch(SQLException e){
			throw new DaoException("Erreur lors de la recherche d'un client grâce à son identifiant.");
		}finally{
			if(rs!=null){
				try{
					rs.close();
				}catch(SQLException e){
					System.err.println("Une erreur est survenue lors de la fermeture du ResultSet.");
				}
			}
		}
		return Optional.ofNullable(clients);
	}
}
