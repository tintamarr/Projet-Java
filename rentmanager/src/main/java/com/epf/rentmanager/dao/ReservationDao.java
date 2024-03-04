package com.epf.rentmanager.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import exception.DaoException;
import model.Reservation;
import java.sql.*;
import java.util.Optional;

import com.epf.rentmanager.persistence.ConnectionManager;
public class ReservationDao {

	private static ReservationDao instance = null;
	private ReservationDao() {}
	public static ReservationDao getInstance() {
		if(instance == null) {
			instance = new ReservationDao();
		}
		return instance;
	}
	
	private static final String CREATE_RESERVATION_QUERY = "INSERT INTO Reservation(client_id, vehicle_id, debut, fin) VALUES(?, ?, ?, ?);";
	private static final String DELETE_RESERVATION_QUERY = "DELETE FROM Reservation WHERE id=?;";
	private static final String FIND_RESERVATIONS_BY_CLIENT_QUERY = "SELECT id, vehicle_id, debut, fin FROM Reservation WHERE client_id=?;";
	private static final String FIND_RESERVATIONS_BY_VEHICLE_QUERY = "SELECT id, client_id, debut, fin FROM Reservation WHERE vehicle_id=?;";
	private static final String FIND_RESERVATIONS_QUERY = "SELECT id, client_id, vehicle_id, debut, fin FROM Reservation;";
		
	public long create(Reservation reservation) throws DaoException {

		ResultSet rs =null;

		try(Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(CREATE_RESERVATION_QUERY, Statement.RETURN_GENERATED_KEYS)){
			ps.setLong(1, reservation.getClient_id());
			ps.setLong(2, reservation.getVehicle_id());
			ps.setDate(3, Date.valueOf(reservation.getDebut()));
			ps.setDate(4,Date.valueOf(reservation.getFin()));
			ps.executeUpdate();

			rs = ps.getGeneratedKeys();

			long reservationId = -1;
			if (rs.next()) {
				reservationId = rs.getLong(1);
			}
			return reservationId;

		}catch(SQLException e){
			throw new DaoException("Une erreur est survenue lors de la création de la réservation.");
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
	
	public long delete(Reservation reservation) throws DaoException {
		try(Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(DELETE_RESERVATION_QUERY)){

			ps.setLong(1,reservation.getId());
			ps.executeUpdate();

		}catch (SQLException e){
			throw new DaoException("Une erreur s'est produite lors de la suppression du client.");
		}
		return reservation.getId();
	}

	
	public Optional<List<Reservation>> findResaByClientId(long clientId) throws DaoException {
		ResultSet rs = null;
		List<Reservation> reservations = new ArrayList<>();

		try(Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(FIND_RESERVATIONS_BY_CLIENT_QUERY)){

			ps.setLong(1,clientId);
			rs = ps.executeQuery();

			while (rs.next()) {
				Reservation reservation = new Reservation();
				reservation.setId(rs.getLong("id"));
				reservation.setClient_id(rs.getLong("client_id"));
				reservation.setVehicle_id(rs.getLong("vehicle_id"));
				reservation.setDebut(((java.sql.Date) rs.getObject("debut")).toLocalDate());
				reservation.setFin(((java.sql.Date) rs.getObject("fin")).toLocalDate());
				reservations.add(reservation);
			}

		}catch(SQLException e){
			throw new DaoException("Une erreur s'est produite lors de la recherche des réservations liées à l'id d'un client.");
		}finally{
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					System.err.println("Une erreur est survenue lors de la fermeture du ResultSet.");
				}
			}
		}
		return Optional.ofNullable(reservations);
	}
	
	public Optional<List<Reservation>> findResaByVehicleId(long vehicleId) throws DaoException {
		ResultSet rs = null;
		List<Reservation> reservations = new ArrayList<>();

		try(Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(FIND_RESERVATIONS_BY_VEHICLE_QUERY)){

			ps.setLong(1,vehicleId);
			rs = ps.executeQuery();

			while (rs.next()) {
				Reservation reservation = new Reservation();
				reservation.setId(rs.getLong("id"));
				reservation.setClient_id(rs.getLong("client_id"));
				reservation.setVehicle_id(rs.getLong("vehicle_id"));
				reservation.setDebut(((java.sql.Date) rs.getObject("debut")).toLocalDate());
				reservation.setFin(((java.sql.Date) rs.getObject("fin")).toLocalDate());
				reservations.add(reservation);
			}

		}catch(SQLException e){
			throw new DaoException("Une erreur s'est produite lors de la recherche des réservations liées à l'id d'un véhicule.");
		}finally{
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					System.err.println("Une erreur est survenue lors de la fermeture du ResultSet.");
				}
			}
		}
		return Optional.ofNullable(reservations);
	}
	public Optional<List<Reservation>> findAll() throws DaoException {
		ResultSet rs = null;
		List<Reservation> reservations = new ArrayList<>();

		try(Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(FIND_RESERVATIONS_QUERY)){

			rs = ps.executeQuery();

			while(rs.next()){
				Reservation reservation = new Reservation();
				reservation.setId(rs.getLong("id"));
				reservation.setClient_id(rs.getLong("client_id"));
				reservation.setVehicle_id(rs.getLong("vehicle_id"));
				reservation.setDebut(((java.sql.Date) rs.getObject("debut")).toLocalDate());
				reservation.setFin(((java.sql.Date) rs.getObject("fin")).toLocalDate());
				reservations.add(reservation);
			}
		}catch(SQLException e){
			throw new DaoException("Erreur lors de la recherche d'une réservation grâce à son identifiant.");
		}finally{
			if(rs!=null){
				try{
					rs.close();
				}catch(SQLException e){
					System.err.println("Une erreur est survenue lors de la fermeture du ResultSet.");
				}
			}
		}
		return Optional.ofNullable(reservations);
	}
}
