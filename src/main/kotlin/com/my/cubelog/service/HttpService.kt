package com.my.cubelog.service

import com.my.cubelog.dto.CubeFromNexonResponseDto
import com.my.cubelog.dto.OpenAPIRequestDto
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class HttpService(private val webBuilder: WebClient.Builder){
    var BASE_URL:String = "https://public.api.nexon.com/openapi/maplestory/v1/cube-use-results"
    var webClient:WebClient = webBuilder.baseUrl(BASE_URL).build()
    fun getCubeFromNexon(params: OpenAPIRequestDto, apiKey: String): CubeFromNexonResponseDto? {
        val response = webClient.get()
            .uri{ uriBuilder -> uriBuilder
                //.path(url)
                .queryParam("count", params.count)
                .queryParam("date", params.date)
                .queryParam("cursor", params.cursor)
                .build()}
            .headers{ headers: HttpHeaders ->
                headers.setBearerAuth(apiKey)
            }
            .retrieve()
            .bodyToMono(CubeFromNexonResponseDto::class.java)
            .block();

        return response

    }
}
