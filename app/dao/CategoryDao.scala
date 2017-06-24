package dao

import Models.CategoryModel

import scala.concurrent.Future

/**
  * Created by kiran on 18/6/17.
  */
trait CategoryDao {

  def getCategories(): Future[List[CategoryModel]]

}
