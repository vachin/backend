package Models

/**
  * Created by jyothi on 24/6/17.
  */
class UserModel(
               username: String,
               password: String,
               realm: Option[String], //moderator etc
               moderated: Int, //moderated text count
               texts: List[String] //texts he entered for site
               )
