package core

import akka.actor.{Props, ActorSystem}
import scala.annotation.tailrec

object Main extends App {
  import Commands._
  import akka.actor.ActorDSL._

  @tailrec
  private def commandLoop(): Unit = {
    Console.readLine() match {
      case QuitCommand                => return

      case _                          => println("WTF??!!")
    }

    commandLoop()
  }

  // start processing the commands
  commandLoop()

}

/**
 * Various regexes for the ``Shell`` to use
 */
object Commands {

  val ListCommand  = "list (\\d+)".r
  val CountCommand = "count"
  val QuitCommand  = "quit"
  val ScanCommand  = "scan (.*)".r

}
