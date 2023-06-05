package com.example.myapplication.roomdatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.roomdatabase.TrackingInfo
import com.example.myapplication.roomdatabase.TrackingInfoDao


@Database(entities = [TrackingInfo::class], version = 2, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackingInfoDao(): TrackingInfoDao
}
