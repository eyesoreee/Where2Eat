package com.example.where2eat.db

import androidx.room.TypeConverter
import com.example.where2eat.db.entity.PriceRange


object Converters {
    @TypeConverter
    fun fromPriceRange(priceRange: PriceRange?): String? {
        return priceRange?.symbol
    }

    @TypeConverter
    fun toPriceRange(priceRangeSymbol: String?): PriceRange? {
        return PriceRange.entries.find { it.symbol == priceRangeSymbol }
    }
}