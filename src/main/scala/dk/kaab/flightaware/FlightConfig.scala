package dk.kaab.flightaware

import java.net.URLEncoder

import akka.http.scaladsl.model.headers.{Authorization, BasicHttpCredentials}
import com.typesafe.config.{Config, ConfigFactory}

import scala.collection.JavaConversions._
import scala.collection.mutable

object FlightConfig extends FlightConfig(ConfigFactory.load()){

}
class FlightConfig(config:Config) {
  private val flightConf = config.getConfig("flightaware")
  val user: String = flightConf.getString("user")
  val apikey: String = flightConf.getString("apikey")

  val authorization: Authorization = Authorization(BasicHttpCredentials(user,apikey))

  val urlSearch: String = flightConf.getString("urls.search-latlong")
  val urlFlightRoute: String = flightConf.getString("urls.decode-flight-route")

  val areas: Seq[AreaConfig] =flightConf.getConfigList("areas").map(AreaConfig)
}

case class AreaConfig(conf:Config){
  val min: String = conf.getString("min")
  val max: String = conf.getString("max")
  val area = s"$min $max"
  val areaUrlEncoded: String = URLEncoder.encode(area,"UTF-8")
}

case class mySQLConfig(conf:Config){
  val mySqlUser: String = conf.getString("db.default.user")
  val mySqlPass: String = conf.getString("db.default.password")
  val mySqlDb: String = conf.getString("db.default.db")
}