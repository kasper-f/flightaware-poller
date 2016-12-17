package dk.kaab.flightaware

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import dk.kaab.flightaware.datatypes.SearchResult
import dk.kaab.flightaware.query.{JsonQuery, Query}

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
  private val queryActor = context.actorOf(Props[JsonQuery])
  private val store = context.actorOf(Props[SqlStore])
  context.system.scheduler.schedule(index.seconds, 30.seconds, queryActor, Query(area))

  override def receive: Receive = {
    case Some(SearchResult(off, air)) =>
      log.info(s"result from flightaware, ${air.size} flights in the air")
      store ! air
    case x =>
      log.info(s"result from flightaware ????, ${x.toString}")
  }
}