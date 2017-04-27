package uk.gov.sample.snakes_and_ladders.controllers

import play.api.libs.json._
import play.api.mvc._
import uk.gov.hmrc.play.frontend.controller.FrontendController
import uk.gov.sample.snakes_and_ladders.models._
import uk.gov.sample.snakes_and_ladders.views.html.game._
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import scala.concurrent.Future

object SnakesAndLaddersController extends SnakesAndLaddersController

trait SnakesAndLaddersController extends FrontendController {

  val game_index = Action.async { implicit request =>
    Future.successful(Ok(index()))
  }

  val init_game = (players:Int, robots:Int) => Action.async { implicit request =>
    var board = Board()
    for(i <- 1 to 5) { board = board.withLadder().withSnake() }
    var game = Game(board,  Seq.fill(players)(Token()) ++ Seq.fill(robots)(Token(true)), 0)
    var initialRolls:Seq[Int] = Seq.fill(players)(0) ++ Seq.tabulate(robots){_ => game.rollDice}

    Future.successful(Ok(initial(game, initialRolls)).withNewSession.addingToSession(
      "game" -> Json.toJson(game).toString,
      "initial_rolls" -> Json.toJson(initialRolls).toString
    ))
  }

  val who_starts = Action.async { implicit request =>
    val rollsOption = request.session.get("initial_rolls").fold[Option[Seq[Int]]](None) { json =>
      Json.parse(json).asOpt[Seq[Int]]
    }
    val gameOption = request.session.get("game").fold[Option[Game]](None) { gameJson =>
      Json.parse(gameJson).asOpt[Game]
    }
    Future.successful((gameOption, rollsOption) match {
      // When there's initial rolls in the session then roll for the next player
      case (Some(game: Game), Some(rolls: Seq[Int])) => {
        val rollTurn = rolls.indexWhere(_==0)
        val newRolls = rolls.patch(rollTurn, Seq(game.rollDice), 1)
        Ok(initial(game, newRolls)).addingToSession(
          "game" -> Json.toJson(game).toString,
          "initial_rolls" -> Json.toJson(newRolls).toString
        )
      }
      case _ => Redirect(routes.SnakesAndLaddersController.game_index()) //Anything else, go to start
    })
  }

  val game_start = Action.async { implicit request =>
    val gameOption = request.session.get("game").fold[Option[Game]](None) { gameJson =>
      Json.parse(gameJson).asOpt[Game]
    }
    val rollsOption = request.session.get("initial_rolls").fold[Option[Seq[Int]]](None) { json =>
     Json.parse(json).asOpt[Seq[Int]]
    }
    Future.successful(gameOption.fold(Redirect(routes.SnakesAndLaddersController.game_index())){ sessionGame =>
    val currentGame =  rollsOption.fold(sessionGame.start) { rolls => sessionGame.startWith(rolls.zipWithIndex.maxBy(_._1)._2+1)}
      Ok(game(currentGame, None)).addingToSession(
        "game" -> Json.toJson(currentGame).toString
      )
    })
  }


  val roll = Action.async { implicit request =>
    val gameOption = request.session.get("game").fold[Option[Game]](None) { gameJson =>
      Json.parse(gameJson).asOpt[Game]
    }
    Future.successful(gameOption.fold(Redirect(routes.SnakesAndLaddersController.game_index())) { currentGame =>
      Ok(game(currentGame, Some(currentGame.rollDice)))
    })

  }

  val move = (distance:Int) => Action.async { implicit request =>
    val gameOption = request.session.get("game").fold[Option[Game]](None) { gameJson =>
      Json.parse(gameJson).asOpt[Game]
    }
    Future.successful(gameOption.fold(Redirect(routes.SnakesAndLaddersController.game_index())) { currentGame =>
      var nextGame = currentGame.moveCurrentToken(distance)
      //If this is a robot player, then just do the move
      if(nextGame.tokens(nextGame.currentToken-1).robot) {
        nextGame = nextGame.moveCurrentToken(nextGame.rollDice)
      }
      nextGame.winner.fold(Ok(game(nextGame, None)).addingToSession(
        "game" -> Json.toJson(nextGame).toString
      )){ i =>
        Ok(winner(nextGame, currentGame.currentToken))
      }
    })
  }

}