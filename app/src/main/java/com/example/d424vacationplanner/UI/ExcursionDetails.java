package com.example.d424vacationplanner.UI;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.d424vacationplanner.R;
import com.example.d424vacationplanner.database.Repository;
import com.example.d424vacationplanner.entities.Excursion;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExcursionDetails extends AppCompatActivity {

    private Calendar calendar;
    private Repository repository;
    private EditText editTitle;
    private Button excursionDate;
    private Date selectedStartDate;
    private int excursionID;
    private int vacationID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_excursion_details);

        editTitle = findViewById(R.id.editTitle);
        excursionDate = findViewById(R.id.excursionDateButton);

        // Handling Toolbar and Contents
        Toolbar toolbar = findViewById(R.id.materialToolbar);
        setSupportActionBar(toolbar);

        // Get the overflow icon
        Drawable overflowIcon = toolbar.getOverflowIcon();
        if (overflowIcon != null) {
            // Apply white tint to text
            overflowIcon.setTint(ContextCompat.getColor(this, R.color.white));
        }

        // Show the back arrow icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close current activity and go back
            }
        });

        // Intent being passed from Vacation Details page...
        vacationID = getIntent().getIntExtra("vacationID", -1);

        if (vacationID == -1) {
            // Log.e("ExcursionDetails", "No vacation ID passed");
            finish(); // prevents FK crash
            return;
        }



        calendar = Calendar.getInstance();

