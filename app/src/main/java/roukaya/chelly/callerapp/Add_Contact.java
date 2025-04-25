package roukaya.chelly.callerapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Add_Contact extends AppCompatActivity {
    EditText firstname, lastname, phone;
    Button add, back;
    TextView result;
    Contact_Manager mg;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        // Find views by ID
        firstname = findViewById(R.id.et_firstname_add);
        lastname = findViewById(R.id.et_lastname_add);
        phone = findViewById(R.id.etph_phone_add);
        add = findViewById(R.id.btn_add_add);
        back = findViewById(R.id.btn_back_addc);
        result = findViewById(R.id.tv_result_add);

        // Initialize Contact_Manager
        mg = new Contact_Manager(Add_Contact.this);
        mg.Ouvrir();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if fields are not empty
                if (firstname.getText().toString().isEmpty() ||
                        lastname.getText().toString().isEmpty() ||
                        phone.getText().toString().isEmpty()) {

                    Toast.makeText(Add_Contact.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create a new contact
                Contact c = new Contact(
                        firstname.getText().toString(),
                        lastname.getText().toString(),
                        phone.getText().toString()
                );

                // Add the contact to the database
                long g = mg.ajout(c.firstname, c.lastname, c.phone);

                if (g > 0) {
                    // Show success message
                    result.setText("Contact added successfully!");
                    Toast.makeText(Add_Contact.this, "Contact added successfully!", Toast.LENGTH_SHORT).show();

                    // Clear the input fields
                    firstname.setText("");
                    lastname.setText("");
                    phone.setText("");

                    // Update the static data in Home
                    if (Home.data != null) {
                        Home.data = mg.getAllContacts();
                    }
                } else {
                    result.setText("Failed to add contact");
                    Toast.makeText(Add_Contact.this, "Failed to add contact", Toast.LENGTH_SHORT).show();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Add_Contact.this.finish();
            }
        });
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