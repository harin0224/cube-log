package com.my.cubelog.controller

import com.my.cubelog.dto.CubeResponseDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import com.my.cubelog.service.CubeService


@RestController
class CubeController(val service: CubeService) {
    @GetMapping("/cube")
    fun cube(@RequestParam("key") key: String): CubeResponseDto = service.getCube(key)
}
