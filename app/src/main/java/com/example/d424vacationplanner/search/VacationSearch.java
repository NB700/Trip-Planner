package com.example.d424vacationplanner.search;

import com.example.d424vacationplanner.entities.Vacation;

import java.util.List;

public interface VacationSearch {
    List<Vacation> search(List<Vacation> vacations);
}
