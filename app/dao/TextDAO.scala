package  dao

import models.{TagModel, TagWithCount, TextModel, TextPaginatedModel}

import scala.concurrent.Future

trait TextDAO {

  def find(textId: String): Future[Option[TextModel]]

  def find(tag: Option[String], version: Int, limit: Int): Future[TextPaginatedModel]

  def search(q: String, tag: Option[String], version: Int, limit: Int, words: Option[String]): Future[TextPaginatedModel]

  def insert(textModel: TextModel): Future[Boolean]

  def update(textId: String, textModel: TextModel): Future[Boolean]

  def updateViews(textId: String, viewCount: Int = 1): Future[Boolean]

  def findTagsWithCount(version: Option[Int], limit: Option[Int]): Future[List[TagWithCount]]

  def findTags(version: Option[Int], limit: Option[Int]): Future[List[TagModel]]

}