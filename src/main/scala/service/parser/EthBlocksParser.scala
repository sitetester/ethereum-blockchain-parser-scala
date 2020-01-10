package service.parser

import db.{BlockNumbers, BlockNumbersTable}
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
    val BlockNumbersTable = TableQuery[BlockNumbersTable]
    var blocksSequence = mutable.Seq[BlockNumbers]()

    parsedBlocks.foreach(block => {
      blocksSequence = blocksSequence :+ BlockNumbers(hexToLong(block.number).toInt,
                                                      block.hash,
                                                      hexToLong(block.difficulty))
    })

    val insert: DBIO[Option[Int]] = BlockNumbersTable ++= blocksSequence
    val insertAction: Future[Option[Int]] = db.run(insert)
    val rowCount = Await.result(insertAction, 2.seconds)

    println(blocksSequence.toList)
    println(rowCount)
  }

  def hexToLong(hexString: String): Long = {
    java.lang.Long.valueOf(hexString.drop(2), 16)
  }
}
