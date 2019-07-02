package net.bashayer.mygym.network.model.typeconverters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.bashayer.mygym.network.model.Exercise;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class ExerciseTypeConverter {

    static Gson gson = new Gson();

    @TypeConverter
    public static List<Exercise> stringToExerciseList(String exercises) {
        if (exercises == null) {
            return Collections.emptyList();
        } else {
            Type listType = new TypeToken<List<Exercise>>() {
            }.getType();
            return gson.fromJson(exercises, listType);
        }
    }

    @TypeConverter
    public static String exerciseListToString(List<Exercise> exercises) {
        return gson.toJson(exercises);
    }


}
