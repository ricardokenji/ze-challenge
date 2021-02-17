package kenji.ze.application.services

import kenji.ze.domain.Partner
import kenji.ze.infrastructure.rest.PartnerRestController.*
import java.math.BigDecimal
import java.util.*

interface PartnerService {
    fun createPartner(createPartnerRequest: CreatePartnerRequest): Partner
    fun findPartner(partnerId: UUID): Partner?
    fun searchPartner(searchPartnerRequest: SearchPartnerRequest): Partner?
}