package com.example.d424vacationplanner;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

import com.example.d424vacationplanner.search.VacationNameSearch;
import com.example.d424vacationplanner.search.VacationSearch;
import com.example.d424vacationplanner.search.VacationSearchService;
import com.example.d424vacationplanner.entities.Vacation;

public class VacationNameSearchTest {

    private List<Vacation> vacationList;
    private VacationSearchService searchService;

    @Before
    public void setUp() {
        vacationList = new ArrayList<>();

        Date startDate = new Date(1700000000000L);
        Date endDate = new Date(1700800000000L);
        vacationList.add(new Vacation("Hawaii Beach", "Hotel A", startDate, endDate));

        startDate = new Date(1700810000000L);
        endDate = new Date(1701800000000L);
        vacationList.add(new Vacation("Egypt Pyramid", "Hotel B", startDate, endDate));

        searchService = new VacationSearchService();
    }

    // Test that Empty search input should return all vacations
    @Test
    public void returnAllVacations() {
        VacationSearch searchStrategy = new VacationNameSearch("");

        List<Vacation> result = searchService.search(vacationList, searchStrategy);

        assertEquals(vacationList, result);
    }

    // Test that Valid and Case-insensitive filtering works correctly
    @Test
    public void returnMatchingVacation() {
        VacationSearch searchStrategy = new VacationNameSearch("hawaii");

        List<Vacation> result = searchService.search(vacationList, searchStrategy);

        assertEquals(1, result.size());
        assertEquals("Hawaii Beach", result.get(0).getVacationTitle());
    }

}
