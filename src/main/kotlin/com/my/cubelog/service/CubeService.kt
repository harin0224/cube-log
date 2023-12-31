package com.my.cubelog.service

import com.my.cubelog.common.Common.Companion.getProbability
import com.my.cubelog.dto.*
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient;
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.pow

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
        LocalDate.of(2022, 11, 25)//20221125
            .datesUntil(LocalDate.of(year, month, date - 1).plusDays(1))
            .forEach { it ->
                params.date = it.toString()
                var response = httpService.getCubeFromNexon(params, key)
                var cursor = response?.next_cursor
                // 데이터 병합
                while (!cursor.isNullOrEmpty()) {
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
            val eventType = if (idx.miracle_time_flag == "이벤트 적용되지 않음") "common"
            else "miracle" //미라클인지 아닌지
            var changeGrade = setUpgradeGrade(Grade from cubeGrade, idx.item_upgrade_result)
            if (!upgradeGroup(cubeType, changeGrade)) {
                continue
            }
            var lastIndex: Int = 0
            var cubeData = mutableMapOf<CubeType, ArrayList<CubeRollHistories>>()
            if (eventType == "miracle") {
                cubeData = resultData.miracle as MutableMap<CubeType, ArrayList<CubeRollHistories>>
            } else if (eventType == "common") {
                cubeData = resultData.common as MutableMap<CubeType, ArrayList<CubeRollHistories>>
            }

            if (cubeData[cubeType] != null) {
                var filtered = cubeData[cubeType]?.filter { it.grade == changeGrade }
                if (!filtered.isNullOrEmpty()) {
                    lastIndex = cubeData[cubeType]?.lastIndexOf(filtered.last()) ?: -1
                    if (lastIndex >= 0) { // 이전에 데이터가 있을 시
                        if (idx.item_upgrade_result == "성공") {    // 현 데이터가 등업된 데이터일 시
                            cubeData[cubeType]?.get(lastIndex)?.isUpgrade = true
                            cubeData[cubeType]?.add(CubeRollHistories(count = 1, grade = changeGrade))
                        } else {   // 등업 실패 시
                            cubeData[cubeType]?.get(lastIndex)?.count = cubeData[cubeType]?.get(lastIndex)?.count!! + 1
                            cubeData[cubeType]?.get(lastIndex)?.count = cubeData[cubeType]?.get(lastIndex)?.count!! + 1
                        }
                    } else {  // 이전에 데이터가 없을 시
                        cubeData[cubeType]?.add(CubeRollHistories(count = 1, grade = changeGrade))
                    }
                } else {
                    cubeData[cubeType]?.add(CubeRollHistories(count = 1, grade = changeGrade))
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
                        grade = changeGrade
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

    fun potentialGroup(useCube: CubeType): String {
        return when (useCube) {
            CubeType.OCCULT_CUBE, CubeType.MASTER_CRAFTS_MANS_CUBE, CubeType.MEISTER_CUBE, CubeType.EVENT_RING_CUBE, CubeType.RED_CUBE, CubeType.BLACK_CUBE -> "potentialOption"
            else -> "additionalPotentialOption"
            //CubeType.BONUS_POTENTIAL_CUBE
            //CubeType.WHITE_CUBE
        }
    }

    fun upgradeGroup(useCube: CubeType, useGrade: Grade?): Boolean {
        return if (useGrade == Grade.EPIC_TO_UNIQUE && (useCube == CubeType.OCCULT_CUBE || useCube == CubeType.BONUS_OCCULT_CUBE)) {
            false
        } else if (useGrade == Grade.UNIQUE_TO_LEGENDARY && (useCube == CubeType.MEISTER_CUBE)) {
            false
        } else useGrade != null
    }

    fun setUpgradeGrade(grade: Grade?, upgrade: String): Grade? {
        return if (upgrade == "성공") {
            when (grade) {
                Grade.UNIQUE_TO_LEGENDARY -> Grade.EPIC_TO_UNIQUE
                Grade.EPIC_TO_UNIQUE -> Grade.RARE_TO_EPIC
                else -> Grade.UNIQUE_TO_LEGENDARY
            }
        } else {
            grade
        }
    }

    fun getCubePersonalAverage(key: String): CubePersonalAverageDto {
        var resultData = getCube(key)
        var miracleData: MutableMap<CubeType, MutableMap<Grade, ArrayList<Int>>> = mutableMapOf()
        var commonData: MutableMap<CubeType, MutableMap<Grade, ArrayList<Int>>> = mutableMapOf()

        var successRate: Double? = 0.0    //성공확률
        var trials = 0
        var targetSuccess = 0
        var average: ArrayList<MutableMap<CubeType, MutableMap<Grade, Double>>> = arrayListOf(
            mutableMapOf(),
            mutableMapOf()
        )
        resultData.miracle.forEach { (key, value) ->
            for (idx in value) {
                miracleData[key] = (miracleData[key] ?: mutableMapOf())
                miracleData[key]!![idx.grade!!] = (miracleData[key]!![idx.grade] ?: arrayListOf(0, 0))
                miracleData[key]!![idx.grade!!]!![0] += idx.count
                if (idx.isUpgrade) {
                    miracleData[key]!![idx.grade!!]!![1]++
                }
            }
        }
        miracleData.forEach { (key, value) ->
            value.forEach { (gradeKey, info) ->
                successRate = getProbability(key, gradeKey) // 성공 확률
                trials = info[0]
                targetSuccess = info[1]
                average[0][key] = (average[0][key] ?: mutableMapOf())
                average[0][key]!![gradeKey] = calculateSuccessProbability(successRate!!, trials, targetSuccess)
            }
        }

        resultData.common.forEach { (key, value) ->
            for (idx in value) {
                commonData[key] = (commonData[key] ?: mutableMapOf())
                commonData[key]!![idx.grade!!] = (commonData[key]!![idx.grade] ?: arrayListOf(0, 0))
                commonData[key]!![idx.grade!!]!![0] += idx.count
                if (idx.isUpgrade) {
                    commonData[key]!![idx.grade!!]!![1]++
                }
            }
        }
        commonData.forEach { (key, value) ->
            value.forEach { (gradeKey, info) ->
                successRate = getProbability(key, gradeKey) // 성공 확률
                trials = info[0]
                targetSuccess = info[1]
                average[0] = (average[0] ?: mutableMapOf())
                average[1][key] = (average[1][key] ?: mutableMapOf())
                average[1][key]!![gradeKey] = calculateSuccessProbability(successRate!!, trials, targetSuccess)
            }
        }
        return average
    }

    fun calculateSuccessProbability(successRate: Double, trials: Int, targetSuccess: Int): Double {
        val failureRate = 100 - successRate
        val binomialCoefficient = calculateBinomialCoefficient(trials, targetSuccess)
        val successPower = (successRate / 100).pow(targetSuccess.toDouble())
        val failurePower = (failureRate / 100).pow((trials - targetSuccess).toDouble())
        return binomialCoefficient * successPower * failurePower * 100
    }

    fun calculateBinomialCoefficient(n: Int, k: Int): Long {
        if (k > n) return 0
        if (k == 0 || k == n) return 1

        var result = 1L
        var min = k.coerceAtMost(n - k)

        for (i in 0 until min) {
            result *= (n - i)
            result /= (i + 1)
        }

        return result
    }

    fun getCubeAllAverage(key: String): CubeResponseDto {
        var a = CubeResponseDto()
        return a
    }
}