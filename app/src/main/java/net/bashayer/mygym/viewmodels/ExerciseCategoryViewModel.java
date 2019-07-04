package net.bashayer.mygym.viewmodels;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import net.bashayer.mygym.network.data.LoadExerciseCategoriesDataCallback;
import net.bashayer.mygym.network.data.MyGymRoomDatabase;
import net.bashayer.mygym.network.model.Exercise;
import net.bashayer.mygym.network.model.ExerciseCategory;

import java.util.List;

public class ExerciseCategoryViewModel extends AndroidViewModel {

    private MyGymRoomDatabase database;

    private LiveData<List<Exercise>> exercises;
    private LiveData<List<ExerciseCategory>> exerciseCategories;
    private LoadExerciseCategoriesDataCallback callback;

//    public ExerciseCategoryViewModel(@NonNull Application application, LoadExerciseCategoriesDataCallback callback) {
//        super(application);
//        this.database = MyGymRoomDatabase.getDatabase(application);
//        this.exercises = new LiveData<>();
//        this.exerciseCategories = new LiveData<>();
//        this.callback = callback;
//    }

    public ExerciseCategoryViewModel(@NonNull Application application) {
        super(application);
        this.database = MyGymRoomDatabase.getDatabase(application);
        this.exercises = new MutableLiveData<>();
        this.exerciseCategories = new MutableLiveData<>();
    }

    public void setCallback(LoadExerciseCategoriesDataCallback callback) {
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

    private class GetExerciseCategoriesAsyncTask extends AsyncTask<Void, Void, LiveData<List<ExerciseCategory>>> {
        @Override
        protected LiveData<List<ExerciseCategory>> doInBackground(Void... voids) {
            return database.exerciseDao().getAllExerciseCategories();
        }

        @Override
        protected void onPostExecute(LiveData<List<ExerciseCategory>> exerciseCategories) {
            super.onPostExecute(exerciseCategories);

            callback.onExerciseCategoriesLoaded(exerciseCategories);
        }
    }

}
