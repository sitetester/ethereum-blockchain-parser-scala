import db.{BlocksTable, Message, MessageTable, TransactionsTable}
import slick.jdbc.SQLiteProfile.api._
import slick.lifted.TableQuery

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

// The main application
object DbSchema extends App {

  def addMessages(): Unit = {
    val db = Database.forConfig("dbConfig")
    val messages = TableQuery[MessageTable]
    Await.result(db.run(messages.schema.create), 2.seconds)

    val insert: DBIO[Option[Int]] = messages ++= MessagesData
    val insertAction: Future[Option[Int]] = db.run(insert)
    val rowCount = Await.result(insertAction, 2.seconds)

    println("rowCount ------- " + rowCount)
  }

  def MessagesData = Seq(
    Message("Dave", Some("Hello, HAL. Do you read me, HAL?")),
    Message("HAL", None),
    /*Message("Dave", "Open the pod bay doors, HAL."),
    Message("HAL", "I'm sorry, Dave. I'm afraid I can't do that.")*/
  )

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
