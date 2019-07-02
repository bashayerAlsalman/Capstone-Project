package net.bashayer.mygym.exercise;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.bashayer.mygym.MyGymApplication;
import net.bashayer.mygym.R;
import net.bashayer.mygym.addexercise.AddExerciseActivity;
import net.bashayer.mygym.editexercise.EditExerciseActivity;
import net.bashayer.mygym.network.data.ExerciseWorker;
import net.bashayer.mygym.network.data.LoadExerciseDataCallback;
import net.bashayer.mygym.network.model.Exercise;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static net.bashayer.mygym.common.Constants.EXERCISE_CATEGORY_KEY;
import static net.bashayer.mygym.common.Constants.EXERCISE_CATEGORY_NAME;
import static net.bashayer.mygym.common.Constants.EXERCISE_KEY;
import static net.bashayer.mygym.common.Constants.REQUEST_CODE_ADD;
import static net.bashayer.mygym.common.Constants.REQUEST_CODE_EDITED;

public class ExerciseActivity extends MyGymApplication implements ExerciseCallback, LoadExerciseDataCallback {


    private int exerciseCategory;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.add_exercise_button)
    FloatingActionButton button;


    private ExerciseWorker worker;
    private ExerciseAdapter adapter;
    private String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        ButterKnife.bind(this);
        exerciseCategory = getIntent().getIntExtra(EXERCISE_CATEGORY_KEY, 1);
        worker = new ExerciseWorker(this, this);
        worker.getAllExercisesByCategory(exerciseCategory);

        categoryName = getIntent().getStringExtra(EXERCISE_CATEGORY_NAME);
        getSupportActionBar().setTitle(categoryName);

        initListener();
    }

    private void initListener() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addExercise();
            }
        });
    }

    private void addExercise() {
        Intent intent = new Intent(this, AddExerciseActivity.class);
        intent.putExtra(EXERCISE_CATEGORY_NAME, this.categoryName);
        intent.putExtra(EXERCISE_CATEGORY_KEY, this.exerciseCategory);
        startActivityForResult(intent, REQUEST_CODE_ADD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD && resultCode == RESULT_OK) {
            adapter.addExercise(data.getParcelableExtra(EXERCISE_KEY));
        } else if (requestCode == REQUEST_CODE_EDITED && resultCode == RESULT_OK) {
            adapter.updateExercise(data.getParcelableExtra(EXERCISE_KEY));
        } else if (requestCode == REQUEST_CODE_EDITED && resultCode == REQUEST_CODE_EDITED) {
            adapter.deleteExercise(data.getParcelableExtra(EXERCISE_KEY));
        }
    }

    private void initExerciseAdapter(List<Exercise> exercises) {
        adapter = new ExerciseAdapter(this, exercises, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onExerciseClick(Exercise exercise) {
        Intent intent = new Intent(this, EditExerciseActivity.class);
        intent.putExtra(EXERCISE_KEY, exercise);

        startActivityForResult(intent, REQUEST_CODE_EDITED);
    }

    @Override
    public void onExerciseDoneClick(Exercise exercise, Button button, ImageView imageView) {
        if (!exercise.isDone()) {
            imageView.setVisibility(View.VISIBLE);
            button.setText(R.string.undone);
            adapter.moveToEnd(exercise);
        } else {
            imageView.setVisibility(View.GONE);
            button.setText(R.string.done);
        }
        worker.reverseDoneExercise(exercise);
    }

    @Override
    public void onExerciseLoaded(List<Exercise> exercises) {
        initExerciseAdapter(exercises);
    }

    @Override
    public void onExerciseSaved() {
        //todo
    }

    @Override
    public void onExerciseDeleted() {
        //ignore
    }


    public void showEmptyHolder(boolean showHolder) {
        if (showHolder) {
            //todo show holder
        } else {
            //todo hide holder
        }
    }
}
