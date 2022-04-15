package com.test.demo.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.test.demo.data.db.product.ProductDao
import com.test.demo.data.db.product.ProductEntity

@Database(entities = [ProductEntity::class], version = 1)
abstract class AppDb: RoomDatabase() {

    abstract fun productDao(): ProductDao

    companion object {
        const val DB_NAME = "app-db"
        fun provideDb(context: Context): AppDb {
            return Room.databaseBuilder(context, AppDb::class.java, DB_NAME)
                .build()
        }
    }
}