// e.  Include validation that the excursion date is during the associated vacation.
        excursionDate.setOnClickListener(view -> {

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            repository.getVacationById(vacationID, vacation -> {

                if (vacation == null || vacation.getStartDate() == null || vacation.getEndDate() == null) {

                    Toast.makeText(
                            this,
                            "Set vacation start and end dates first.",
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }

                Date vacationStartDate = vacation.getStartDate();
                Date vacationEndDate = vacation.getEndDate();

                DatePickerDialog dpd = new DatePickerDialog(
                        ExcursionDetails.this,
                        (datePicker, selectYear, selectMonth, selectDay) -> {

                            calendar.set(selectYear, selectMonth, selectDay);

                            selectedStartDate = calendar.getTime();

                            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);

                            String dateString = sdf.format(selectedStartDate);
                            excursionDate.setText(dateString);
                        },
                        year, month, day
                );

                // Set dates min and max
                Calendar minCal = Calendar.getInstance();
                minCal.setTime(vacationStartDate);
                minCal.set(Calendar.HOUR_OF_DAY, 0);
                minCal.set(Calendar.MINUTE, 0);
                minCal.set(Calendar.SECOND, 0);
                minCal.set(Calendar.MILLISECOND, 0);

                Calendar maxCal = Calendar.getInstance();
                maxCal.setTime(vacationEndDate);
                maxCal.set(Calendar.HOUR_OF_DAY, 23);
                maxCal.set(Calendar.MINUTE, 59);
                maxCal.set(Calendar.SECOND, 59);
                maxCal.set(Calendar.MILLISECOND, 999);

                dpd.getDatePicker().setMinDate(minCal.getTimeInMillis());
                dpd.getDatePicker().setMaxDate(maxCal.getTimeInMillis());

                dpd.show();
            });
        });

        repository = new Repository(getApplication());

        excursionID = getIntent().getIntExtra("excursionID", -1);

        if (excursionID != -1) {
            // Fetch full vacation asynchronously
            repository.getExcursionById(excursionID, excursion -> {
                if (excursion != null) {
                    // Populate EditText fields
                    editTitle.setText(excursion.getExcursionTitle());

                    // Populate buttons with formatted dates
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.getDefault());

                    Date start = excursion.getExcursionDate();
                    if (start != null) {
                        excursionDate.setText(sdf.format(start));
                        selectedStartDate = start;
                    } else {
                        excursionDate.setText("Select Date"); // placeholder text
                        selectedStartDate = null;
                    }

                    selectedStartDate = excursion.getExcursionDate();
                }
            });
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.excursion_details), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_excursion_details, menu);
        return true;
    }

    // Controls what happens when you click on menu item { Save, Delete, etc. }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuSave) {
            saveExcursion();
            return true;
        }
        if (id == R.id.menuDelete) {
            deleteExcursion();
            return true;
        }
        if (id == R.id.menuAlert) {
            alert();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveExcursion() {
        // Get user input
        String title = editTitle.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(this, "Enter a title.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Test if save is working...
        // Log.d("SAVE","ID=" + excursionID + ", start=" + selectedStartDate );

        // Create Excursion object
        Excursion excursion = new Excursion(title, selectedStartDate, vacationID);

        if (excursionID == -1) {

            // Insert into database via repository
            repository.addExcursion(excursion, savedExcursion -> {
                Toast.makeText(this, "Excursion saved.", Toast.LENGTH_SHORT).show();
                finish();
            });

        } else {
            // Excursion must exist to update.
            excursion.setExcursionID(excursionID);

            repository.updateExcursion(excursion, updatedExcursion -> {
                // Log.d("UPDATE", "Excursion updated: " + excursion.getExcursionID());
                Toast.makeText(this, "Excursion updated.", Toast.LENGTH_SHORT).show();
                finish();
            });

        }

    }

    private void deleteExcursion() {

        if (excursionID <= 0) {
            Toast.makeText(
                    this,
                    "Save the excursion first.",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        repository.getExcursionById(excursionID, excursion -> {

            if (excursion == null) {
                Toast.makeText(
                        this,
                        "Excursion does not exist.",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            // Delete alerts before deleting item
            cancelExcursionAlert(excursionID);

            repository.deleteExcursion(excursion, deletedExcursion -> {

                Toast.makeText(
                        this,
                        "Excursion deleted.",
                        Toast.LENGTH_SHORT
                ).show();

                finish();
            });
        });
    }

    private void cancelExcursionAlert(int excursionId) {

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if (alarmManager == null) return;

        cancelAlert(alarmManager, excursionId * 2);

    }

    private void cancelAlert(AlarmManager alarmManager, int requestCode) {

        Intent intent = new Intent(this, MyVacationReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Checking if alert exists
        if (pendingIntent == null) {
            // Log.d("ALERT_CANCEL", "No alert found for requestCode=" + requestCode);
            return;
        }

        alarmManager.cancel(pendingIntent);

        // Making sure alert was deleted.
        // Log.d("ALERT_CANCEL", "Alert canceled for requestCode=" + requestCode);
    }


    private void alert() {
        String title = editTitle.getText().toString();

        if (excursionID <= 0) {
            Toast.makeText(this,"Save excursion before setting alert.",
                    Toast.LENGTH_SHORT).show();
            return;
        }


        repository.getExcursionById(excursionID, excursion -> {
            if (excursion == null || excursion.getExcursionDate() == null) {
                Toast.makeText(this,
                        "Save excursion date before setting alert.",
                        Toast.LENGTH_SHORT).show();
                return;
            }


            scheduleAlert(
                    excursion.getExcursionDate(),
                    title + " excursion starts today.",
                    excursionID * 2

            );

        });

    }

    private void scheduleAlert(Date date, String message, int requestCode) {

        if (date == null ) {
            return;
        }

        Intent intent = new Intent(this, MyVacationReceiver.class);

        intent.putExtra("message", message);
        // To distinguish between vacation and excursion alert
        intent.putExtra("alert_type", "EXCURSION");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Testing if alert is working..
        // Log.d("ALERT_SCHEDULE", "ALERT is running.");

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!alarmManager.canScheduleExactAlarms()) {
                // Needs user permission to schedule alarms
                Intent intent2 = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivity(intent2);
                return;
            }
        }

        // Log creation to show request code for alert
        /* Log.d("ALERT_CREATE","Scheduling alert. requestCode=" + requestCode + ", " +
                "time=" + date.getTime() + ", message=\"" + message + "\"");

         */

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, date.getTime(), pendingIntent);

    }

}