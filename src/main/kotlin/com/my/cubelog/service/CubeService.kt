package com.my.cubelog.service

import com.my.cubelog.dto.CubeResponseDto
import org.springframework.stereotype.Service

@Service
class CubeService {
    fun getCube(key: String): CubeResponseDto {
        var a = CubeResponseDto()
        return a
    }
}