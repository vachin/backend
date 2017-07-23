package models

import play.api.libs.json.Json

/**
  * Created by jyothi on 24/6/17.
  */
case class TextModel(
                      _id: String,
                      text: String,
                      views: Int,
                      //language: Option[String],
                      tags: List[String],
                      by: Option[String],
                      user: Option[String],
                      verified: Option[Boolean]
                    )

object TextModel {
  implicit val textModelFormat = Json.format[TextModel]
}

case class TextRequestModel(
                      text: String,
                      tags: List[String],
                      by: Option[String],
                      user: Option[String],
                      verified: Option[Boolean]
                    )

object TextRequestModel {
  implicit val textRequestModelFormat = Json.format[TextRequestModel]

  def getTextId(text: String): String = {
    val strippedText = text.replaceAll("[^a-zA-Z\\d\\s:]", "").replaceAll(" ", "-").toLowerCase
    val stripLength = if(strippedText.length > 100) 100 else strippedText.length
    strippedText.substring(0, stripLength)
  }

  def toTextModel(textRequestModel: TextRequestModel): TextModel = {
    TextModel(getTextId(textRequestModel.text), textRequestModel.text, 0, textRequestModel.tags, textRequestModel.by, textRequestModel.user, None)
  }

}