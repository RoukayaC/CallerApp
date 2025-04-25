package roukaya.chelly.callerapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Home extends AppCompatActivity {
    Button btAdd, btShow, btLogout, btExit;
    TextView welcome;
    public static ArrayList<Contact> data = new ArrayList<Contact>();
    private Contact_Manager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Find views by ID
        btAdd = findViewById(R.id.btn_addc_home);
        btShow = findViewById(R.id.btn_showc_home);
        btLogout = findViewById(R.id.btn_logout_home);
        btExit = findViewById(R.id.btn_exit_home);
        welcome = findViewById(R.id.tv_title_home);

        // Get username from login screen
        Intent x = this.getIntent();
        if (x.hasExtra("Name")) {
            Bundle b = x.getExtras();
            String name = b.getString("Name");
            welcome.setText("Welcome Mm. " + name);
        }

        // Initialize Contact_Manager
        manager = new Contact_Manager(Home.this);
        manager.Ouvrir();

        // Load contacts
        data = manager.getAllContacts();

        // Add contact button
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home.this, Add_Contact.class);
                startActivity(i);
            }
        });

        // Show contacts button
        btShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Refresh data before showing
                data = manager.getAllContacts();

                Intent i = new Intent(Home.this, Affiche.class);
                startActivity(i);
            }
        });

        // Logout button
        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Clear login status
                SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("isLoggedIn", false);
                editor.apply();

                // Go back to login screen
                Intent i = new Intent(Home.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        // Exit button
        btExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh data when returning to this activity
        if (manager != null) {
            data = manager.getAllContacts();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the database connection
        if (manager != null) {
            manager.fermer();
        }
    }
}