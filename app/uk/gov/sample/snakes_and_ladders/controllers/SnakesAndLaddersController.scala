package uk.gov.sample.snakes_and_ladders.controllers

import uk.gov.hmrc.play.frontend.controller.FrontendController
import play.api.mvc._

import scala.concurrent.Future
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import uk.gov.hmrc.selfservicetimetopay.util.JacksonMapper
import uk.gov.sample.snakes_and_ladders.views.html.game.{game, index, winner}
import uk.gov.sample.snakes_and_ladders.models._
import scala.util.control.Exception.catching

object SnakesAndLaddersController extends SnakesAndLaddersController

trait SnakesAndLaddersController extends FrontendController {

  val game_index = Action.async { implicit request =>
    Future.successful(Ok(index()))
  }

  val game_start = (players:Int) => Action.async { implicit request =>
    var currentGame = Game(Board(),  List.fill(players)(Token(0)), 0)
    currentGame = currentGame.start()
    Future.successful(Ok(game(currentGame, None)).withNewSession.addingToSession(
      "game" -> JacksonMapper.writeValueAsString(currentGame)
    ))
  }

  val roll = Action.async { implicit request =>
    val gameOption = request.session.get("game").fold[Option[Game]](None) { gameJson =>
      catching(classOf[Exception]) opt JacksonMapper.readValue(gameJson, classOf[Game])
    }
    Future.successful(gameOption.fold(Redirect(routes.SnakesAndLaddersController.game_index())) { currentGame =>
      Ok(game(currentGame, Some(currentGame.rollDice)))
    })

  }

  val move = (distance:Int) => Action.async { implicit request =>
    val gameOption = request.session.get("game").fold[Option[Game]](None) { gameJson =>
      catching(classOf[Exception]) opt JacksonMapper.readValue(gameJson, classOf[Game])
    }
    Future.successful(gameOption.fold(Redirect(routes.SnakesAndLaddersController.game_index())) { currentGame =>
      val nextGame = currentGame.moveCurrentToken(distance)
      nextGame.winner.fold(Ok(game(nextGame, None)).addingToSession(
        "game" -> JacksonMapper.writeValueAsString(nextGame)
      )){ i =>
        Ok(winner(nextGame, currentGame.currentToken))
      }
    })
  }
}