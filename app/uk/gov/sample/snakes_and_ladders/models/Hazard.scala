package uk.gov.sample.snakes_and_ladders.models

import play.api.libs.json.Json

case class Hazard(start: Int, end: Int)

object Hazard {
  implicit val format = Json.format[Hazard]
}
