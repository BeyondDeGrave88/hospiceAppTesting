package ru.iteco.fmhandroid.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.iteco.fmhandroid.dao.*
import ru.iteco.fmhandroid.entity.NewsCategoryEntity
import ru.iteco.fmhandroid.entity.NewsEntity

@Database(
    entities = [
        NewsEntity::class,
        NewsCategoryEntity::class,
    ], version = 1, exportSchema = false
)


abstract class AppDb : RoomDatabase() {
    abstract fun getNewsDao(): NewsDao
    abstract fun getNewsCategoryDao(): NewsCategoryDao
}
