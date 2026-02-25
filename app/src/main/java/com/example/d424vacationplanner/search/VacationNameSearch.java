package com.example.d424vacationplanner.search;

import com.example.d424vacationplanner.entities.Vacation;
import com.example.d424vacationplanner.search.VacationSearch;

import java.util.List;
import java.util.stream.Collectors;

public class VacationNameSearch implements VacationSearch {
    private String name;
    public VacationNameSearch(String name) {
        this.name = name;
    }
    @Override
    public List<Vacation> search(List<Vacation> vacations) {
        return vacations.stream()
                .filter(v -> v.getVacationTitle().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }
}
