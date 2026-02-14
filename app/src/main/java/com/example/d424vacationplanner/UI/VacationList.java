package com.example.d424vacationplanner.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.Manifest;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d424vacationplanner.R;
import com.example.d424vacationplanner.database.Repository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class VacationList extends AppCompatActivity {

    private Repository repository;
    private VacationAdapter adapter;
    private static final int NOTIFICATION_PERMISSION_CODE = 1001;

    private static final String PREFS_NAME = "permission_prefs";
    private static final String KEY_NOTIFICATION_ASKED = "notification_asked";

    // Notification dialog box only appears once. User has to allow notifications in device settings.
    private void requestNotificationPermission() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean alreadyAsked = prefs.getBoolean(KEY_NOTIFICATION_ASKED, false);

        if (alreadyAsked) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED) {

                // Mark as asked
                prefs.edit().putBoolean(KEY_NOTIFICATION_ASKED, true).apply();

                // Ask permission
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        NOTIFICATION_PERMISSION_CODE
                );
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_list);

        Toolbar toolbar = findViewById(R.id.materialToolbar);
        setSupportActionBar(toolbar);

        // Get the overflow icon
        Drawable overflowIcon = toolbar.getOverflowIcon();
        if (overflowIcon != null) {
            // Apply white color/tint to text
            overflowIcon.setTint(ContextCompat.getColor(this, R.color.white));
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        repository = new Repository(getApplication());

        //Recycler View
        RecyclerView vacationRecycler = findViewById(R.id.recyclerVacation);

        adapter = new VacationAdapter(new ArrayList<>());

        adapter.setOnVacationClickListener(vacation -> {
            Intent intent = new Intent(this, VacationDetails.class);
            intent.putExtra("vacationID", vacation.getVacationID());
            startActivity(intent);
        });

        // Connects adapter to recycler view.
        vacationRecycler.setAdapter(adapter);
        vacationRecycler.setLayoutManager(new LinearLayoutManager(this));

        repository.getAllVacations(vacations -> {
            // Gives data to recycler view
            adapter.setVacations(vacations);
        });

        FloatingActionButton fab = findViewById(R.id.addVacationFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VacationList.this, VacationDetails.class);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.vacation_list), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_list, menu);
        return true;
    }

    //Controls what happens when you click on menu item { Save, Delete, etc. }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuNotify) {
            notifyButton();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Notification Dialog box that asks for user permission once.
    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NOTIFICATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Notifications enabled.", Toast.LENGTH_SHORT).show();
                // Log.d("ALERT_SCHEDULE", "Notification permission GRANTED");
            } else {
                Toast.makeText(this, "Notifications disabled.", Toast.LENGTH_SHORT).show();
                // Log.d("ALERT_SCHEDULE", "Notification permission DENIED");

            }
        }
    }

    // Notification dialog box only appears once. User has to allow notifications in device settings.
    private void notifyButton() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    == PackageManager.PERMISSION_GRANTED) {
                // Permission granted. Open settings
                openNotificationSettings();
            } else {
                // Permission denied. Try to ask once
                boolean askedBefore = getSharedPreferences("permission_prefs", MODE_PRIVATE)
                        .getBoolean("notification_asked", false);

                if (!askedBefore) {
                    // First time asking. Show system dialog
                    requestNotificationPermission();

                    // Remember that already asked once
                    getSharedPreferences("permission_prefs", MODE_PRIVATE)
                            .edit()
                            .putBoolean("notification_asked", true)
                            .apply();
                } else {
                    // Already asked once. Go to system settings
                    openNotificationSettings();
                }
            }
        }
    }

    private void openNotificationSettings() {
        Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                .putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadVacations();
    }

    private void loadVacations() {
        repository.getAllVacations(vacations -> {
            adapter.setVacations(vacations);
        });
    }

}