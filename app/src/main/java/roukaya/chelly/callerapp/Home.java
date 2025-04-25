package roukaya.chelly.callerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Home extends AppCompatActivity {
    Button btadd, btshow;
    TextView welcome;
    public static ArrayList<Contact> data = new ArrayList<Contact>();
    private Contact_Manager mg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Find views by ID
        btadd = findViewById(R.id.btn_addc_home);
        btshow = findViewById(R.id.btn_showc_home);
        welcome = findViewById(R.id.tv_title_home);

        // Get username from login screen
        Intent x = this.getIntent();
        if (x.hasExtra("Name")) {
            Bundle b = x.getExtras();
            String name = b.getString("Name");
            welcome.setText("Welcome Ms. " + name);
        }

        // Initialize Contact_Manager
        mg = new Contact_Manager(Home.this);
        mg.Ouvrir();

        // Initial load of contacts
        data = mg.getAllContacts();

        btadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home.this, Add_Contact.class);
                startActivity(i);
            }
        });

        btshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Refresh data before showing
                data = mg.getAllContacts();

                if (data.size() > 0) {
                    Intent i = new Intent(Home.this, Affiche.class);
                    startActivity(i);
                } else {
                    Toast.makeText(Home.this, "No contacts to display", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh data when returning to this activity
        if (mg != null) {
            data = mg.getAllContacts();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the database connection
        if (mg != null) {
            mg.fermer();
        }
    }
}