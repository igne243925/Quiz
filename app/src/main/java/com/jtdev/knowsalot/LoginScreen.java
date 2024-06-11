package com.jtdev.knowsalot;

import android.content.Intent;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginScreen extends AppCompatActivity {
    EditText email, password;
    Button loginadmin,loginstudent;
    TextView textregister;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        email = findViewById(R.id.userEmail);
        password = findViewById(R.id.userPassword);
        loginadmin = findViewById(R.id.adminBtn);
        loginstudent = findViewById(R.id.studentBtn);
        textregister = findViewById(R.id.text2);


 // Teacher Login
        loginadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser("admin");
            }
        });

 // Student Login
        loginstudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser("student");
            }
        });

// If the user want to register this will go the Registration Sreen Class
        textregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginScreen.this,RegisterScreen.class);
                startActivity(intent);
                finish();
            }
        });

    }

// Toast if the user did not put email and password
void loginUser(final String expectedRole) {
        String userEmail = String.valueOf(email.getText());
        String userPassword = String.valueOf(password.getText());

        if (TextUtils.isEmpty(userEmail)) {
            Toast.makeText(LoginScreen.this, "Enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(userPassword)) {
            Toast.makeText(LoginScreen.this, "Enter password", Toast.LENGTH_SHORT).show();
            return;
        }

// Account from Firebase   (It's either Teacher or Student)
        firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            String userId = user.getUid();

                            // Check user's role in Firestore to determine if login is correct
                            firestore.collection("users")
                                    .document(userId)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful() && task.getResult() != null) {
                                                String actualRole = task.getResult().getString("role");
                                                if (expectedRole.equals(actualRole)) {
                                                    // Role matches expected role, proceed with login
                                                    if (actualRole.equals("admin")) {
                                                        Intent intent = new Intent(LoginScreen.this, AdminScreen.class);
                                                        startActivity(intent);
                                                        finish();
                                                    } else if (actualRole.equals("student")) {
                                                        Intent intent = new Intent(LoginScreen.this, PlayerScreen.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                } else {
                                                    // Role doesn't match, show an error
                                                    Toast.makeText(LoginScreen.this, "Unauthorized access. Wrong login screen.", Toast.LENGTH_SHORT).show();
                                                    firebaseAuth.signOut(); // Sign out to prevent unauthorized access
                                                }
                                            } else {
                                                Toast.makeText(LoginScreen.this, "Error fetching user role", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(LoginScreen.this, "Login failed. Check your credentials.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}