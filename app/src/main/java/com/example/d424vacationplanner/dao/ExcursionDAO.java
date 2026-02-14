package com.example.d424vacationplanner.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.d424vacationplanner.entities.Excursion;

import java.util.List;
@Dao
public interface ExcursionDAO {

    // Insert a excursion
    @Insert
    long insertExcursion(Excursion excursion);

    // Update a excursion
    @Update
    void updateExcursion(Excursion excursion);

    // Delete a excursion
    @Delete
    void deleteExcursion(Excursion excursion);

    // Get all excursions
    @Query("SELECT * FROM excursions")
    List<Excursion> getAllExcursions();

    // Get excursion by ID
    @Query("SELECT * FROM excursions WHERE excursionID = :id")
    Excursion getExcursionById(int id);

    // Get excursions for a specific vacation
    @Query("SELECT * FROM excursions WHERE vacationID = :vacationId")
    List<Excursion> getExcursionsForVacation(int vacationId);

}
