package dk.kaab.flightaware

import akka.actor.{Actor, ActorLogging}
import dk.kaab.flightaware.datatypes.FlightSample
import scalikejdbc._

object SqlStore{

  implicit class FlightSampleJdbc(s:FlightSample){

    def insert():Unit={
      // initialize JDBC driver & connection pool
      Class.forName("org.h2.Driver")
      Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
      ConnectionPool.singleton("jdbc:h2:mem:hello", "user", "pass")

      // ad-hoc session provider on the REPL
      implicit val session = AutoSession

      // table creation, you can run DDL statements using #execute as with JDBC
      /*sql"""
      create table members (
        id serial not null primary key,
        name varchar(64),
        created_at timestamp not null
      )
      """.execute.apply()*/

      sql"insert into flightsampler_search (faFlightID, ident, prefix) values (${s.faFlightID}, ${s.ident}, ${s.prefix})".update.apply()
    }
  }

}
class SqlStore extends Actor with ActorLogging{
  import SqlStore._
  override def receive: Receive = {
    case f:Seq[FlightSample] =>
//      log.warning("Should store message in DB")
      //todo store in sql db
      //f.foreach(_.insert())
  }
}


