package dk.kaab.flightaware.datatypes

import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._

/**
  * Matches result from a search
  */
object FlightSample{

  def toJson(g:FlightSample) = g.asJson.noSpaces
}
case class FlightSample(
                         // flightSamplerId:BigInt,
                         faFlightID: String,
                         ident: String,
                         prefix: String,
                         `type`: String,
                         suffix: String,
                         origin: String,
                         destination: String,
                         timeout: String,
                         timestamp: Long,
                         departureTime: Long,
                         firstPositionTime: Long,
                         arrivalTime: Long,
                         longitude: Float,
                         latitude: Float,
                         lowLongitude: Float,
                         lowLatitude: Float,
                         highLongitude: Float,
                         highLatitude: Float,
                         groundspeed: Int,
                         altitude: Int,
                         heading: Int,
                         altitudeStatus: String,
                         updateType: String,
                         altitudeChange: String,
                         waypoints: String

                       ) {
  //destination":"SBKP","timeout":"ok","timestamp":"1481595850","departureTime":"1481587980","firstPositionTime":"1481588019","arrivalTime":"0","longitude":"-70.5941","latitude":"11.8926","lowLongitude":"-80.1001","lowLatitude":"11.8926","highLongitude":"-70.5941","highLatitude":"26.0802","groundspeed":"484","altitude":"350","heading":"146","altitudeStatus":"","updateType":"TA","altitudeChange":"","waypoints":""}
}

case class SearchResultCon(SearchResult:SearchResult)
object SearchResult{

  def fromRawJson(raw:String): Option[SearchResult] ={
    import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._
    decode[SearchResultCon](raw).right.toOption.map(_.SearchResult)
  }
}
case class SearchResult(next_offset:Int, aircraft:Seq[FlightSample])

case class FlightDetails(s:FlightSample, w:List[Waypoint])