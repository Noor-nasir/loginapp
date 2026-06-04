package com.example.loginapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Calendar;
public class home extends AppCompatActivity {
    TextView textWelcomeMessage, textUserDetails;
    Button logoutButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        textWelcomeMessage = findViewById(R.id.textWelcomeMessage);
        textUserDetails = findViewById(R.id.textUserDetails);
        logoutButton = findViewById(R.id.logoutButton);
        SharedPreferences preferences = getSharedPreferences("UserData",
                MODE_PRIVATE);
        String currentUser = preferences.getString("currentUser", null);
        if (currentUser == null) {
            Intent intent = new Intent(home.this, login.class);
            startActivity(intent);
            finish();
            return;
        }
        String userData = preferences.getString("users", "{}");
        try {
            JSONObject users = new JSONObject(userData);
            if (users.has(currentUser)) {
                JSONObject userDetails = users.getJSONObject(currentUser);
                String name = userDetails.getString("name");
                String sex = userDetails.getString("sex");
                int birthYear = userDetails.getInt("birthYear");
                int birthMonth = userDetails.getInt("birthMonth");
                int birthDay = userDetails.getInt("birthDay");
                // Get today's date
                Calendar today = Calendar.getInstance();
                int currentYear = today.get(Calendar.YEAR);
                int currentMonth = today.get(Calendar.MONTH); // 0-based (January = 0)
                int currentDay = today.get(Calendar.DAY_OF_MONTH);
                // Calculate age
                int age = currentYear - birthYear;
                if (currentMonth < birthMonth || (currentMonth == birthMonth && currentDay <
                        birthDay)) {
                    age--; // Adjust age if birthday hasn't occurred yet this year
                }
                textWelcomeMessage.setText("Welcome " + currentUser + "!");
                textUserDetails.setText("Your details:\nName: " + name + "\nSex: " + sex + "\nAge: " + age);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        logoutButton.setOnClickListener(v -> {
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("currentUser");
            editor.apply();
            Intent intent = new Intent(home.this, login.class);
            startActivity(intent);
            finish();
        });
    }
}
