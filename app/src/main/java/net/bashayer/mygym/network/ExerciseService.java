package net.bashayer.mygym.network;

import net.bashayer.mygym.network.model.ExerciseCategory;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface ExerciseService {

    @GET("v2/5d1e362a30000058b6d72565/")
    Observable<List<ExerciseCategory>> getAllExercisesCategory();
}
