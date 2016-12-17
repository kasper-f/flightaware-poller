package dk.kaab.flightaware.query

import akka.actor.{Actor, ActorLogging}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, StatusCodes}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import akka.util.ByteString
import dk.kaab.flightaware.{AreaConfig, FlightConfig}
import dk.kaab.flightaware.datatypes.SearchResult

case class Query(area: AreaConfig)

class JsonQuery extends Actor with ActorLogging {

  import akka.pattern.pipe
  import context.dispatcher

  final implicit val materializer: ActorMaterializer = ActorMaterializer(ActorMaterializerSettings(context.system))
  val searchUrl = FlightConfig.urlSearch
  val http = Http(context.system)

  def receive = {
    case Query(config) =>
      http.singleRequest(HttpRequest(
        uri = searchUrl.replace("AREA",config.areaUrlEncoded),
        headers = List(FlightConfig.authorization)))
        .pipeTo(self)
    case HttpResponse(StatusCodes.OK, headers, entity, _) =>
      val rr = Unmarshal(entity).to[String]
      log.debug("Got response, body: " + rr)
      rr.map(r =>  SearchResult.fromRawJson(r)) pipeTo context.parent
    case HttpResponse(code, _, _, _) =>
      log.warning("Request failed, response code: " + code)
      if(code == StatusCodes.Unauthorized){
        log.error(s"Stopping poller as we can't authenticate against target with ${FlightConfig.user}")
        context.system.terminate()
      }

  }
}
