package domain

import java.util.Date

case class TweetId(id: String) extends AnyVal

case class User(user: String) extends AnyVal

case class Text(text: String) extends AnyVal

case class Tweet(id: TweetId, user: User, text: Text, createdAt: Date)
