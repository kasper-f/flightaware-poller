package dk.kaab.flightaware

import akka.actor.{Actor, ActorLogging, ActorSystem}
import com.typesafe.config.ConfigFactory
import dk.kaab.flightaware.datatypes.{FlightDetails, FlightResults}
import scalikejdbc.config.DBs
//import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import dk.kaab.flightaware.datatypes.FlightSample
//import dk.kaab.flightaware.{AreaConfig, FlightConfig, mySQLConfig}
import scalikejdbc._


object SqlStore{

  implicit class FlightSampleJdbc(d:FlightDetails){

    //todo Trying to get access to my mySQL val's but isnt working
    //val config = ConfigFactory.load().getString("flightaware.db.default.driver")
    //println(s"My secret value is $config")

    def insert():Unit={
      val s = d.sample

      // initialize JDBC driver & connection pool
      //Class.forName("org.h2.Driver")
      //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
      Class.forName("db.default.driver")
      //ConnectionPool.singleton("jdbc:h2:mem:", "mySQLuser", "pass")
      ConnectionPool.singleton("db.default.url", "db.default.user", "db.default.password")

      // ad-hoc session provider on the REPL
      implicit val session = AutoSession

      sql"insert into flightsampler_search (faFlightID, ident, prefix, type, suffix, origin, destination, timeout, timestamp, departureTime, firstPositionTime, arrivalTime, longitude, latitude, lowLongitude, lowLatitude, highLongitude, highLatitude, groundspeed, altitude, heading, altitudeStatus, updateType, altitudeChange, waypoints) values (${s.faFlightID}, ${s.ident}, ${s.prefix}, ${s.`type`}, ${s.suffix}, ${s.origin}, ${s.destination}, ${s.timeout}, ${s.timestamp}, ${s.departureTime}, ${s.firstPositionTime}, ${s.arrivalTime}, ${s.longitude}, ${s.latitude}, ${s.lowLongitude}, ${s.lowLatitude}, ${s.highLongitude}, ${s.highLatitude}, ${s.groundspeed}, ${s.altitude}, ${s.heading}, ${s.altitudeStatus}, ${s.updateType}, ${s.altitudeChange}, ${s.waypoints})".update.apply()
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


