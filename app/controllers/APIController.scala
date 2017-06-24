package controllers

import org.slf4j.Logger
import play.api.libs.json.Json
import play.api.mvc._
import services.ApiDataService
import scala.concurrent.ExecutionContext.Implicits.global


class APIController(dataService: ApiDataService, logger: Logger) extends Controller {

  def isThere() = Action {
    Ok("Version 0.0.1")
  }

  /*def getCategories() = Action.async {
    dataService.getCategories().map { result =>
      Ok(Json.toJson(result)).withHeaders("access-control-allow-origin" -> "*")
    }
  }

  def getMessages(categoryId: String, count: Int, page: Int) = Action.async {
    dataService.getMessages(categoryId, count, page)   .map { result =>
      Ok(Json.toJson(result)).withHeaders("access-control-allow-origin" -> "*")
    }
  }

  def searchMessage(q: String, categoryId: Option[String]) = Action.async {
    dataService.searchMessage(q, categoryId) map { result =>
      Ok(Json.toJson(result)).withHeaders("access-control-allow-origin" -> "*")
    }
  }

  def updateMessageUsedCount(categoryId: String, messageId: String) = Action.async {
    dataService.updateMessageUsedCount( categoryId, messageId) map { result =>
      Ok(Json.toJson(result)).withHeaders("access-control-allow-origin" -> "*")
    }
  }*/



  def options(path: String) = Action { request =>
    Ok.withHeaders(ACCESS_CONTROL_ALLOW_HEADERS -> Seq(AUTHORIZATION, CONTENT_TYPE, "Target-URL").mkString(","))
      .withHeaders("access-control-allow-origin" -> "*" ).withHeaders("access-control-allow-origin" -> "*")
  }


}