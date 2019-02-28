package com.blahblah.livedataplayground.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Description:
 * Created by shmuel on 27.2.19.
 */
@Entity(tableName = "Discover")
class OneMovieEntity {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var movieName: String = ""
    var imageUri: String = ""
    var synopsis: String = ""
}