package kenji.ze.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Configuration
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import kenji.ze.infrastructure.rest.PartnerRestController
import kenji.ze.infrastructure.rest.serializers.PointDTOSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary


@Configuration
class JacksonConfig {

    @Bean
    @Primary
    fun objectMapper(): ObjectMapper {
        val mapper = ObjectMapper()
        mapper.registerModule(KotlinModule())

        val module = SimpleModule()
        module.addSerializer(PartnerRestController.PointDTO::class.java, PointDTOSerializer())
        mapper.registerModule(module)

        return mapper
    }
}