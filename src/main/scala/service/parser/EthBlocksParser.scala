package service.parser

import db.{Blocks, BlocksTable, Transaction, TransactionsTable}
import service.client.WebClient
import slick.jdbc.SQLiteProfile.api._

import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class EthBlocksParser {

  def parseBlocks(blockNumbers: List[Int]): Unit = {

    val parsedBlocksFutures = for (block <- blockNumbers)
      yield {
        Future {
          new WebClient().getBlockByNumber(block)
        }
      }

    val parsedBlocks = parsedBlocksFutures.map(Await.result(_, Duration.Inf))

    val db = Database.forConfig("dbConfig")

    val blockNumbersTable = TableQuery[BlocksTable]
    val transactionsTable = TableQuery[TransactionsTable]

    var blocks = mutable.Seq[Blocks]()

    parsedBlocks.foreach(block => {
      var transactions = mutable.Seq[Transaction]()

      for (tx <- block.transactions) {
        transactions = transactions :+ Transaction(tx.blockHash,
                                                   hexToInt(tx.blockNumber),
                                                   Option(tx.from),
                                                   Option(tx.to))
      }

      val insertTransactionsAction: Future[Option[Int]] = db.run(transactionsTable ++= transactions)
      Await.result(insertTransactionsAction, 2.seconds)
      blocks = blocks :+ Blocks(hexToInt(block.number), block.hash, hexToLong(block.difficulty))
    })

    val insertBlocksAction: Future[Option[Int]] = db.run(blockNumbersTable ++= blocks)
    Await.result(insertBlocksAction, 2.seconds)
  }

  def hexToInt(hexString: String): Int = {
    Integer.valueOf(hexString.drop(2), 16)
  }

  def hexToLong(hexString: String): Long = {
    java.lang.Long.valueOf(hexString.drop(2), 16)
  }
}
