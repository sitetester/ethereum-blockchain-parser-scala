import db.{Blocks, Transaction}
import slick.jdbc.SQLiteProfile.api._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

// The main application
object DbSchema extends App {
  val db = Database.forConfig("dbConfig")
  try {

    // The query interface for the Suppliers table
    val blocks: TableQuery[Blocks] = TableQuery[Blocks]

    // the query interface for the Coffees table
    val transactions: TableQuery[Transaction] = TableQuery[Transaction]

    val setupAction: DBIO[Unit] = DBIO.seq(
      // Create the schema by combining the DDLs for the Suppliers and Coffees
      // tables using the query interfaces
      (blocks.schema ++ transactions.schema).create,
      // Insert some blocks
      /*blocks += (101, "Acme, Inc.", "99 Market Street", "Groundsville", "CA", "95199"),
      blocks += (49, "Superior Coffee", "1 Party Place", "Mendocino", "CA", "95460"),
      blocks += (150, "The High Ground", "100 Coffee Lane", "Meadows", "CA", "93966")*/
    )

    val setupFuture: Future[Unit] = db.run(setupAction)
    val f = setupFuture
    /*.flatMap { _ =>
        //#insertAction
        // Insert some transactions (using JDBC's batch insert feature)
        val insertAction: DBIO[Option[Int]] = transactions ++= Seq(
          ("Colombian", 101, 7.99, 0, 0),
          ("French_Roast", 49, 8.99, 0, 0),
          ("Espresso", 150, 9.99, 0, 0),
          ("Colombian_Decaf", 101, 8.99, 0, 0),
          ("French_Roast_Decaf", 49, 9.99, 0, 0)
        )

        val insertAndPrintAction: DBIO[Unit] = insertAction.map { coffeesInsertResult =>
          // Print the number of rows inserted
          coffeesInsertResult foreach { numRows =>
            println(s"Inserted $numRows rows into the Coffees table")
          }
        }
        //#insertAction

        val allSuppliersAction: DBIO[Seq[(Int, String, String, String, String, String)]] =
          blocks.result

        val combinedAction: DBIO[Seq[(Int, String, String, String, String, String)]] =
          insertAndPrintAction andThen allSuppliersAction

        val combinedFuture: Future[Seq[(Int, String, String, String, String, String)]] =
          db.run(combinedAction)

        combinedFuture.map { allSuppliers =>
          allSuppliers.foreach(println)
        }

      }*/
    /*.flatMap { _ =>
        /* Streaming */

        val coffeeNamesAction: StreamingDBIO[Seq[String], String] =
          transactions.map(_.blockHash).result

        val coffeeNamesPublisher: DatabasePublisher[String] =
          db.stream(coffeeNamesAction)

        coffeeNamesPublisher.foreach(println)

      }*/
    /*.flatMap { _ =>
        /* Filtering / Where */

        // Construct a query where the price of Coffees is > 9.0
        val filterQuery: Query[Transaction, (String, Int, Double, Int, Int), Seq] =
          transactions.filter(_.from > 9.0)

        // Print the SQL for the filter query
        println("Generated SQL for filter query:\n" + filterQuery.result.statements)

        // Execute the query and print the Seq of results
        db.run(filterQuery.result.map(println))

      }*/
    /* .flatMap { _ =>
        /* Update */

        // Construct an update query with the sales column being the one to update
        val updateQuery: Query[Rep[Int], Int, Seq] = transactions.map(_.to)

        val updateAction: DBIO[Int] = updateQuery.update(1)

        // Print the SQL for the Coffees update query
        println("Generated SQL for Coffees update:\n" + updateQuery.updateStatement)

        // Perform the update
        db.run(updateAction.map { numUpdatedRows =>
          println(s"Updated $numUpdatedRows rows")
        })
      }*/

    Await.result(f, Duration.Inf)

  } finally db.close
}
