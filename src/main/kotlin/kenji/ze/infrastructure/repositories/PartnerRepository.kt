package kenji.ze.infrastructure.repositories

import kenji.ze.domain.Partner
import java.util.*

interface PartnerRepository {
    fun save(partner: Partner)
    fun findById(partnerId: UUID): Partner?
    fun findByDocument(document: String): Partner?
    fun findNearest(long: Double, lat: Double): Partner?
}