package com.rokobit.almaz.local_data

import kotlinx.coroutines.flow.Flow

class ImageRepository(val imageDao: ImageDao) {

    suspend fun saveImage(image: ImageEntity) = imageDao.saveImage(image)

    fun getAllImages(): Flow<List<ImageEntity>> = imageDao.getAllImages()

    fun getImageWithId(imageId: Long): Flow<List<ImageEntity>> = imageDao.getImageWithId(imageId)

    fun deleteImageWithId(imageId: Long) = imageDao.deleteImageWithId(imageId)

}