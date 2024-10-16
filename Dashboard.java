package com.car;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Dashboard extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private List<ParkItem> parkItemList;
    private ParkAdapter parkAdapter;
    private TextView dateTextView, timeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        dateTextView = findViewById(R.id.dateTextView);
        timeTextView = findViewById(R.id.timeTextView);

        updateTime();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("parkedCars");

        parkItemList = new ArrayList<>();
        parkAdapter = new ParkAdapter(parkItemList);

        RecyclerView recyclerView = findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(parkAdapter);

        Button parkBtn = findViewById(R.id.ParkBtn);
        parkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, ParkCar.class);
                startActivity(intent);
            }
        });

        // Set OnClickListener for the profile image
        ImageView profileImage = findViewById(R.id.profileImage);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open ProfileDetails activity
                Intent profileIntent = new Intent(Dashboard.this, UserProfileActivity.class);
                startActivity(profileIntent);
            }
        });

        fetchDataFromFirebase();
    }

    private void updateTime() {
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                // Get current date and time
                Date currentDate = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM dd, yyyy", Locale.getDefault());
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

                // Set date and time to TextViews
                dateTextView.setText(dateFormat.format(currentDate));
                timeTextView.setText(timeFormat.format(currentDate));

                // Update every second
                handler.postDelayed(this, 1000);
            }
        });
    }

    private void fetchDataFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                parkItemList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ParkItem parkItem = snapshot.getValue(ParkItem.class);
                    parkItemList.add(parkItem);

                    // Log the retrieved data
                    if (parkItem != null) {
                        Log.d("FirebaseData", "VehicleType: " + parkItem.getVehicleType());
                        Log.d("FirebaseData", "Amount: " + parkItem.getAmount());
                    }
                }

                // Notify the adapter that data has changed
                parkAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                Toast.makeText(Dashboard.this, "Failed to fetch data from Firebase", Toast.LENGTH_SHORT).show();
                Log.e("FirebaseError", "Error: " + databaseError.getMessage());
            }
        });
    }

    // Adapter for RecyclerView
    private class ParkAdapter extends RecyclerView.Adapter<ParkViewHolder> {

        private List<ParkItem> parkItemList;

        public ParkAdapter(List<ParkItem> parkItemList) {
            this.parkItemList = parkItemList;
        }

        @NonNull
        @Override
        public ParkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.park_item, parent, false);
            return new ParkViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ParkViewHolder holder, int position) {
            ParkItem parkItem = parkItemList.get(position);
            holder.bind(parkItem);
        }

        @Override
        public int getItemCount() {
            return parkItemList.size();
        }
    }

    // ViewHolder for RecyclerView
    private static class ParkViewHolder extends RecyclerView.ViewHolder {
        private TextView vehicleTypeTextView;
        private TextView numberPlateTextView;
        private TextView driverNameTextView;
        private TextView dateTextView;
        private TextView amountTextView;
        private TextView locationTextView; // Added location TextView

        public ParkViewHolder(@NonNull View itemView) {
            super(itemView);
            vehicleTypeTextView = itemView.findViewById(R.id.VehicleType);
            numberPlateTextView = itemView.findViewById(R.id.NumberPlate);
            driverNameTextView = itemView.findViewById(R.id.DriverName);
            dateTextView = itemView.findViewById(R.id.DriverNumber);
            amountTextView = itemView.findViewById(R.id.Amount);
            locationTextView = itemView.findViewById(R.id.LocationSpinner); // Initialize location TextView
        }

        public void bind(ParkItem parkItem) {
            vehicleTypeTextView.setText(parkItem.getVehicleType());
            numberPlateTextView.setText(parkItem.getNumberPlate());
            driverNameTextView.setText(parkItem.getDriverName());
            dateTextView.setText(parkItem.getDriverNumber());
            amountTextView.setText(parkItem.getAmount());
            locationTextView.setText(parkItem.getLocation()); // Set location text
        }
    }
}
