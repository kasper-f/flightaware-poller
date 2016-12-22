package dk.kaab.flightaware

import java.util.concurrent.TimeUnit

import org.scalatest.FunSuite
import akka.actor.ActorSystem
import akka.actor.Actor
import akka.actor.Props
import akka.testkit.{ImplicitSender, TestActors, TestKit}
import com.typesafe.config.ConfigFactory
import org.scalatest.WordSpecLike
import org.scalatest.Matchers
import org.scalatest.BeforeAndAfterAll

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class FlightawareActorTest() extends TestKit(ActorSystem("flighta",ConfigFactory.defaultReference())) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "query" must {

    "send back messages unchanged" in {
      val q = system.actorOf(Props(classOf[FlightawareActor],1,FlightConfig.areas.head))
      Thread.sleep(5000)
      Await.ready(system.whenTerminated,Duration(25,TimeUnit.SECONDS))
    }

  }
}

