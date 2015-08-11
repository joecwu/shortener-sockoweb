package com.joecwu.shortener.server.sockoweb

import akka.actor._
import com.joecwu.shortener.db.memory.MemoryDBClient
import com.joecwu.shortener.exception._
import org.mashupbots.socko.events.HttpResponseStatus._
import org.mashupbots.socko.infrastructure.Logger
import org.mashupbots.socko.routes._
import com.joecwu.shortener.hash.Shortener._
import org.mashupbots.socko.webserver._
import org.scalactic._

/**
 * Created by Joe_Wu on 8/10/15.
 */
object ShortenerServer extends App with Logger {
  val actorSystem = ActorSystem("ShortenerActorSystem")
  implicit val dbClient = MemoryDBClient

  val routes = Routes({
    case HttpRequest(httpRequest) => {
      httpRequest match {
        case GET(Path("/shorter")) => {
          implicit val traceInfo = TracerInfo(httpRequest.request.headers.get("Tracer-Id").getOrElse(java.util.UUID.randomUUID.toString))
          httpRequest.request.endPoint.getQueryString("url").map { url =>
            shorter(url) match {
              case Good(short) => httpRequest.response.write(OK, short)
              case Bad(ex) => ex.writeLog(); httpRequest.response.write(BAD_REQUEST)
            }
          }
        }
        case GET(PathSegments("shorter" :: shorter :: Nil)) => {
          implicit val traceInfo = TracerInfo(httpRequest.request.headers.get("Tracer-Id").getOrElse(java.util.UUID.randomUUID.toString))
          taller(shorter) match {
            case Good(url) => httpRequest.response.write(OK, url)
            case Bad(ex) => {
              ex match {
                case ShorterNotInDBException(_,_) => httpRequest.response.write(NOT_FOUND)
                case _ => ex.writeLog(); httpRequest.response.write(BAD_REQUEST)
              }
            }
          }
        }
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
