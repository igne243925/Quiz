package com.jtdev.knowsalot;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

@RunWith(MockitoJUnitRunner.class)
public class LoginActivityTest {

    @Mock
    FirebaseAuth mockFirebaseAuth;

    @Mock
    FirebaseFirestore mockFirestore;

    private LoginScreen loginScreen;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        loginScreen = new LoginScreen();
        loginScreen.firebaseAuth = mockFirebaseAuth;
        loginScreen.firestore = mockFirestore;
    }


    @Test
    public void testAdminLoginWithValidCredentials() {
        String email = "admin@example.com";
        String password = "admin123";
        String expectedRole = "admin";


        Mockito.when(mockFirebaseAuth.signInWithEmailAndPassword(email, password))
                .thenReturn(Mockito.mock(Task.class));

        Mockito.when(mockFirestore.collection("users").document(Mockito.anyString()).get())
                .thenReturn(Mockito.mock(Task.class));


        loginScreen.loginUser(expectedRole);


    }

    @Test
    public void testStudentLoginWithValidCredentials() {
        String email = "student@example.com";
        String password = "student123";
        String expectedRole = "student";

        Mockito.when(mockFirebaseAuth.signInWithEmailAndPassword(email, password))
                .thenReturn(Mockito.mock(Task.class));

        Mockito.when(mockFirestore.collection("users").document(Mockito.anyString()).get())
                .thenReturn(Mockito.mock(Task.class));

        loginScreen.loginUser(expectedRole);


    }


    @Test
    public void testLoginWithInvalidCredentials() {
        String email = "invalid@example.com";
        String password = "invalid123";
        String expectedRole = "admin";


        Mockito.when(mockFirebaseAuth.signInWithEmailAndPassword(email, password))
                .thenReturn(Mockito.mock(Task.class));


        loginScreen.loginUser(expectedRole);

    }
}
