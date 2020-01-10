import service.EthImportManager

import scala.collection.mutable

object Main extends App {

  EthImportManager.manageImport()

  var blocksSequence = mutable.Seq[Int]()
  blocksSequence = blocksSequence :+ 1
  println(blocksSequence.toList)

}
