package com.example.microservice.datasource

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class DataSourceController {

    @GetMapping("/getColor/{thing}")
    fun getColor(@PathVariable thing: String): ColorResponse {
         val result = when (thing) {
             "sky" -> "blue"
             "sun" -> "yellow"
             else -> {
                 "not known"
             }
         }

        return ColorResponse(result)
    }
}


data class ColorResponse(
    val color: String
)

