package roukaya.chelly.callerapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerApdater extends RecyclerView.Adapter<RecyclerApdater.ContactViewHolder> implements Filterable {

    Context context;
    ArrayList<Contact> contactList;
    ArrayList<Contact> contactListFiltered;
    Contact_Manager manager;

    public RecyclerApdater(Context context, ArrayList<Contact> contactList) {
        this.context = context;
        this.contactList = contactList;
        this.contactListFiltered = new ArrayList<>(contactList);
        this.manager = new Contact_Manager(context);
        manager.Ouvrir();
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contactListFiltered.get(position);
        holder.firstName.setText(contact.firstname);
        holder.lastName.setText(contact.lastname);
        holder.phone.setText(contact.phone);
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String searchString = constraint.toString().toLowerCase().trim();

                if (searchString.isEmpty()) {
                    contactListFiltered = new ArrayList<>(contactList);
                } else {
                    ArrayList<Contact> filteredList = new ArrayList<>();
                    for (Contact contact : contactList) {
                        if (contact.firstname.toLowerCase().contains(searchString) ||
                                contact.lastname.toLowerCase().contains(searchString) ||
                                contact.phone.contains(searchString)) {
                            filteredList.add(contact);
                        }
                    }
                    contactListFiltered = filteredList;
                }

                FilterResults results = new FilterResults();
                results.values = contactListFiltered;
                results.count = contactListFiltered.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                contactListFiltered = (ArrayList<Contact>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView firstName, lastName, phone;
        ImageView callButton, editButton, deleteButton;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);

            // Find views
            firstName = itemView.findViewById(R.id.tv_fn_contact);
            lastName = itemView.findViewById(R.id.tv_ln_contact);
            phone = itemView.findViewById(R.id.tv_phone_contact);
            callButton = itemView.findViewById(R.id.ivcall);
            editButton = itemView.findViewById(R.id.ivedit);
            deleteButton = itemView.findViewById(R.id.ivdelete);

            // Set click listeners
            callButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Contact contact = contactListFiltered.get(position);

                        // Check for call permission
                        if (ActivityCompat.checkSelfPermission(context,
                                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:" + contact.phone));
                            context.startActivity(intent);
                        } else {
                            // Request permission
                            ActivityCompat.requestPermissions((Activity) context,
                                    new String[]{Manifest.permission.CALL_PHONE}, 1);

                            // Show dial instead if permission not granted
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + contact.phone));
                            context.startActivity(intent);
                        }
                    }
                }
            });

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Contact contact = contactListFiltered.get(position);
                        showEditDialog(contact, position);
                    }
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Contact contact = contactListFiltered.get(position);
                        showDeleteConfirmation(contact, position);
                    }
                }
            });
        }
    }

    private void showEditDialog(final Contact contact, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Contact");

        // Inflate edit dialog layout
        View dialogView = LayoutInflater.from(context).inflate(R.layout.edit_contact, null);
        builder.setView(dialogView);

        // Get references to edit texts
        final EditText edFirstName = dialogView.findViewById(R.id.et_firstname_edit);
        final EditText edLastName = dialogView.findViewById(R.id.et_lastname_edit);
        final EditText edPhone = dialogView.findViewById(R.id.et_phone_edit);

        // Set current values
        edFirstName.setText(contact.firstname);
        edLastName.setText(contact.lastname);
        edPhone.setText(contact.phone);

        // Add buttons
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get new values
                String newFirstName = edFirstName.getText().toString();
                String newLastName = edLastName.getText().toString();
                String newPhone = edPhone.getText().toString();

                // Validate input
                if (newFirstName.isEmpty() || newLastName.isEmpty() || newPhone.isEmpty()) {
                    Toast.makeText(context, "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Update in database (by removing old and adding new)
                manager.supprimer(contact.phone);

                // Update fields
                contact.firstname = newFirstName;
                contact.lastname = newLastName;
                contact.phone = newPhone;

                // Add updated contact
                manager.ajout(contact.firstname, contact.lastname, contact.phone);

                // Update data list
                contactList.set(position, contact);

                // Find the position in the filtered list
                int filteredPosition = contactListFiltered.indexOf(contact);
                if (filteredPosition >= 0) {
                    contactListFiltered.set(filteredPosition, contact);
                }

                // Update Home.data
                Home.data = manager.getAllContacts();

                // Refresh view
                notifyDataSetChanged();

                Toast.makeText(context, "Contact updated successfully", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showDeleteConfirmation(final Contact contact, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Contact");
        builder.setMessage("Are you sure you want to delete this contact?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Delete from database
                manager.supprimer(contact.phone);

                // Remove from lists
                contactList.remove(contact);
                contactListFiltered.remove(contact);

                // Update Home.data
                Home.data = manager.getAllContacts();

                // Refresh view
                notifyItemRemoved(position);

                Toast.makeText(context, "Contact deleted", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("No", null);
        builder.show();
    }

    // Clean up resources
    public void cleanup() {
        if (manager != null) {
            manager.fermer();
        }
    }
}