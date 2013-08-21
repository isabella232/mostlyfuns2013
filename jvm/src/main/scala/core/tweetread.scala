package core

import akka.actor.Actor
import com.datastax.driver.core.{Cluster, Row}
import com.datastax.driver.core.querybuilder.QueryBuilder
import core.TweetReaderActor.{FindAll, CountAll}
import domain.Tweet


object TweetReaderActor {
  case object CountAll
  case class FindAll(maximum: Int)
}

class TweetReaderActor(cluster: Cluster) extends Actor {
  val session = cluster.connect(Keyspaces.akkaCassandra)
  val countAll = session.prepare("select count(*) from tweets;")

  import scala.collection.JavaConversions._
  import cassandra.resultset._
  import akka.pattern.pipe
  import context.dispatcher

  def buildTweet(r: Row): Tweet = {
    val id = r.getString("key")
    val user = r.getString("user_user")
    val text = r.getString("text")
    val createdAt = r.getDate("createdat")
    Tweet(id, user, text, createdAt)
  }

  def receive: Actor.Receive = {
    case CountAll         =>
      session.executeAsync(countAll.bind()).map(_.one().getLong(0)) pipeTo sender
    case FindAll(maximum) =>
      val query = QueryBuilder.select().all().from(Keyspaces.akkaCassandra, "tweets").limit(maximum)
      session.executeAsync(query).map(_.all().map(buildTweet).toList) pipeTo sender

  }
}