package com.my.cubelog.service

import com.my.cubelog.dto.CubeResponseDto
import com.my.cubelog.dto.OpenAPIRequestDto
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient;

@Service
class CubeService(private val webBuilder: WebClient.Builder, val httpService: HttpService){
    fun getCube(key: String): CubeResponseDto {
        httpService.getCubeFromNexon(OpenAPIRequestDto(), key)
        println(httpService.getCubeFromNexon(OpenAPIRequestDto(), key))

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