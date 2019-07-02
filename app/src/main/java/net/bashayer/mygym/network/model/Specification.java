package net.bashayer.mygym.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;


@Entity
public class Specification implements Parcelable {

    @NotNull
    @PrimaryKey
    private String name;
    private int min;
    private int max;
    private int mine;
    private int exerciseId;

    public Specification() {
    }

    protected Specification(Parcel in) {
        name = in.readString();
        min = in.readInt();
        max = in.readInt();
        mine = in.readInt();
        exerciseId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(min);
        dest.writeInt(max);
        dest.writeInt(mine);
        dest.writeInt(exerciseId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Specification> CREATOR = new Creator<Specification>() {
        @Override
        public Specification createFromParcel(Parcel in) {
            return new Specification(in);
        }

        @Override
        public Specification[] newArray(int size) {
            return new Specification[size];
        }
    };

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMine() {
        return mine;
    }

    public void setMine(int mine) {
        this.mine = mine;
    }
}
