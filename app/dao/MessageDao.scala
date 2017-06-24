package  dao

import Models.{CategoryModel, MessageModel}

import scala.concurrent.Future

trait MessageDao {

  def getMessages(categoryId: String, count: Int, page: Int): Future[List[MessageModel]]

  def searchMessage(q: String, categoryId: Option[String]): Future[List[MessageModel]]

  def updateMessageUsedCount(categoryId: String, messageId: String): Future[Boolean]

}