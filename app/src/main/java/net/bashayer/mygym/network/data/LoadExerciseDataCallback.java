package net.bashayer.mygym.network.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveData;

import net.bashayer.mygym.network.model.Exercise;

import java.util.List;

public interface LoadExerciseDataCallback {
    void onExerciseLoaded(LiveData<List<Exercise>> exercises);

    void onExerciseLoaded(List<Exercise> exercises);

    void onExerciseSaved();

    void onExerciseDeleted();
}
