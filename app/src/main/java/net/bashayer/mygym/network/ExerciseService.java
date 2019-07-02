package net.bashayer.mygym.network;

import net.bashayer.mygym.network.model.ExerciseCategory;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface ExerciseService {

    @GET("v2/5d1936fb30000061568bebfe/")
    Observable<List<ExerciseCategory>> getAllExercisesCategory();
}
