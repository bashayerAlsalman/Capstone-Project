package net.bashayer.mygym.network.data;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;

import net.bashayer.mygym.network.model.Exercise;

import java.util.List;

public class ExerciseWorker {

    private MyGymRoomDatabase database;
    private LoadExerciseDataCallback callback;

    public ExerciseWorker(Context context, LoadExerciseDataCallback callback) {
        this.database = MyGymRoomDatabase.getDatabase(context);
        this.callback = callback;
    }

    public void getAllExercisesByCategory(int categoryId) {
        new GetExerciseByCategoryAsyncTask().execute(categoryId);
    }

    public void reverseDoneExercise(Exercise exercise) {
        exercise.setDone(!exercise.isDone());
        new SaveDoneExerciseAsyncTask().execute(exercise);
    }

    public void updateExercise(Exercise exercise) throws SQLiteConstraintException {
        new UpdateExerciseAsyncTask().execute(exercise);
    }

    public void saveExercise(Exercise exercise) throws SQLiteConstraintException {
        new SaveExerciseAsyncTask().execute(exercise);
    }

    public void deleteExercise(Exercise exercise) {
        new DeleteExerciseAsyncTask().execute(exercise);
    }

    public void getAllExercises() {
        new GetAllxerciseAsyncTask().execute();
    }


    private class UpdateExerciseAsyncTask extends AsyncTask<Exercise, Void, Void> {
        @Override
        protected Void doInBackground(Exercise... exercises) {
            database.exerciseDao().updateExercise(exercises[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            callback.onExerciseSaved();
        }
    }

    private class DeleteExerciseAsyncTask extends AsyncTask<Exercise, Void, Void> {
        @Override
        protected Void doInBackground(Exercise... exercises) {
            database.exerciseDao().deleteExercise(exercises[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            callback.onExerciseDeleted();
        }
    }

    private class SaveExerciseAsyncTask extends AsyncTask<Exercise, Void, Void> {
        @Override
        protected Void doInBackground(Exercise... exercises) {
            database.exerciseDao().insertExercise(exercises[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            callback.onExerciseSaved();
        }
    }

    private class SaveDoneExerciseAsyncTask extends AsyncTask<Exercise, Void, Void> {
        @Override
        protected Void doInBackground(Exercise... exercises) {
            database.exerciseDao().updateExercise(exercises[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            callback.onExerciseSaved();
        }
    }

    private class GetExerciseByCategoryAsyncTask extends AsyncTask<Integer, Void, List<Exercise>> {
        @Override
        protected List<Exercise> doInBackground(Integer... ids) {
            return database.exerciseDao().getExerciseByCategory(ids[0]);
        }

        @Override
        protected void onPostExecute(List<Exercise> exercises) {
            super.onPostExecute(exercises);
            callback.onExerciseLoaded(exercises);
        }
    }
    private class GetAllxerciseAsyncTask extends AsyncTask<Void, Void, List<Exercise>> {
        @Override
        protected List<Exercise> doInBackground(Void... voids) {
            return database.exerciseDao().getAllExercise();
        }

        @Override
        protected void onPostExecute(List<Exercise> exercises) {
            super.onPostExecute(exercises);
            callback.onExerciseLoaded(exercises);
        }
    }


}
