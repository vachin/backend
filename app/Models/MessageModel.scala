package Models

import play.api.libs.json.Json

/**
  * Created by kiran on 18/6/17.
  */
case class MessageModel(
                       _id: String,
                       category_id: Option[String],
                       category_name: Option[String],
                       text: String,
                       count: Int,
                       time: String,
                       language: Option[String]
                       )


object MessageModel {
  implicit val messageModelFormat = Json.format[MessageModel]
}