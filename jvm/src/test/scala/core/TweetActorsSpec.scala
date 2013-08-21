package core

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestActorRef, TestKit}
import org.specs2.mutable.SpecificationLike
import java.util.Date

class TweetActorsSpec extends TestKit(ActorSystem())
  with SpecificationLike with ImplicitSender {
  sequential

}
