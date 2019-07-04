package net.bashayer.mygym.network.data;

import androidx.lifecycle.LiveData;

import net.bashayer.mygym.network.model.ExerciseCategory;

import java.util.List;

public interface LoadExerciseCategoriesDataCallback {
    void onExerciseCategoriesLoaded(LiveData<List<ExerciseCategory>> exerciseCategories);
}
