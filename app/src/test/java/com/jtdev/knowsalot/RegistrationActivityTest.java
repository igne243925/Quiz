package com.jtdev.knowsalot;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;


import org.junit.Before;
import org.junit.Test;

public class RegistrationActivityTest {
    private RegisterScreen registerScreen;

    @Before
    public void setUp() {
        registerScreen = new RegisterScreen();
    }


    @Test
    public void testRegistrationWithValidCredentials() {

        registerScreen.username.setText("Test_Username");
        registerScreen.rEmail.setText("Test_Email");
        registerScreen.rPassword.setText("Test_Password");


        registerScreen.registerUser("student");

        assertNotNull("Registration successful", registerScreen.firebaseAuth.getCurrentUser());
    }


    @Test
    public void testRegistrationWithEmptyCredentials() {

        registerScreen.username.setText(null);
        registerScreen.rEmail.setText(null);
        registerScreen.rPassword.setText(null);


        registerScreen.registerUser("student");

        assertNull("Registration failed due to empty credentials", registerScreen.firebaseAuth.getCurrentUser());
    }


    @Test
    public void testRegistrationWithEmptyUsername() {

        registerScreen.username.setText(null);
        registerScreen.rEmail.setText("Test_Email");
        registerScreen.rPassword.setText("Test_Password");


        registerScreen.registerUser("student");


        assertNull("Registration failed due to empty username", registerScreen.firebaseAuth.getCurrentUser());
    }

    // Empty Email
    @Test
    public void testRegistrationWithEmptyEmail() {

        registerScreen.username.setText("Test_Username");
        registerScreen.rEmail.setText(null);
        registerScreen.rPassword.setText("Test_Password");


        registerScreen.registerUser("student");

        assertNull("Registration failed due to empty email", registerScreen.firebaseAuth.getCurrentUser());
    }


    @Test
    public void testRegistrationWithEmptyPassword() {

        registerScreen.username.setText("Test_Username");
        registerScreen.rEmail.setText("Test_Email");
        registerScreen.rPassword.setText(null);


        registerScreen.registerUser("student");


        assertNull("Registration failed due to empty password", registerScreen.firebaseAuth.getCurrentUser());
    }
}
