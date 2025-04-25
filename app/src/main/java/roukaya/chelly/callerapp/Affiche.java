package roukaya.chelly.callerapp;

import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Affiche extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerApdater adapter;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affiche);

        // Find views
        recyclerView = findViewById(R.id.rv_contacts);
        searchView = findViewById(R.id.search_contacts);

        // Set up RecyclerView with LinearLayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Check if there are contacts to display
        if (Home.data != null && Home.data.size() > 0) {
            // Set up adapter
            adapter = new RecyclerApdater(Affiche.this, Home.data);
            recyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(this, "No contacts to display", Toast.LENGTH_SHORT).show();
        }

        // Set up search functionality
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter contacts based on search query
                if (adapter != null) {
                    adapter.getFilter().filter(newText);
                }
                return true;
            }
        });
    }

    // Handle back button click
    public void goBack(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up resources
        if (adapter != null) {
            adapter.cleanup();
        }
    }
}