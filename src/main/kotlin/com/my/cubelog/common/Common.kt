package com.my.cubelog.common

import com.my.cubelog.dto.CubeType
import com.my.cubelog.dto.Grade

class Common {
    companion object {
        private val probabilities = mapOf(
            CubeType.OCCULT_CUBE to mapOf
                (Grade.RARE_TO_EPIC to 0.9901),
            CubeType.MASTER_CRAFTS_MANS_CUBE to mapOf
                (Grade.RARE_TO_EPIC to 7.9994, Grade.EPIC_TO_UNIQUE to 1.6959, Grade.UNIQUE_TO_LEGENDARY to 0.1996),
            CubeType.MEISTER_CUBE to mapOf
                (Grade.RARE_TO_EPIC to 4.7619, Grade.EPIC_TO_UNIQUE to 1.1858),
            CubeType.EVENT_RING_CUBE to mapOf
                (Grade.RARE_TO_EPIC to 7.9994, Grade.EPIC_TO_UNIQUE to 1.6959, Grade.UNIQUE_TO_LEGENDARY to 0.1996),
            CubeType.RED_CUBE to mapOf
                (Grade.RARE_TO_EPIC to 6.0000, Grade.EPIC_TO_UNIQUE to 1.8000, Grade.UNIQUE_TO_LEGENDARY to 0.3000),
            CubeType.BLACK_CUBE to mapOf
                (Grade.RARE_TO_EPIC to 15.0000, Grade.EPIC_TO_UNIQUE to 3.5000, Grade.UNIQUE_TO_LEGENDARY to 1.4000),
            CubeType.BONUS_OCCULT_CUBE to mapOf
                (Grade.RARE_TO_EPIC to 0.4000),
            CubeType.BONUS_POTENTIAL_CUBE to mapOf
                (Grade.RARE_TO_EPIC to 4.7619, Grade.EPIC_TO_UNIQUE to 1.9608, Grade.UNIQUE_TO_LEGENDARY to 0.7000),
            CubeType.WHITE_CUBE to mapOf
                (Grade.RARE_TO_EPIC to 4.7619, Grade.EPIC_TO_UNIQUE to 1.9608, Grade.UNIQUE_TO_LEGENDARY to 0.7000),
        )

        fun getProbability(cube: CubeType, grade: Grade?): Double? {
            return probabilities[cube]?.get(grade)
        }
    }
}