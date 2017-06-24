package  dao

import models.{TagWithCount, TextModel}

import scala.concurrent.Future

trait TextDAO {

  def find(textId: String): Future[Option[TextModel]]

  def find(tag: Option[String], version: Int, limit: Int): Future[List[TextModel]]

  def search(q: String, tag: Option[String]): Future[List[TextModel]]

  def insert(textModel: TextModel): Future[Boolean]

  def update(textId: String, textModel: TextModel): Future[Boolean]

  def updateLikes(textId: String): Future[Boolean]

  def findTagsWithCount(version: Int, limit: Int): Future[List[TagWithCount]]

}