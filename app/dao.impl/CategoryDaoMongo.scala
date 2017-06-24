package dao.impl

import Models.CategoryModel
import dao.CategoryDao
import org.slf4j.Logger
import play.api.libs.json.Json
import reactivemongo.play.json.collection.JSONCollection
import reactivemongo.play.json._
import reactivemongo.api.DefaultDB
import reactivemongo.bson.BSONNull
import reactivemongo.api.MongoConnection
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by kiran on 18/6/17.
  */
class CategoryDaoMongo (val connection: MongoConnection, val logger: Logger) extends  CategoryDao{

  def getCategories(): Future[List[CategoryModel]] = {
    val categoryDB = connection.apply("vachin")
    val query = Json.obj()

    val collection = categoryDB[JSONCollection]("categories")

    collection.find(query).cursor[CategoryModel]().collect[List]()
  }

}
