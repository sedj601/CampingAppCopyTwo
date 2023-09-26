/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.camping.model;

import java.util.Arrays;
import java.util.Objects;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

 

public class Client {
    static public enum MODE {
        SELECT_CLIENT, EDIT_CLIENT;
    }
    
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty firstName = new SimpleStringProperty();
    private final StringProperty lastName = new SimpleStringProperty();
    private final StringProperty phoneNumber = new SimpleStringProperty();
    private final StringProperty dni = new SimpleStringProperty();
    private final StringProperty vehiclePlateNumber = new SimpleStringProperty();

    public Client(int id, String firstName, String lastName, String phoneNumber, String dni, String vehiclePlateNumber) {
        this.id.set(id);
        this.firstName.set(firstName);
        this.lastName.set(lastName);
        this.phoneNumber.set(phoneNumber);
        this.dni.set(dni);
        this.vehiclePlateNumber.set(vehiclePlateNumber);
    }

    public Client(String firstName, String lastName, String phoneNumber, String dni, String vehiclePlateNumber) {
        this.id.set(-1);
        this.firstName.set(firstName);
        this.lastName.set(lastName);
        this.phoneNumber.set(phoneNumber);
        this.dni.set(dni);
        this.vehiclePlateNumber.set(vehiclePlateNumber);
    }
    
    
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty(){
        return id;
    }
    
    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }
 
    public StringProperty firstNameProperty(){
        return firstName;
    }
    
    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }
    
    public StringProperty lastNameProperty(){
        return lastName;
    }
    
    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }
    
    public StringProperty phoneNumberProperty(){
        return phoneNumber;
    }
    
    public String getDni() {
        return dni.get();
    }

    public void setDni(String dni) {
        this.dni.set(dni);
    }

    public StringProperty dniProperty(){
        return dni;
    }
    
    public String getVehiclePlateNumber() {
        return vehiclePlateNumber.get();
    }

    public void setVehiclePlateNumber(String vehiclePlateNumber) {
        this.vehiclePlateNumber.set(vehiclePlateNumber);
    }
    
    public StringProperty vehiclePlateNumberProperty(){
        return vehiclePlateNumber;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Client{");
        sb.append("id=").append(id.get());
        sb.append(", firstName=").append(firstName.get());
        sb.append(", lastName=").append(lastName.get());
        sb.append(", phoneNumber=").append(phoneNumber.get());
        sb.append(", dni=").append(dni.get());
        sb.append(", vehiclePlateNumber=").append(vehiclePlateNumber.get());
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + Objects.hashCode(this.id.get());
        hash = 83 * hash + Objects.hashCode(this.firstName.get());
        hash = 83 * hash + Objects.hashCode(this.lastName.get());
        hash = 83 * hash + Objects.hashCode(this.phoneNumber.get());
        hash = 83 * hash + Objects.hashCode(this.dni.get());
        hash = 83 * hash + Objects.hashCode(this.vehiclePlateNumber.get());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Client other = (Client) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!this.firstName.equals(other.firstName)) {
            return false;
        }
        if (!this.lastName.equals(other.lastName)) {
            return false;
        }
        if (!this.phoneNumber.equals(other.phoneNumber)) {
            return false;
        }
        if (!this.dni.equals(other.dni)) {
            return false;
        }
        return this.vehiclePlateNumber.equals(other.vehiclePlateNumber);
    }
    
    
    
}
