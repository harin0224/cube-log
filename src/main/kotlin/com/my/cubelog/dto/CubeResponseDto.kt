package com.my.cubelog.dto

data class CubeResponseDto(
    val data: String = " "
)

typealias CubePersonalAverageDto = ArrayList<MutableMap<CubeType, MutableMap<Grade, Double>>>

data class OpenAPIRequestDto(
    val count: Int = 1000,
    var date: String? = "2022-11-25",
    var cursor: String? = null
)

data class CubeFromNexonResponseDto(
    var count: Int,
    var cube_histories: Array<CubeHistoriesDto>,
    var next_cursor: String
)

data class CubeHistoriesDto(
    val id: String,
    val character_name: String,
    val cube_type: String,
    val create_date: String,
    val item_upgrade_result: String,
    val miracle_time_flag: String,
    val item_equip_part: String,
    val item_level: Int,
    val target_item: String,
    val potential_option_grade: String,
    val additional_potential_option_grade: String,
    val upgradeguarantee: Boolean,
    val upgradeguaranteecount: Int,
    val before_potential_options: Array<BeforePotentialOptionDto>,
    val before_additional_potential_options: Array<BeforeAdditionalPotentialOptionDto>,
    val after_potential_options: Array<AfterPotentialOptionDto>,
    val after_additional_potential_options: Array<AfterAdditionalPotentialOptionDto>
)

data class BeforePotentialOptionDto(
    var value: String,
    var grade: String
)

data class BeforeAdditionalPotentialOptionDto(
    var value: String,
    var grade: String
)

data class AfterPotentialOptionDto(
    var value: String,
    var grade: String
)

data class AfterAdditionalPotentialOptionDto(
    var value: String,
    var grade: String
)

data class CubeEvent(
    var miracle: Map<CubeType, List<CubeRollHistories>> = mutableMapOf(),
    var common: Map<CubeType, List<CubeRollHistories>> = mutableMapOf()
)

enum class CubeType(val value: String) {
    OCCULT_CUBE("수상한 큐브"),
    MASTER_CRAFTS_MANS_CUBE("명장의 큐브"),
    MEISTER_CUBE("장인의 큐브"),
    EVENT_RING_CUBE("이벤트 링 전용 명장의 큐브"),
    RED_CUBE("레드 큐브"),
    BLACK_CUBE("블랙 큐브"),
    BONUS_OCCULT_CUBE("수상한 에디셔널 큐브"),
    BONUS_POTENTIAL_CUBE("에디셔널 큐브"),
    WHITE_CUBE("화이트 에디셔널 큐브");
}

data class CubeRollHistories(
    var grade: Grade?,
    var count: Int = 1,
    var isUpgrade: Boolean = false,
)

enum class Grade(val value: String) {
    RARE_TO_EPIC("레어"),
    EPIC_TO_UNIQUE("에픽"),
    UNIQUE_TO_LEGENDARY("유니크");

    companion object {
        infix fun from(value: String): Grade? = Grade.values().firstOrNull { it.value == value }
    }

}