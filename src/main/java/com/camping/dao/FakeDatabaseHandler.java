/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.camping.dao;

import com.camping.model.Parcel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author blj0011
 */
public class FakeDatabaseHandler {
    List<Parcel> parcels = new ArrayList();
    
    
    public FakeDatabaseHandler()
    {        
        for(int i = 0; i < 50; i++)
        {
            //parcels.add(new Parcel(Long.valueOf(i), ThreadLocalRandom.current().nextBoolean(), ThreadLocalRandom.current().nextLong()));
        }
    }
    
    
    public List<Parcel> getAllParcels()
    {
        return parcels;
    }
}
