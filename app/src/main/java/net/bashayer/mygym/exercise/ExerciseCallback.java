package net.bashayer.mygym.exercise;

import android.widget.Button;
import android.widget.ImageView;

import net.bashayer.mygym.network.model.Exercise;

public interface ExerciseCallback {
    void onExerciseClick(Exercise exercise);
    void onExerciseDoneClick(Exercise exercise, Button button, ImageView view);
}
