package com.example.d424vacationplanner.UI;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.example.d424vacationplanner.entities.Vacation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Report extends AppCompatActivity {

    private ReportData reportData;
    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_report);

        Toolbar toolbar = findViewById(R.id.materialToolbar);
        setSupportActionBar(toolbar);
        Drawable overflowIcon = toolbar.getOverflowIcon();
        if (overflowIcon != null) overflowIcon.setTint(ContextCompat.getColor(this, R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        repository = new Repository(getApplication());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.report), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        generateReport();
    }

    private void generateReport() {
        reportData = new ReportData();
        reportData.reportTitle = "Vacations & Excursions Report";
        reportData.date = new SimpleDateFormat("MM/dd/yyyy  |  HH:mm:ss").format(new Date());

        repository.getAllVacations(vacationsList -> {
            if (vacationsList == null || vacationsList.isEmpty()) {
                runOnUiThread(this::displayReport);
                return;
            }

            reportData.vacations.addAll(vacationsList);

            final int totalVacations = vacationsList.size();
            final int[] loadedCount = {0};

            for (Vacation vacation : vacationsList) {
                repository.getExcursionsForVacation(vacation.vacationID, excursionsList -> {
                    if (excursionsList != null) {
                        reportData.vacationExcursions.put(vacation.vacationID, excursionsList);
                    }

                    loadedCount[0]++;

                    // Only update UI after all excursions are loaded
                    if (loadedCount[0] == totalVacations) {
                        runOnUiThread(this::displayReport);
                    }
                });
            }
        });
    }

    private void displayReport() {
        LinearLayout container = findViewById(R.id.reportContainer);
        container.removeAllViews();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        addLargeField(container, reportData.reportTitle, 22f);
        addLargeField(container, reportData.date, 16f);

        for (Vacation vacation : reportData.vacations) {
            TextView vacationHeader = new TextView(this);
            vacationHeader.setText(safeString(vacation.getVacationTitle()));
            vacationHeader.setTypeface(null, Typeface.BOLD);
            vacationHeader.setTextSize(16f);
            vacationHeader.setPadding(0, 16, 0, 8);
            container.addView(vacationHeader);

            addField(container, "Hotel", safeString(vacation.getVacationHotel()));
            addField(container, "Start Date", safeDate(vacation.getStartDate(), sdf));
            addField(container, "End Date", safeDate(vacation.getEndDate(), sdf));

            List<Excursion> excursions = reportData.vacationExcursions.get(vacation.vacationID);
            if (excursions != null && !excursions.isEmpty()) {

                TextView excursionHeader = new TextView(this);
                excursionHeader.setText("Excursions:");
                excursionHeader.setTypeface(null, Typeface.BOLD_ITALIC);
                excursionHeader.setPadding(8, 8, 0, 4);
                container.addView(excursionHeader);

                for (Excursion excursion : excursions) {
                    addField(container,
                            "• " + safeString(excursion.getExcursionTitle()),
                            safeDate(excursion.getExcursionDate(), sdf));
                }
            } else {

                TextView excursionHeader = new TextView(this);
                excursionHeader.setText("Excursions:");
                excursionHeader.setTypeface(null, Typeface.BOLD_ITALIC);
                excursionHeader.setPadding(8, 8, 0, 4);
                container.addView(excursionHeader);

                TextView noneText = new TextView(this);
                noneText.setText("None");
                noneText.setPadding(16, 4, 0, 4);
                container.addView(noneText);
            }
        }
    }

    private void addLargeField(LinearLayout container, String text, float textSize) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(textSize);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setPadding(0, 8, 0, 12);
        container.addView(textView);

    }

    private void addField(LinearLayout container, String label, String value) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setPadding(0, 4, 0, 4);

        TextView labelView = new TextView(this);
        labelView.setText(label + ": ");
        labelView.setTypeface(null, Typeface.BOLD);
        labelView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        TextView valueView = new TextView(this);
        valueView.setText(value);
        valueView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2));

        row.addView(labelView);
        row.addView(valueView);
        container.addView(row);
    }

    private String safeString(String s) {
        return s != null ? s : "";
    }

    private String safeDate(Date d, SimpleDateFormat sdf) {
        return d != null ? sdf.format(d) : "";
    }
}
