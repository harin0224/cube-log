package com.my.cubelog.dto

data class CubeResponseDto (
    val data: String = " "
)

data class OpenAPIRequestDto(
    val count: Int = 1000,
    val date: String = "2023-10-15",
    val cursor: String? = null
)

data class CubeFromNexonResponseDto(
    val count: Int,
    val cube_histories: CubeHistoriesDto
)

data class CubeHistoriesDto(
    val id: String,
    val characte_name: String,
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
    //val beforePotentialOptions: BeforePotentialOptionDto
)

//data class BeforePotentialOptionDto( )
