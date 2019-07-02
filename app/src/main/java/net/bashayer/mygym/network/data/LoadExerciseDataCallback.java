package net.bashayer.mygym.network.data;

import net.bashayer.mygym.network.model.Exercise;

import java.util.List;

public interface LoadExerciseDataCallback {
    void onExerciseLoaded(List<Exercise> exercises);
    void onExerciseSaved();
    void onExerciseDeleted();
}
