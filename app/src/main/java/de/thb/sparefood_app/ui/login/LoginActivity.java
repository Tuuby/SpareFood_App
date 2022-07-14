package de.thb.sparefood_app.ui.login;

import static java.nio.charset.StandardCharsets.UTF_8;
import static de.thb.sparefood_app.model.MealRepository.getURL;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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


public class LoginActivity extends AppCompatActivity {

    public static final String UNAUTHORIZED = "UNAUTHORIZED";
    private MaterialButton loginButton;
    private TextInputLayout emailField;
    private TextInputLayout passwordField;

    protected boolean emailValid = false;
    protected boolean passwordValid = false;

    private String authToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_layout);
        View layout = findViewById(R.id.loginLayout);

        // email
        emailField = layout.findViewById(R.id.login_email);
        EditText editEmailField = emailField.getEditText();

        editEmailField.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String email = editEmailField.getText().toString();
                processEmailInput(email);
            }
        });

        editEmailField.setOnKeyListener((v, keyCode, event) -> {
            emailFieldTyping();
            return false;
        });

        editEmailField.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                String email = v.getText().toString();

                processEmailInput(email);
                return false;
            }
            return false;
        });

        // password
        passwordField = layout.findViewById(R.id.login_password);
        EditText editPasswordField = passwordField.getEditText();

        editPasswordField.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String pw = editPasswordField.getText().toString();
                processPassword(pw);
            }
        });

        editPasswordField.setOnKeyListener((v, keyCode, event) -> {
            passwordFieldTyping();
            return false;
        });

        editPasswordField.setOnEditorActionListener((v, arg1, arg2) -> {
            if (arg1 == EditorInfo.IME_ACTION_GO) {
                String pw = v.getText().toString();

                processPassword(pw);
                executeLogin();
            }
            return false;
        });

        loginButton = layout.findViewById(R.id.login_button);
        loginButton.setOnClickListener(view -> executeLogin());
    }

    private void processEmailInput(String text) {
        //Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        if (!Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
            this.emailField.getEditText().setBackgroundColor(ContextCompat.getColor(this, R.color.error_red));
            this.emailValid = false;
        } else {
            this.emailField.getEditText().setBackgroundColor(Color.BLACK);
            this.emailValid = true;
        }
        updateButtonState();
    }

    private void processPassword(String pw) {
        if (pw.length() < 3) {
            this.passwordField.getEditText().setBackgroundColor(ContextCompat.getColor(this, R.color.error_red));
            this.passwordValid = false;
        } else {
            this.passwordField.getEditText().setBackgroundColor(Color.BLACK);
            this.passwordValid = true;
        }
        updateButtonState();
    }

    private void emailFieldTyping() {
        this.emailValid = false;
        this.emailField.getEditText().setBackgroundColor(Color.BLACK);
        updateButtonState();
    }

    private void passwordFieldTyping() {
        this.passwordValid = false;
        // this.failedLogin.setVisibility(View.INVISIBLE);
        updateButtonState();
    }

    private void updateButtonState() {
        if (this.emailValid && this.passwordValid) {
            this.loginButton.setEnabled(true);
        } else
            this.loginButton.setEnabled(false);
    }

    private void executeLogin() {
        String email = emailField.getEditText().getText().toString();
        String password = passwordField.getEditText().getText().toString();

        try {
            sendAuthenticationRequest(email, password);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("FATAL_ERROR", "Failed to login user", e);
        }

        boolean success = waitForRequestToFinish();

        if (success) {
            // todo persist the authToken
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        } else {
            Toast.makeText(LoginActivity.this, "Wrong credentials", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean waitForRequestToFinish() {
        boolean success = false;

        // i know that this is very ugly but it is what it is
        while (true) {
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
        return success;
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                this.emailField.getEditText().getGlobalVisibleRect(outRect);
                Rect pwRect = new Rect();
                this.passwordField.getEditText().getGlobalVisibleRect(pwRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY()) && !pwRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}
