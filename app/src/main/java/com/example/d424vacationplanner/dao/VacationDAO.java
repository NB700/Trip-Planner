package com.example.d424vacationplanner.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.d424vacationplanner.entities.Vacation;

import java.util.List;
@Dao
public interface VacationDAO {

    // Insert a vacation
    @Insert
    long insertVacation(Vacation vacation);

    // Update a vacation
    @Update
    void updateVacation(Vacation vacation);

    // Delete a vacation
    @Delete
    void deleteVacation(Vacation vacation);

    // Get all vacations
    @Query("SELECT * FROM vacations")
    List<Vacation> getAllVacations();

    // Get vacation by ID
    @Query("SELECT * FROM vacations WHERE vacationID = :id")
    Vacation getVacationById(int id);

}
