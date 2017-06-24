package  services
import Models.{TagModel, TextModel, TextRequestModel}
import dao.{TagDAO, TextDAO}
import dao.impl.{TagDAOMongo, TextDAOMongo}
import org.slf4j.Logger
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class ApiDataService(textDao: TextDAO, tagDao: TagDAO, logger: Logger) {

  def getTags() = {
    tagDao.find().map { tags =>
      Json.toJson(tags)
    }
  }

  def createTag(tagName: String, description: Option[String]) = {
    val tagId = tagName.replace(" ", "-").toLowerCase
    tagDao.insert(TagModel(tagId, tagName, description)).map( created =>
      Json.toJson(created)
    )
  }

  def deleteTag(tagId: String) = {
    tagDao.delete(tagId).map(deleted =>
      Json.toJson(deleted)
    )
  }

  def updateTag(tagId: String, description: String) = {
    tagDao.update(tagId, description).map(updated =>
      Json.toJson(updated)
    )
  }

  def findText(textId: String) = {
    textDao.find(textId).map(text =>
      Json.toJson(text)
    )
  }

  def searchTexts(tagId: Option[String], version: Int = 1, limit: Int = 20) = {
    textDao.find(tagId, version, limit).map(texts =>
      Json.toJson(texts)
    )
  }

  def insertText(textRequestModel: TextRequestModel) = {
    val stripLength = if(textRequestModel.text.length > 100) 100 else textRequestModel.text.length
    val textId = textRequestModel.text.replace(" ", "_").substring(0, stripLength)
    textDao.find(textId).flatMap(someTextModel => {
      if(someTextModel.isDefined){
        Future{
          Json.toJson(false)
        }
      }else{
        val textModel = TextModel(textId, textRequestModel.text, 0, textRequestModel.by, textRequestModel.tags)
        textDao.insert(textModel).map(inserted =>
          Json.toJson(inserted)
        )
      }
    })
  }



}