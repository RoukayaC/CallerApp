package roukaya.chelly.callerapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    // Declaration of components
    Button btnExit, btnValidate;
    EditText edName, edPassword;
    CheckBox chkRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the XML interface on the screen
        setContentView(R.layout.activity_main);

        // Check if user is already logged in
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            // User is already logged in, go straight to Home
            Intent i = new Intent(MainActivity.this, Home.class);
            i.putExtra("Name", prefs.getString("username", "User"));
            startActivity(i);
            finish(); // Close login screen
            return;
        }

        // Get references to components
        btnExit = findViewById(R.id.btn_exit_auth);
        btnValidate = findViewById(R.id.btn_valider_auth);
        edName = findViewById(R.id.ed_user_auth);
        edPassword = findViewById(R.id.etnp_password_auth);
        chkRememberMe = findViewById(R.id.chk_remember_me);

        // Events
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // Close the current activity
            }
        });

        btnValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edName.getText().toString();
                String pwd = edPassword.getText().toString();

                // Simple authentication - for beginners
                if (name.equalsIgnoreCase("roukaya") && pwd.equals("2000")) {
                    // Save login state if "Remember Me" is checked
                    SharedPreferences.Editor editor = prefs.edit();

                    if (chkRememberMe.isChecked()) {
                        editor.putBoolean("isLoggedIn", true);
                        editor.putString("username", name);
                    } else {
                        editor.putBoolean("isLoggedIn", false);
                    }
                    editor.apply();

                    // Navigate to Home activity
                    Intent i = new Intent(MainActivity.this, Home.class);
                    i.putExtra("Name", name);
                    startActivity(i);
                    finish();
                } else {
                    // Show error message
                    Toast.makeText(MainActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}