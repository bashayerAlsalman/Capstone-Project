package net.bashayer.mygym.exercisecategory;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import net.bashayer.mygym.R;
import net.bashayer.mygym.common.ExercisesTypes;
import net.bashayer.mygym.network.model.ExerciseCategory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExerciseCategoryAdapter extends RecyclerView.Adapter<ExerciseCategoryAdapter.ExerciseCategoryViewHolder> {

    private Context context;
    private List<ExerciseCategory> exercises;
    private Resources resources;

    private ExerciseCategoryCallback exerciseCategoryCallback;

    public ExerciseCategoryAdapter(Context context, List<ExerciseCategory> exercise, ExerciseCategoryCallback exerciseCategoryCallback) {
        this.context = context;
        this.exercises = exercise;
        this.exerciseCategoryCallback = exerciseCategoryCallback;
    }

    @NonNull
    @Override
    public ExerciseCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ExerciseCategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.item_exercise_category, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseCategoryViewHolder holder, int position) {
        holder.bind(exercises.get(position));
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public class ExerciseCategoryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.root)
        CardView root;
        @BindView(R.id.category)
        TextView categoryTextView;
        @BindView(R.id.icon)
        ImageView iconImageView;
        @BindView(R.id.category_description)
        TextView descriptionTextView;

        public ExerciseCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(ExerciseCategory exerciseCategory) {
            this.categoryTextView.setText(exerciseCategory.getName());
            this.iconImageView.setImageDrawable(getIcon(exerciseCategory.getId()));
            this.descriptionTextView.setText(exerciseCategory.getDescription());
            this.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    exerciseCategoryCallback.onExerciseCategoryClick(exerciseCategory);
                }
            });
        }

        private Drawable getIcon(int exerciseId) {//todo
            if (exerciseId == ExercisesTypes.CARDIO.id) {
                int i = R.drawable.ic_favorite_gray_16dp;
            }
            return null;
        }

    }
}
