package kenji.ze.domain

import org.springframework.data.mongodb.core.index.GeoSpatialIndexType
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Partner(
    val id: String,
    val tradingName: String,
    val ownerName: String,
    @Indexed val document: String,
    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE) val coverageArea: CoverageArea,
    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE) val address: Address
)

class CoverageArea(val coordinates: List<List<List<Coordinate>>>, val type: String = "MultiPolygon")

class Address(val coordinates: Coordinate, val type: String = "Point")

class Coordinate(points: List<Double>) : ArrayList<Double>(points) {
    constructor(x: Double, y:Double) : this(arrayListOf(x, y))
    constructor() : this(arrayListOf())

    fun getX() = this.first()
    fun getY() = this.last()
}