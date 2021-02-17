package kenji.ze.config

import org.springframework.context.annotation.Bean
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor

class ZeConfig {
    @Bean
    fun methodValidationPostProcessor(): MethodValidationPostProcessor? {
        return MethodValidationPostProcessor()
    }
}