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

  def buildTweet(r: Row): Tweet = {
    val id = r.getString("key")
    val user = r.getString("user_user")
    val text = r.getString("text")
    val createdAt = r.getDate("createdat")
    Tweet(id, user, text, createdAt)
  }

  def receive: Actor.Receive = {
    case CountAll         =>
      sender ! session.execute(countAll.bind()).one().getLong(0)
    case FindAll(maximum) =>
      val query = QueryBuilder.select().all().from(Keyspaces.akkaCassandra, "tweets").limit(maximum)
      sender ! session.execute(query).all().map(buildTweet).toList

  }
}