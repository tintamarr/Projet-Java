package com.epf.rentmanager.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import java.util.Optional;

import com.epf.rentmanager.persistence.ConnectionManager;
import exception.DaoException;
import model.Vehicle;
import org.springframework.stereotype.Repository;

@Repository
public class VehicleDao {
	private static final String CREATE_VEHICLE_QUERY = "INSERT INTO Vehicle(constructeur, modele,nb_places) VALUES(?, ?,?);";
	private static final String DELETE_VEHICLE_QUERY = "DELETE FROM Vehicle WHERE id=?;";
	private static final String FIND_VEHICLE_QUERY = "SELECT id, constructeur, modele,nb_places FROM Vehicle WHERE id=?;";
	private static final String FIND_VEHICLES_QUERY = "SELECT id, constructeur,modele, nb_places FROM Vehicle;";
	private static final String COUNT_VEHICLES= "SELECT COUNT(id) AS count FROM Vehicle" ;

	public long countVehicle() throws DaoException {
		long nb_vehicles = 0;
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(COUNT_VEHICLES);
			 ResultSet rs = ps.executeQuery()) {
			if (rs.next()) {
				nb_vehicles = rs.getLong(1);
			}
		} catch (SQLException e) {
			throw new DaoException("Une erreur s'est produite lors du dénombrement des véhicules");
		}
		return nb_vehicles;
	}



	public long create(Vehicle vehicle) throws DaoException {

		ResultSet rs = null;

		try(Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(CREATE_VEHICLE_QUERY, Statement.RETURN_GENERATED_KEYS)){

			ps.setString(1, vehicle.getConstructeur());
			ps.setInt(3, vehicle.getNb_places());
			ps.setString(2,vehicle.getModele());
			ps.executeUpdate();

			rs = ps.getGeneratedKeys();

			long vehicleId = -1;
			if (rs.next()) {
				vehicleId = rs.getLong(1);
			}
			rs.close();
			return vehicleId;
		}
		catch(SQLException e){
			throw new DaoException("Une erreur s'est produite lors de la création du véhicule.");
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

	public long delete(Vehicle vehicle) throws DaoException {

		try(Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(DELETE_VEHICLE_QUERY)){

			ps.setLong(1,vehicle.getId());
			ps.executeUpdate();
		}
		catch(SQLException e){
			throw new DaoException("Une erreur s'est produite lors de la suppression du véhicule.");
		}
		return vehicle.getId();
	}

	public Optional<Vehicle> findById(long id) throws DaoException {

		ResultSet rs = null;
		Vehicle vehicle = null;
		try(Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(FIND_VEHICLE_QUERY)){
			ps.setLong(1,id);
			rs = ps.executeQuery();

			if (rs.next()) {
				vehicle = new Vehicle();
				vehicle.setId(rs.getLong("id"));
				vehicle.setConstructeur(rs.getString("constructeur"));
				vehicle.setModele(rs.getString("modele"));
				vehicle.setNb_places(rs.getInt("nb_places"));
			}
		} catch(SQLException e){
			throw new DaoException("Erreur lors de la recherche d'un véhicule grâce à son identifiant.");
		}finally{
			if(rs!=null){
				try{
					rs.close();
				}catch(SQLException e){
					System.err.println("Une erreur est survenue lors de la fermeture du ResultSet.");
				}
			}
		}
		return Optional.ofNullable(vehicle);
	}

	public Optional<List<Vehicle>> findAll() throws DaoException {
		ResultSet rs = null;
		List<Vehicle> vehicles = new ArrayList<>();
		try(Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(FIND_VEHICLES_QUERY)){

			rs = ps.executeQuery();

			while(rs.next()){
				Vehicle vehicle = new Vehicle();
				vehicle.setId(rs.getLong("id"));
				vehicle.setConstructeur(rs.getString("constructeur"));
				vehicle.setModele(rs.getString("modele"));
				vehicle.setNb_places(rs.getInt("nb_places"));
				vehicles.add(vehicle);
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
		return Optional.ofNullable(vehicles);
	}
}
