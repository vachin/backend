package Models

import play.api.libs.json.Json

/**
  * Created by jyothi on 24/6/17.
  */
case class TagModel( _id: String, name: String, description: Option[String])

object TagModel {
  implicit val TagModel = Json.format[TagModel]
}
