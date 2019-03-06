package com.blahblah.livedataplayground.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Description: One movie entry, and the class that is propagated to describe a movie
 * Created by shmuel on 27.2.19.
 */
@Entity(tableName = "MovieEntry")
class OneMovieEntity {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var movieName: String = ""
    var posterUri: String = ""
    var backdropUri: String = ""
    var synopsis: String = ""
    var popularity: Double = 0.0
    var voteAverage: Double = 0.0
    var cameFromPage: Int = 0
}