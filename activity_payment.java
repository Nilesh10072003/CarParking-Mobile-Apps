package com.car;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class activity_payment extends AppCompatActivity {
    EditText cardNumberEditText, amountEditText,cvvEditText;
    Spinner expiryDateSpinner;
    Button payNowBtn;
    ImageView paymentImage;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Initialize views
        paymentImage = findViewById(R.id.paymentImage);
        cardNumberEditText = findViewById(R.id.cardNumberEditText);
        amountEditText = findViewById(R.id.amountEditText);
        expiryDateSpinner = findViewById(R.id.expiryDateSpinner);
        cvvEditText = findViewById(R.id.CvvEditText);
        payNowBtn = findViewById(R.id.payNowBtn);

        // Initialize Firebase Database
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("payments");

        // Setup Expiry Date Spinner
        setupExpiryDateSpinner();

        // Set OnClickListener for the Pay Now button
        payNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve values from EditText fields
                String cardNumber = cardNumberEditText.getText().toString().trim();
                String expiryDate = expiryDateSpinner.getSelectedItem().toString();
                String amount = amountEditText.getText().toString().trim();
                String cvv = cvvEditText.getText().toString().trim();

                // Check if any of the fields are empty
                if (cardNumber.isEmpty() || amount.isEmpty()) {
                    Toast.makeText(activity_payment.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else if (cardNumber.length() != 12) {
                    Toast.makeText(activity_payment.this, "Please enter a 12-digit card number", Toast.LENGTH_SHORT).show();
                } else if (cvv.length() != 3) {
                    Toast.makeText(activity_payment.this, "Please enter a 3-digit CVV number", Toast.LENGTH_SHORT).show();
                } else {
                    // Create Payment object
                    Payment payment = new Payment(cardNumber, expiryDate, amount,cvv);

                    // Store payment object in Firebase Database
                    reference.push().setValue(payment);

                    // Display success message
                    Toast.makeText(activity_payment.this, "Payment Successful!", Toast.LENGTH_SHORT).show();

                    // Redirect to Dashboard activity
                    Intent intent = new Intent(activity_payment.this, Dashboard.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void setupExpiryDateSpinner() {
        List<String> months = new ArrayList<>();
        List<String> years = new ArrayList<>();

        // Populate months list
        for (int i = 1; i <= 12; i++) {
            months.add(String.format("%02d", i)); // Add leading zero for single-digit months
        }

        // Populate years list (starting from current year)
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear; i < currentYear + 10; i++) {
            years.add(String.valueOf(i).substring(2)); // Get last two digits of the year
        }

        // Combine months and years lists into a single list for spinner adapter
        List<String> expiryDates = new ArrayList<>();
        for (String month : months) {
            for (String year : years) {
                expiryDates.add(month + "/" + year);
            }
        }

        // Create adapter for the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, expiryDates);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set adapter to the spinner
        expiryDateSpinner.setAdapter(adapter);
    }
}
