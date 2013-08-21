package core

import akka.actor.ActorSystem
import org.specs2.mutable.SpecificationLike
import akka.testkit.{TestActorRef, TestKit, ImplicitSender}

class TweetScanActorIntegrationSpec extends TestKit(ActorSystem())
  with SpecificationLike with ImplicitSender {
  sequential

}
