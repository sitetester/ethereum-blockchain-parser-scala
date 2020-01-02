package service.client

import com.google.gson.Gson
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.util.EntityUtils

case class TransactionReceiptRequest(jsonrpc: String,
                                     method: String,
                                     params: Array[String],
                                     id: Int)

case class TransactionReceipt(transactionHash: String,
                              transactionIndex: String,
                              blockHash: String,
                              blockNumber: String,
                              status: String)

case class TransactionReceiptResponse(jsonrpc: String,
                                      id: Int,
                                      result: TransactionReceipt)

case class EventLogTopic(hash: String)

case class EventLog(
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

case class EventLogResponse(jsonrpc: String, id: Int, result: Array[EventLog])

case class BlockByNumberRequest(jsonrpc: String,
                                method: String,
                                params: Array[Any],
                                id: Int)

case class Transaction(blockHash: String,
                       blockNumber: String,
                       from: String,
                       to: String,
                       hash: String,
                       transactionIndex: String)

case class Block(difficulty: String,
                 hash: String,
                 number: String,
                 transactions: Array[Transaction],
                 var eventLogs: Array[EventLog])

case class BlockByNumberResponse(jsonrpc: String, id: Int, result: Block)

class WebClient {
  val REMOTE_URL =
    "https://mainnet.infura.io/v3/ae2f42cad8de499ca2cd734e5a5affe5"

  // https://www.rapidtables.com/convert/number/hex-to-decimal.html
  // https://infura.io/docs/ethereum/json-rpc/eth_getBlockByNumber
  // https://www.baeldung.com/httpclient-post-http-request
  def getBlockByNumber(blockNumber: Int): Block = {

    val blockByNumberRequest = BlockByNumberRequest("2.0",
                                                    "eth_getBlockByNumber",
                                                    Array("0x5BAD55", true),
                                                    1)
    val responseString = postRequestWithJsonString(
      new Gson().toJson(blockByNumberRequest))

    val blockByNumberResponse =
      new Gson().fromJson(responseString, classOf[BlockByNumberResponse])
    val block = blockByNumberResponse.result

    if (block.hash != "") {
      block.eventLogs = getLogs(block.hash)
    }

    block.transactions.foreach(t => {
      val status = getTransactionReceipt(t.hash)
      println("status --- " + status)
    })

    block
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

  private def getLogs(blockHash: String): Array[EventLog] = {
    val p: Array[EventLogRequestParams] = Array(
      EventLogRequestParams(blockHash))
    val getLogRequest = GetLogRequest("2.0", "eth_getLogs", p, 1)
    val responseString = postRequestWithJsonString(
      new Gson().toJson(getLogRequest))

    val eventLogResponse =
      new Gson().fromJson(responseString, classOf[EventLogResponse])
    eventLogResponse.result
  }

  private def getTransactionReceipt(transactionHash: String): String = {
    val a = Array(transactionHash)

    val transactionReceiptRequest = TransactionReceiptRequest(
      "2.0",
      "eth_getTransactionReceipt",
      Array(
        "0xbb3a336e3f823ec18197f1e13ee875700f08f03e2cab75f0d0b118dabb44cba0"),
      1)
    val responseString = postRequestWithJsonString(
      new Gson().toJson(transactionReceiptRequest))

    val transactionReceiptResponse =
      new Gson().fromJson(responseString, classOf[TransactionReceiptResponse])

    transactionReceiptResponse.result.status
  }
}
