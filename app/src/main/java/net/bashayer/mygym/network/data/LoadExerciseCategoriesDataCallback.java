package net.bashayer.mygym.network.data;

import net.bashayer.mygym.network.model.ExerciseCategory;

import java.util.List;

public interface LoadExerciseCategoriesDataCallback {
    void onExerciseCategoriesLoaded(List<ExerciseCategory> exerciseCategories);
}
