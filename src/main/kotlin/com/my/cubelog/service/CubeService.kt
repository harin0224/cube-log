package com.my.cubelog.service

import com.my.cubelog.dto.CubeFromNexonResponseDto
import com.my.cubelog.dto.CubeHistoriesDto
import com.my.cubelog.dto.CubeResponseDto
import com.my.cubelog.dto.OpenAPIRequestDto
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient;
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

@Service
class CubeService(private val webBuilder: WebClient.Builder, val httpService: HttpService){
    fun getCube(key: String): CubeResponseDto {

        val calendar: Calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val date = calendar.get(Calendar.DAY_OF_MONTH)
        val params = OpenAPIRequestDto()
        var saveResponse = CubeFromNexonResponseDto(count = 1000, cube_histories = arrayOf<CubeHistoriesDto>(), next_cursor = "")
        //var saveResponse: CubeFromNexonResponseDto? = null
        LocalDate.of(2022, 11, 25)//20221125
            .datesUntil(LocalDate.of(year, month, date-1).plusDays(1))
            .forEach { it ->
                params.date = it.toString()
                var response = httpService.getCubeFromNexon(params, key)
                var cursor = response?.next_cursor
                println("날짜:" + params.date)
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
                    saveResponse.count += response.count
                    saveResponse.cube_histories = saveResponse.cube_histories.plus(response.cube_histories)
                }
            }
        print(saveResponse.cube_histories.size)
        var a = CubeResponseDto()   //추후 가공데이터 입력
        return a
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