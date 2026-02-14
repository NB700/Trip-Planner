package com.example.d424vacationplanner.database;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import com.example.d424vacationplanner.dao.VacationDAO;
import com.example.d424vacationplanner.dao.ExcursionDAO;
import com.example.d424vacationplanner.entities.Vacation;
import com.example.d424vacationplanner.entities.Excursion;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class Repository {

    private VacationDAO vacationDAO;
    private ExcursionDAO excursionDAO;

    private static final ExecutorService dbExecutor =
            Executors.newFixedThreadPool(4);

    public Repository(Application application) {
        VacationDatabase db = VacationDatabase.getDatabase(application);
        vacationDAO = db.vacationDao();
        excursionDAO = db.excursionDao();
    }


    // Vacation

    public void addVacation(Vacation vacation, Consumer<Vacation> callback) {
        dbExecutor.execute(() -> {
            vacationDAO.insertVacation(vacation);
            new Handler(Looper.getMainLooper()).post(() -> callback.accept(vacation));
        });
    }

    public void updateVacation(Vacation vacation, Consumer<Vacation> callback) {
        dbExecutor.execute(() -> {
            vacationDAO.updateVacation(vacation);
            new Handler(Looper.getMainLooper()).post(() -> callback.accept(vacation));
        });
    }

    public void deleteVacation(Vacation vacation, Consumer<Vacation> callback) {
        dbExecutor.execute(() -> {
            vacationDAO.deleteVacation(vacation);
            new Handler(Looper.getMainLooper()).post(() -> callback.accept(vacation));
        });
    }

    public void getVacationById(int id, Consumer<Vacation> callback) {
        dbExecutor.execute(() -> {
            Vacation vacation = vacationDAO.getVacationById(id);
            new Handler(Looper.getMainLooper()).post(() -> callback.accept(vacation));
        });
    }

    public void getAllVacations(Consumer<List<Vacation>> callback) {
        dbExecutor.execute(() -> {
            List<Vacation> vacations = vacationDAO.getAllVacations();
            new Handler(Looper.getMainLooper()).post(() -> callback.accept(vacations));
        });
    }


    // Excursion

    public void addExcursion(Excursion excursion, Consumer<Excursion> callback) {
        dbExecutor.execute(() -> {
            excursionDAO.insertExcursion(excursion);
            new Handler(Looper.getMainLooper()).post(() -> callback.accept(excursion));
        });

    }

    public void updateExcursion(Excursion excursion, Consumer<Excursion> callback) {
        dbExecutor.execute(() -> {
            excursionDAO.updateExcursion(excursion);
            new Handler(Looper.getMainLooper()).post(() -> callback.accept(excursion));
        });
    }

    public void deleteExcursion(Excursion excursion, Consumer<Excursion> callback) {
        dbExecutor.execute(() -> {
            excursionDAO.deleteExcursion(excursion);
            new Handler(Looper.getMainLooper()).post(() -> callback.accept(excursion));
        });
    }

    public void getAllExcursions(Consumer<List<Excursion>> callback) {
        dbExecutor.execute(() -> {
            List<Excursion> excursions = excursionDAO.getAllExcursions();
            new Handler(Looper.getMainLooper()).post(() -> callback.accept(excursions));
        });
    }

    public void getExcursionById(int id, Consumer<Excursion> callback) {
        dbExecutor.execute(() -> {
            Excursion excursion = excursionDAO.getExcursionById(id); // DAO query
            new Handler(Looper.getMainLooper()).post(() -> callback.accept(excursion));
        });
    }

    public void getExcursionsForVacation(int vacationID, Consumer<List<Excursion>> callback) {
        dbExecutor.execute(() -> {
            List<Excursion> excursions = excursionDAO.getExcursionsForVacation(vacationID);
            new Handler(Looper.getMainLooper()).post(() -> callback.accept(excursions));
        });
    }

}
