package net.bashayer.mygym.network.data;

import android.content.Context;
import android.os.AsyncTask;

import net.bashayer.mygym.network.model.ExerciseCategory;

import java.util.List;

public class ExerciseCategoriesWorker {

    private MyGymRoomDatabase database;
    private LoadExerciseCategoriesDataCallback callback;

    public ExerciseCategoriesWorker(Context context, LoadExerciseCategoriesDataCallback callback) {
        this.database = MyGymRoomDatabase.getDatabase(context);
        this.callback = callback;
    }

    public void saveExerciseCategories(List<ExerciseCategory> exerciseCategories) {
        new SaveExerciseCategoriesAsyncTask().execute(exerciseCategories);
    }

    public void getAllExerciseCategories() {
        new GetExerciseCategoriesAsyncTask().execute();
    }

    private class SaveExerciseCategoriesAsyncTask extends AsyncTask<List<ExerciseCategory>, Void, Void> {
        @Override
        protected Void doInBackground(List<ExerciseCategory>... exerciseCategories) {
            database.exerciseDao().insetExerciseCategories(exerciseCategories[0]);
            return null;
        }

    }

    private class GetExerciseCategoriesAsyncTask extends AsyncTask<Void, Void, List<ExerciseCategory>> {
        @Override
        protected List<ExerciseCategory> doInBackground(Void... voids) {
            return database.exerciseDao().getAllExerciseCategories();
        }

        @Override
        protected void onPostExecute(List<ExerciseCategory> exerciseCategories) {
            super.onPostExecute(exerciseCategories);
            callback.onExerciseCategoriesLoaded(exerciseCategories);
        }
    }
}
