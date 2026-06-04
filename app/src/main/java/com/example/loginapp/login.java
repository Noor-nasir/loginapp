package com.example.loginapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;
public class login extends AppCompatActivity {
    EditText editUsername, editPassword;
    Button loginButton;
    TextView registerLink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        editUsername = findViewById(R.id.editUsernameLogin);
        editPassword = findViewById(R.id.editPasswordLogin);
        loginButton = findViewById(R.id.loginButton);
        registerLink = findViewById(R.id.registerLink);
        loginButton.setOnClickListener(v -> {
            String username = editUsername.getText().toString().trim();
            String password = editPassword.getText().toString().trim();
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter your username and password", Toast.LENGTH_SHORT).show();
            } else if (!isValidCredentials(username, password)) {
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            } else {
                // Successful login logic here
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();

                // Save the current user's username
                SharedPreferences preferences = getSharedPreferences("UserData", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("currentUser", username); // Save the logged-in username
                editor.apply();

                // Navigate to the home page
                Intent intent = new Intent(login.this, home.class);
                startActivity(intent);
                finish();
            }
        });
        registerLink.setOnClickListener(v -> {
            Intent intent = new Intent(login.this, register.class);
            startActivity(intent);
        });
    }
    private boolean isValidCredentials(String username, String password) {
        SharedPreferences preferences = getSharedPreferences("UserData",
                MODE_PRIVATE);
        String userData = preferences.getString("users", "{}");
        try {
            JSONObject users = new JSONObject(userData);
            if (users.has(username)) {
                JSONObject userDetails = users.getJSONObject(username);
                return userDetails.getString("password").equals(password);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
}
