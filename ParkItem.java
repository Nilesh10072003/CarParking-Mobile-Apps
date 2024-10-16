package com.car;

public class ParkItem {

    private String vehicleType;
    private String numberPlate;
    private String driverName;
    private String driverNumber;
    private String amount;

    private String location;

    // Constructors
    public ParkItem() {
        // Default constructor
    }

    public ParkItem(String vehicleType , String numberPlate, String driverName, String driverNumber, String amount,String location) {
        this.vehicleType = vehicleType;
        this.numberPlate = numberPlate;
        this.driverName = driverName;
        this.driverNumber = driverNumber;
        this.amount = amount;
        this.location=location;
    }

    // Getter and Setter methods

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverNumber() {
        return driverNumber;
    }

    public void setDriverNumber(String driverNumber) {
        this.driverNumber = driverNumber;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location){
        this.location=location;
    }


}

