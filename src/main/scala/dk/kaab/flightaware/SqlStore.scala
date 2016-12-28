package dk.kaab.flightaware

import akka.actor.{Actor, ActorLogging, ActorSystem}
import dk.kaab.flightaware.datatypes.{FlightDetails, FlightResults}
import scalikejdbc.config.DBs
import scalikejdbc._


object SqlStore{

  implicit class FlightSampleJdbc(d:FlightDetails){


    def insert():Unit={
      val s = d.sample

      DB localTx { implicit session =>
        sql"insert into flightsampler_search (faFlightID, ident, prefix, type, suffix, origin, destination, timeout, timestamp, departureTime, firstPositionTime, arrivalTime, longitude, latitude, lowLongitude, lowLatitude, highLongitude, highLatitude, groundspeed, altitude, heading, altitudeStatus, updateType, altitudeChange, waypoints) values (${s.faFlightID}, ${s.ident}, ${s.prefix}, ${s.`type`}, ${s.suffix}, ${s.origin}, ${s.destination}, ${s.timeout}, ${s.timestamp}, ${s.departureTime}, ${s.firstPositionTime}, ${s.arrivalTime}, ${s.longitude}, ${s.latitude}, ${s.lowLongitude}, ${s.lowLatitude}, ${s.highLongitude}, ${s.highLatitude}, ${s.groundspeed}, ${s.altitude}, ${s.heading}, ${s.altitudeStatus}, ${s.updateType}, ${s.altitudeChange}, ${s.waypoints})".update.apply()
      }
    }
  }

}
class SqlStore extends Actor with ActorLogging{
  import SqlStore._
  @scala.throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    super.preStart()

    scalikejdbc.config.DBs.setupAll()
  }

  @scala.throws[Exception](classOf[Exception])
  override def postStop(): Unit = {
    super.postStop()
    DBs.closeAll()
  }

  override def receive: Receive = {
    case f:FlightResults =>
//      log.warning("Should store message in DB")
      //todo store in sql db
      f.planes.foreach(_.insert())



  }
}


