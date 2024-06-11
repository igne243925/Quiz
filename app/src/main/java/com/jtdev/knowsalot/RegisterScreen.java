package com.jtdev.knowsalot;

import android.content.Intent;
import android.graphics.RegionIterator;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class RegisterScreen extends AppCompatActivity {

    EditText username, rEmail, rPassword;
    TextView textlogin;
    Button regbutton;
    Button adminRegButton;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        username = findViewById(R.id.userName);
        rEmail = findViewById(R.id.userEmail);
        rPassword = findViewById(R.id.userPassword);
        textlogin = findViewById(R.id.text2);
        regbutton = findViewById(R.id.registerBtn);
        adminRegButton = findViewById(R.id.admin_registerBtn);


// Register Button for Student
        regbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser("student");
            }
        });

// Register Button for Teacher
        adminRegButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser("admin");
            }
        });

// Go to Login Screen
        textlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterScreen.this,LoginScreen.class);
                startActivity(intent);
                finish();
            }
        });
    }

// Toast if the user did not put information
void registerUser(final String userType) {
        String userEmail = String.valueOf(rEmail.getText());
        String userPassword = String.valueOf(rPassword.getText());
        String userName = String.valueOf(username.getText());

        if (TextUtils.isEmpty(userName)) {
            Toast.makeText(RegisterScreen.this, "Enter username", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(userEmail)) {
            Toast.makeText(RegisterScreen.this, "Enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(userPassword)) {
            Toast.makeText(RegisterScreen.this, "Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }


// Account Save to Firebase
        firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            String userId = user.getUid();

                            Map<String, Object> userData = new HashMap<>();
                            userData.put("username", userName);
                            userData.put("email", userEmail);
                            userData.put("role", userType);  // Set role based on userType

                            firestore.collection("users")
                                    .document(userId)
                                    .set(userData, SetOptions.merge())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                String successMessage = userType.equals("admin") ? "Register as Admin Successful!" : "Register as Student Successful!";
                                                Toast.makeText(RegisterScreen.this, successMessage, Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(RegisterScreen.this, LoginScreen.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(RegisterScreen.this, "Error storing user data", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(RegisterScreen.this, "Registration failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}