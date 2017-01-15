package uk.gov.sample.snakes_and_ladders.controllers

import uk.gov.hmrc.play.frontend.controller.FrontendController
import play.api.mvc._
import scala.concurrent.Future
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import uk.gov.sample.snakes_and_ladders.views.html.helloworld.hello_world


object SnakesAndLaddersController extends SnakesAndLaddersController

trait SnakesAndLaddersController extends FrontendController {

  val start: Action[AnyContent] = Action.async { implicit request =>
    Future.successful(Ok(hello_world()))
  }

}