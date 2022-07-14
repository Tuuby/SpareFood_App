package de.thb.sparefood_app.ui.login;

import static java.nio.charset.StandardCharsets.UTF_8;
import static de.thb.sparefood_app.model.MealRepository.getURL;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;

import de.thb.sparefood_app.MainActivity;
import de.thb.sparefood_app.R;
import de.thb.sparefood_app.threading.ApplicationExecutors;


public class LoginActivity extends AppCompatActivity {

    public static final String UNAUTHORIZED = "UNAUTHORIZED";
    private TextInputLayout emailField;
    private TextInputLayout passwordField;
    private String authToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_login);
        View layout = findViewById(R.id.loginLayout);

        emailField = layout.findViewById(R.id.login_email);
        passwordField = layout.findViewById(R.id.login_password);
        MaterialButton loginButton = layout.findViewById(R.id.login_button);

        loginButton.setOnClickListener(view -> {
            String email = emailField.getEditText().getText().toString();
            String password = passwordField.getEditText().getText().toString();

            Log.d("CALLED_LOG", "email=" + email + "; password=" + password);
            try {
                sendAuthenticationRequest(email, password);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("FATAL_ERROR", "Failed to login user", e);
            }

            boolean success = false;
            while (true) {
                // wait for request to finish
                // i know that this is very ugly but it is what it is
                if (authToken != null) {
                    if (authToken.equals(UNAUTHORIZED)) {
                        // wrong password
                        authToken = null;
                    } else {
                        success = true;
                    }
                    break;
                }
            }

            if (success) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            } else {
                Toast.makeText(LoginActivity.this, "Wrong credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sendAuthenticationRequest(String email, String password) throws IOException {
        HttpURLConnection connection = createAuthenticationRequest();

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                String body = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\"}";

                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = body.getBytes(UTF_8);
                    os.write(input, 0, input.length);
                }
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    authToken = reader.readLine();
                } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    authToken = UNAUTHORIZED;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            connection.disconnect();
        });
    }

    private HttpURLConnection createAuthenticationRequest() throws IOException {
        URL loginURL = new URL(getURL("auth/generate-token", null));

        HttpURLConnection connection = (HttpURLConnection) loginURL.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");

        return connection;
    }
}
