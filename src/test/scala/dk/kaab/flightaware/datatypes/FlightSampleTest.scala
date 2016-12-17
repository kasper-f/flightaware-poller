package dk.kaab.flightaware.datatypes

import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._


/**
  * Created with IntelliJ IDEA.
  * User: kasperf
  * Date: 12/16/16
  * Time: 9:35 PM
  */
class FlightSampleTest extends org.scalatest.FunSuite {

  test("Read json format"){
    val res = getClass.getClassLoader.getResourceAsStream("json-output/SearchResult.json")
    val rawJson = scala.io.Source.fromInputStream(res).mkString
    val read = decode[SearchResultCon](rawJson)
    assert(read.isRight)
    assert(read.right.get.SearchResult.aircraft.size === 15)
  }

}
