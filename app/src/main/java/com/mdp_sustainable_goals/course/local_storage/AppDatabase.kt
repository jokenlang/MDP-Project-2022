package com.mdp_sustainable_goals.course.local_storage

import android.content.Context
import androidx.room.*
import com.mdp_sustainable_goals.course.local_storage.dao.*
import com.mdp_sustainable_goals.course.local_storage.entity.*

@Database(
    entities = [
        UserEntity::class,
        ClassEntity::class,
        ModuleEntity::class,
        PilihanEntity::class,
        QuizEntity::class,
        JoinClassEntity::class,
    ], version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun classDao(): ClassDao
    abstract fun moduleDao(): ModuleDao
    abstract fun pilihanDao(): PilihanDao
    abstract fun quizDao(): QuizDao
    abstract fun joinClassDao(): JoinClassDao

    companion object {
        private var _database: AppDatabase? = null

        fun build(context: Context?): AppDatabase {
            if (_database == null) {
                _database = Room.databaseBuilder(context!!, AppDatabase::class.java, "a").build()
            }
            return _database!!
        }
    }
}
