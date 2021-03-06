package  services
import models.{TagModel, TagRequestModel, TextModel, TextRequestModel}
import dao.{TagDAO, TextDAO}
import dao.impl.{TagDAOMongo, TextDAOMongo}
import org.slf4j.Logger
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class ApiDataService(textDao: TextDAO, tagDao: TagDAO, logger: Logger) {

  def getTags() = {
    textDao.findTags(None, None) //FIXME: not from here
    //tagDao.find()
  }

  def findTag(tag: String) = {
    tagDao.find(tag)
  }

  def findTagsWithCount(version: Option[Int], limit: Option[Int]) = {
    textDao.findTagsWithCount(version, limit)
  }

  def searchTags(q: String) = {
    tagDao.search(q)
  }

  def insertTag(tagRequestModel: TagRequestModel) = {
    val tagId = tagRequestModel.name.replace(" ", "-").toLowerCase
    tagDao.insert(TagModel(tagId, tagRequestModel.name, tagRequestModel.description))
  }

  def deleteTag(tagId: String) = {
    tagDao.delete(tagId)
  }

  def updateTag(tagId: String, description: String) = {
    tagDao.update(tagId, description)
  }

  def updateViews(textId: String, viewCount: Int = 1) = {
    textDao.updateViews(textId, viewCount)
  }

  def findText(textId: String) = {
    updateViews(textId)
    textDao.find(textId)
  }

  def findTexts(tagId: Option[String], version: Int = 1, limit: Int = 20) = {
    textDao.find(tagId, version, limit)
  }

  def searchTexts(q: String, tag: Option[String], version: Int, limit: Int, words: Option[String]) = {
    textDao.search(q, tag, version, limit, words)
  }

  def insertText(textRequestModel: TextRequestModel) = {
    val textId = TextRequestModel.getTextId(textRequestModel.text)
    textDao.find(textId).flatMap(someTextModel => {
      if(someTextModel.isDefined){
        Future("")
      }else{
        textDao.insert(TextRequestModel.toTextModel(textRequestModel)).map(inserted =>
          if(inserted){
            textId
          }else{
            ""
          }
        )
      }
    })
  }

  def updateText(textId: String, textRequestModel: TextRequestModel) = {
    val textModel = TextRequestModel.toTextModel(textRequestModel)
    textDao.update(textId, textModel).map(inserted =>
      if(inserted){
        textModel._id
      }else{
        ""
      }
    )
  }

}