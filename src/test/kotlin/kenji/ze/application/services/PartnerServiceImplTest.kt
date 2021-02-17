package kenji.ze.application.services

import io.mockk.every
import io.mockk.mockkClass
import io.mockk.slot
import kenji.ze.application.exceptions.DuplicatedDocumentException
import kenji.ze.application.exceptions.NotFoundException
import kenji.ze.domain.Address
import kenji.ze.domain.Coordinate
import kenji.ze.domain.CoverageArea
import kenji.ze.domain.Partner
import kenji.ze.infrastructure.repositories.PartnerRepository
import kenji.ze.infrastructure.rest.PartnerRestController
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*


class PartnerServiceImplTest {

    private val partnerRepository = mockkClass(PartnerRepository::class)
    private val partnerService = PartnerServiceImpl(partnerRepository)

    @Test
    fun `Should successfully create a Partner`() {
        val request = PartnerRestController.CreatePartnerRequest(
            tradingName = "Trading name",
            ownerName = "Owner",
            document = "Document",
            coverageArea = arrayListOf(arrayListOf(arrayListOf(PartnerRestController.PointDTO(1.0, 1.0), PartnerRestController.PointDTO(1.0, 1.0), PartnerRestController.PointDTO(1.0, 1.0), PartnerRestController.PointDTO(1.0, 1.0)))),
            address = PartnerRestController.PointDTO(1.0, 1.0),
        )

        val slot = slot<Partner>()

        every { partnerRepository.findByDocument(request.document) } returns null
        every { partnerRepository.save(capture(slot)) } returns Unit

        partnerService.createPartner(request)

        val partner = slot.captured

        assertThat(partner.ownerName).isEqualTo(partner.ownerName)
        assertThat(partner.tradingName).isEqualTo(partner.tradingName)
        assertThat(partner.document).isEqualTo(partner.document)
    }

    @Test
    fun `Should throw duplicated exception if duplicated on Partner creation`() {
        val request = PartnerRestController.CreatePartnerRequest(
            tradingName = "Trading name",
            ownerName = "Owner",
            document = "Document",
            coverageArea = arrayListOf(arrayListOf(arrayListOf(PartnerRestController.PointDTO(1.0, 1.0), PartnerRestController.PointDTO(1.0, 1.0), PartnerRestController.PointDTO(1.0, 1.0), PartnerRestController.PointDTO(1.0, 1.0)))),
            address = PartnerRestController.PointDTO(1.0, 1.0),
        )

        every { partnerRepository.findByDocument(request.document) } returns Partner("id", "tradingName", ownerName = "OwnerName", document = "a", CoverageArea(arrayListOf()), Address(Coordinate(1.0,0.0)))

        assertThrows(DuplicatedDocumentException::class.java) {
            partnerService.createPartner(request)
        }
    }

    @Test
    fun `Should throw not found exception if Partner not found`() {
        val id = UUID.randomUUID()

        every { partnerRepository.findById(id) } returns null

        assertThrows(NotFoundException::class.java) {
            partnerService.findPartner(id)
        }
    }

    @Test
    fun `Should throw not found exception if search Partner not found`() {
        val long = 1.0
        val lat = 1.0

        every { partnerRepository.findNearest(long, lat) } returns null

        assertThrows(NotFoundException::class.java) {
            partnerService.searchPartner(PartnerRestController.SearchPartnerRequest(long, lat))
        }
    }
}