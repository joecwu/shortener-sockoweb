package com.joecwu.shortener.server.sockoweb.util

import com.joecwu.shortener.exception.TracerInfo
import com.joecwu.shortener.server.sockoweb.Constants._
import org.mashupbots.socko.events.HttpRequestMessage

/**
 * Created by Joe_Wu on 8/11/15.
 */
class RequestHandler(request:HttpRequestMessage) {
  def parseTracerInfo = TracerInfo(request.headers.get(HTTP_HEADER_TRACER_ID).getOrElse(java.util.UUID.randomUUID.toString))
}

object RequestHandler {
  implicit def RequestHandler(request:HttpRequestMessage) = new RequestHandler(request)
}