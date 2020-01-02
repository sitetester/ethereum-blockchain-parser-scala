package service

import service.parser.EthBlocksParser

object EthImportManager {

  // It will save parsed data into DB
  def manageImport(): Unit = {
    new EthBlocksParser().parseBlocks(List(1, 2, 3)
    )
  }
}
