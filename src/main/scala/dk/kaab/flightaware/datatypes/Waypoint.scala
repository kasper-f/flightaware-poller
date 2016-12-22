package dk.kaab.flightaware.datatypes

object Waypoints{

  def fromRawJson(raw:String): Option[List[Waypoint]] ={
      import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._
      decode[List[Waypoint]](raw).right.toOption
    }

}
case class Waypoint(latitude:Float, longitude:Float, name:String, `type`:String, next_offset:Int)