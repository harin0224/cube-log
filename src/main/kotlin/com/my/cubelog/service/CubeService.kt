package com.my.cubelog.service

import com.my.cubelog.dto.*
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient;
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

@Service
class CubeService(private val webBuilder: WebClient.Builder, val httpService: HttpService) {
    fun getCube(key: String): CubeEvent {
        // 가져올 날짜 설정
        val calendar: Calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val date = calendar.get(Calendar.DAY_OF_MONTH)
        val params = OpenAPIRequestDto()
        var saveResponse =
            CubeFromNexonResponseDto(count = 1000, cube_histories = arrayOf<CubeHistoriesDto>(), next_cursor = "")
        //var saveResponse: CubeFromNexonResponseDto? = null
        LocalDate.of(2023, 9, 15)//20221125
            .datesUntil(LocalDate.of(year, month, date - 1).plusDays(1))
            .forEach { it ->
                params.date = it.toString()
                var response = httpService.getCubeFromNexon(params, key)
                var cursor = response?.next_cursor
                //println("날짜:" + params.date)
                // 데이터 병합
                while (!cursor.isNullOrEmpty()) {
                    //println("와일도는중" + cursor)
                    var nextParams = OpenAPIRequestDto()
                    nextParams.date = ""
                    nextParams.cursor = cursor
                    var nextResponse = httpService.getCubeFromNexon(nextParams, key)
                    if (nextResponse != null) {
                        saveResponse.count += nextResponse.count
                        saveResponse.cube_histories = saveResponse.cube_histories.plus(nextResponse.cube_histories)
                    }
                    cursor = nextResponse?.next_cursor
                }
                if (response != null) {
                    saveResponse.count += response.count    // 모든 사용한 큐브 개수
                    saveResponse.cube_histories = saveResponse.cube_histories.plus(response.cube_histories)
                }
            }   // 데이터 모두 병합 완료
        var resultData = CubeEvent()


        for (idx in saveResponse.cube_histories) {

            val cubeType = mapStringToCubeType(idx.cube_type)
            var cubeGrade: String =
                if (potentialGroup(cubeType) == "potentialOption") idx.potential_option_grade
                else idx.additional_potential_option_grade
            if (!upgradeGroup(cubeType, cubeGrade)) {
                continue
            }
//            if (cubeType == CubeType.MASTER_CRAFTS_MANS_CUBE || cubeType == CubeType.EVENT_RING_CUBE) {
//                println(idx)
//            }

            //var lastHistory: CubeRollHistories? = null
            var lastIndex: Int = 0
            //var eventType:String = ""
            val eventType = if (idx.miracle_time_flag == "이벤트 적용되지 않음") "common"
            else "miracle" //미라클인지 아닌지
            //println("이벤트 타입:$eventType")
            var cubeData = mutableMapOf<CubeType, ArrayList<CubeRollHistories>>()
            if (eventType == "miracle") {
                cubeData = resultData.miracle as MutableMap<CubeType, ArrayList<CubeRollHistories>>
            } else if (eventType == "common") {
                cubeData = resultData.common as MutableMap<CubeType, ArrayList<CubeRollHistories>>
            }

            //lastIndex = (cubeData[cubeType]?.filter{it.grade == Grade from idx.potential_option_grade}?.size ?: 0) - 1    //마지막 값
            if (cubeData[cubeType] != null) {
                var filtered = cubeData[cubeType]?.filter { it.grade == Grade from cubeGrade }
                if (!filtered.isNullOrEmpty()) {
                    //println("필터:$filtered")
                    lastIndex = cubeData[cubeType]?.lastIndexOf(filtered.last()) ?: -1
                    if (lastIndex >= 0) { //이전에 데이터가 있을 시
                        if (idx.item_upgrade_result == "성공") {    //현 데이터가 등업된 데이터일 시
                            cubeData[cubeType]?.get(lastIndex)?.isUpgrade = true
                            cubeData[cubeType]?.add(CubeRollHistories(count = 1, grade = Grade from cubeGrade))
                        } else {   //등업 실패 시
                            cubeData[cubeType]?.get(lastIndex)?.count = cubeData[cubeType]?.get(lastIndex)?.count!! + 1
                            //println("카운트 추가")
                        }
                    } else {  //이전에 데이터가 없을 시
                        cubeData[cubeType]?.add(CubeRollHistories(count = 1, grade = Grade from cubeGrade))
                    }
                } else {
                    cubeData[cubeType]?.add(CubeRollHistories(count = 1, grade = Grade from cubeGrade))
                    //println("필터null:$cubeData")
                }

                if (eventType == "miracle") {
                    resultData.miracle = cubeData
                } else if (eventType == "common") {
                    resultData.common = cubeData
                }
            } else {
                cubeData[cubeType] = mutableListOf(
                    CubeRollHistories(
                        count = 1,
                        grade = Grade.from(cubeGrade)
                    )
                ) as ArrayList<CubeRollHistories>
            }
        }
        return resultData
    }

    fun mapStringToCubeType(value: String): CubeType {
        return when (value) {
            "수상한 큐브" -> CubeType.OCCULT_CUBE
            "명장의 큐브", "카르마 명장의 큐브" -> CubeType.MASTER_CRAFTS_MANS_CUBE
            "장인의 큐브", "카르마 장인의 큐브" -> CubeType.MEISTER_CUBE
            "이벤트 링 전용 명장의 큐브" -> CubeType.EVENT_RING_CUBE
            "레드 큐브" -> CubeType.RED_CUBE
            "블랙 큐브" -> CubeType.BLACK_CUBE
            "수상한 에디셔널 큐브" -> CubeType.BONUS_OCCULT_CUBE
            "에디셔널 큐브" -> CubeType.BONUS_POTENTIAL_CUBE
            //"화이트 에디셔널 큐브" -> CubeType.WHITE_CUBE
            else -> CubeType.WHITE_CUBE
        }
    }

    fun getCount(useCube: List<CubeHistoriesDto>): Array<Int> {  // 큐브별 성공 개수 출력 함수
        var count: Int = 0
        var successCount: Array<Int> = arrayOf()
        for (idx in useCube) {
            if (idx.item_upgrade_result == "실패") {
                count++
            } else if (idx.item_upgrade_result == "성공") {
                successCount += count
                count = 0
            }
        }
        return successCount
    }

    fun potentialGroup(useCube: CubeType): String {
        return when (useCube) {
            CubeType.OCCULT_CUBE, CubeType.MASTER_CRAFTS_MANS_CUBE, CubeType.MEISTER_CUBE, CubeType.EVENT_RING_CUBE, CubeType.RED_CUBE, CubeType.BLACK_CUBE -> "potentialOption"
            else -> "additionalPotentialOption"
            //CubeType.BONUS_POTENTIAL_CUBE
            //CubeType.WHITE_CUBE
        }
    }

    fun upgradeGroup(useCube: CubeType, useGrade: String): Boolean {
        return if (useGrade == "에픽" && (useCube == CubeType.OCCULT_CUBE || useCube == CubeType.BONUS_OCCULT_CUBE)) {
            false
        } else if (useGrade == "유니크" && (useCube == CubeType.MEISTER_CUBE)) {
            false
        } else useGrade != "레전드리"
    }


    fun getCubePersonalAverage(key: String): CubeResponseDto {
        var a = CubeResponseDto()
        return a
    }

    fun getCubeAllAverage(key: String): CubeResponseDto {
        var a = CubeResponseDto()
        return a
    }

    fun getCubeSimulatorAverage(key: String): CubeResponseDto {
        var a = CubeResponseDto()
        return a
    }

}