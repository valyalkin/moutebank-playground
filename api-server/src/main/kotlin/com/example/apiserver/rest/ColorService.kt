package com.example.apiserver.rest

import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity

@Service
class ColorService(
    val colorsClient: ColorsClient
) {
    fun getColorFromRemoteService(thing: String): String {
        val response = colorsClient.getColor(thing)
        return response.color
    }
}

data class ColorResponse(
    val color: String
)

@FeignClient(value = "colors", url = "\${colorservice.url}")
interface ColorsClient {
    @RequestMapping(method = [RequestMethod.GET], value = ["/getColor/{thing}"], produces = ["application/json"])
    fun getColor(@PathVariable("thing") thing: String): ColorResponse
}