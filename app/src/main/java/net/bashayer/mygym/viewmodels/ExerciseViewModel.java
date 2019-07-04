package net.bashayer.mygym.viewmodels;

import android.app.Application;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import net.bashayer.mygym.network.data.LoadExerciseDataCallback;
import net.bashayer.mygym.network.data.MyGymRoomDatabase;
import net.bashayer.mygym.network.model.Exercise;
import net.bashayer.mygym.network.model.ExerciseCategory;

import java.util.List;

public class ExerciseViewModel extends AndroidViewModel {

    private MyGymRoomDatabase database;

    private LiveData<List<Exercise>> exercises;
    private LiveData<List<ExerciseCategory>> exerciseCategories;
    private LoadExerciseDataCallback callback;

    public ExerciseViewModel(@NonNull Application application) {
        super(application);
        this.database = MyGymRoomDatabase.getDatabase(application);
        this.exercises = new MutableLiveData<>();
        this.exerciseCategories = new MutableLiveData<>();
    }

    public void setCallback(LoadExerciseDataCallback callback) {
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


    private void setExercises(LiveData<List<Exercise>> exercises) {
        this.exercises = exercises;
    }

    private void setExercisesCategories(LiveData<List<ExerciseCategory>> exercisesCategories) {
        this.exerciseCategories = exerciseCategories;
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

    private class GetExerciseByCategoryAsyncTask extends AsyncTask<Integer, Void, LiveData<List<Exercise>>> {
        @Override
        protected LiveData<List<Exercise>> doInBackground(Integer... ids) {
            return database.exerciseDao().getExerciseByCategory(ids[0]);
        }

        @Override
        protected void onPostExecute(LiveData<List<Exercise>> exercises) {
            super.onPostExecute(exercises);
            setExercises(exercises);
            callback.onExerciseLoaded(exercises);
        }
    }

    private class GetAllxerciseAsyncTask extends AsyncTask<Void, Void, LiveData<List<Exercise>>> {
        @Override
        protected LiveData<List<Exercise>> doInBackground(Void... voids) {
            return database.exerciseDao().getAllExercise();
        }

        @Override
        protected void onPostExecute(LiveData<List<Exercise>> exercises) {
            super.onPostExecute(exercises);
            setExercises(exercises);
            callback.onExerciseLoaded(exercises);
        }
    }

}
