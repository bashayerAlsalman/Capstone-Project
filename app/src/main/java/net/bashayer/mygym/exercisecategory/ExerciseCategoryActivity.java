package net.bashayer.mygym.exercisecategory;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.bashayer.mygym.MyGymApplication;
import net.bashayer.mygym.R;
import net.bashayer.mygym.exercise.ExerciseActivity;
import net.bashayer.mygym.network.ExerciseService;
import net.bashayer.mygym.network.data.LoadExerciseCategoriesDataCallback;
import net.bashayer.mygym.network.model.ExerciseCategory;
import net.bashayer.mygym.viewmodels.ExerciseCategoryViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static net.bashayer.mygym.common.Constants.EXERCISE_CATEGORY_KEY;
import static net.bashayer.mygym.common.Constants.EXERCISE_CATEGORY_NAME;

public class ExerciseCategoryActivity extends MyGymApplication implements ExerciseCategoryCallback, LoadExerciseCategoriesDataCallback, LifecycleOwner {


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.my_toolbar)
    Toolbar toolbar;

    private ExerciseCategoryViewModel viewModel;
    private List<ExerciseCategory> exerciseCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_category);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.exercises_categories));

        viewModel = ViewModelProviders.of(this).get(ExerciseCategoryViewModel.class);
        viewModel.setCallback(this::onExerciseCategoriesLoaded);
        viewModel.getAllExerciseCategories();
    }

    private void loadData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.mocky.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        ExerciseService service = retrofit.create(ExerciseService.class);

        service.getAllExercisesCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ExerciseCategory>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<ExerciseCategory> exerciseCategories) {
                        initExerciseCategoryAdapter(exerciseCategories);
                        //save the exercise categories list after uploading it from the api
                        viewModel.saveExerciseCategories(exerciseCategories);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    private void initExerciseCategoryAdapter(List<ExerciseCategory> exerciseCategoryList) {
        ExerciseCategoryAdapter adapter = new ExerciseCategoryAdapter(this, exerciseCategoryList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onExerciseCategoryClick(ExerciseCategory exerciseCategory) {
        Intent intent = new Intent(this, ExerciseActivity.class);
        intent.putExtra(EXERCISE_CATEGORY_KEY, exerciseCategory.getId());
        intent.putExtra(EXERCISE_CATEGORY_NAME, exerciseCategory.getName());

        startActivity(intent);
    }

    @Override
    public void onExerciseCategoriesLoaded(LiveData<List<ExerciseCategory>> exerciseCategoriesList) {
        exerciseCategoriesList.observe(this, new androidx.lifecycle.Observer<List<ExerciseCategory>>() {
            @Override
            public void onChanged(List<ExerciseCategory> exerciseCategories) {
                setExerciseCategories(exerciseCategories);
            }
        });
    }

    private void setExerciseCategories(List<ExerciseCategory> exerciseCategories) {
        this.exerciseCategories = exerciseCategories;
        if (exerciseCategories == null || exerciseCategories.size() == 0) {
            loadData();
        } else {
            initExerciseCategoryAdapter(exerciseCategories);
        }
    }
}
