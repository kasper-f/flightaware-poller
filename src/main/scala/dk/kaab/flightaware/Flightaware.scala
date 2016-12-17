package dk.kaab.flightaware

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import dk.kaab.flightaware.datatypes.SearchResult
import dk.kaab.flightaware.query.{JsonQuery, Query}

import scala.concurrent.duration._

/**
  * Created with IntelliJ IDEA.
  * User: kasperf
  * Date: 12/16/16
  * Time: 5:31 PM
  */
object Flightaware extends App {
  val system = ActorSystem("flightaware")
  FlightConfig.areas.foreach(c => {
    system.actorOf(Props(classOf[FlightawareActor],c))
  }
  )
}


class FlightawareActor(area: AreaConfig) extends Actor with ActorLogging {

  implicit val exe = context.dispatcher
  private val queryActor = context.actorOf(Props[JsonQuery])
  private val store = context.actorOf(Props[SqlStore])
  context.system.scheduler.schedule(3.seconds, 30.seconds, queryActor, Query(area))

  override def receive: Receive = {
    case Some(SearchResult(off, air)) =>
      log.info(s"result from flightaware, ${air.size} flights in the air")
      store ! air
    case x =>
      log.info(s"result from flightaware ????, ${x.toString}")
    //TODO send result to db
  }
}