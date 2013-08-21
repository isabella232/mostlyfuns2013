package core

import akka.actor.{Props, ActorSystem}
import scala.annotation.tailrec
import core.TweetReaderActor.{FindAll, CountAll}

object Main extends App with ConfigCassandraCluster {
  import Commands._
  import akka.actor.ActorDSL._

  implicit lazy val system = ActorSystem()
  val write = system.actorOf(Props(new TweetWriterActor(cluster)))
  val read = system.actorOf(Props(new TweetReaderActor(cluster)))
  implicit val _ = actor(new Act {
    become {
      case x => println(">>> " + x)
    }
  })

  @tailrec
  private def commandLoop(): Unit = {
    Console.readLine() match {
      case QuitCommand                => return

      case ListCommand(count)         => read ! FindAll(count.toInt)
      case CountCommand               => read ! CountAll

      case _                          => println("WTF??!!")
    }

    commandLoop()
  }

  // start processing the commands
  commandLoop()

  system.shutdown()
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
