/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.camping;

import com.camping.dao.SqliteDatabaseHandler;
import com.camping.model.Client;
import com.camping.model.Parcel;
import com.camping.model.Reservation;
import com.camping.model.Reservation.STATUS;
import java.util.List;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author blj0011
 */
public class DataModel {
    private final ObservableList<Parcel> parcelList = FXCollections.observableArrayList(model -> new Observable[]{model.nameProperty(), model.occupiedProperty()});
    private final ObjectProperty<Parcel> currentParcel = new SimpleObjectProperty();
    
    public ObjectProperty<Parcel> currentParcelProperty(){
        return this.currentParcel;
    }
    
    public final void setCurrentParcel(Parcel parcel){
        currentParcel.set(parcel);
    }
    
    public ObservableList<Parcel> getParcelList(){
        return parcelList;
    }
    
    private final ObservableList<Client> clientList = FXCollections.observableArrayList(model -> 
            new Observable[]{model.firstNameProperty(), model.lastNameProperty(), model.phoneNumberProperty(), model.dniProperty(), model.vehiclePlateNumberProperty()});
    private final ObjectProperty<Client> currentClient = new SimpleObjectProperty();
    
    public ObjectProperty<Client> currentClientProperty(){
        return this.currentClient;
    }
    
    public final void setCurrentClient(Client client){
        currentClient.set(client);
    }
    
    public ObservableList<Client> getClientList(){
        return clientList;
    }
    
    
    //DB Operations!
    public List<Parcel> dbGetAllParcels(){        
        SqliteDatabaseHandler sqliteDatabaseHandler = new SqliteDatabaseHandler();        
        List<Parcel> parcels = sqliteDatabaseHandler.getAllParcels();        
        sqliteDatabaseHandler.closeConnection();
        
        return parcels;
    } 
    
    public boolean addNewParcel(Parcel parcel){
        SqliteDatabaseHandler sqliteDatabaseHandler = new SqliteDatabaseHandler();          
        boolean control = sqliteDatabaseHandler.addNewParcel(parcel);       
        sqliteDatabaseHandler.closeConnection();
        
        return control;
    }
    
    public boolean updateParcelOccupied(Parcel parcel){
        SqliteDatabaseHandler sqliteDatabaseHandler = new SqliteDatabaseHandler();
        boolean control = sqliteDatabaseHandler.updateParcelOccupied(parcel);
        sqliteDatabaseHandler.closeConnection();
        
        return control;        
    }
    
    public boolean deleteParcel(Parcel parcel){
        SqliteDatabaseHandler sqliteDatabaseHandler = new SqliteDatabaseHandler();
        boolean control = sqliteDatabaseHandler.deleteParcel(parcel);
        sqliteDatabaseHandler.closeConnection();
        
        return control;
    }
    
    
    
    public List<Client> dbGetAllClients(){        
        SqliteDatabaseHandler sqliteDatabaseHandler = new SqliteDatabaseHandler();        
        List<Client> clients = sqliteDatabaseHandler.getAllClients();        
        sqliteDatabaseHandler.closeConnection();
        
        return clients;
    }     
    
    public boolean addNewClient(Client client){
        SqliteDatabaseHandler sqliteDatabaseHandler = new SqliteDatabaseHandler();          
        boolean control = sqliteDatabaseHandler.addNewClient(client);        
        sqliteDatabaseHandler.closeConnection();
        
        return control;
    }
    
    public boolean updateClient(Client client){
        SqliteDatabaseHandler sqliteDatabaseHandler = new SqliteDatabaseHandler();
        boolean control = sqliteDatabaseHandler.updateClient(client);
        sqliteDatabaseHandler.closeConnection();
        
        return control;
    }
    
    public boolean deleteClient(Client client){
        SqliteDatabaseHandler sqliteDatabaseHandler = new SqliteDatabaseHandler();
        boolean control = sqliteDatabaseHandler.deleteClient(client);
        sqliteDatabaseHandler.closeConnection();
        
        return control;
    }
    
    
    public List<Reservation> dbGetAllReservations(){        
        SqliteDatabaseHandler sqliteDatabaseHandler = new SqliteDatabaseHandler();        
        List<Reservation> reservations = sqliteDatabaseHandler.getAllReservations();        
        sqliteDatabaseHandler.closeConnection();
        
        return reservations;
    }    
    
    public boolean updateReservationStatus(Client client, Parcel parcel, STATUS oldStatus, STATUS newStatus){
        SqliteDatabaseHandler sqliteDatabaseHandler = new SqliteDatabaseHandler();
        boolean control = sqliteDatabaseHandler.updateReservationStatus(client, parcel, oldStatus, newStatus);
        sqliteDatabaseHandler.closeConnection();
        
        return control;
    }
    
    public boolean addNewReservation(Reservation reservation){
        SqliteDatabaseHandler sqliteDatabaseHandler = new SqliteDatabaseHandler();          
        boolean control = sqliteDatabaseHandler.addNewReservation(reservation);  
        sqliteDatabaseHandler.closeConnection();
        
        return control;
    }
    
    public boolean updateReservation(Reservation reservation){
        SqliteDatabaseHandler sqliteDatabaseHandler = new SqliteDatabaseHandler();
        boolean control = sqliteDatabaseHandler.updateReservation(reservation);
        sqliteDatabaseHandler.closeConnection();
        
        return control;
    }
    
    public boolean deleteReservation(Reservation reservation){
        SqliteDatabaseHandler sqliteDatabaseHandler = new SqliteDatabaseHandler();
        boolean control = sqliteDatabaseHandler.deleteReservation(reservation);
        sqliteDatabaseHandler.closeConnection();
        
        return control;
    }
    
    public int getLastTableId(String tableName){
        SqliteDatabaseHandler sqliteDatabaseHandler = new SqliteDatabaseHandler();
        int lastId = sqliteDatabaseHandler.getLastIDFromTable(tableName);
        sqliteDatabaseHandler.closeConnection();
        
        return lastId;
    }
}
