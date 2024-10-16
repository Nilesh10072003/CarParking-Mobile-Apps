package com.car;
public class CarHelperClass {
    private String driverName;
    private String numberPlate;
    private String driverNumber;
    private String vehicleType;
    private String amount;
    private String location;

    // Default constructor for Firebase
    public CarHelperClass() {
    }

    // Constructor with parameters
    public CarHelperClass(String driverName, String numberPlate, String driverNumber, String vehicleType, String amount, String location) {
        this.driverName = driverName;
        this.numberPlate = numberPlate;
        this.driverNumber = driverNumber;
        this.vehicleType = vehicleType;
        this.amount = amount;
        this.location = location;
    }

    // Getter and setter methods
    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public String getDriverNumber() {
        return driverNumber;
    }

    public void setDriverNumber(String driverNumber) {
        this.driverNumber = driverNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
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

    public void setLocation(String location) {
        this.location = location;
    }
}
