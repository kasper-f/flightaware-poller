package dk.kaab.flightaware

import akka.actor.{Actor, ActorLogging}
import dk.kaab.flightaware.datatypes.FlightSample

/**
  * Created with IntelliJ IDEA.
  * User: kasperf
  * Date: 12/17/16
  * Time: 11:19 AM
  */
class SqlStore extends Actor with ActorLogging{
  override def receive: Receive = {
    case f:Seq[FlightSample] =>
//      log.warning("Should store message in DB")
      //todo store in sql db

  }
}
