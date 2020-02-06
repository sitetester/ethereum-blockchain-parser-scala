import db.{BlocksTable, TransactionsTable}
import slick.jdbc.SQLiteProfile.api._
import slick.lifted.TableQuery

import scala.concurrent.Await
import scala.concurrent.duration._

// The main application
object DbSchema extends App {

  def addBlocksSchema(): Unit = {
    val db = Database.forConfig("dbConfig")
    val blocks = TableQuery[BlocksTable]
    Await.result(db.run(blocks.schema.create), 2.seconds)

    val transactions = TableQuery[TransactionsTable]
    Await.result(db.run(transactions.schema.create), 2.seconds)
  }

  addBlocksSchema()
  println("DONE!")
}
