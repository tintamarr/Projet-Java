package com.epf.rentmanager.dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.epf.rentmanager.persistence.ConnectionManager;
import exception.DaoException;
import model.Client;
import org.springframework.stereotype.Repository;

@Repository

public class ClientDao {
	private static final String CREATE_CLIENT_QUERY = "INSERT INTO Client(nom, prenom, email, naissance) VALUES(?, ?, ?, ?);";
	private static final String DELETE_CLIENT_QUERY = "DELETE FROM Client WHERE id=?;";
	private static final String FIND_CLIENT_QUERY = "SELECT id, nom, prenom, email, naissance FROM Client WHERE id=?;";
	private static final String FIND_CLIENTS_QUERY = "SELECT id, nom, prenom, email, naissance FROM Client;";
	
	public long create(Client client) throws DaoException {

		try(Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(CREATE_CLIENT_QUERY)){
			ps.setString(1, client.getNom());
			ps.setString(2,client.getPrenom());
			ps.setString(3, client.getEmail());
			ps.setDate(4, Date.valueOf(client.getNaissance()));
			ps.execute();

		}catch (SQLException e){
			throw new DaoException(e.getMessage());
		}
		return client.getId();

	}
	
	public long delete(Client client) throws DaoException {

		try(Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(DELETE_CLIENT_QUERY)){

			ps.setLong(1,client.getId());
			ps.executeUpdate();

		}catch (SQLException e){
			throw new DaoException(e.getMessage());
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
				LocalDate DateNaissance = ((Timestamp) rs.getObject("naissance")).toLocalDateTime().toLocalDate();
				client.setNaissance(DateNaissance);
			}

		}catch(SQLException e){
			throw new DaoException(e.getMessage());
		}finally{
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					System.err.println(e.getMessage());
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
				client.setNaissance(((Timestamp) rs.getObject("naissance")).toLocalDateTime().toLocalDate());
				clients.add(client);
			}
		}catch(SQLException e){
			throw new DaoException(e.getMessage());
		}finally{
			if(rs!=null){
				try{
					rs.close();
				}catch(SQLException e){
					System.err.println(e.getMessage());
				}
			}
		}
		return Optional.ofNullable(clients);
	}
}
