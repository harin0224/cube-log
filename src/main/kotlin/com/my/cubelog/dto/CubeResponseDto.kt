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
    val upgrade_guarantee: Boolean,
    val upgrade_guarantee_count: Int,
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

