package controllers

import models.{TagRequestModel, TextRequestModel}
import org.slf4j.Logger
import play.api.libs.json.{JsError, JsSuccess, Json}
import play.api.mvc._
import services.ApiDataService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class APIController(dataService: ApiDataService, logger: Logger) extends Controller {

  def isThere() = Action {
    Ok("Version 0.0.1")
  }

  def getTags = Action.async {
    dataService.getTags().map(result =>
      Ok(Json.toJson(result)).withHeaders("access-control-allow-origin" -> "*")
    )
  }

  def getTag(tagId: String) = Action.async {
    dataService.findTag(tagId).map(result =>
      if(result.isDefined)
        Ok(Json.toJson(result.get)).withHeaders("access-control-allow-origin" -> "*")
      else
        NoContent.withHeaders("access-control-allow-origin" -> "*")
    )
  }

  def getTagCounts(version: Option[Int], limit: Option[Int]) = Action.async {
    dataService.findTagsWithCount(version, limit).map(result =>
      Ok(Json.toJson(result)).withHeaders("access-control-allow-origin" -> "*")
    )
  }

  def insertTag() = Action.async(parse.json) { implicit request =>
    val tagRequestModelOpt = request.body.validate[TagRequestModel]
    val tagRequestModel = tagRequestModelOpt match {
      case error: JsError => logger.warn("Errors: " + JsError.toJson(error)); None
      case model: JsSuccess[_] => Some(model.get.asInstanceOf[TagRequestModel])
    }
    if(tagRequestModel.isDefined) {
      dataService.insertTag(tagRequestModel.get).map(result =>
        Ok(Json.toJson(result)).withHeaders("access-control-allow-origin" -> "*")
      )
    }else{
      Future(BadRequest.withHeaders("access-control-allow-origin" -> "*"))
    }
  }

  def searchTags(q: String) = Action.async {
    dataService.searchTags(q).map(result =>
      Ok(Json.toJson(result)).withHeaders("access-control-allow-origin" -> "*")
    )
  }

  def updateTag(tag: String) = Action.async(parse.json) { implicit request =>
    val description = (request.body \ "description").validate[String].asOpt
    if(description.isDefined) {
      dataService.updateTag(tag, description.get).map(result =>
        Ok(Json.toJson(result)).withHeaders("access-control-allow-origin" -> "*")
      )
    }else{
      Future(BadRequest.withHeaders("access-control-allow-origin" -> "*"))
    }
  }

  def deleteTag(tag: String) = Action.async {
    dataService.deleteTag(tag).map(result =>
      Ok(Json.toJson(result)).withHeaders("access-control-allow-origin" -> "*")
    )
  }

  def getText(textId: String) = Action.async {
    dataService.findText(textId).map(result =>
      if(result.isDefined)
        Ok(Json.toJson(result.get)).withHeaders("access-control-allow-origin" -> "*")
      else
        NoContent.withHeaders("access-control-allow-origin" -> "*")
    )
  }

  def getTexts(tag: Option[String], version: Option[Int], limit: Option[Int]) = Action.async {
    dataService.findTexts(tag, version.getOrElse(1), limit.getOrElse(10)).map(result =>
      Ok(Json.toJson(result)).withHeaders("access-control-allow-origin" -> "*")
    )
  }

  def searchTexts(q: String, tag: Option[String], version: Option[Int], limit: Option[Int], words: Option[String]) = Action.async {
    dataService.searchTexts(q, tag, version.getOrElse(1), limit.getOrElse(10), words).map(result =>
      Ok(Json.toJson(result)).withHeaders("access-control-allow-origin" -> "*")
    )
  }

  def insertText() = Action.async(parse.json) { implicit request =>
    val textRequestModelOpt = request.body.validate[TextRequestModel]
    val textRequestModel = textRequestModelOpt match {
      case error: JsError => logger.warn("Errors: " + JsError.toJson(error)); None
      case model: JsSuccess[_] => Some(model.get.asInstanceOf[TextRequestModel])
    }
    if(textRequestModel.isDefined) {
      dataService.insertText(textRequestModel.get).map(result =>
        Ok(Json.toJson(result)).withHeaders("access-control-allow-origin" -> "*")
      )
    }else{
      Future(BadRequest.withHeaders("access-control-allow-origin" -> "*"))
    }
  }

  def updateText(textId: String) = Action.async(parse.json) { implicit request =>
    val textRequestModelOpt = request.body.validate[TextRequestModel]
    val textRequestModel = textRequestModelOpt match {
      case error: JsError => logger.warn("Errors: " + JsError.toJson(error)); None
      case model: JsSuccess[_] => Some(model.get.asInstanceOf[TextRequestModel])
    }
    if(textRequestModel.isDefined) {
      dataService.updateText(textId, textRequestModel.get).map(result =>
        Ok(Json.toJson(result)).withHeaders("access-control-allow-origin" -> "*")
      )
    }else{
      Future(BadRequest.withHeaders("access-control-allow-origin" -> "*"))
    }
  }

  def options(path: String) = Action { request =>
    Ok.withHeaders(ACCESS_CONTROL_ALLOW_HEADERS -> Seq(AUTHORIZATION, CONTENT_TYPE, "Target-URL").mkString(","))
      .withHeaders("access-control-allow-origin" -> "*" ).withHeaders("access-control-allow-origin" -> "*")
  }


}