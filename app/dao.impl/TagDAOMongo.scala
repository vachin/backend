package dao.impl

import Models.TagModel
import dao.TagDAO
import org.slf4j.Logger
import play.api.libs.json.{JsObject, Json}
import reactivemongo.api.MongoConnection
import reactivemongo.play.json.collection.JSONCollection
import play.modules.reactivemongo.json._

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TagDAOMongo(val connection: MongoConnection, val dbName: String, val logger: Logger) extends  TagDAO {

  val vachinDB = connection.apply(dbName)

  val collectionName = "tags"

  val collection = vachinDB[JSONCollection](collectionName)

  def find(): Future[List[TagModel]] = {

    val query = Json.obj()
    collection.find(query).cursor[TagModel]().collect[List]()

  }

  def insert(tagModel: TagModel): Future[Boolean] = {

    val body = Json.toJson(tagModel).as[JsObject]
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

  def update(tagId: String, description: String): Future[Boolean] = {

    val query = Json.obj("_id" -> tagId)
    val updater = Json.obj("description" -> description)
    collection.update(query, updater, multi = true).map { lastError =>
      lastError.ok
    }

  }

  def delete(tagId: String): Future[Boolean] = {

    val query = Json.obj("_id" -> tagId)
    collection.remove(query).map { lastError =>
      lastError.ok
    }

  }

}
