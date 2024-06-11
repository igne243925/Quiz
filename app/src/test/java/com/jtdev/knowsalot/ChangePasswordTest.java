package com.jtdev.knowsalot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Test;

public class ChangePasswordTest {
    private FirebaseAuth firebaseAuth;

    @Before
    public void setUp() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Test
    public void testChangePassword() {

        String newPassword = "NewPassword";


        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            String oldPassword = "OldPassword";
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);
            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Change password
                                user.updatePassword(newPassword)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    // Password change successful
                                                    assertTrue("Password should be changed", true);
                                                } else {
                                                    // Password change failed
                                                    assertTrue("Password change failed", false);
                                                }
                                            }
                                        });
                            } else {

                                assertTrue("Re-authentication failed", false);
                            }
                        }
                    });
        } else {

            assertTrue("No user found", false);
        }
    }
}
