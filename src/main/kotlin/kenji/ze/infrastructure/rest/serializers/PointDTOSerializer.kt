package kenji.ze.infrastructure.rest.serializers

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import kenji.ze.infrastructure.rest.PartnerRestController


class PointDTOSerializer : JsonSerializer<PartnerRestController.PointDTO>() {

    override fun serialize(value: PartnerRestController.PointDTO, jgen: JsonGenerator, provider: SerializerProvider) {
        jgen.writeObject(listOf(value.long, value.lat))
    }
}