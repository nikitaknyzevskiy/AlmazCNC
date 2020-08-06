package com.rokobit.almaz.local_data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveImage(image: ImageEntity)

    @Query("SELECT * FROM imageentity")
    fun getAllImages(): Flow<List<ImageEntity>>

    @Query("SELECT * FROM imageentity WHERE imageId LIKE :imageId")
    fun getImageWithId(imageId: Long): Flow<List<ImageEntity>>

    @Query("DELETE FROM imageentity WHERE imageId LIKE :imageId")
    fun deleteImageWithId(imageId: Long)

}