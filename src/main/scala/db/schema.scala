package db

import slick.jdbc.SQLiteProfile.api._
import slick.lifted.ProvenShape

class Blocks(tag: Tag) extends Table[(Int, String, String)](tag, "blocks") {

  // Every table needs a * projection with the same type as the table's type parameter
  def * : ProvenShape[(Int, String, String)] =
    (id, hash, number)

  // This is the primary key column:
  def id: Rep[Int] = column[Int]("id", O.PrimaryKey)

  def hash: Rep[String] = column[String]("hash")

  def number: Rep[String] = column[String]("number")
}

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

}
