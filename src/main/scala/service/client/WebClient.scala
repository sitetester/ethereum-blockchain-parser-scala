package service.client

import com.google.gson.Gson
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.util.EntityUtils

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

case class TransactionReceiptRequest(jsonrpc: String,
                                     method: String,
                                     params: Array[String],
                                     id: Int)

case class TransactionReceipt(transactionHash: String,
                              transactionIndex: String,
                              blockHash: String,
                              blockNumber: String,
                              status: String)

case class TransactionReceiptResponse(jsonrpc: String, id: Int, result: TransactionReceipt)

case class EventLogTopic(hash: String)

case class EthEventLog(
    removed: Boolean,
    logIndex: String,
    transactionIndex: String,
    transactionHash: String,
    blockHash: String,
    blockNumber: String,
    address: String,
    data: String,
    // topics: Array[EventLogTopic]
)

case class EventLogRequestParams(blockHash: String)

case class GetLogRequest(jsonrpc: String,
                         method: String,
                         params: Array[EventLogRequestParams],
                         id: Int)

case class EventLogResponse(jsonrpc: String, id: Int, result: Array[EthEventLog])

case class BlockByNumberRequest(jsonrpc: String, method: String, params: Array[Any], id: Int)

case class EthTransaction(blockHash: String,
                          blockNumber: String,
                          from: String,
                          to: String,
                          hash: String,
                          transactionIndex: String)

case class EthBlock(number: String,
                    hash: String,
                    difficulty: String,
                    transactions: Array[EthTransaction],
                    var eventLogs: Array[EthEventLog])

case class BlockByNumberResponse(jsonrpc: String, id: Int, result: EthBlock)

class WebClient {
  val REMOTE_URL =
    "https://mainnet.infura.io/v3/ae2f42cad8de499ca2cd734e5a5affe5"

  // https://www.rapidtables.com/convert/number/hex-to-decimal.html
  // https://infura.io/docs/ethereum/json-rpc/eth_getBlockByNumber
  // https://www.baeldung.com/httpclient-post-http-request
  def getBlockByNumber(blockNumber: Int): EthBlock = {

    val blockByNumberRequest = BlockByNumberRequest(
      "2.0",
      "eth_getBlockByNumber",
      Array("0x" + blockNumber.toHexString.toUpperCase, true),
      1)
    val responseString = postRequestWithJsonString(new Gson().toJson(blockByNumberRequest))

    val blockByNumberResponse = new Gson().fromJson(responseString, classOf[BlockByNumberResponse])
    val block = blockByNumberResponse.result

    if (block.hash != "") {
      block.eventLogs = getLogs(block.hash)
    }

    // TODO: this could be fired in batch of 20 to get the combined result
    // val partialTransactions = block.transactions.take(20)
    val partialTransactions = block.transactions
    val hashes = partialTransactions.map(_.hash)

    val statusFutures = for (hash <- hashes)
      yield
        Future {
          getTransactionReceipt(hash)
        }

    val statuses = statusFutures.map(Await.result(_, Duration.Inf))
    // println()
    // statuses.foreach(println(_))
    block
  }

  private def getLogs(blockHash: String): Array[EthEventLog] = {

    val p: Array[EventLogRequestParams] = Array(EventLogRequestParams(blockHash))
    val getLogRequest = GetLogRequest("2.0", "eth_getLogs", p, 1)
    val responseString = postRequestWithJsonString(new Gson().toJson(getLogRequest))

    val eventLogResponse = new Gson().fromJson(responseString, classOf[EventLogResponse])
    eventLogResponse.result
  }

  private def getTransactionReceipt(transactionHash: String): (String, String) = {

    val transactionReceiptRequest =
      TransactionReceiptRequest("2.0", "eth_getTransactionReceipt", Array(transactionHash), 1)

    val responseString = postRequestWithJsonString(new Gson().toJson(transactionReceiptRequest))
    val transactionReceiptResponse =
      new Gson().fromJson(responseString, classOf[TransactionReceiptResponse])

    (transactionHash, transactionReceiptResponse.result.status)
  }

  private def postRequestWithJsonString(jsonString: String): String = {

    val httpPost = new HttpPost(REMOTE_URL)
    httpPost.setHeader("Content-type", "application/json")
    httpPost.setHeader("Accept", "application/json")

    httpPost.setEntity(new StringEntity(jsonString))
    val client = new DefaultHttpClient
    val response = client.execute(httpPost)

    val responseString = EntityUtils.toString(response.getEntity)

    responseString
  }
}
