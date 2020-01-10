package db

import slick.jdbc.SQLiteProfile.api._
import slick.lifted.ProvenShape

case class Blocks(number: Int, hash: String, difficulty: Long)

class BlocksTable(tag: Tag) extends Table[Blocks](tag, "blocks") {

  def number: Rep[Int] = column[Int]("number")

  def hash: Rep[String] = column[String]("hash")

  def difficulty: Rep[Long] = column[Long]("difficulty")

  def * = (number, hash, difficulty).mapTo[Blocks]
}

case class Transaction(blockHash: String,
                       blockNumber: Int,
                       from: Option[String],
                       to: Option[String])

class TransactionsTable(tag: Tag) extends Table[Transaction](tag, "transactions") {

  def blockHash: Rep[String] = column[String]("blockHash")

  def * : ProvenShape[Transaction] =
    (blockHash, blockNumber, from, to).mapTo[Transaction]

  def blockNumber: Rep[Int] = column[Int]("blockNumber")

  def from: Rep[Option[String]] = column[Option[String]]("from")

  def to: Rep[Option[String]] = column[Option[String]]("to")
}

case class Message(sender: String, content: Option[String], id: Long = 0L)

class MessageTable(tag: Tag) extends Table[Message](tag, "messages") {

  def sender = column[String]("sender")

  def * = (sender, content, id).mapTo[Message]

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def content = column[Option[String]]("content")
}
