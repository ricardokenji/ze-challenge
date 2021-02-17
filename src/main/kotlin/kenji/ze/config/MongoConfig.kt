package kenji.ze.config

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.bson.UuidRepresentation
import org.bson.codecs.UuidCodecProvider
import org.bson.codecs.configuration.CodecRegistries
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory
import org.springframework.data.mongodb.core.convert.DbRefResolver
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver
import org.springframework.data.mongodb.core.convert.MappingMongoConverter
import org.springframework.data.mongodb.core.convert.MongoCustomConversions
import org.springframework.data.mongodb.core.mapping.MongoMappingContext

@Configuration
class MongoConfig {
    @Bean
    fun mongo(mongoConfigParams: MongoConfigParams): MongoClient {
        val codecRegistries = CodecRegistries
            .fromRegistries(
                CodecRegistries.fromProviders(UuidCodecProvider(UuidRepresentation.STANDARD)),
                MongoClientSettings.getDefaultCodecRegistry()
            )

        val clientSettings = MongoClientSettings.builder()
            .applyConnectionString(ConnectionString(mongoConfigParams.url))
            .codecRegistry(codecRegistries)
            .build()

        return MongoClients.create(clientSettings)
    }

    @Bean
    fun mongoTemplate(mongoClient: MongoClient, mongoConfigParams: MongoConfigParams): MongoTemplate {
        val factory = SimpleMongoClientDatabaseFactory(mongoClient, mongoConfigParams.database)
        val dbRefResolver: DbRefResolver = DefaultDbRefResolver(factory)
        val conversions = MongoCustomConversions(emptyList<Any>())

        val mappingContext = MongoMappingContext()
        mappingContext.setSimpleTypeHolder(conversions.simpleTypeHolder)
        mappingContext.afterPropertiesSet()
        mappingContext.isAutoIndexCreation = true

        val converter = MappingMongoConverter(dbRefResolver, mappingContext)
        converter.setCustomConversions(conversions)
        converter.setCodecRegistryProvider(factory)
        converter.afterPropertiesSet()
        return MongoTemplate(factory, converter)
    }
}

@ConstructorBinding
@ConfigurationProperties(prefix = "mongodb")
data class MongoConfigParams(
    var url: String,
    var database: String
)
