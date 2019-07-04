package net.bashayer.mygym.addexercise;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import net.bashayer.mygym.MyGymApplication;
import net.bashayer.mygym.R;
import net.bashayer.mygym.exercise.ExerciseCallback;
import net.bashayer.mygym.network.ExerciseService;
import net.bashayer.mygym.network.data.LoadExerciseDataCallback;
import net.bashayer.mygym.network.model.Exercise;
import net.bashayer.mygym.network.model.ExerciseCategory;
import net.bashayer.mygym.viewmodels.ExerciseViewModel;

import java.util.ArrayList;
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
import static net.bashayer.mygym.common.Constants.EXERCISE_KEY;

public class AddExerciseActivity extends MyGymApplication implements ExerciseCallback, LoadExerciseDataCallback, LifecycleOwner {


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.first_factor)
    TextInputEditText firstFactorTextInputEditText;
    @BindView(R.id.second_factor)
    TextInputEditText secondFactorTextInputEditText;
    @BindView(R.id.third_factor)
    TextInputEditText thirdFactorTextInputEditText;


    @BindView(R.id.first_factor_text_input_layout)
    TextInputLayout firstFactorTextInputLayout;
    @BindView(R.id.second_factor_text_input_layout)
    TextInputLayout secondFactorTextInputLayout;
    @BindView(R.id.third_factor_text_input_layout)
    TextInputLayout thirdFactorTextInputLayout;

    @BindView(R.id.specs_root_linear_layout)
    LinearLayout specsLinearLayout;

    @BindView(R.id.button)
    AppCompatButton button;

    @BindView(R.id.adView)
    AdView mAdView;
    @BindView(R.id.my_toolbar)
    Toolbar toolbar;

    private AddExerciseAdapter adapter;
    private int categoryId;

    private ExerciseViewModel viewModel;

    private Exercise selectedExercise = null;
    private List<Exercise> allInsertedExercises = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        ButterKnife.bind(this);
        viewModel = ViewModelProviders.of(this).get(ExerciseViewModel.class);
        viewModel.setCallback(this);
        String categoryName = getIntent().getStringExtra(EXERCISE_CATEGORY_NAME);

        toolbar.setTitle(categoryName);
        setSupportActionBar(toolbar);

        this.categoryId = getIntent().getIntExtra(EXERCISE_CATEGORY_KEY, 1);

        viewModel.getAllExercisesByCategory(this.categoryId);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (formIsValid()) {
                    saveExercise();
                }
            }
        });

        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);
    }


    private boolean formIsValid() {
        if (selectedExercise == null) {
            showMessage(R.string.please_select_exercise);
            return false;
        }
        for (int i = 0; i < selectedExercise.getSpecifications().size(); i++) {
            int value = 0;

            String name = selectedExercise.getSpecifications().get(i).getName();
            int max = selectedExercise.getSpecifications().get(i).getMax();
            int min = selectedExercise.getSpecifications().get(i).getMin();

            switch (i) {
                case 0:
                    value = getIntValue(this.firstFactorTextInputEditText.getText().toString());
                    break;
                case 1:
                    value = getIntValue(this.secondFactorTextInputEditText.getText().toString());
                    break;
                case 2:
                    value = getIntValue(this.thirdFactorTextInputEditText.getText().toString());
                    break;
            }
            if (value < min || value > max) {
                showMessage(name, min, max);
                return false;
            }
            selectedExercise.getSpecifications().get(i).setMine(value);
        }
        return true;
    }

    private int getIntValue(String string) {
        try {
            return Integer.valueOf(string);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private void showMessage(String name, int min, int max) {
        Toast.makeText(this, name + " " + getString(R.string.the_number_shall_be_from) + " " + min + " " + getString(R.string.to) + " " + max, Toast.LENGTH_SHORT).show();
    }

    private void showMessage(int messageId) {
        Toast.makeText(this, getString(messageId), Toast.LENGTH_SHORT).show();
    }

    private void saveExercise() throws SQLiteConstraintException {
        viewModel.saveExercise(selectedExercise);
    }

    private void loadExercisesData() {
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
                        initExerciseAdapter(exerciseCategories.get(categoryId - 1).getExercises());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        showMessage(R.string.internet_connectivity);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void initExerciseAdapter(List<Exercise> exercises) {
        adapter = new AddExerciseAdapter(this, exercises, this, allInsertedExercises);
        GridLayoutManager linearLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onExerciseLoaded(LiveData<List<Exercise>> exercises) {
        exercises.observe(this, new androidx.lifecycle.Observer<List<Exercise>>() {
            @Override
            public void onChanged(List<Exercise> exercises) {
                setExercises(exercises);
            }
        });
    }

    private void setExercises(List<Exercise> exercises) {
        this.allInsertedExercises = exercises;
        loadExercisesData();
    }

    @Override
    public void onExerciseLoaded(List<Exercise> exercises) {
        //ignore
    }


    @Override
    public void onExerciseSaved() {
        Intent intent = new Intent();
        intent.putExtra(EXERCISE_KEY, selectedExercise);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onExerciseDeleted() {
        //ignore
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setMessage(R.string.discard_changes)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        callOnBackPressed();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
    }

    private void callOnBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onExerciseClick(Exercise exercise) {
        this.selectedExercise = exercise;
        this.specsLinearLayout.setVisibility(View.VISIBLE);
        for (int i = 0; i < exercise.getSpecifications().size(); i++) {

            String name = exercise.getSpecifications().get(i).getName();
            String helper = exercise.getSpecifications().get(i).getMin() + " - " + exercise.getSpecifications().get(i).getMax();

            switch (i) {
                case 0:
                    this.firstFactorTextInputEditText.setVisibility(View.VISIBLE);
                    this.firstFactorTextInputLayout.setVisibility(View.VISIBLE);

                    this.firstFactorTextInputLayout.setHint(name);
                    this.firstFactorTextInputLayout.setHelperText(helper);
                    break;
                case 1:
                    this.secondFactorTextInputEditText.setVisibility(View.VISIBLE);
                    this.secondFactorTextInputLayout.setVisibility(View.VISIBLE);

                    this.secondFactorTextInputLayout.setHint(name);
                    this.secondFactorTextInputLayout.setHelperText(helper);

                    break;
                case 2:
                    this.thirdFactorTextInputEditText.setVisibility(View.VISIBLE);
                    this.thirdFactorTextInputLayout.setVisibility(View.VISIBLE);

                    this.thirdFactorTextInputLayout.setHint(name);
                    this.thirdFactorTextInputLayout.setHelperText(helper);

                    break;
            }
        }

    }

    @Override
    public void onExerciseDoneClick(Exercise exercise, Button button, ImageView imageView) {
        //ignore
    }
}
