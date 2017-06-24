package  services
import Models.{TagModel, TagRequestModel, TextModel, TextRequestModel}
import dao.{TagDAO, TextDAO}
import dao.impl.{TagDAOMongo, TextDAOMongo}
import org.slf4j.Logger
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class ApiDataService(textDao: TextDAO, tagDao: TagDAO, logger: Logger) {

  def getTags() = {
    tagDao.find()
  }

  def findTag(tag: String) = {
    tagDao.find(tag)
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

  def findText(textId: String) = {
    textDao.find(textId)
  }

  def findTexts(tagId: Option[String], version: Int = 1, limit: Int = 20) = {
    textDao.find(tagId, version, limit)
  }

  def searchTexts(q: String, tag: Option[String]) = {
    textDao.search(q, tag)
  }

  def insertText(textRequestModel: TextRequestModel) = {
    val stripLength = if(textRequestModel.text.length > 100) 100 else textRequestModel.text.length
    val textId = textRequestModel.text.replace(" ", "_").substring(0, stripLength)
    textDao.find(textId).flatMap(someTextModel => {
      if(someTextModel.isDefined){
        Future{
          false
        }
      }else{
        val textModel = TextModel(textId, textRequestModel.text, 0, textRequestModel.by, textRequestModel.tags)
        textDao.insert(textModel).map(inserted =>
          inserted
        )
      }
    })
  }



}