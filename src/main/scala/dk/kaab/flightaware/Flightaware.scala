package dk.kaab.flightaware

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import dk.kaab.flightaware.datatypes.{FlightDetails, FlightResults, SearchResult}
import dk.kaab.flightaware.query.{Query, SearchFlightArea}

import scala.concurrent.duration._

/**
  * Application that will start up
  */
object Flightaware extends App {
  val system = ActorSystem("flightaware")
  FlightConfig.areas.zipWithIndex.foreach(c => {
    system.actorOf(Props(classOf[FlightawareActor],c._2, c._1))
  }
  )
}


/**
  * Actor that manage a single area.
  * Delegates polling and data storage to a sub actor
  * @param index area index used to spred the polling
  * @param area configuration for the area
  */
class FlightawareActor(index:Int, area: AreaConfig) extends Actor with ActorLogging {

  implicit val exe = context.dispatcher
  private val queryActor = context.actorOf(Props[SearchFlightArea])
  private val store = context.actorOf(Props[SqlStore])
  context.system.scheduler.schedule(index.seconds, 30.seconds, queryActor, Query(area))

  override def receive: Receive = {
    case r@FlightResults(air) =>
      val flightList = air.map(flight => s"${flight.sample.ident} ${flight.sample.faFlightID} ${flight.sample.origin} ${flight.sample.destination} ${flight.sample.latitude}:${flight.sample.longitude}").mkString("\n")
      log.info(s"result from flightaware, ${air.size} flights in the air :\n" + flightList)
      log.info(s"result from flightaware, ${air.size} flights in the air")
      store ! r
    case x =>
      log.warning(s"unexpected message to FlightawareActor ????, ${x.toString}")
  }
}