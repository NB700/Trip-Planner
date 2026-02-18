package com.example.d424vacationplanner.UI;

import com.example.d424vacationplanner.entities.Vacation;

import java.util.List;

public class VacationSearchService {
    public List<Vacation> search(List<Vacation> vacations,
                                 VacationSearch searchStrategy) {
        return searchStrategy.search(vacations);
    }
}
