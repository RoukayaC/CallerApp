package roukaya.chelly.callerapp;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Affiche extends AppCompatActivity {
    ListView lv;
    MonAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affiche);

        lv = findViewById(R.id.lv_affiche);

        // Check if there's data to display
        if (Home.data != null && Home.data.size() > 0) {
            adapter = new MonAdapter(Affiche.this, Home.data);
            lv.setAdapter(adapter);
        } else {
            Toast.makeText(this, "No contacts to display", Toast.LENGTH_SHORT).show();
            finish(); // Return to previous screen if no data
        }
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