package com.example.d424vacationplanner.UI;

import androidx.core.content.ContextCompat;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d424vacationplanner.R;
import com.example.d424vacationplanner.database.Repository;
import com.example.d424vacationplanner.entities.Excursion;
import com.example.d424vacationplanner.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class VacationDetails extends AppCompatActivity {

    private Calendar calendar;
    private Repository repository;
    private ExcursionAdapter adapter;
    private EditText editTitle;
    private EditText editHotel;
    private Button startDate;
    private Button endDate;
    private Date selectedStartDate;
    private Date selectedEndDate;
    private int vacationID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_details);

        /*
        // Testing if works... broadcast
        Intent testIntent = new Intent(this, MyVacationReceiver.class);
        testIntent.putExtra("message", "Receiver test message");
        sendBroadcast(testIntent);
        */


        editTitle = findViewById(R.id.editTitle);
        editHotel = findViewById(R.id.editHotel);
        startDate = findViewById(R.id.startDateButton);
        endDate = findViewById(R.id.endDateButton);

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


        calendar = Calendar.getInstance();

        startDate.setOnClickListener(view -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dpd = new DatePickerDialog(
                    VacationDetails.this,
                    (datePicker, selectYear, selectMonth, selectDay) -> {
                        calendar.set(selectYear, selectMonth, selectDay);

                        // This Date will eventually be stored in database
                        selectedStartDate = calendar.getTime();
                        // Log.d("DATE_PICKER", "Selected start date is: " + selectedStartDate);
                        // Logger prints "Selected start date is: day_of_week MM dd hh:mm:ss PST yy"

                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
                        String dateString = sdf.format(selectedStartDate);
                        // Log.d("DATE_PICKER_STRING", "Selected start date is: " + dateString);
                        // Logger prints "Selected start date is: MM/dd/yy
                        startDate.setText(dateString);
                    },
                    year, month, day
            );

            // Set min date to today if it exists
            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);
            dpd.getDatePicker().setMinDate(today.getTimeInMillis());

            dpd.show();
        });

        endDate.setOnClickListener(view -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dpd = new DatePickerDialog(
                    VacationDetails.this,
                    (datePicker, selectYear, selectMonth, selectDay) -> {
                        calendar.set(selectYear, selectMonth, selectDay);

                        selectedEndDate = calendar.getTime();

                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
                        String dateString = sdf.format(selectedEndDate);
                        endDate.setText(dateString);
                    },
                    year, month, day
            );

            if (selectedStartDate == null) {
                Toast.makeText(this, "Select a start date first.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Set min date to start date if it exists. Allows same day alert testing.
            /*
            if (selectedStartDate != null) {
                dpd.getDatePicker().setMinDate(selectedStartDate.getTime());
            }
             */

            // d. Include validation that the vacation end date is after the start date.
            if (selectedStartDate != null) {
                Calendar minEndDate = Calendar.getInstance();
                minEndDate.setTime(selectedStartDate);
                minEndDate.add(Calendar.DAY_OF_MONTH, 1); // +1 day

                dpd.getDatePicker().setMinDate(minEndDate.getTimeInMillis());
            }

            dpd.show();
        });

        repository = new Repository(getApplication());

        // Get vacationID from Intent on Vacation List page
        vacationID = getIntent().getIntExtra("vacationID", -1);

        // Show vacation data persistently when click vacation in recycler view
        if (vacationID != -1) {
            // Fetch Vacation asynchronously
            repository.getVacationById(vacationID, vacation -> {
                if (vacation != null) {
                    // Populate EditText fields
                    editTitle.setText(vacation.getVacationTitle());
                    editHotel.setText(vacation.getVacationHotel());

                    // Populate buttons with formatted dates
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.getDefault());

                    Date start = vacation.getStartDate();
                    if (start != null) {
                        startDate.setText(sdf.format(start));
                        selectedStartDate = start;
                    } else {
                        startDate.setText("Select Start Date"); // placeholder text
                        selectedStartDate = null;
                    }

                    Date end = vacation.getEndDate();
                    if (end != null) {
                        endDate.setText(sdf.format(end));
                        selectedEndDate = end;
                    } else {
                        endDate.setText("Select End Date"); // placeholder text
                        selectedEndDate = null;
                    }

                    selectedStartDate = vacation.getStartDate();
                    selectedEndDate = vacation.getEndDate();
                }
            });
        }

        //Recycler View
        RecyclerView excursionRecycler = findViewById(R.id.recyclerExcursion);

        adapter = new ExcursionAdapter(new ArrayList<>());

        // Setting actions on user frontend.
        adapter.setOnExcursionClickListener(excursion -> {
            Intent intent = new Intent(this, ExcursionDetails.class);
            intent.putExtra("excursionID", excursion.getExcursionID());
            intent.putExtra("vacationID", vacationID); // To select excursion and edit existing excursion
            startActivity(intent);
        });

        excursionRecycler.setAdapter(adapter);
        excursionRecycler.setLayoutManager(new LinearLayoutManager(this));

        repository.getExcursionsForVacation(vacationID, excursions -> {
            // Gives data to recycler view
            adapter.setExcursions(excursions);
        });


        FloatingActionButton fab = findViewById(R.id.addExcursionFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (vacationID == -1) {
                    Toast.makeText(VacationDetails.this,
                            "Save vacation before adding excursion.",
                            Toast.LENGTH_SHORT).show();


                } else {
                    Intent intent = new Intent(VacationDetails.this, ExcursionDetails.class);
                    intent.putExtra("vacationID", vacationID); // This part makes it necessary to add vacation before moving pages...
                    startActivity(intent);
                }
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.vacation_details), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_details, menu);
        return true;
    }

    // Controls what happens when you click on menu item { Save, Delete, etc. }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuSave) {
            saveVacation();
            return true;
        }
        if (id == R.id.menuDelete) {
            deleteVacation();
            return true;
        }
        if (id == R.id.menuAlert) {
            alert();
            return true;
        }
        if (id == R.id.menuShare) {
            share();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveVacation() {
        // Get user input
        String title = editTitle.getText().toString().trim();
        String hotel = editHotel.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(this, "Enter a title.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Test if save is working...
        // Log.d("SAVE","ID=" + vacationID +", start=" + selectedStartDate +", end=" + selectedEndDate);

        // Create Vacation object
        Vacation vacation = new Vacation(title, hotel, selectedStartDate, selectedEndDate);

        if (vacationID == -1) {

            // Insert into database via repository
            repository.addVacation(vacation, savedVacation -> {
                Toast.makeText(this, "Vacation saved.", Toast.LENGTH_SHORT).show();
                finish();
            });

        } else {
            // Vacation must exist to update.
            vacation.setVacationID(vacationID);

            repository.updateVacation(vacation, updatedVacation -> {
                // Log.d("UPDATE", "Vacation updated: " + vacation.getVacationID());
                Toast.makeText(this, "Vacation updated.", Toast.LENGTH_SHORT).show();
                finish();
            });

        }

    }

    private void deleteVacation() {

        if (vacationID <= 0) {
            Toast.makeText(
                    this,
                    "Save vacation before deleting.",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        repository.getVacationById(vacationID, vacation -> {

            if (vacation == null) {
                Toast.makeText(
                        this,
                        "Vacation does not exist.",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            // b.  Implement validation so that a vacation cannot be deleted if excursions are associated with it.
            repository.getExcursionsForVacation(vacationID, excursions -> {

                if (excursions != null && !excursions.isEmpty()) {
                    Toast.makeText(
                            this,
                            "Cannot delete vacation with associated excursions.",
                            Toast.LENGTH_LONG
                    ).show();
                    return;
                }

                // Delete alerts before deleting item
                cancelVacationAlerts(vacationID);

                repository.deleteVacation(vacation, deletedVacation -> {

                    Toast.makeText(
                            this,
                            "Vacation deleted.",
                            Toast.LENGTH_SHORT
                    ).show();

                    finish();
                });
            });
        });
    }

    private void cancelVacationAlerts(int vacationId) {

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if (alarmManager == null) return;

        // Start date alert
        cancelAlert(alarmManager, vacationId * 2);

        // End date alert
        cancelAlert(alarmManager, vacationId * 2 + 1);
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

        if (vacationID <= 0) {
            Toast.makeText(this,"Save vacation before setting alerts.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        repository.getVacationById(vacationID, vacation -> {
            if (vacation == null || vacation.getStartDate() == null || vacation.getEndDate() == null) {
                Toast.makeText(this,
                        "Save vacation start and end dates first.",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            scheduleAlert(
                    vacation.getStartDate(),
                    title + " vacation starts today.",
                    vacationID * 2

            );

            scheduleAlert(
                    vacation.getEndDate(),
                    title + " vacation ends today.",
                    vacationID * 2 + 1
            );

        });

    }

    private void share() {
        String title = editTitle.getText().toString();
        String hotel = editHotel.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);

        String startDate = (selectedStartDate != null)
                ? sdf.format(selectedStartDate)
                : "N/A";

        String endDate = (selectedEndDate != null)
                ? sdf.format(selectedEndDate)
                : "N/A";

        repository.getExcursionsForVacation(vacationID, excursions -> {

            String allExcursions = "";
            for (Excursion excursion : excursions) {
                String titleE = excursion.getExcursionTitle();
                String dateE = (excursion.getExcursionDate() != null)
                        ? sdf.format(excursion.getExcursionDate())
                        : "N/A";

                allExcursions += "Excursion Title: " + titleE + "\n" + "Excursion Date: " + dateE + "\n";
            }

            // Build full data after excursions are fetched
            String fullData = "Vacation Title: " + title + "\n"
                    + "Vacation Hotel: " + hotel + "\n"
                    + "Vacation Start Date: " + startDate + "\n"
                    + "Vacation End Date: " + endDate + "\n"
                    + allExcursions;

            // Create and start share intent
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TITLE, "Vacation: " + title);
            sendIntent.putExtra(Intent.EXTRA_TEXT, fullData);
            sendIntent.setType("text/plain");
            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        });
    }

    private void scheduleAlert(Date date, String message, int requestCode) {

        if (date == null ) {
            return;
        }

        Intent intent = new Intent(this, MyVacationReceiver.class);

        intent.putExtra("message", message);
        // To distinguish between vacation and excursion alert
        intent.putExtra("alert_type", "VACATION");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Testing if alert is working..
         Log.d("ALERT_SCHEDULE", "ALERT is running... ");

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

    @Override
    protected void onResume() {
        super.onResume();
        loadExcursions();
    }

    private void loadExcursions() {
        repository.getExcursionsForVacation(vacationID, excursions -> {
            adapter.setExcursions(excursions);
        });
    }

}