package com.car;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ParkAdapter extends RecyclerView.Adapter<ParkAdapter.ParkViewHolder> {

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
        Log.d("parkedCars", "VehicalType: " + parkItem.getVehicleType());
        Log.d("parkedCars", "NumberPlate: " + parkItem.getNumberPlate());
        Log.d("parkedCars", "DriverName: " + parkItem.getDriverName());
        Log.d("parkedCars", "DriverNumber: " + parkItem.getDriverNumber());
        Log.d("parkedCars", "Amount: " + parkItem.getAmount());
        holder.bind(parkItem);
    }

    @Override
    public int getItemCount() {
        return parkItemList.size();
    }

    public static class ParkViewHolder extends RecyclerView.ViewHolder {
        private TextView vehicalTypeTextView;
        private TextView numberPlateTextView;
        private TextView driverNameTextView;
        private TextView dateTextView;
        private TextView amountTextView;

        public ParkViewHolder(@NonNull View itemView) {
            super(itemView);
            vehicalTypeTextView = itemView.findViewById(R.id.VehicleType);
            numberPlateTextView = itemView.findViewById(R.id.NumberPlate);
            driverNameTextView = itemView.findViewById(R.id.DriverName);
            dateTextView = itemView.findViewById(R.id.DriverNumber);
            amountTextView = itemView.findViewById(R.id.Amount);
        }

        public void bind(ParkItem parkItem) {
            vehicalTypeTextView.setText(parkItem.getVehicleType());
            numberPlateTextView.setText(parkItem.getNumberPlate());
            driverNameTextView.setText(parkItem.getDriverName());
            dateTextView.setText(parkItem.getDriverNumber());
            amountTextView.setText(parkItem.getAmount());
        }
    }
}

