package db

import slick.jdbc.SQLiteProfile.api._

/*
case class Block(difficulty: String,
                 hash: String,
                 number: String,
                 transactions: Array[Transaction],
                 var eventLogs: Array[EventLog])
 */

case class BlockNumbers(number: Int, hash: String, difficulty: Long, id: Long = 0L)

class BlockNumbersTable(tag: Tag) extends Table[BlockNumbers](tag, "blocks") {
  def * = (number, hash, difficulty, id).mapTo[BlockNumbers]

  def number: Rep[Int] = column[Int]("number")

  def hash: Rep[String] = column[String]("hash")

  def difficulty: Rep[Long] = column[Long]("difficulty")

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
}

/*
class Transaction(tag: Tag)
    extends Table[(Int, String, String, String, String, String, String)](tag, "transactions") {

  def * : ProvenShape[(Int, String, String, String, String, String, String)] =
    (id, blockHash, blockNumber, from, to, hash, transactionIndex)

  def id: Rep[Int] = column[Int]("id", O.PrimaryKey)
  def blockHash: Rep[String] = column[String]("blockHash")
  def blockNumber: Rep[String] = column[String]("blockNumber")
  def from: Rep[String] = column[String]("from")
  def to: Rep[String] = column[String]("to")
  def hash: Rep[String] = column[String]("hash")
  def transactionIndex: Rep[String] = column[String]("transactionIndex")

}*/

case class Message(sender: String, content: String, id: Long = 0L)

class MessageTable(tag: Tag) extends Table[Message](tag, "messages") {
  def * = (sender, content, id).mapTo[Message]

  def sender = column[String]("sender")

  def content = column[String]("content")

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
}
