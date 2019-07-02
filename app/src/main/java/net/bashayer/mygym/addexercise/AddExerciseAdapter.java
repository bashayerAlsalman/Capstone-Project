package net.bashayer.mygym.addexercise;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import net.bashayer.mygym.R;
import net.bashayer.mygym.exercise.ExerciseCallback;
import net.bashayer.mygym.network.model.Exercise;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddExerciseAdapter extends RecyclerView.Adapter<AddExerciseAdapter.AddExerciseViewHolder> {

    private Context context;
    private List<Exercise> exercises;
    private ExerciseCallback exerciseCallback;
    private List<Exercise> allInsertedExercises;

    public AddExerciseAdapter(Context context, List<Exercise> exercise, ExerciseCallback exerciseCallback, List<Exercise> allInsertedExercises) {
        this.context = context;
        this.exercises = exercise;
        this.exerciseCallback = exerciseCallback;
        this.allInsertedExercises = allInsertedExercises;
        this.removeInsertedData();
    }

    private void removeInsertedData() {
        exercises.removeAll(allInsertedExercises);
    }

    @NonNull
    @Override
    public AddExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddExerciseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_box_exercise, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddExerciseViewHolder holder, int position) {
        holder.bind(exercises.get(position));
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public class AddExerciseViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.root)
        ConstraintLayout root;
        @BindView(R.id.exercise_name)
        TextView exerciseNameTextView;
        @BindView(R.id.exercise_image)
        ImageView exerciseImageView;
        @BindView(R.id.exercise_number)
        TextView exerciseNumber;


        public AddExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Exercise exercise) {
            this.exerciseNameTextView.setText(exercise.getExerciseName());
            this.exerciseNumber.setText(exercise.getExerciseId() + "");
            Glide.with(context).load(exercise.getExerciseImageUrl()).into(exerciseImageView);
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    exerciseCallback.onExerciseClick(exercise);
                    drawBorderOnSelectedExercise(view);
                }
            });

        }

        private void drawBorderOnSelectedExercise(View view) {
            //todo
            //undraw prevous
        }

    }
}
