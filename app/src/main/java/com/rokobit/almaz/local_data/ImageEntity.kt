package com.rokobit.almaz.local_data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class ImageEntity(
    @PrimaryKey(autoGenerate = true)
    var imageId: Long = 0L,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var image: ByteArray = byteArrayOf(),
    var size: Long = 0L,
    var date: String = ""
)