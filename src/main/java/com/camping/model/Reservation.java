/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.camping.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author blj0011
 */
public class Reservation {
    static final public DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    IntegerProperty reservationId = new SimpleIntegerProperty();
    IntegerProperty clientId = new SimpleIntegerProperty();
    IntegerProperty parcelId = new SimpleIntegerProperty();
    StringProperty checkinTime = new SimpleStringProperty();
    StringProperty checkoutTime = new SimpleStringProperty();

    public Reservation(int reservationId, int clientId, int parcelId, LocalDateTime checkinTime, LocalDateTime checkoutTime) {
        this.reservationId.set(reservationId);
        this.clientId.set(clientId);
        this.parcelId.set(parcelId);
        this.checkinTime.set(checkinTime.format(DATE_FORMATTER));
        this.checkoutTime.set(checkoutTime.format(DATE_FORMATTER));
    }
    
    public Reservation(int clientId, int parcelId, LocalDateTime checkinTime, LocalDateTime checkoutTime) {
        reservationId.set(-1);
        this.clientId.set(clientId);
        this.parcelId.set(parcelId);
        this.checkinTime.set(checkinTime.format(DATE_FORMATTER));
        this.checkoutTime.set(checkoutTime.format(DATE_FORMATTER));
    }
    
    public int getReservationId(){
        return reservationId.get();
    }    
    
    public void setreservationId(int reservationId){
        this.reservationId.set(reservationId);
    }
    
    public IntegerProperty reservationIdProperty(){
        return this.reservationId;
    }
    
    
    public int getClientId(){
        return this.clientId.get();
    }
    
    public void setClientId(int clientId){
        this.clientId.set(clientId);
    }
    
    public IntegerProperty clientIdProperty(){
        return this.clientId;
    }
    
    
    public int getParcelId(){
        return this.parcelId.get();
    }
    
    public void setParcelId(int parcelId){
        this.parcelId.set(parcelId);
    }
    
    public IntegerProperty parcelIdProperty(){
        return this.parcelId;
    }
    
    
    public LocalDateTime getCheckinTime(){
        return LocalDateTime.parse(checkinTime.get(), DATE_FORMATTER);
    }
    
    public void setCheckinTime(LocalDateTime checkinTime){
        this.checkinTime.set(checkinTime.format(DATE_FORMATTER));
    }
    
    public StringProperty checkinTimeProperty(){
        return this.checkinTime;
    }
    
    
    public LocalDateTime getCheckoutTime(){
        return LocalDateTime.parse(checkoutTime.get(), DATE_FORMATTER);
    }   
    
    public void setCheckoutTime(LocalDateTime checkoutTime){
        this.checkoutTime.set(checkoutTime.format(DATE_FORMATTER));
    }  
    
    public StringProperty checkoutTimeProperty(){
        return this.checkoutTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Reservation{");
        sb.append("reservationId=").append(reservationId.get());
        sb.append(", clientId=").append(clientId.get());
        sb.append(", parcelId=").append(parcelId.get());
        sb.append(", checkinTime=").append(checkinTime.get());
        sb.append(", checkoutTime=").append(checkoutTime.get());
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.reservationId);
        hash = 41 * hash + Objects.hashCode(this.clientId);
        hash = 41 * hash + Objects.hashCode(this.parcelId);
        hash = 41 * hash + Objects.hashCode(this.checkinTime);
        hash = 41 * hash + Objects.hashCode(this.checkoutTime);
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
        final Reservation other = (Reservation) obj;
        if (this.reservationId.get() != other.reservationId.get()) {
            return false;
        }
        if (this.clientId.get() != other.clientId.get()) {
            return false;
        }
        if (this.parcelId.get() != other.parcelId.get()) {
            return false;
        }
        if (!LocalDateTime.parse(this.checkinTime.get()).isEqual(LocalDateTime.parse(other.checkinTime.get()))) {
            return false;
        }
        return LocalDateTime.parse(this.checkoutTime.get()).isEqual(LocalDateTime.parse(other.checkoutTime.get()));
    }
    
    
}
