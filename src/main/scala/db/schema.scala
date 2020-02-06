package db

import slick.jdbc.SQLiteProfile.api._
import slick.lifted.ProvenShape

case class Blocks(number: Int, hash: String, difficulty: Long, transactionsCount: Int)

class BlocksTable(tag: Tag) extends Table[Blocks](tag, "blocks") {

  def number: Rep[Int] = column[Int]("number")

  def hash: Rep[String] = column[String]("hash")

  def difficulty: Rep[Long] = column[Long]("difficulty")

  def * = (number, hash, difficulty, transactionsCount).mapTo[Blocks]

  def transactionsCount: Rep[Int] = column[Int]("transactionsCount")
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
