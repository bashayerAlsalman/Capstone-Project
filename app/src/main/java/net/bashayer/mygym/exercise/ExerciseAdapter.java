package net.bashayer.mygym.exercise;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import net.bashayer.mygym.R;
import net.bashayer.mygym.network.model.Exercise;
import net.bashayer.mygym.network.model.Specification;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

    private Context context;
    private List<Exercise> exercises;
    private Resources resources;
    private ExerciseCallback exerciseCallback;

    public ExerciseAdapter(Context context, List<Exercise> exercise, ExerciseCallback exerciseCallback) {
        this.context = context;
        this.exercises = exercise;
        this.exerciseCallback = exerciseCallback;
    }

    public void addExercise(Exercise exercise) {
        exercises.add(exercise);
        notifyItemChanged(exercises.size() - 1);
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ExerciseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        holder.bind(exercises.get(position));
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public void deleteExercise(Exercise exercise) {
        exercises.remove(exercise);
        notifyDataSetChanged();
    }

    public void updateExercise(Exercise exercise) {
        exercises.remove(exercise);
        exercises.add(0, exercise);
        notifyItemChanged(0);
    }

    public void moveToEnd(Exercise exercise) {
        int itemIndex = exercises.indexOf(exercise);
        notifyItemMoved(itemIndex, exercises.size() - 1);
    }

    public class ExerciseViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.root)
        ConstraintLayout root;
        @BindView(R.id.exercise_name)
        TextView exerciseNameTextView;
        @BindView(R.id.exercise_image)
        ImageView exerciseImageView;
        @BindView(R.id.exercise_done_image)
        ImageView doneIconImageView;
        @BindView(R.id.done_button)
        Button doneButton;
        @BindView(R.id.exercise_number)
        TextView exerciseNumber;

        @BindView(R.id.first_factor)
        TextView firstFactorTextView;
        @BindView(R.id.second_factor)
        TextView secondFactorTextView;
        @BindView(R.id.third_factor)
        TextView thirdFactorTextView;

        @BindView(R.id.first_factor_value)
        TextView firstFactorValueTextView;
        @BindView(R.id.second_factor_value)
        TextView secondFactorValueTextView;
        @BindView(R.id.third_factor_value)
        TextView thirdFactorValueTextView;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Exercise exercise) {
            this.exerciseNameTextView.setText(exercise.getExerciseName());
            this.exerciseNumber.setText(exercise.getExerciseId() + "");
            Glide.with(context).load(exercise.getExerciseImageUrl()).into(exerciseImageView);
            bindSpecification(exercise.getSpecifications());
            if (exercise.isDone()) {
                doneIconImageView.setVisibility(View.VISIBLE);
                doneButton.setText(R.string.undone);
            }

            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    exerciseCallback.onExerciseClick(exercise);
                }
            });
            doneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    exerciseCallback.onExerciseDoneClick(exercise, doneButton, doneIconImageView);
                }
            });

        }

        private void bindSpecification(List<Specification> specifications) {
            if (specifications.size() > 0 && specifications.get(0) != null) {
                this.firstFactorTextView.setText(specifications.get(0).getName());
                this.firstFactorValueTextView.setText(specifications.get(0).getMine() + "");
            }

            if (specifications.size() > 1 && specifications.get(1) != null) {
                this.secondFactorTextView.setText(specifications.get(1).getName());
                this.secondFactorValueTextView.setText(specifications.get(1).getMine() + "");
            }

            if (specifications.size() > 2 && specifications.get(2) != null) {
                this.thirdFactorTextView.setText(specifications.get(2).getName());
                this.thirdFactorValueTextView.setText(specifications.get(2).getMine() + "");
            }

        }
    }
}
