package dao

import Models.{TagModel}

import scala.concurrent.Future

/**
  * Created by jyothi on 24/6/17.
  */
trait TagDAO {

  def find(): Future[List[TagModel]]

  def insert(tagModel: TagModel): Future[Boolean]

  def update(tagId: String, description: String): Future[Boolean]

  def delete(tagId: String): Future[Boolean]

}
