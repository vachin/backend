package  services
import dao.impl.{CategoryDaoMongo, MessageDaoMongo}
import org.slf4j.Logger
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global


class ApiDataService( messageDao: MessageDaoMongo, categoryDao: CategoryDaoMongo, logger: Logger) {

  def getCategories() = {
    categoryDao.getCategories() map { model =>
      Json.toJson(model)
    }
  }

  def getMessages(categoryId: String, count: Int, page: Int) = {
    messageDao.getMessages(categoryId, count, page) map { allMessages =>
     Json.toJson(allMessages)
    }
  }


  def searchMessage(q: String, categoryId: Option[String]) = {
    messageDao.searchMessage(q, categoryId) map { searchMessages =>
      Json.toJson(searchMessages)
    }
  }

  def updateMessageUsedCount(categoryId: String, messageId: String) = {
    messageDao.updateMessageUsedCount(categoryId, messageId).map { response =>
      Json.toJson(response)
    }
  }



}