package service

import service.parser.EthBlocksParser
object EthImportManager {

  // It will save parsed data into DB
  def manageImport(): Unit = {
    val blockNumbers = List[Int](6008149, 6008148, 6008147)
    new EthBlocksParser().parseBlocks(blockNumbers)
  }
}
