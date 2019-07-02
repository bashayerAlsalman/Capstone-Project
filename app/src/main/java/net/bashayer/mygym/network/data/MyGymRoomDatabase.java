package net.bashayer.mygym.network.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import net.bashayer.mygym.common.Constants;
import net.bashayer.mygym.network.ExerciseDao;
import net.bashayer.mygym.network.model.Exercise;
import net.bashayer.mygym.network.model.ExerciseCategory;
import net.bashayer.mygym.network.model.Specification;

@Database(entities = {Exercise.class, ExerciseCategory.class, Specification.class}, version = 1)
public abstract class MyGymRoomDatabase extends RoomDatabase {

    public abstract ExerciseDao exerciseDao();

    private static MyGymRoomDatabase gymRoomDatabase;

    public static MyGymRoomDatabase getDatabase(Context context) {
        if (gymRoomDatabase == null) {
            gymRoomDatabase = Room.databaseBuilder(context, MyGymRoomDatabase.class, Constants.MY_GYM_DATABASE_NAME)
                    .fallbackToDestructiveMigration().build();
        }
        return gymRoomDatabase;
    }

}
