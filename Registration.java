package com.car;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity {

    Button callLogin, register_btn;
    ImageView image;
    TextView logo_name, slogantext;
    TextInputLayout reg_name, reg_username, reg_email, reg_phone, reg_password;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        callLogin = findViewById(R.id.login);
        image = findViewById(R.id.logoImage);
        logo_name = findViewById(R.id.logoname);
        slogantext = findViewById(R.id.slogan_name);
        reg_username = findViewById(R.id.username);
        reg_password = findViewById(R.id.password);
        register_btn = findViewById(R.id.Register);
        reg_name = findViewById(R.id.name);
        reg_email = findViewById(R.id.email);
        reg_phone = findViewById(R.id.phone);
        mAuth = FirebaseAuth.getInstance();

        callLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Registration.this, LogIn.class);

                Pair[] pairs = new Pair[10];

                pairs[0] = new Pair<View, String>(image, "logo_image");
                pairs[1] = new Pair<View, String>(logo_name, "logo_name");
                pairs[2] = new Pair<View, String>(slogantext, "logo_desc");
                pairs[3] = new Pair<View, String>(reg_username, "user_trans");
                pairs[4] = new Pair<View, String>(reg_password, "pass_trans");
                pairs[5] = new Pair<View, String>(register_btn, "register_trans");
                pairs[6] = new Pair<View, String>(callLogin, "login_trans");
                pairs[7] = new Pair<View, String>(reg_name, "name_trans");
                pairs[8] = new Pair<View, String>(reg_email, "email_trans");
                pairs[9] = new Pair<View, String>(reg_phone, "phone_trans");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Registration.this, pairs);
                startActivity(intent, options.toBundle());
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = reg_name.getEditText().getText().toString();
                String username = reg_username.getEditText().getText().toString();
                String mail = reg_email.getEditText().getText().toString();
                String phone = reg_phone.getEditText().getText().toString();
                String password = reg_password.getEditText().getText().toString();

                if (!name.isEmpty()) {
                    reg_name.setError(null);
                    reg_name.setErrorEnabled(false);
                    if (!username.isEmpty() && username.length() <= 15) {
                        reg_username.setError(null);
                        reg_username.setErrorEnabled(false);
                        if (!mail.isEmpty() && isValidEmail(mail)) {
                            reg_email.setError(null);
                            reg_email.setErrorEnabled(false);
                            if (!phone.isEmpty() && phone.length() == 10 && isNumeric(phone)) {
                                reg_phone.setError(null);
                                reg_phone.setErrorEnabled(false);
                                if (!password.isEmpty() && isValidPassword(password)) {
                                    reg_password.setError(null);
                                    reg_password.setErrorEnabled(false);

                                    rootNode = FirebaseDatabase.getInstance();
                                    reference = rootNode.getReference("Users");

                                    String name_s = reg_name.getEditText().getText().toString();
                                    String username_s = reg_username.getEditText().getText().toString();
                                    String mail_s = reg_email.getEditText().getText().toString();
                                    String phone_s = reg_phone.getEditText().getText().toString();
                                    String password_s = reg_password.getEditText().getText().toString();

                                    UserHelperClass helperClass = new UserHelperClass(name_s, username_s, mail_s, phone_s, password_s);

                                    reference.child(phone).setValue(helperClass);
                                    registerNewUser();
                                    Toast.makeText(getApplicationContext(), "Register Successfully", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(getApplicationContext(), LogIn.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    reg_password.setError("Password should contain at least one uppercase letter, one lowercase letter, and one special character.");
                                }
                            } else {
                                reg_phone.setError("Please enter a valid 10-digit phone number");
                            }
                        } else {
                            reg_email.setError("Please enter a valid email address");
                        }
                    } else {
                        reg_username.setError("Please enter a username with a maximum of 15 characters");
                    }
                } else {
                    reg_name.setError("Please enter full name");
                }
            }
        });
    }
    private void registerNewUser() {
        String email, password;
        email = reg_email.getEditText().getText().toString();
        password = reg_password.getEditText().getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Invalid email or password", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Firebase Registration successful!", Toast.LENGTH_LONG).show();
                            // Add code here to handle successful registration, if needed
                        } else {
                            Toast.makeText(getApplicationContext(), "Firebase Registration failed!!", Toast.LENGTH_LONG).show();
                            Log.e("FirebaseAuth", "Error: " + task.getException().getMessage());
                        }
                    }
                });
    }




    private boolean isValidEmail(CharSequence target) {
        return Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        return password.matches(passwordPattern);
    }

    private boolean isNumeric(String str) {
        return str.matches("\\d+");
    }
}
