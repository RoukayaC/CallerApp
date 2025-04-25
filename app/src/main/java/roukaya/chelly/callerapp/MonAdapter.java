package roukaya.chelly.callerapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MonAdapter extends BaseAdapter {

    ArrayList<Contact> data;
    Context con;
    Contact_Manager mg;

    // Constructor takes a context and contact data
    public MonAdapter(Context con, ArrayList<Contact> data) {
        this.con = con;
        this.data = data;
        this.mg = new Contact_Manager(con);
        mg.Ouvrir();
    }

    // Return number of views to create
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        // Convert an XML file to a view
        LayoutInflater inf = LayoutInflater.from(con);
        View v = inf.inflate(R.layout.view_contact, null);

        // Get references to view holders
        TextView fn = v.findViewById(R.id.tv_fn_contact);
        TextView ln = v.findViewById(R.id.tv_ln_contact);
        TextView phone = v.findViewById(R.id.tv_phone_contact);
        ImageView imgcall = v.findViewById(R.id.ivcall);
        ImageView imgedit = v.findViewById(R.id.ivedit);
        ImageView imgdelete = v.findViewById(R.id.ivdelete);

        // Set data to holders
        Contact c = data.get(position);
        fn.setText(c.firstname);
        ln.setText(c.lastname);
        phone.setText(c.phone);

        // Call action
        imgcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_DIAL);
                i.setData(Uri.parse("tel:" + c.phone));
                con.startActivity(i);
            }
        });

        // Edit action - NEW CODE
        imgedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a dialog for editing
                AlertDialog.Builder builder = new AlertDialog.Builder(con);
                builder.setTitle("Edit Contact");

                // Create a layout for the dialog
                LinearLayout layout = new LinearLayout(con);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setPadding(20, 10, 20, 10);

                // Create input fields
                final EditText editFirstName = new EditText(con);
                editFirstName.setHint("First Name");
                editFirstName.setText(c.firstname);

                final EditText editLastName = new EditText(con);
                editLastName.setHint("Last Name");
                editLastName.setText(c.lastname);

                final EditText editPhone = new EditText(con);
                editPhone.setHint("Phone Number");
                editPhone.setText(c.phone);

                // Add views to layout
                layout.addView(editFirstName);
                layout.addView(editLastName);
                layout.addView(editPhone);

                // Set the dialog view
                builder.setView(layout);

                // Add Save button
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String oldPhone = c.phone;
                        String newFirstName = editFirstName.getText().toString();
                        String newLastName = editLastName.getText().toString();
                        String newPhone = editPhone.getText().toString();

                        // Validation
                        if (newFirstName.isEmpty() || newLastName.isEmpty() || newPhone.isEmpty()) {
                            Toast.makeText(con, "Please fill all fields", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Update in database by deleting and re-adding
                        mg.supprimer(oldPhone);
                        long result = mg.ajout(newFirstName, newLastName, newPhone);

                        if (result > 0) {
                            // Update in the list
                            c.firstname = newFirstName;
                            c.lastname = newLastName;
                            c.phone = newPhone;

                            // Update UI
                            fn.setText(newFirstName);
                            ln.setText(newLastName);
                            phone.setText(newPhone);

                            // Update the main data list
                            Home.data = mg.getAllContacts();

                            // Notify adapter of changes
                            notifyDataSetChanged();

                            Toast.makeText(con, "Contact updated successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(con, "Failed to update contact", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                // Add Cancel button
                builder.setNegativeButton("Cancel", null);

                // Show the dialog
                builder.show();
            }
        });

        // Delete action
        imgdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(con)
                        .setTitle("Suppression")
                        .setMessage("Vous êtes sûr de supprimer ce contact ?")
                        .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Contact c = data.get(position);
                                mg.supprimer(c.phone);
                                data.remove(position);
                                notifyDataSetChanged();

                                // Update the main data list
                                Home.data = mg.getAllContacts();
                            }
                        })
                        .setNegativeButton("Non", null)
                        .show();
            }
        });

        return v;
    }

    // Clean up resources
    public void cleanup() {
        if (mg != null) {
            mg.fermer();
        }
    }
}