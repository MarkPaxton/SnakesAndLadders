package uk.gov.sample.snakes_and_ladders.controllers

import uk.gov.hmrc.play.frontend.controller.FrontendController
import play.api.mvc._

import scala.concurrent.Future
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import uk.gov.hmrc.selfservicetimetopay.util.JacksonMapper
import uk.gov.sample.snakes_and_ladders.views.html.game._
import uk.gov.sample.snakes_and_ladders.models._
import scala.util.control.Exception.catching

object SnakesAndLaddersController extends SnakesAndLaddersController

trait SnakesAndLaddersController extends FrontendController {

  val game_index = Action.async { implicit request =>
    Future.successful(Ok(index()))
  }

  val init_game = (players:Int) => Action.async { implicit request =>
    var board = Board()
    for(i <- 1 to 5) { board = board.withLadder().withSnake() }
    var game = Game(board,  Seq.fill(players)(Token(0)), 0)
    var initialRolls:Seq[Int] = Seq.fill(players)(0)

    Future.successful(Ok(initial(initialRolls)).withNewSession.addingToSession(
      "game" -> JacksonMapper.writeValueAsString(game),
      "initial_rolls" -> JacksonMapper.writeValueAsString(initialRolls)
    ))
  }

  val who_starts = Action.async { implicit request =>
    val rollsOption = request.session.get("initial_rolls").fold[Option[Seq[Int]]](None) { json =>
      catching(classOf[Exception]) opt JacksonMapper.readValue(json, classOf[Seq[Int]])
    }
    val gameOption = request.session.get("game").fold[Option[Game]](None) { gameJson =>
      catching(classOf[Exception]) opt JacksonMapper.readValue(gameJson, classOf[Game])
    }
    Future.successful((gameOption, rollsOption) match {
      // When there's initial rolls in the session then roll for the next player
      case (Some(game: Game), Some(rolls: Seq[Int])) => {
        val rollTurn = rolls.indexWhere(_==0)
        val newRolls = rolls.patch(rollTurn, Seq(game.rollDice), 1)
        Ok(initial(newRolls)).addingToSession(
          "game" -> JacksonMapper.writeValueAsString(game),
          "initial_rolls" -> JacksonMapper.writeValueAsString(newRolls)
        )
      }
      case _ => Redirect(routes.SnakesAndLaddersController.game_index()) //Anything else, go to start
    })
  }

  val game_start = Action.async { implicit request =>
    val gameOption = request.session.get("game").fold[Option[Game]](None) { gameJson =>
      catching(classOf[Exception]) opt JacksonMapper.readValue(gameJson, classOf[Game])
    }
    val rollsOption = request.session.get("initial_rolls").fold[Option[Seq[Int]]](None) { json =>
      catching(classOf[Exception]) opt JacksonMapper.readValue(json, classOf[Seq[Int]])
    }
    Future.successful(gameOption.fold(Redirect(routes.SnakesAndLaddersController.game_index())){ sessionGame =>
    val currentGame =  rollsOption.fold(sessionGame.start) { rolls => sessionGame.startWith(rolls.zipWithIndex.maxBy(_._1)._2+1)}
      Ok(game(currentGame, None)).addingToSession(
        "game" -> JacksonMapper.writeValueAsString(currentGame)
      )
    })
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