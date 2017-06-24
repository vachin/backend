package dao.impl

import Models.{CategoryModel, MessageModel}
import dao.MessageDao
import org.slf4j.Logger
import play.api.libs.json.Json
import reactivemongo.api.{DefaultDB, MongoConnection, QueryOpts}
import reactivemongo.play.json._
import reactivemongo.bson.BSONNull
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class MessageDaoMongo(val connection: MongoConnection, val logger: Logger) extends  MessageDao {

  def getMessages(categoryId: String, count: Int, page: Int): Future[List[MessageModel]] = {

    val vachinDB = connection.apply("vachin")
    val query =  Json.obj("category_id" -> categoryId)

    val collection = vachinDB[JSONCollection]("messages")

    collection.find(query).options(QueryOpts(skipN =((page-1) *count))).cursor[MessageModel]().collect[List](count)
  }

  def searchMessage(q: String, categoryId: Option[String]): Future[List[MessageModel]] = {
    val vachinDB = connection.apply("vachin")
    val query = categoryId match {
      case Some(data) => Json.obj("category_id" -> data)
      case None => Json.obj()
    }

    val collection = vachinDB[JSONCollection]("messages")

    val mainQuery = query ++ (Json.obj("text" -> Json.obj("$regex" -> q)))

    println("main query is", mainQuery)

    collection.find(mainQuery).cursor[MessageModel]().collect[List]()
  }

  def updateMessageUsedCount(categoryId: String, messageId: String): Future[Boolean] = {
    val vachinDB = connection.apply("vachin")
    val collection = vachinDB[JSONCollection]("messages")

    val query = Json.obj("_id" -> messageId)
    val updater = Json.obj("$inc" -> Json.obj("count" -> 1))
    collection.update(query, updater, multi = true).map { lastError =>
      lastError.ok
    }

  }

}