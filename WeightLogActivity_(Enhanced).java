package com.WasiqAfzaal.Mobile2App;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class WeightLogActivity extends AppCompatActivity {

    private RecyclerView recyclerWeights;
    private WeightAdapter adapter;
    private final ArrayList<WeightEntry> weightList = new ArrayList<>();

    private DatabaseHelper db;

    // TEMP: using a test user until LoginActivity is wired
    private long userId = 1;

    private static final String PREFS = "wt_prefs";
    private static final String KEY_USER_ID = "logged_in_user_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_log);

        db = new DatabaseHelper(this);

        // Try to use a logged-in user id if it exists; otherwise create/use a "test" user
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        long savedUserId = prefs.getLong(KEY_USER_ID, -1);

        if (savedUserId != -1) {
            userId = savedUserId;
        } else {
            if (!db.usernameExists("test")) {
                long newId = db.createUser("test", "test");
                if (newId != -1) userId = newId;
            } else {
                long id = db.validateLogin("test", "test");
                if (id != -1) userId = id;
            }
        }

        recyclerWeights = findViewById(R.id.recyclerWeights);

        recyclerWeights.setLayoutManager(new GridLayoutManager(this, 1));

        adapter = new WeightAdapter(weightList, new WeightAdapter.OnWeightActionListener() {
            @Override
            public void onEdit(WeightEntry entry) {
                Intent intent = new Intent(WeightLogActivity.this, AddEditWeightActivity.class);
                intent.putExtra("weightId", entry.id);
                intent.putExtra("date", entry.date);
                intent.putExtra("weight", entry.weight);
                startActivity(intent);
            }

            @Override
            public void onDelete(WeightEntry entry) {
                boolean ok = db.deleteWeight(entry.id);
                if (ok) {
                    Toast.makeText(WeightLogActivity.this, "Deleted.", Toast.LENGTH_SHORT).show();
                    loadFromDatabase();
                } else {
                    Toast.makeText(WeightLogActivity.this, "Delete failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        recyclerWeights.setAdapter(adapter);

        Button addButton = findViewById(R.id.buttonAddWeight);
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(WeightLogActivity.this, AddEditWeightActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFromDatabase();
    }

    /**
     * Loads weight data, stores it in an ArrayList, and sorts it.
     */
    private void loadFromDatabase() {
        weightList.clear();

        Cursor c = db.getAllWeightsForUser(userId);
        if (c != null) {
            while (c.moveToNext()) {
                long id = c.getLong(c.getColumnIndexOrThrow(DatabaseHelper.COL_WEIGHT_ID));
                String date = c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COL_DATE));
                double weight = c.getDouble(c.getColumnIndexOrThrow(DatabaseHelper.COL_WEIGHT));

                weightList.add(new WeightEntry(id, date, weight));
            }
            c.close();
        }

        // Updated for CS 499: Enhancement Two: Algorithms and Data Structures - 03-26-2026

        // Sort weight entries by date (newest first) using a Comparator
        Collections.sort(weightList, new Comparator<WeightEntry>() {
            @Override
            public int compare(WeightEntry entry1, WeightEntry entry2) {
                return entry2.date.compareTo(entry1.date);
            }
        });

        adapter.notifyDataSetChanged();
    }
}