package com.example.mdp_project

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = [UserEntity::class], version = 1)
abstract class AppDatabase:RoomDatabase() {
    abstract fun userDao(): UserDao
}