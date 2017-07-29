package dao.impl

import models._
import dao.TextDAO
import org.slf4j.Logger
import play.api.libs.json.{JsArray, JsObject, JsValue, Json}
import reactivemongo.api.{DefaultDB, MongoConnection, QueryOpts}
import reactivemongo.play.json.collection.JSONCollection
import play.modules.reactivemongo.json._
import reactivemongo.api.commands.Command
import reactivemongo.bson.BSONString
import reactivemongo.play.json.JSONSerializationPack

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

  import reactivemongo.api.commands.bson.BSONCountCommand.{ Count, CountResult }

  def find(tag: Option[String], version: Int, limit: Int): Future[TextPaginatedModel] = {

    val query =  if(tag.isDefined) Json.obj("tags" -> tag) else Json.obj()

    collection.runCommand(Count(query)).flatMap(result => {
      collection.find(query).options(QueryOpts(skipN = (version - 1) * limit)).cursor[TextModel]().collect[List](limit).map(data =>
        TextPaginatedModel(
          PaginationModel(limit, version, result.count),
          data
        )
      )
    })

  }


  override def search(q: String, tag: Option[String], version: Int, limit: Int): Future[TextPaginatedModel] = {

    val query = tag match {
      case Some(data) => Json.obj("tag" -> data)
      case None => Json.obj()
    }
    val mainQuery = query ++ Json.obj("text" -> Json.obj("$regex" -> q))

    collection.runCommand(Count(mainQuery)).flatMap(result => {
      collection.find(mainQuery).options(QueryOpts(skipN = (version - 1) * limit)).cursor[TextModel]().collect[List](limit).map(data =>
        TextPaginatedModel(
          PaginationModel(limit, version, result.count),
          data
        )
      )
    })

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

  override def updateViews(textId: String, viewCount: Int = 1): Future[Boolean] = {

    val query = Json.obj("_id" -> textId)
    val updater = Json.obj("$inc" -> Json.obj("views" -> viewCount))
    collection.update(query, updater, multi = true).map { lastError =>
      lastError.ok
    }

  }

  override def findTagsWithCount(version: Int, limit: Int): Future[List[TagWithCount]] = {

    val runner = Command.run(JSONSerializationPack)

    val command = Json.obj("aggregate" -> collectionName,
      "pipeline" -> JsArray(List(
        Json.obj("$unwind" -> "$tags"),
        Json.obj("$group" -> Json.obj("_id" -> "$tags", "count" -> Json.obj("$sum" -> 1))),
        Json.obj("$sort" -> Json.obj("count" -> -1))
      ))
    )

    runner.apply(vachinDB, runner.rawCommand(command)).one[JsValue].map { mapper =>
      (mapper \ "result").as[List[JsValue]].map { group =>
        val name = (group \ "_id").as[String]
        val count = (group \ "count").as[Int]
        new TagWithCount(name, None, count)
      }
    }

  }

}