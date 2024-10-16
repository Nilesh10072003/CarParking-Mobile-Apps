package com.car;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.NumberKeyListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ParkCar extends AppCompatActivity {

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    Button parkCarBtn;
    EditText driverNameEditText, numberPlateEditText, driverNumberEditText, amountEditText;
    Spinner vTypeSpinner, locationSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.park_car);

        // Initialize Firebase
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("parkedCars");

        parkCarBtn = findViewById(R.id.ParkCarBtn);

        driverNameEditText = findViewById(R.id.DriverName);
        numberPlateEditText = findViewById(R.id.NumberPlate);
        driverNumberEditText = findViewById(R.id.DriverNumber);
        amountEditText = findViewById(R.id.Amount);

        vTypeSpinner = findViewById(R.id.VehicleType);
        locationSpinner = findViewById(R.id.LocationSpinner);

        // Add InputFilters to enforce constraints on driverNumberEditText and numberPlateEditText
        addInputFilters();

        // Populate Spinner with vehicle type options
        populateVehicleTypeSpinner();

        // Populate Spinner with location options
        populateLocationSpinner();

        // Set default amount based on the selected vehicle type
        setDefaultAmount();

        // Button click listener
        parkCarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get data from EditText fields
                String driverName = driverNameEditText.getText().toString().trim();
                String numberPlate = numberPlateEditText.getText().toString().trim().toUpperCase(); // Convert to uppercase
                String driverNumber = driverNumberEditText.getText().toString().trim();
                String vehicleType = vTypeSpinner.getSelectedItem().toString().trim();
                String amount = amountEditText.getText().toString().trim();

                // Get selected location from Spinner
                String selectedLocation = locationSpinner.getSelectedItem().toString().trim();

                // Validate input fields
                if (validateInput(driverName, numberPlate, driverNumber)) {
                    // Create a Car object
                    CarHelperClass helperClass = new CarHelperClass(driverName, numberPlate, driverNumber, vehicleType, amount, selectedLocation);

                    // Push the data to the database
                    reference.push().setValue(helperClass);

                    // Clear EditText fields after pushing data
                    clearEditTextFields();

                    // Navigate to the dashboard screen
                    Intent intent = new Intent(ParkCar.this, activity_payment.class);
                    startActivity(intent);

                    Toast.makeText(getApplicationContext(), "Car parked successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addInputFilters() {
        // Set an InputFilter to allow only 10 digits for driverNumberEditText
        InputFilter[] filtersDriverNumber = new InputFilter[1];
        filtersDriverNumber[0] = new InputFilter.LengthFilter(10);
        driverNumberEditText.setFilters(filtersDriverNumber);

        // Set an InputFilter to allow only numbers for driverNumberEditText
        driverNumberEditText.setKeyListener(new NumberKeyListener() {
            protected char[] getAcceptedChars() {
                return "0123456789".toCharArray();
            }

            public int getInputType() {
                return InputType.TYPE_CLASS_NUMBER;
            }
        });

        // Set an InputFilter to allow only 10 digits for numberPlateEditText
        InputFilter[] filtersNumberPlate = new InputFilter[1];
        filtersNumberPlate[0] = new InputFilter.LengthFilter(10);
        numberPlateEditText.setFilters(filtersNumberPlate);
    }

    private void populateVehicleTypeSpinner() {
        // Create an array of vehicle type options
        String[] vehicleTypes = {"Car", "Motorcycle", "Truck", "Van"};

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, vehicleTypes);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        vTypeSpinner.setAdapter(adapter);
    }

    private void populateLocationSpinner() {
        // Create an array of location options
        String[] locations = {"Mulund West Slot No.1", "Mulund West Slot No.2","Mulund West Slot No.3","Mulund Est Slot No.1", "Mulund Est Slot No.2","Mulund Est Slot No.3","Kamgar Naka Slot No.1","Kamgar Naka Slot No.uu2","Kamgar Naka Slot No.3"};

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, locations);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        locationSpinner.setAdapter(adapter);
    }

    private void setDefaultAmount() {
        // Set default amount based on the selected vehicle type
        vTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedVType = parentView.getItemAtPosition(position).toString();

                // Set default amount based on the selected vehicle type
                switch (selectedVType) {
                    case "Car":
                        amountEditText.setText("100");
                        break;
                    case "Motorcycle":
                        amountEditText.setText("50");
                        break;
                    case "Truck":
                        amountEditText.setText("150");
                        break;
                    case "Van":
                        amountEditText.setText("120");
                        break;
                    default:
                        // Handle other cases if needed
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });
    }

    private boolean validateInput(String driverName, String numberPlate, String driverNumber) {
        if (driverName.isEmpty()) {
            driverNameEditText.setError("Driver name cannot be empty");
            return false;
        }

        if (numberPlate.length() != 10 || !numberPlate.matches("[A-Z0-9]+")) {
            numberPlateEditText.setError("Number plate must be 10 characters with uppercase letters and numbers only");
            return false;
        }

        if (driverNumber.length() != 10 || !driverNumber.matches("\\d+")) {
            driverNumberEditText.setError("Driver number must be 10 digits and numbers only");
            return false;
        }

        // Additional validations if needed...

        return true;
    }

    private void clearEditTextFields() {
        driverNameEditText.setText("");
        numberPlateEditText.setText("");
        driverNumberEditText.setText("");
        amountEditText.setText("");
    }
}
