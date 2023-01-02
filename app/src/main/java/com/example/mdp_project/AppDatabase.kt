package com.example.mdp_project

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = [
    UserEntity::class,
    ClassEntity::class,
    ModuleEntity::class,
    PilihanEntity::class,
    QuizEntity::class,
], version = 1)
abstract class AppDatabase:RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun classDao(): ClassDao
    abstract fun moduleDao(): ModuleDao
    abstract fun pilihanDao(): PilihanDao
    abstract fun quizDao(): QuizDao

    companion object {
        private var _database: AppDatabase? = null

        fun build(context:Context?): AppDatabase {
            if(_database == null){
                //
                _database = Room.databaseBuilder(context!!,AppDatabase::class.java,"db").build()
            }
            return _database!!
        }
    }
}