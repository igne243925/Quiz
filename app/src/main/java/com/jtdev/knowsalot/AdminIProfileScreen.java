package com.jtdev.knowsalot;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class AdminIProfileScreen extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    Button homebutton,logoutbutton;
    TextView adminNameTextView;
    TextView adminEmailTextView;
    ImageView adminPicImageView;
    TextView adminChangePass;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_iprofile_screen);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        adminNameTextView = findViewById(R.id.adminName);
        adminEmailTextView = findViewById(R.id.userEmail);
        adminPicImageView = findViewById(R.id.adminPic);
        adminChangePass = findViewById(R.id.changePass);

        homebutton = findViewById(R.id.homeBtn);
        logoutbutton = findViewById(R.id.logoutBtn);

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            String adminId = currentUser.getUid();

            // Fetch admin data from Firestore
            firestore.collection("users")
                    .document(adminId)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    String adminName = document.getString("username");
                                    String adminEmail = document.getString("email");
                                    String profileImage = document.getString("profileImage");

                                    adminNameTextView.setText(adminName);
                                    adminEmailTextView.setText(adminEmail);

                                    // Load the profile image if available
                                    if (profileImage != null && !profileImage.isEmpty()) {
                                        loadImage(profileImage);
                                    }
                                } else {
                                    Toast.makeText(AdminIProfileScreen.this, "No admin data found", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(AdminIProfileScreen.this, "Error fetching admin data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            // Allow admin to change profile picture
            adminPicImageView.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            });
        }

 // Homebutton
        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminIProfileScreen.this, AdminScreen.class);
                startActivity(intent);
            }
        });


// Log out Btn
        logoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminIProfileScreen.this, HomeScreen.class);
                showLogoutConfirmationDialog();
            }
        });


        adminChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog();
            }
        });
    }




// Display picture from firebase
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();

            if (imageUri != null) {
                adminPicImageView.setImageURI(imageUri); // Display the image
                uploadImageToFirebase(imageUri); // Upload to Firebase
            } else {
                Toast.makeText(this, "Failed to retrieve image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String adminId = currentUser.getUid();
            StorageReference storageRef = firebaseStorage.getReference()
                    .child("profileImages/" + adminId + "_" + UUID.randomUUID().toString());

            storageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        storageRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                                    firestore.collection("users")
                                            .document(adminId)
                                            .update("profileImage", downloadUri.toString())
                                            .addOnSuccessListener(aVoid -> {
                                                Toast.makeText(this, "Profile image updated", Toast.LENGTH_SHORT).show();
                                                loadImage(downloadUri.toString()); // Reload the image
                                            })
                                            .addOnFailureListener(e -> {
                                                Toast.makeText(this, "Failed to update profile image URL", Toast.LENGTH_SHORT).show();
                                                Log.e("Upload", "Error updating Firestore", e);
                                            });
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Failed to get download URL", Toast.LENGTH_SHORT).show();
                                    Log.e("Upload", "Error getting download URL", e);
                                });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void loadImage(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this)
                    .load(imageUrl)
                    .into(adminPicImageView); // Load image with Glide
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
                            startActivity(new Intent(AdminIProfileScreen.this, HomeScreen.class));
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
                        Intent intent = new Intent(AdminIProfileScreen.this, HomeScreen.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}