package net.bashayer.mygym.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import net.bashayer.mygym.network.model.typeconverters.SpecificationTypeConverter;

import java.util.List;
import java.util.Objects;

@Entity
public class Exercise implements Parcelable {

    @PrimaryKey
    private int exerciseId;
    private String exerciseName;
    private String exerciseImageUrl;
    private String targetBodyPart;
    private int exerciseCategoryId;
    private boolean isDone = false;

    @TypeConverters(value = SpecificationTypeConverter.class)
    private List<Specification> specifications;

    public Exercise() {
    }

    protected Exercise(Parcel in) {
        exerciseId = in.readInt();
        exerciseName = in.readString();
        exerciseImageUrl = in.readString();
        targetBodyPart = in.readString();
        exerciseCategoryId = in.readInt();
        isDone = in.readByte() != 0;
        specifications = in.createTypedArrayList(Specification.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(exerciseId);
        dest.writeString(exerciseName);
        dest.writeString(exerciseImageUrl);
        dest.writeString(targetBodyPart);
        dest.writeInt(exerciseCategoryId);
        dest.writeByte((byte) (isDone ? 1 : 0));
        dest.writeTypedList(specifications);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Exercise> CREATOR = new Creator<Exercise>() {
        @Override
        public Exercise createFromParcel(Parcel in) {
            return new Exercise(in);
        }

        @Override
        public Exercise[] newArray(int size) {
            return new Exercise[size];
        }
    };

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getExerciseImageUrl() {
        return exerciseImageUrl;
    }

    public void setExerciseImageUrl(String exerciseImageUrl) {
        this.exerciseImageUrl = exerciseImageUrl;
    }

    public String getTargetBodyPart() {
        return targetBodyPart;
    }

    public void setTargetBodyPart(String targetBodyPart) {
        this.targetBodyPart = targetBodyPart;
    }

    public int getExerciseCategoryId() {
        return exerciseCategoryId;
    }

    public void setExerciseCategoryId(int exerciseCategoryId) {
        this.exerciseCategoryId = exerciseCategoryId;
    }

    public List<Specification> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(List<Specification> specifications) {
        this.specifications = specifications;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exercise exercise = (Exercise) o;
        return exerciseId == exercise.exerciseId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(exerciseId);
    }
}
