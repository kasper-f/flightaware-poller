package dk.kaab.flightaware

import com.typesafe.config.ConfigFactory
import org.scalatest.FunSuite

/**
  * Created with IntelliJ IDEA.
  * User: kasperf
  * Date: 12/22/16
  * Time: 10:24 PM
  */
class ConfigTest extends FunSuite {
  val conf = new FlightConfig(ConfigFactory.parseString("""flightaware {
                                                              |  user = "your-username"
                                                              |  apikey = "your-apikey"
                                                              |  urls {
                                                              |    search-latlong = "http://flightxml.flightaware.com/json/FlightXML2/Search?query=-latlong%20{AREA}"
                                                              |    decode-flight-route = "http://flightxml.flightaware.com/json/FlightXML2/DecodeFlightRoute?faFlightID={FAFLIGHTID}"
                                                              |  }
                                                              |
                                                              |  areas = [
                                                              |    {
                                                              |      min = "11.348 -66.990"
                                                              |      max = "17.576 -75.024"
                                                              |    },
                                                              |    {
                                                              |      min = "11.00 -66.000"
                                                              |      max = "17.000 -75.000"
                                                              |    }
                                                              |  ]
                                                              |
                                                              |}""".stripMargin))

  test("api key extract") {
    assert(conf.apikey === "your-apikey")
  }
  test("username") {
    assert(conf.user === "your-username")
  }
  test("search url") {
    assert(FlightConfig.urlSearch === "http://flightxml.flightaware.com/json/FlightXML2/Search?query=-latlong%20{AREA}")
  }
  test("area count") {
    assert(FlightConfig.areas.size === 1)
  }

  test("testArea") {

    assert(conf.areas.size === 2)
    assert(conf.areas.head.area === "11.348 -66.990 17.576 -75.024")

  }

}
