package com.jgerontis.android.foodie

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID
import java.util.Date

@Entity
data class Recipe(@PrimaryKey val id: UUID = UUID.randomUUID(),
                  var name: String = "",
                  var directions: String = "",
                  var date: Date = Date(),
                  var category: Int = 0,
                  var isFavorite: Boolean = false)
