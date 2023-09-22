/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.camping.model;

import java.util.Objects;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author blj0011
 */
public class Parcel {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty name = new SimpleStringProperty();
    private final BooleanProperty occupied = new SimpleBooleanProperty();

    public Parcel(int id, String name, boolean isOccupied) {
        this.id.set(id);
        this.name.set(name);
        this.occupied.set(isOccupied);
    }
    
    public Parcel(String name) {
        this.id.set(-1);
        this.name.set(name);
        this.occupied.set(false);
    }

    public int getId() {
        return this.id.getValue();
    }

    public void setId(int id) {
        this.id.set(id);
    }
    
    public IntegerProperty idProperty(){
        return this.id;
    }
    
    public String getName(){
        return this.name.get();
    }
    
    public void setName(String name){
        this.name.set(name);
    }    

    public StringProperty nameProperty(){
        return this.name;
    }
    
    public boolean isOccupied() {
        return occupied.getValue();
    }

    public void setOccupied(boolean isOccupied) {
        this.occupied.set(isOccupied);
    }
    
    public BooleanProperty occupiedProperty(){
        return this.occupied;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Parcel{");
        sb.append("id=").append(id.get());
        sb.append(", name=").append(name.get());
        sb.append(", occupied=").append(occupied.get());
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.id.get());
        hash = 73 * hash + Objects.hashCode(this.name.get());
        hash = 73 * hash + Objects.hashCode(this.occupied.get());
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
        final Parcel other = (Parcel) obj;
        if (this.id.get() != other.id.get()) {
            return false;
        }
        if (!this.name.get().equals(other.name.get())) {
            return false;
        }
        
        return this.occupied.get() == other.occupied.get();
    }

    

   
      
}
