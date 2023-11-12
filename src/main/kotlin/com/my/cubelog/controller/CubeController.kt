package com.my.cubelog.controller

import com.my.cubelog.dto.CubeEvent
import com.my.cubelog.dto.CubePersonalAverageDto
import com.my.cubelog.dto.CubeResponseDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import com.my.cubelog.service.CubeService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod


@RestController
@RequestMapping("/cube")
class CubeController(val service: CubeService) {
    @GetMapping()
    fun cube(@RequestParam("key") key: String): CubeEvent = service.getCube(key)

    @GetMapping("/personal-average")
    fun cubePersonalAverage(@RequestParam("key") key: String): CubePersonalAverageDto =
        service.getCubePersonalAverage(key)

    @GetMapping("/all-average")
    fun cubeAllAverage(@RequestParam("key") key: String): CubeResponseDto = service.getCubeAllAverage(key)

    @GetMapping("/simulator-average")
    fun cubeSimulatorAverage(@RequestParam("key") key: String): CubeResponseDto = service.getCubeSimulatorAverage(key)
}
