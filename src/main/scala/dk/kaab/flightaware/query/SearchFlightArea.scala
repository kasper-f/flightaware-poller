package dk.kaab.flightaware.query

import java.net.URLEncoder

import akka.actor.{Actor, ActorLogging, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, StatusCodes}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.{ActorMaterializer, ActorMaterializerSettings, Materializer}
import dk.kaab.flightaware.datatypes._
import dk.kaab.flightaware.{AreaConfig, FlightConfig}
import org.slf4j.LoggerFactory

import scala.concurrent.{ExecutionContext, Future}

case class Query(area: AreaConfig)

class SearchFlightArea extends Actor with ActorLogging {

  import akka.pattern.pipe
  import context.dispatcher

  final implicit val system : ActorSystem =context.system
  final implicit val materializer: ActorMaterializer = ActorMaterializer(ActorMaterializerSettings(context.system))
  val config = new FlightConfig(context.system.settings.config)
  val searchUrl = config.urlSearch
  val http = Http(context.system)

  def receive = {
    case Query(area) =>
        http.singleRequest(HttpRequest(
        uri = searchUrl.replace("AREA", area.areaUrlEncoded),
        headers = List(config.authorization)))
        .pipeTo(self)
    case HttpResponse(StatusCodes.OK, headers, entity, _) =>
      val rr = Unmarshal(entity).to[String]
      log.debug("Got response, body: " + rr)
      val withRout : Future[List[FlightDetails]] = for{
        l <- rr.map(SearchResult.fromRawJson)
        d <- QueryHelper(config).queryDecodeFlightRoutes(l.get.aircraft.toList)

      } yield d
//      rr.map(r => SearchResult.fromRawJson(r)) pipeTo context.parent
      withRout.map(FlightResults) pipeTo context.parent


      case HttpResponse(code, _, _, _) =>
      log.warning("Request failed, response code: " + code)
      if (code == StatusCodes.Unauthorized) {
        log.error(s"Stopping poller as we can't authenticate against target with ${config.user}")
        context.system.terminate()
      }


    }
}

case class QueryHelper(config:FlightConfig) {

  def queryDecodeFlightRoutes(sa:List[FlightSample])(implicit mat:Materializer, sys: ActorSystem, ece: ExecutionContext): Future[List[FlightDetails]] = {
    Future(sa.map(s => FlightDetails(s,List.empty)))
    //Future.sequence(sa.map(s => queryDecodeFlightRoute(s.faFlightID).map(w => FlightDetails(s,w))))
//    Future.sequence(sa.map(s => queryDecodeFlightRoute(s"${s.ident}@${s.departureTime}").map(w => FlightDetails(s,w))))
  }
  def queryDecodeFlightRoute(id: String)(implicit mat:Materializer, sys: ActorSystem, ece: ExecutionContext): Future[List[Waypoint]] = {
    val http = Http(sys)
    val url = config.urlFlightRoute.replace("FAFLIGHTID", URLEncoder.encode(id,"UTF-8"))
    val f = http.singleRequest(HttpRequest(
      uri = url,
      headers = List(config.authorization)
    ))
    val response = for {
      resp <- f
      ro <- Unmarshal(resp.entity).to[String]
      /*ro <- if(resp.status != StatusCodes.OK) {
        LoggerFactory.getLogger(getClass).warn(s"Can't complete query to $url completed with ${resp.status} $ro")
        ro
      }else{
        ro
      }*/
    } yield Waypoints.fromRawJson(ro).getOrElse({
      LoggerFactory.getLogger(getClass).warn(s"Can't complete query to $url completed with ${resp.status} $ro")
      List.empty[Waypoint]
    })
    response
  }
}


