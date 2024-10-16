package com.car;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        // Back to Dashboard Button
        Button btnBackToDashboard = findViewById(R.id.btnBack);
        btnBackToDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle back to dashboard action, for example:
                Intent intent = new Intent(AboutUsActivity.this, Dashboard.class);
                startActivity(intent);
            }
        });
    }
}
