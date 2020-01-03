package service.parser

import service.client.WebClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class EthBlocksParser {

  def parseBlocks(blockNumbers: List[Int]): Unit = {

    val blocks = Array(6008149, 6008148, 6008147)
    val parsedBlocksFutures = for (block <- blocks)
      yield
        Future {
          new WebClient().getBlockByNumber(block)
        }

    val parsedBlocks = parsedBlocksFutures.map(Await.result(_, Duration.Inf))

    parsedBlocks.foreach(block => {
      println(s"block(${block.number}.transactions.length=" + block.transactions.length)
      println(s"block(${block.number}.eventLogs.length=" + block.eventLogs.length)

      println()
    })
  }
}
