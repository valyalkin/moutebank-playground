package com.example.apiserver.rest

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ApiController(
    private val colorService: ColorService
) {

    @GetMapping("/color/{thing}")
    fun getStatus(@PathVariable thing: String): TestResponse {
        val color = colorService.getColorFromRemoteService(thing)

        return TestResponse("Color of the $thing is $color")
    }

}

data class TestResponse(
    val message: String
)
