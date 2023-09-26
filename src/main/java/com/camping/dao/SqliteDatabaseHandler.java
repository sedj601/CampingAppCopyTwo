/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.camping.dao;

import com.camping.model.Client;
import com.camping.model.Parcel;
import com.camping.model.Reservation;
import com.camping.model.Reservation.STATUS;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author blj0011
 */
public class SqliteDatabaseHandler implements AutoCloseable
{

    Connection connection;

    public SqliteDatabaseHandler(){
        try {
            
            connection = DriverManager.getConnection("jdbc:sqlite:main.sqlite3");
            connection.createStatement().execute("PRAGMA foreign_keys = ON");
            System.out.println("Connected to SQLite Db: main.sqlite3");
        }
        catch (SQLException ex) {
            System.out.println(ex.toString());
        }
    }

    public List<Parcel> getAllParcels(){
        String query = "SELECT * FROM parcel";
        List<Parcel> data = new ArrayList();

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Parcel parcel = new Parcel(
                        resultSet.getInt("parcel_id"), 
                        resultSet.getString("parcel_name"), 
                        resultSet.getInt("is_occupied") == 1);
                
                data.add(parcel);
            }
        }
        catch (SQLException ex) {
            System.out.println(ex.toString());
        }

        return data;
    }

    public boolean addNewParcel(Parcel parcel){
        String sqlQuery = "INSERT INTO parcel(parcel_name, is_occupied) VALUES (?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
            pstmt.setString(1, parcel.getName());
            pstmt.setInt(2, parcel.isOccupied()? 1 : 0);
            pstmt.executeUpdate();

            return true;
        }
        catch (SQLException ex) {
            System.out.println(ex.toString());
            return false;
        }
    }    

    public boolean deleteParcel(Parcel parcel){
        String sqlQuery = "DELETE FROM parcel WHERE parcel_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
            pstmt.setInt(1, parcel.getId());
            pstmt.executeUpdate();

            return true;
        }
        catch (SQLException ex) {
            System.out.println(ex.toString());
            return false;
        }
    }

    public boolean updateParcelOccupied(Parcel parcel){
        String sqlQuery = "UPDATE parcel SET is_occupied = ? WHERE parcel_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
            pstmt.setInt(1, parcel.isOccupied()? 1 : 0);
            pstmt.setInt(2, parcel.getId());
            pstmt.executeUpdate();

            return true;
        }
        catch (SQLException ex) {
            System.out.println(ex.toString());
            return false;
        }
    }
    
    
    
    public List<Client> getAllClients(){
        String query = "SELECT * FROM client";
        List<Client> data = new ArrayList();

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Client client = new Client(
                        resultSet.getInt("client_id"), 
                        resultSet.getString("first_name"), 
                        resultSet.getString("last_name"),
                        resultSet.getString("phone_number"), 
                        resultSet.getString("dni"),
                        resultSet.getString("vehicle_plate_number")
                );
                
                data.add(client);
            }
        }
        catch (SQLException ex) {
            System.out.println(ex.toString());
        }

        return data;
    }
    
    public boolean addNewClient(Client client){
        String sqlQuery = "INSERT INTO client(first_name, last_name, phone_number, dni, vehicle_plate_number) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
            pstmt.setString(1, client.getFirstName());
            pstmt.setString(2, client.getLastName());
            pstmt.setString(3, client.getPhoneNumber());
            pstmt.setString(4, client.getDni());
            pstmt.setString(5, client.getVehiclePlateNumber());
            pstmt.executeUpdate();

            return true;
        }
        catch (SQLException ex) {
            System.out.println("add new Client error: \n "+ ex.toString());
            return false;
        }
    }    

    public boolean deleteClient(Client client){
        String sqlQuery = "DELETE FROM client WHERE client_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
            pstmt.setInt(1, client.getId());
            pstmt.executeUpdate();

            return true;
        }
        catch (SQLException ex) {
            System.out.println("delete client error:\n"  + ex.toString());
            return false;
        }
    }
    
    public boolean updateClient(Client client){
        String sqlQuery = "UPDATE client SET first_name = ?, last_name = ?, phone_number = ?, dni = ?, vehicle_plate_number = ? WHERE client_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
            pstmt.setString(1, client.getFirstName());
            pstmt.setString(2, client.getLastName());
            pstmt.setString(3, client.getPhoneNumber());
            pstmt.setString(4, client.getDni());
            pstmt.setString(5, client.getVehiclePlateNumber());
            pstmt.setInt(6, client.getId());
            pstmt.executeUpdate();
            return true;
        }
        catch (SQLException ex) {
            System.out.println("udpate client error:\n"  + ex.toString());
            return false;
        }
    }
    
    public Client getClient(Reservation reservation){
        String query = "SELECT client.client_id, client.first_name, client.last_name, client.phone_number, client.dni, client.vehicle_plate_number FROM reservation LEFT JOIN client ON reservation.client_id = client.client_id WHERE reservation_id = 3";
                
        

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Client client = new Client(
                        resultSet.getInt("client_id"), 
                        resultSet.getString("first_name"), 
                        resultSet.getString("last_name"),
                        resultSet.getString("phone_number"), 
                        resultSet.getString("dni"),
                        resultSet.getString("vehicle_plate_number")
                );
                
                return client;
            }
        }
        catch (SQLException ex) {
            System.out.println(ex.toString());
        }        
        
        Client dummyClient = new Client(-1, "-1", "-1", "-1", "-1", "-1");
        
        return dummyClient;
    }
    
    
    
    public List<Reservation> getAllReservations(){
        String query = "SELECT * FROM reservation";
        List<Reservation> data = new ArrayList();

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Reservation reservation = new Reservation(
                        resultSet.getInt("reservation_id"), 
                        resultSet.getInt("client_id"), 
                        resultSet.getInt("parcel_id"),
                        LocalDateTime.parse(resultSet.getString("checkin_time"), Reservation.DATE_FORMATTER), 
                        LocalDateTime.parse(resultSet.getString("checkout_time"), Reservation.DATE_FORMATTER),
                        Reservation.STATUS.convertIntToStatus(resultSet.getInt("status"))
                );
                
                data.add(reservation);
            }
        }
        catch (SQLException ex) {
            System.out.println("get all reservations error:\n"  + ex.toString());
        }

        return data;
    }
    
    public boolean updateReservationStatus(Client client, Parcel parcel, STATUS oldStatus, STATUS newStatus){
        String sqlQuery = "UPDATE reservation SET status = ? WHERE client_id = ? AND parcel_id = ? AND status = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
            pstmt.setInt(1, STATUS.convertStatusToInt(newStatus));
            pstmt.setInt(2, client.getId());
            pstmt.setInt(3, parcel.getId());
            pstmt.setInt(4, STATUS.convertStatusToInt(oldStatus));
            pstmt.executeUpdate();
            return true;
        }
        catch (SQLException ex) {
            System.out.println("updateReservationStatus error:\n"  + ex.toString());
            return false;
        }
    }
    
    public boolean addNewReservation(Reservation reservation){
        String sqlQuery = "INSERT INTO reservation(client_id, parcel_id, checkin_time, checkout_time, status) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
            pstmt.setInt(1, reservation.getClientId());
            pstmt.setInt(2, reservation.getParcelId());
            pstmt.setString(3, reservation.getCheckinTime().format(Reservation.DATE_FORMATTER));
            pstmt.setString(4, reservation.getCheckoutTime().format(Reservation.DATE_FORMATTER));
            pstmt.setInt(5, Reservation.STATUS.convertStatusToInt(reservation.getStatus()));
            pstmt.executeUpdate();

            return true;
        }
        catch (SQLException ex) {
            System.out.println("add new reservation error: \n "+ ex.toString());
            return false;
        }
    }    

    public boolean deleteReservation(Reservation reservation){
        String sqlQuery = "DELETE FROM client WHERE client_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
            pstmt.setInt(1, reservation.getReservationId());
            pstmt.executeUpdate();

            return true;
        }
        catch (SQLException ex) {
            System.out.println("delete reservation error:\n"  + ex.toString());
            return false;
        }
    }
    
    public boolean updateReservation(Reservation reservation){
        String sqlQuery = "UPDATE reservation SET client_id = ?, parcel_id = ?, checkin_time = ?, checkout_time = ?, reservation_status = ? WHERE reservation_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
            pstmt.setInt(1, reservation.getClientId());
            pstmt.setInt(2, reservation.getParcelId());
            pstmt.setString(3, reservation.getCheckinTime().format(Reservation.DATE_FORMATTER));
            pstmt.setString(4, reservation.getCheckoutTime().format(Reservation.DATE_FORMATTER));
            pstmt.setInt(5, Reservation.STATUS.convertStatusToInt(reservation.getStatus()));
            pstmt.setInt(6, reservation.getReservationId());
            pstmt.executeUpdate();
            return true;
        }
        catch (SQLException ex) {
            System.out.println("update reservation error:\n"  + ex.toString());
            return false;
        }
    }
    
    
    
    
    public int getLastIDFromTable(String tableName)
    {
        String sqlQuery = "SELECT seq FROM sqlite_sequence WHERE name = ?";

        try{
            PreparedStatement pstmt = connection.prepareStatement(sqlQuery);
            pstmt.setString(1, tableName);
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                return resultSet.getInt("seq");
            }
        }
        catch (SQLException ex) {
            System.out.println("getLastIDFrom " + tableName + " error:");
            System.out.println("\t" + ex.toString());
            
            return -1;
        }
        
        return -1;
    }

    public void closeConnection()
    {
        try {
            connection.close();
            System.out.println("Closed connection to SQLite Db: main.sqlite3");
        }
        catch (SQLException ex) {
            System.out.println(ex.toString());
        }
    }

    @Override
    public void close() throws Exception {
        closeConnection();
    }
    
    
    
}