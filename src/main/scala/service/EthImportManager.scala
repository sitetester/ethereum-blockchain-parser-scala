package service

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import service.parser.EthBlocksParser

object EthImportManager {

  // It will save parsed data into DB
  def manageImport(): Unit = {

    while (true) {
      val lastScannedBlockNumber = db.DbHelper.getLastBlockNumber

      val fromBlockNumber = lastScannedBlockNumber.getOrElse(0) + 1
      val tillBlockNumber = if (fromBlockNumber == 0) 10 else 9
      val blockNumbers = (fromBlockNumber to (fromBlockNumber + tillBlockNumber)).toList

      new EthBlocksParser().parseBlocks(blockNumbers)

      val currentTime = DateTimeFormatter
        .ofPattern("HH:mm:ss")
        .format(LocalDateTime.now)

      println(currentTime + " - done with block#: " + blockNumbers.last)
    }
  }
}
