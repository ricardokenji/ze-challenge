package kenji.ze.infrastructure.rest

import com.mongodb.client.model.geojson.MultiPolygon
import com.mongodb.client.model.geojson.Point
import kenji.ze.application.services.PartnerService
import kenji.ze.domain.Address
import kenji.ze.domain.CoverageArea
import kenji.ze.domain.Partner
import kenji.ze.infrastructure.rest.validators.CoverageCoordinates
import org.jetbrains.annotations.NotNull
import org.springframework.data.mongodb.core.geo.GeoJsonMultiPolygon
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.NotEmpty
import kotlin.streams.toList

@Controller
@Validated
@RequestMapping("/partner")
class PartnerRestController(
    private val partnerService: PartnerService
) {
    @GetMapping("/{partnerId}")
    fun getPartner(@PathVariable partnerId: UUID) : ResponseEntity<PartnerResponse> {
        return partnerService.findPartner(partnerId)?.let { partner ->
            return ResponseEntity.ok(PartnerResponse.from(partner))
        } ?: ResponseEntity.notFound().build()
    }

    @PostMapping
    fun createPartner(@RequestBody @Valid createPartnerRequest: CreatePartnerRequest) : ResponseEntity<PartnerResponse> {
        val partner = partnerService.createPartner(createPartnerRequest)
        return ResponseEntity.ok(PartnerResponse.from(partner))
    }

    @GetMapping("/search")
    fun searchPartner(@RequestBody searchPartnerRequest: SearchPartnerRequest) : ResponseEntity<PartnerResponse> {
        return partnerService.searchPartner(searchPartnerRequest)?.let { partner ->
            return ResponseEntity.ok(PartnerResponse.from(partner))
        } ?: ResponseEntity.notFound().build()
    }

    data class CreatePartnerRequest (
        val tradingName: String,
        val ownerName: String,
        val document: String,
        @get:CoverageCoordinates
        val coverageArea: List<List<List<PointDTO>>>,
        @get:NotNull
        val address: PointDTO
    )

    data class PartnerResponse (
        val id: String,
        val tradingName: String,
        val ownerName: String,
        val document: String,
        val coverageArea: CoverageAreaDTO,
        val address: AddressDTO
    ) {
        companion object {
            fun from(partner: Partner) = PartnerResponse (
                id = partner.id,
                tradingName = partner.tradingName,
                ownerName = partner.ownerName,
                document = partner.document,
                coverageArea = CoverageAreaDTO.from(partner.coverageArea),
                address = AddressDTO.from(partner.address)
            )
        }
    }

    data class CoverageAreaDTO (
        val type: String,
        val coordinates: List<List<List<List<Double>>>>
    ) {
        companion object {
            fun from(coverageArea: CoverageArea) = CoverageAreaDTO (
                coverageArea.type,
                coverageArea.coordinates
            )
        }
    }

    data class AddressDTO (
        val type: String,
        val coordinates: List<Double>
    ) {
        companion object {
            fun from(address: Address) = AddressDTO(address.type, address.coordinates)
        }
    }

    data class PointDTO(
        val long: Double,
        val lat: Double
    )

    data class SearchPartnerRequest (
        val long: Double,
        var lat: Double
    )
}