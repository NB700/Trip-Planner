package com.example.d424vacationplanner.search;

import com.example.d424vacationplanner.entities.Vacation;
import com.example.d424vacationplanner.search.VacationSearch;

import java.util.List;
import java.util.stream.Collectors;

public class HotelNameSearch implements VacationSearch {
    private String hotelName;
    public HotelNameSearch(String hotelName) {
        this.hotelName = hotelName;
    }
    @Override
    public List<Vacation> search(List<Vacation> vacations) {
        return vacations.stream()
                .filter(v -> v.getVacationHotel().toLowerCase().contains(hotelName.toLowerCase()))
                .collect(Collectors.toList());
    }
}
