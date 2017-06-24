package dao.impl

import Models.{TextModel, TextRequestModel}
import dao.TextDAO
import org.slf4j.Logger
import play.api.libs.json.{JsObject, Json}
import reactivemongo.api.{DefaultDB, MongoConnection, QueryOpts}
import reactivemongo.play.json.collection.JSONCollection
import play.modules.reactivemongo.json._

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TextDAOMongo(val connection: MongoConnection, val dbName: String, val logger: Logger) extends  TextDAO {

  val vachinDB = connection.apply(dbName)
  val collectionName = "texts"
  val collection = vachinDB[JSONCollection](collectionName)

  override def find(textId: String): Future[Option[TextModel]] = {

    val query =  Json.obj("_id" -> textId)
    collection.find(query).one[TextModel]

  }

  def find(tag: Option[String], version: Int, limit: Int): Future[List[TextModel]] = {

    val query =  if(tag.isDefined) Json.obj("tags" -> tag) else Json.obj()
    collection.find(query).options(QueryOpts(skipN = (version - 1) * limit)).cursor[TextModel]().collect[List](limit)

  }


  override def search(q: String, tag: Option[String]): Future[List[TextModel]] = {

    val query = tag match {
      case Some(data) => Json.obj("tag" -> data)
      case None => Json.obj()
    }
    val mainQuery = query ++ Json.obj("$text" -> Json.obj("$search" -> q))
    collection.find(mainQuery).cursor[TextModel]().collect[List]()

  }

  override def insert(textModel: TextModel): Future[Boolean] = {

    val body = Json.toJson(textModel).as[JsObject]
    collection.insert(body).map { lastError =>
      if (lastError.ok) {
        logger.info("insert - insert success")
        true
      } else {
        logger.error("insert - insert failed due to" + lastError.message)
        false
      }
    }

  }

  override def update(textId: String, textModel: TextModel): Future[Boolean] = {

    val query = Json.obj("_id" -> textId)
    val updater = Json.toJson(textModel).as[JsObject]
    collection.update(query, updater, multi = true).map { lastError =>
      lastError.ok
    }

  }

  override def updateLikes(textId: String): Future[Boolean] = {

    val query = Json.obj("_id" -> textId)
    val updater = Json.obj("$inc" -> Json.obj("likes" -> 1))
    collection.update(query, updater, multi = true).map { lastError =>
      lastError.ok
    }

  }

}