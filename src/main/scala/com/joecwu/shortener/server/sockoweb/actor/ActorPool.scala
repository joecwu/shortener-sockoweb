package com.joecwu.shortener.server.sockoweb.actor

import akka.actor.{Props, ActorSystem}

/**
 * Created by Joe_Wu on 8/11/15.
 */
object ActorPool {
  val actorSystem = ActorSystem("shortener-service")

  lazy val shortenerActor = actorSystem.actorOf(Props(new ShortenerActor()),"shortener-actor")
}
