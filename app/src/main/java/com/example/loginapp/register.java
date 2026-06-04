package com.example.loginapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;
public class register extends AppCompatActivity {
    EditText editNameSurname, editUsernameRegister, editPasswordRegister,
            editRePasswordRegister;
    RadioGroup radioGroupSex;
    DatePicker datePickerBirthYear;
    Button clearButton, registerButton, goBackButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        editNameSurname = findViewById(R.id.editNameSurname);
        editUsernameRegister = findViewById(R.id.editUsernameRegister);
        editPasswordRegister = findViewById(R.id.editPasswordRegister);
        editRePasswordRegister = findViewById(R.id.editRePasswordRegister);
        radioGroupSex = findViewById(R.id.radioGroupSex);
        datePickerBirthYear = findViewById(R.id.datePickerBirthYear);
        clearButton = findViewById(R.id.clearButton);
        registerButton = findViewById(R.id.registerButton);
        goBackButton = findViewById(R.id.goBackButton);
        // Clear button functionality
        clearButton.setOnClickListener(v -> {
            editNameSurname.setText("");
            editUsernameRegister.setText("");
            editPasswordRegister.setText("");
            editRePasswordRegister.setText("");
            radioGroupSex.clearCheck();
            datePickerBirthYear.updateDate(2000, 0, 1);
        });
        // Register button functionality
        registerButton.setOnClickListener(v -> {
            String nameSurname = editNameSurname.getText().toString().trim();
            String username = editUsernameRegister.getText().toString().trim();
            String password = editPasswordRegister.getText().toString();
            String rePassword = editRePasswordRegister.getText().toString();
            int selectedSexId = radioGroupSex.getCheckedRadioButtonId();
            RadioButton selectedSex = selectedSexId != -1 ? findViewById(selectedSexId) :
                    null;
            // Input validations
            if (nameSurname.isEmpty() || nameSurname.length() < 5) {
                Toast.makeText(this, "Please enter your full name and surname (minimum 5 characters)", Toast.LENGTH_SHORT).show();
                return;
            }
            if (username.isEmpty() || username.length() < 3) {
                Toast.makeText(this, "Please enter a username (minimum 3 characters)",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            SharedPreferences preferences = getSharedPreferences("UserData",
                    MODE_PRIVATE);
            String userData = preferences.getString("users", "{}");
            try {
                JSONObject users = new JSONObject(userData);
                if (users.has(username)) {
                    Toast.makeText(this, "Username is already taken",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (password.isEmpty() || password.length() < 5) {
                Toast.makeText(this, "Please enter a password (minimum 5 characters)",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (!password.equals(rePassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }
            if (selectedSex == null) {
                Toast.makeText(this, "Please select your sex", Toast.LENGTH_SHORT).show();
                return;
            }
            String sex = selectedSex.getText().toString();
            int birthYear = datePickerBirthYear.getYear();
            int birthMonth = datePickerBirthYear.getMonth();
            int birthDay = datePickerBirthYear.getDayOfMonth();
            // Save the new user data
            try {
                JSONObject users = new JSONObject(userData);
                JSONObject userDetails = new JSONObject();
                userDetails.put("name", nameSurname);
                userDetails.put("password", password);
                userDetails.put("sex", sex);
                userDetails.put("birthYear", birthYear);
                userDetails.put("birthMonth", birthMonth);
                userDetails.put("birthDay", birthDay);
                users.put(username, userDetails);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("users", users.toString());
                editor.apply();
                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(register.this, login.class));
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        // Go back button functionality
        goBackButton.setOnClickListener(v -> finish());
    }
}

