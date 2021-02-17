package kenji.ze.application.services

import kenji.ze.application.exceptions.DuplicatedDocumentException
import kenji.ze.application.exceptions.NotFoundException
import kenji.ze.domain.Address
import kenji.ze.domain.Coordinate
import kenji.ze.domain.CoverageArea
import kenji.ze.domain.Partner
import kenji.ze.infrastructure.repositories.PartnerRepository
import kenji.ze.infrastructure.rest.PartnerRestController
import org.springframework.stereotype.Service
import java.util.*

@Service
class PartnerServiceImpl(
    private val partnerRepository: PartnerRepository
) : PartnerService {

    override fun createPartner(createPartnerRequest: PartnerRestController.CreatePartnerRequest): Partner {
        partnerRepository.findByDocument(createPartnerRequest.document)?.let {
            throw DuplicatedDocumentException()
        } ?: run {
            val partner = Partner(
                id = UUID.randomUUID().toString(),
                tradingName = createPartnerRequest.tradingName,
                ownerName = createPartnerRequest.ownerName,
                document = createPartnerRequest.document,
                coverageArea = CoverageArea(createPartnerRequest.coverageArea.map { polygons ->
                    polygons.map { coordinates ->
                        coordinates.map { position ->
                            Coordinate(position.long, position.lat)
                        }
                    }
                }.toList()),
                address = Address(Coordinate(createPartnerRequest.address.long, createPartnerRequest.address.lat)),
            )

            partnerRepository.save(partner)

            return partner
        }
    }

    override fun findPartner(partnerId: UUID): Partner? {
        return partnerRepository.findById(partnerId) ?: throw NotFoundException("partner")
    }

    override fun searchPartner(searchPartnerRequest: PartnerRestController.SearchPartnerRequest): Partner? {
        return partnerRepository.findNearest(searchPartnerRequest.long, searchPartnerRequest.lat) ?: throw NotFoundException("partner")
    }
}