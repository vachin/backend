package Models

import play.api.libs.json.Json

/**
  * Created by kiran on 18/6/17.
  */
case class CategoryModel( _id: String, name: String)

object CategoryModel {
  implicit val CategoryModel = Json.format[CategoryModel]
}
