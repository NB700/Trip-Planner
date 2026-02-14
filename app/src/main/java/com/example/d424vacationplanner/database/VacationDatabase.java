package com.example.d424vacationplanner.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.d424vacationplanner.dao.VacationDAO;
import com.example.d424vacationplanner.dao.ExcursionDAO;
import com.example.d424vacationplanner.entities.Vacation;
import com.example.d424vacationplanner.entities.Excursion;

@Database(entities = {Vacation.class, Excursion.class}, version = 9, exportSchema = false)
@TypeConverters({Converter.class})
public abstract class VacationDatabase extends RoomDatabase {

    public abstract VacationDAO vacationDao();
    public abstract ExcursionDAO excursionDao();

    private static volatile VacationDatabase INSTANCE;

    public static VacationDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (VacationDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    VacationDatabase.class,
                                    "VacationDatabase.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
