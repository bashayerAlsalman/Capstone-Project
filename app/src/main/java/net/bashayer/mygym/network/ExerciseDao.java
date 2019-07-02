package net.bashayer.mygym.network;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import net.bashayer.mygym.network.model.Exercise;
import net.bashayer.mygym.network.model.ExerciseCategory;

import java.util.List;

@Dao
public interface ExerciseDao {
    @Delete
    void deleteExercise(Exercise exercise);

    @Update
    void updateExercise(Exercise exercise);

    @Insert
    void insertExercise(Exercise exercise);

    @Query("SELECT * from exercisecategory")
    List<ExerciseCategory> getAllExerciseCategories();

    @Insert
    void insetExerciseCategories(List<ExerciseCategory> exerciseCategories);

    @Query("SELECT * from Exercise where exerciseCategoryId =:categoryId ")
    List<Exercise> getExerciseByCategory(int categoryId);

    @Query("SELECT * from Exercise")
    List<Exercise> getAllExercise();
}
