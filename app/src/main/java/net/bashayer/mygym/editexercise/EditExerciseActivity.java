package net.bashayer.mygym.editexercise;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import net.bashayer.mygym.MyGymApplication;
import net.bashayer.mygym.R;
import net.bashayer.mygym.common.Constants;
import net.bashayer.mygym.network.data.ExerciseWorker;
import net.bashayer.mygym.network.data.LoadExerciseDataCallback;
import net.bashayer.mygym.network.model.Exercise;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static net.bashayer.mygym.common.Constants.EXERCISE_KEY;
import static net.bashayer.mygym.common.Constants.REQUEST_CODE_EDITED;

public class EditExerciseActivity extends MyGymApplication implements LoadExerciseDataCallback {

    private ExerciseWorker worker;
    private Exercise exercise;


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


    @BindView(R.id.button)
    AppCompatButton button;
    @BindView(R.id.delete_button)
    AppCompatButton deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_exercise);

        ButterKnife.bind(this);
        exercise = getIntent().getParcelableExtra(Constants.EXERCISE_KEY);
        worker = new ExerciseWorker(this, this);

        getSupportActionBar().setTitle(exercise.getExerciseName());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (formIsValid()) {
                    saveExercise();
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                worker.deleteExercise(exercise);
            }
        });

        loadSpecs();
    }


    private boolean formIsValid() {
        if (exercise == null) {
            return false;
        }
        for (int i = 0; i < exercise.getSpecifications().size(); i++) {
            int value = 0;

            String name = exercise.getSpecifications().get(i).getName();
            int max = exercise.getSpecifications().get(i).getMax();
            int min = exercise.getSpecifications().get(i).getMin();

            switch (i) {
                case 0:
                    value = getIntValue(this.firstFactorTextInputEditText.getText().toString());

                    if (this.firstFactorTextInputEditText.getText().toString() == null ||
                            this.firstFactorTextInputEditText.getText().toString() == "" || value < min || value > max) {
                        showMessage(name, min, max);
                        return false;
                    }
                case 1:

                    value = getIntValue(this.secondFactorTextInputEditText.getText().toString());

                    if (this.secondFactorTextInputEditText.getText().toString() == null ||
                            this.secondFactorTextInputEditText.getText().toString() == "" || value < min || value > max) {
                        showMessage(name, min, max);
                        return false;
                    }
                case 2:

                    value = getIntValue(this.thirdFactorTextInputEditText.getText().toString());

                    if (this.thirdFactorTextInputEditText.getText().toString() == null ||
                            this.thirdFactorTextInputEditText.getText().toString() == "" || value < min || value > max) {
                        showMessage(name, min, max);
                        return false;
                    }
            }
            exercise.getSpecifications().get(i).setMine(value);
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

    private void saveExercise() {
        worker.updateExercise(exercise);
    }

    private void loadSpecs() {
        for (int i = 0; i < exercise.getSpecifications().size(); i++) {

            String name = exercise.getSpecifications().get(i).getName();
            String helper = exercise.getSpecifications().get(i).getMin() + " - " + exercise.getSpecifications().get(i).getMax();
            String value = exercise.getSpecifications().get(i).getMine() + "";
            switch (i) {
                case 0:
                    this.firstFactorTextInputEditText.setVisibility(View.VISIBLE);
                    this.firstFactorTextInputLayout.setVisibility(View.VISIBLE);

                    this.firstFactorTextInputLayout.setHint(name);
                    this.firstFactorTextInputLayout.setHelperText(helper);
                    this.firstFactorTextInputEditText.setText(value);
                    break;
                case 1:
                    this.secondFactorTextInputEditText.setVisibility(View.VISIBLE);
                    this.secondFactorTextInputLayout.setVisibility(View.VISIBLE);

                    this.secondFactorTextInputLayout.setHint(name);
                    this.secondFactorTextInputLayout.setHelperText(helper);
                    this.secondFactorTextInputEditText.setText(value);

                    break;
                case 2:
                    this.thirdFactorTextInputEditText.setVisibility(View.VISIBLE);
                    this.thirdFactorTextInputLayout.setVisibility(View.VISIBLE);

                    this.thirdFactorTextInputLayout.setHint(name);
                    this.thirdFactorTextInputLayout.setHelperText(helper);
                    this.thirdFactorTextInputEditText.setText(value);

                    break;
            }
        }
    }

    @Override
    public void onExerciseLoaded(List<Exercise> exercises) {
        //ignore
    }

    @Override
    public void onExerciseSaved() {
        Intent intent = new Intent();
        intent.putExtra(EXERCISE_KEY, exercise);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onExerciseDeleted() {
        Intent intent = new Intent();
        intent.putExtra(EXERCISE_KEY, exercise);
        setResult(REQUEST_CODE_EDITED, intent);
        finish();
    }
}
