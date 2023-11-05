package com.my.cubelog.dto

data class CubeResponseDto (
    val data: String = " "
)

data class OpenAPIRequestDto(
    val count: Int = 1000,
    var date: String? = "2023-10-15",
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
    BONUS_POTENTIAL_CUBE("에디셔널 큐브"),
    WHITE_CUBE("화이트 에디셔널 큐브");
}

//data class CubeData(
//    val cubeRollHistories: Array<CubeRollHistories> = arrayOf<CubeRollHistories>()
//)
data class CubeRollHistories(
    var grade: Grade?,
    var count: Int = 1,
    var isUpgrade: Boolean = false,

//    var upgradeResult: String,
//    var potentialOptionGrade: String,       // 현재 등급
//    var upgradeGuarantee: String,
//    var upgradeGuaranteeCount: Int
)

enum class Grade(val value: String){
    RARE_TO_EPIC("노말"),
    EPIC_TO_UNIQUE("에픽"),
    UNIQUE_TO_LEGENDARY("유니크");

    companion object {
        infix fun from(value: String): Grade? = Grade.values().firstOrNull { it.value == value }
    }

}

/*
* {
        "count": 10,
        "cube_histories": [
                {
                        "id": "QzwgKAYn8JG3GOfTs16qFgCW",
                        "character_name": "Ayda",
                        "create_date": "2023-10-15T03:15:07+09:00",
                        "cube_type": "레드 큐브",
                        "item_upgrade_result": "실패",
                        "miracle_time_flag": "이벤트 적용되지 않음",
                        "item_equip_part": "얼굴장식",
                        "item_level": 140,
                        "target_item": "트와일라이트 마크",
                        "potential_option_grade": "레전드리",
                        "additional_potential_option_grade": "노멀",
                        "upgradeguarantee": false,
                        "upgradeguaranteecount": 0,
                        "before_potential_options": [
                                {
                                        "value": "INT : +12%",
                                        "grade": "레전드리"
                                },
                                {
                                        "value": "HP 회복 아이템 및 회복 스킬 효율 : +30%",
                                        "grade": "유니크"
                                },
                                {
                                        "value": "최대 MP : +9%",
                                        "grade": "유니크"
                                }
                        ],
                        "before_additional_potential_options": [],
                        "after_potential_options": [
                                {
                                        "value": "STR : +12%",
                                        "grade": "레전드리"
                                },
                                {
                                        "value": "INT : +12%",
                                        "grade": "레전드리"
                                },
                                {
                                        "value": "INT : +9%",
                                        "grade": "유니크"
                                }
                        ],
                        "after_additional_potential_options": []
                },
                *
                *
                *
                *
* */