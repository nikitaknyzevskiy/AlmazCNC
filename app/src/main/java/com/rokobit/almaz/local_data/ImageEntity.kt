package com.rokobit.almaz.local_data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class ImageEntity(
    var path: String = "",
    var size: Long = 0L
) {
    @PrimaryKey(autoGenerate = true)
    var imageId: Long = 0L
}