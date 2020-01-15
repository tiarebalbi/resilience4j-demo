package com.tiarebalbi.resilience.ratelimit.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.tiarebalbi.resilience.ratelimit.model.Stock
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader
import org.springframework.util.FileCopyUtils
import java.io.IOException
import java.io.InputStreamReader
import java.io.UncheckedIOException
import java.nio.charset.StandardCharsets.UTF_8

@Configuration
class StockDataSourceConfig {

    @Bean
    fun fakeStockDataSource(objectMapper: ObjectMapper, resourceLoader: ResourceLoader): List<Stock> {
        val source = resourceLoader.getResource("classpath:/stockSources.json")
        return objectMapper.readValue(source.asString())
    }

    fun Resource.asString(): String = try {
        InputStreamReader(this.inputStream, UTF_8)
            .use { FileCopyUtils.copyToString(it) }
    } catch (e: IOException) {
        throw UncheckedIOException(e)
    }
}