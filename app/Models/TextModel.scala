package Models

import play.api.libs.json.Json

/**
  * Created by jyothi on 24/6/17.
  */
case class TextModel(
                       _id: String,
                       text: String,
                       likes: Int,
                       //language: Option[String],
                       by: Option[String],
                       tags: List[String]
                    )

object TextModel {
  implicit val textModelFormat = Json.format[TextModel]
}

case class TextRequestModel(
                      text: String,
                      //language: Option[String],
                      by: Option[String],
                      tags: List[String]
                    )

object TextRequestModel {
  implicit val textRequestModelFormat = Json.format[TextRequestModel]
}