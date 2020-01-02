package service.parser

import service.client.WebClient

class EthBlocksParser {

  def parseBlocks(blockNumbers: List[Int]): Unit = {

    val block = new WebClient().getBlockByNumber(6008149)

    println("block.transactions.length=" + block.transactions.length)
    println("block.eventLogs.length=" + block.eventLogs.length)

  }

}