package com.achilles.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

data class Hello(val message: String)

@RestController
class HealthCheckController {
    @GetMapping("/status")
    fun status() = Hello("All working !")
}
