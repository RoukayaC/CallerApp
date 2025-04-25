package roukaya.chelly.callerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    // Declaration of components
    Button btnexit, btnval;
    EditText edname, edpwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the XML interface on the screen
        setContentView(R.layout.activity_main);

        // Get references to components
        btnexit = findViewById(R.id.btn_exit_auth);
        btnval = findViewById(R.id.btn_valider_auth);
        edname = findViewById(R.id.ed_user_auth);
        edpwd = findViewById(R.id.etnp_password_auth);

        // Events
        btnexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // Close the current activity
            }
        });

        btnval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edname.getText().toString();
                String pwd = edpwd.getText().toString();

                // Simple authentication - for beginners
                // In a real app, you would use a more secure method
                if (name.equalsIgnoreCase("a") && pwd.equals("1")) {
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