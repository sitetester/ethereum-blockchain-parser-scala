package service

import service.parser.EthBlocksParser
import slick.jdbc.SQLiteProfile.api._
object EthImportManager {

  // It will save parsed data into DB
  def manageImport(): Unit = {
    val db = Database.forConfig("dbConfig")
    new EthBlocksParser().parseBlocks(List(1, 2, 3))
  }
}
