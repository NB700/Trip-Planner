package com.example.d424vacationplanner.UI;

import com.example.d424vacationplanner.entities.Excursion;
import com.example.d424vacationplanner.entities.Vacation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportData {

    public String reportTitle;
    public String date;
    public List<Vacation> vacations;

    public Map<Integer, List<Excursion>> vacationExcursions = new HashMap<>();

    public ReportData() {
        vacations = new ArrayList<>();
    }
}
