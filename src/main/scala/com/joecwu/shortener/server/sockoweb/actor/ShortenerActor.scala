package com.joecwu.shortener.server.sockoweb.actor

import akka.actor.Actor
import com.joecwu.shortener.db.DBClient
import com.joecwu.shortener.db.memory.MemoryDBClient
import com.joecwu.shortener.exception.{BaseException, ShorterNotInDBException}
import com.joecwu.shortener.hash.Shortener._
import com.joecwu.shortener.server.sockoweb.util.RequestHandler._
import org.mashupbots.socko.events.HttpRequestEvent
import org.mashupbots.socko.events.HttpResponseStatus._
import org.mashupbots.socko.infrastructure.Logger
import org.scalactic._

import scalaz._

/**
 * Created by Joe_Wu on 8/11/15.
 */

case class ShorterRequestEvent(event: HttpRequestEvent)
case class TallerRequestEvent(event: HttpRequestEvent, shorter: String)

class ShortenerActor extends Actor with Logger {
  val dbClient = MemoryDBClient

  override def receive: Receive = {
    case ShorterRequestEvent(event) => {
      implicit val traceInfo = event.request.parseTracerInfo
      event.request.endPoint.getQueryString("url").map { url =>
        run(shorter(url)) match {
          case Good(short) => event.response.write(OK, short)
          case Bad(ex) => ex.writeLog(); event.response.write(BAD_REQUEST)
        }
      }
    }
    case TallerRequestEvent(event,shorter) => {
      implicit val traceInfo = event.request.parseTracerInfo
      run(taller(shorter)) match {
        case Good(url) => event.response.write(OK, url)
        case Bad(ex) => {
          ex match {
            case ShorterNotInDBException(_,_) => event.response.write(NOT_FOUND)
            case _ => ex.writeLog(); event.response.write(BAD_REQUEST)
          }
        }
      }
    }
  }

  def run[A](reader: Reader[DBClient,A Or BaseException]) : A Or BaseException = {
    reader(dbClient)
  }
}
