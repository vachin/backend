package models

import play.api.libs.json.Json

/**
  * Created by jyothi on 24/6/17.
  */
case class UserModel(
               user: String,
               name: String,
               realm: Option[String], //moderator etc
               moderated: Int, //moderated text count
               texts: List[String] //texts he entered for site
               )

object UserModel {

  implicit val userModel = Json.format[UserModel]

}

