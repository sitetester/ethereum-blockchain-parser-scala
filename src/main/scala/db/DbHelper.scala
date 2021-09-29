package db

import slick.dbio.DBIO
import slick.jdbc.SQLiteProfile.api._
import slick.lifted.TableQuery

import scala.concurrent.Await
import scala.concurrent.duration._

object DbHelper {
  val blocks = TableQuery[BlocksTable]

  def getLastBlockNumber: Option[Int] = {
    val q = blocks.sortBy(_.number.desc).map(_.number).take(1)

    // Anything we run against a database is a DBIO[T] (or a DBIOAction, more generally).
    // https://books.underscore.io/essential-slick/essential-slick-3.html
    val action = q.result

    val result = DbHelper.exec(q.result)
    if (result.isEmpty) Some(-1) else Some(result.head)
  }

  // https://books.underscore.io/essential-slick/essential-slick-3.html
  def exec[T](action: DBIO[T]): T = {
    val db = Database.forConfig("dbConfig")
    Await.result(db.run(action), 2.seconds)
  }
}
