package com.jtdev.knowsalot;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerProfileScreen extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView userPic;
    private TextView userNameTextView;
    private TextView userEmailTextView;
    private TextView changePassTextView;
    PieChart pieChart;
    Button backbutton,logoutbutton;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_profile_screen);

        backbutton = findViewById(R.id.homeBtn);
        logoutbutton = findViewById(R.id.logoutBtn);
        userPic = findViewById(R.id.userPic);
        userNameTextView = findViewById(R.id.userName);
        userEmailTextView = findViewById(R.id.userEmail);
        changePassTextView = findViewById(R.id.changePass);
        pieChart = findViewById(R.id.analyticsPie);


        firebaseStorage = FirebaseStorage.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            firestore.collection("quiz_results")
                    .document(userId)
                    .get()  // Fetch user quiz results
                    .addOnSuccessListener(this::updatePieChart)  // Pass the document snapshot to updatePieChart method
                    .addOnFailureListener(e -> {
                        // If there's an error fetching the data, you can handle it here
                    });
        }

        if (currentUser != null) {
            String userId = currentUser.getUid();


            // Fetch user data from Firestore
            firestore.collection("users")
                    .document(userId)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    String userName = document.getString("username");
                                    String userEmail = document.getString("email");
                                    String profileImage = document.getString("profileImage");

                                    userNameTextView.setText(userName);
                                    userEmailTextView.setText(userEmail);

                                    // Load the profile image if available
                                    if (profileImage != null && !profileImage.isEmpty()) {
                                        loadImage(profileImage);
                                    }
                                } else {
                                    Toast.makeText(PlayerProfileScreen.this, "No user data found", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(PlayerProfileScreen.this, "Error fetching user data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

        changePassTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog();
            }
        });

        userPic.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayerProfileScreen.this,PlayerScreen.class);
                startActivity(intent);
            }
        });

       logoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayerProfileScreen.this, HomeScreen.class);
                showLogoutConfirmationDialog();
            }
        });

        pieChart = findViewById(R.id.analyticsPie);

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(80f,"Math"));
        entries.add(new PieEntry(50f,"English"));
        entries.add(new PieEntry(30f,"History"));
        entries.add(new PieEntry(10f,"P.E"));
        entries.add(new PieEntry(70f,"Science"));
        entries.add(new PieEntry(20f,"Music"));

        PieDataSet pieDataSet = new PieDataSet(entries,"Subjects");
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);

        pieChart.getDescription().setEnabled(false);
        pieChart.animateY(2000);
        pieChart.invalidate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();

            if (imageUri != null) {
                userPic.setImageURI(imageUri); // Display the image
                uploadImageToFirebase(imageUri); // Upload to Firebase
            } else {
                Toast.makeText(this, "Failed to retrieve image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updatePieChart(DocumentSnapshot documentSnapshot) {
        ArrayList<PieEntry> pieEntries = new ArrayList<>();

        if (documentSnapshot.exists()) {
            Map<String, Object> data = documentSnapshot.getData();
            if (data != null) {
                for (String subject : data.keySet()) {
                    Object scoreListObj = data.get(subject);
                    if (scoreListObj instanceof List) {
                        List<?> scoreList = (List<?>) scoreListObj;
                        int totalScore = 0;
                        int totalQuestions = 0;

                        for (Object scoreObj : scoreList) {
                            if (scoreObj instanceof Map) {
                                Map<String, Object> scoreData = (Map<String, Object>) scoreObj;
                                int score = (int) ((Long) scoreData.get("score")).intValue();
                                int questions = (int) ((Long) scoreData.get("totalQuestions")).intValue();

                                totalScore += score;
                                totalQuestions += questions;
                            }
                        }

                        if (totalQuestions > 0) {
                            float overallPercentage = (float) totalScore / totalQuestions * 100;  // Calculate the percentage
                            pieEntries.add(new PieEntry(overallPercentage, subject)); // Use the percentage as the pie chart value
                        }
                    }
                }
            }
        }

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Overall Percentage");
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.animateY(2000); // Optional animation
        pieChart.invalidate();  // Refresh the pie chart to show updated data
    }




    private void uploadImageToFirebase(Uri imageUri) {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            StorageReference storageRef = firebaseStorage.getReference().child("profileImages/" + userId + "_" + UUID.randomUUID().toString());

            storageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        storageRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                            firestore.collection("users")
                                    .document(userId)
                                    .update("profileImage", downloadUri.toString())
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(this, "Profile image updated", Toast.LENGTH_SHORT).show();
                                        loadImage(downloadUri.toString());  // Reload the image
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Failed to update profile image URL", Toast.LENGTH_SHORT).show();
                                        Log.e("Upload", "Error updating Firestore", e);
                                    });
                        }).addOnFailureListener(e -> {
                            Toast.makeText(this, "Failed to get download URL", Toast.LENGTH_SHORT).show();
                            Log.e("Upload", "Error getting download URL", e);
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to upload image " + e.getMessage(), Toast.LENGTH_SHORT).show();

                    });
        }
    }



    private void loadImage(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this)
                    .load(imageUrl)
                    .into(userPic);
        }
    }


    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_dialog_change_password, null);
        builder.setView(dialogView);

        EditText currentPasswordEditText = dialogView.findViewById(R.id.currentPassword);
        EditText newPasswordEditText = dialogView.findViewById(R.id.newPassword);
        Button changePasswordBtn = dialogView.findViewById(R.id.changePasswordBtn);
        Button cancelBtn = dialogView.findViewById(R.id.cancelBtn);

        AlertDialog dialog = builder.create();

        changePasswordBtn.setOnClickListener(v -> {
            String currentPassword = currentPasswordEditText.getText().toString();
            String newPassword = newPasswordEditText.getText().toString();

            if (currentPassword.isEmpty() || newPassword.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            changePassword(currentPassword, newPassword, dialog);
        });

        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void changePassword(String currentPassword, String newPassword, AlertDialog dialog) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);
            user.reauthenticate(credential).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    user.updatePassword(newPassword).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            Toast.makeText(this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            firebaseAuth.signOut();
                            startActivity(new Intent(PlayerProfileScreen.this, HomeScreen.class));
                            finish();
                        } else {
                            Toast.makeText(this, "Password change failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        firebaseAuth.signOut();
                        Intent intent = new Intent(PlayerProfileScreen.this, HomeScreen.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }


}