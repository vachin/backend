import controllers.APIController
import dao.impl.{TagDAOMongo, TextDAOMongo}
import services.ApiDataService
import org.slf4j.LoggerFactory
import play.api.{ApplicationLoader, BuiltInComponentsFromContext, LoggerConfigurator}
import play.api.ApplicationLoader.Context
import play.api._
import play.api.libs.ws.ahc.AhcWSComponents
import reactivemongo.api._

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.Try
import scala.concurrent.Future
import router.Routes
import scala.concurrent.ExecutionContext.Implicits.global._

class VachinApplicationLoader extends ApplicationLoader {
  def load(context: Context) = {
    LoggerConfigurator(context.environment.classLoader).foreach{
      _.configure(context.environment)
    }
    new ApiComponents(context).application
  }
}

class ApiComponents(context: Context) extends BuiltInComponentsFromContext(context)  with AhcWSComponents {

  val mongoURI = configuration.getString("mongo.db.uri").get
  val dbName = configuration.getString("mongo.db.name").get

  def tryConnection(driver: reactivemongo.api.MongoDriver): Try[MongoConnection] =
    MongoConnection.parseURI(mongoURI).map { parsedUri =>
      driver.connection(parsedUri)
    }


  val driver = new MongoDriver

  val connection = tryConnection(driver).get
  implicit val ec = materializer.executionContext
  //removing database connnection after application stopped
  applicationLifecycle.addStopHook(() =>
    Future(connection.close())
  )

  val customStrategy =
    FailoverStrategy(
      initialDelay = 500.milliseconds,
      retries = 5,
      delayFactor =
        attemptNumber => 1 + attemptNumber * 0.5
    )


  lazy val logger = LoggerFactory.getLogger("VachinLogger")

  lazy val apiDataService = new ApiDataService(new TextDAOMongo(connection, dbName, logger), new TagDAOMongo(connection, dbName, logger), logger)

  lazy val apiController = new APIController(apiDataService, logger)

  lazy val apiRoutesVersion1 = new v1.Routes(httpErrorHandler, apiController)

  lazy val router = new Routes(httpErrorHandler, apiRoutesVersion1)

}
