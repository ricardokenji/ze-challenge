package kenji.ze.infrastructure.repositories

import kenji.ze.domain.Partner
import org.springframework.data.geo.*
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import java.util.*


@Repository
class PartnerRepositoryImpl(
    private val mongoTemplate: MongoTemplate
) : PartnerRepository {
    override fun save(partner: Partner) {
        mongoTemplate.save(partner)
    }

    override fun findById(partnerId: UUID): Partner? {
        val criteria = Criteria.where("_id").`is`(partnerId.toString())
        return mongoTemplate.findOne(Query(criteria), Partner::class.java)
    }

    override fun findByDocument(document: String): Partner? {
        val criteria = Criteria.where("document").`is`(document)
        return mongoTemplate.findOne(Query(criteria), Partner::class.java)
    }

    override fun findNearest(long: Double, lat: Double): Partner? {
        val point = Point(long, lat)

        val criteria = Criteria.where("address").nearSphere(point)
            .and("coverageArea").intersects(GeoJsonPoint(point))
        return mongoTemplate.findOne(Query(criteria), Partner::class.java)
    }
}