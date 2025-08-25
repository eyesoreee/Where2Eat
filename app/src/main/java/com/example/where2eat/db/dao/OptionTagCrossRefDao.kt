package com.example.where2eat.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.where2eat.db.entity.OptionTagCrossRef

@Dao
interface OptionTagCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOptionTagCrossRef(crossRef: OptionTagCrossRef)

    @Delete
    suspend fun deleteOptionTagCrossRef(crossRef: OptionTagCrossRef)

    @Query("DELETE FROM option_tag_cross_ref WHERE option_id = :optionId")
    suspend fun deleteOptionTagCrossRefsByOptionId(optionId: Long)
}
