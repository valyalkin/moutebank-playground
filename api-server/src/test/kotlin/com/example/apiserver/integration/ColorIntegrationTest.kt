package com.example.apiserver.integration

import com.example.apiserver.ApiServerApplication
import com.example.apiserver.rest.TestResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.core.io.ClassPathResource
import org.springframework.http.*
import org.springframework.util.StreamUtils
import java.nio.charset.Charset


@SpringBootTest(
    classes = [ApiServerApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = ["colorservice.url=http://localhost:4545"]
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ColorIntegrationTest {

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    val restTemplate = RestTemplateBuilder().build()


    @BeforeAll
    fun setUpMocks() {
        val mountebankImpostersUrl = "http://localhost:2525/imposters"
        val configuration = StreamUtils.copyToString(
            ClassPathResource("color-mock-mountebank-response.json").inputStream,
            Charset.defaultCharset()
        )

        print(configuration)

        restTemplate.postForEntity(
            mountebankImpostersUrl,
            HttpEntity(
                configuration,
                HttpHeaders().apply {
                    contentType = MediaType.APPLICATION_JSON
                }
            ),
            String::class.java
        )


    }

    @AfterAll
    fun cleanupMocks() {
        restTemplate.delete("http://localhost:2525/imposters/4545")
    }


    @ParameterizedTest
    @CsvSource(
        "sky,blue",
        "sun,yellow"
    )
    fun testColors(thing: String, color: String) {
        val result = testRestTemplate.getForEntity("/color/$thing", TestResponse::class.java)
        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(result.body!!.message).isEqualTo("Color of the $thing is $color")
    }


    @Test
    @DisplayName("Color of the other thing")
    fun testColorOfTheOtherThing() {
        val result = testRestTemplate.getForEntity("/color/blabla", TestResponse::class.java)
        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(result.body!!.message).isEqualTo("Color of the blabla is not known")
    }


}