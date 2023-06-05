package com.example.myapplication.roomdatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TrackingInfoDao {
    @Query("SELECT * FROM TrackingInfo")
    fun getAll(): List<TrackingInfo>

    @Insert
    fun insert(trackingInfo: TrackingInfo)

    @Query("UPDATE TrackingInfo SET name = :name WHERE id = :id")
    fun updateName(name: String, id: Int)

    @Query("SELECT * FROM TrackingInfo WHERE id = :id")
    fun getById(id: Int): TrackingInfo?

    @Delete
    fun delete(trackingInfo: TrackingInfo)
}
