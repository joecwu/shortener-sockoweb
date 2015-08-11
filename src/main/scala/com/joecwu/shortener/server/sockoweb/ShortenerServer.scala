package com.joecwu.shortener.server.sockoweb

import akka.actor._
import com.joecwu.shortener.server.sockoweb.actor.ActorPool._
import com.joecwu.shortener.server.sockoweb.actor._
import org.mashupbots.socko.events.HttpResponseStatus._
import org.mashupbots.socko.infrastructure.Logger
import org.mashupbots.socko.routes._
import org.mashupbots.socko.webserver._

/**
 * Created by Joe_Wu on 8/10/15.
 */
object ShortenerServer extends App with Logger {

  val routes = Routes({
    case HttpRequest(httpRequest) => {
      httpRequest match {
        case GET(Path("/shorter")) => shortenerActor ! ShorterRequestEvent(httpRequest)
        case GET(PathSegments("shorter" :: shorter :: Nil)) => shortenerActor ! TallerRequestEvent(httpRequest,shorter)
        case _ => httpRequest.response.write(NOT_FOUND)
      }
    }
  })

  val webServer = new WebServer(new WebServerConfig(actorSystem.settings.config, "shortener-server.socko-server"), routes, actorSystem)
  webServer.start()

  Runtime.getRuntime.addShutdownHook(new Thread {
    override def run { webServer.stop() }
  })

}
