package net.bashayer.mygym.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import net.bashayer.mygym.R;
import net.bashayer.mygym.network.data.ExerciseWorker;
import net.bashayer.mygym.network.data.LoadExerciseDataCallback;
import net.bashayer.mygym.network.model.Exercise;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class ExerciseWidget extends AppWidgetProvider implements LoadExerciseDataCallback {
    private static List<Exercise> exercises;
    private static int exerciseIndex = 0;

    private Context context;
    private AppWidgetManager appWidgetManager;
    private int[] appWidgetIds;

    private ExerciseWorker worker;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.exercise_widget);
        views.removeAllViews(R.id.root);

        for (int i = 0; i < 6; i++) {
            if (exerciseIndex < exercises.size()) {
                RemoteViews child = new RemoteViews(context.getPackageName(), R.layout.item_box_exercise);
                child.setTextViewText(R.id.exercise_name, exercises.get(exerciseIndex).getExerciseName());
                child.setTextViewText(R.id.exercise_number, exercises.get(exerciseIndex).getExerciseId() + "");

                views.addView(R.id.root, child);
                exerciseIndex++;
            }
        }
        updateIndex();
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


    private static void updateIndex() {
        if (exerciseIndex >= exercises.size()) {
            exerciseIndex = 0;
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        this.context = context;
        this.appWidgetManager = appWidgetManager;
        this.appWidgetIds = appWidgetIds;
        worker = new ExerciseWorker(context, this);
        worker.getAllExercises();
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


    @Override
    public void onExerciseLoaded(List<Exercise> exercises) {
        this.exercises = exercises;
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onExerciseSaved() {

    }

    @Override
    public void onExerciseDeleted() {

    }
}

