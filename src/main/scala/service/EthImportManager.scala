package service

import service.parser.EthBlocksParser
object EthImportManager {

  // It will save parsed data into DB
  def manageImport(): Unit = {

    while (true) {
      val lastScannedBlockNumber = db.DbHelper.getLastBlockNumber

      val fromBlockNumber = lastScannedBlockNumber.getOrElse(0) + 1
      val tillBlockNumber = if (fromBlockNumber == 0) 50 else 49
      val blockNumbers = (fromBlockNumber to (fromBlockNumber + tillBlockNumber)).toList

      new EthBlocksParser().parseBlocks(blockNumbers)
      println("done with block#: " + blockNumbers.last)
    }
  }
}
