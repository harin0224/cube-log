package controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import service.CubeService


@RestController
class CubeController(val service: CubeService) {
    @GetMapping("/cube")
    fun cube(@RequestParam("key") key: String) = service.getCube(key)
}