package com.my.cubelog

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CubeLogApplication

fun main(args: Array<String>) {
	runApplication<CubeLogApplication>(*args)
}
