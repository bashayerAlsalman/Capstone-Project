package net.bashayer.mygym.network.model.typeconverters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.bashayer.mygym.network.model.ExerciseCategory;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class ExerciseCategoryTypeConverter {

    static Gson gson = new Gson();

    @TypeConverter
    public static List<ExerciseCategory> stringToExerciseCategoryList(String exercises) {
        if (exercises == null) {
            return Collections.emptyList();
        } else {
            Type listType = new TypeToken<List<ExerciseCategory>>() {
            }.getType();
            return gson.fromJson(exercises, listType);
        }
    }

    @TypeConverter
    public static String exerciseCategoryListToString(List<ExerciseCategory> exercises) {
        return gson.toJson(exercises);
    }

}
