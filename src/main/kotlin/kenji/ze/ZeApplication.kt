package kenji.ze

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication
@ConfigurationPropertiesScan
class ZeApplication

fun main(args: Array<String>) {
	runApplication<ZeApplication>(*args)
}
