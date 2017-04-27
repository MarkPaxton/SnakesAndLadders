
package uk.gov.sample.snakes_and_ladders.models

import uk.gov.hmrc.play.test.UnitSpec
import org.scalatest.mock.MockitoSugar

class GameSpec extends UnitSpec with MockitoSugar  {

  "Game" should {
    "start with Tokens on square 1" in {
      val game = Game(mock[Board], Seq.fill(3)(Token()), 0)

      game.start.tokens.filter(_.square==1).size shouldBe 3
    }

    "start with token 1's turn" in {
      val game = Game(mock[Board], Seq.fill(3)(Token()), 0)

      game.start.currentToken shouldBe 1
    }


    "move current turn around all tokens" in {
      var game = Game(Board(100, Seq.empty), Seq.fill(4)(Token()), 1).start
      game.currentToken shouldBe 1
      game = game.moveCurrentToken(1)
      game.currentToken shouldBe 2
      game = game.moveCurrentToken(1)
      game.currentToken shouldBe 3
      game = game.moveCurrentToken(1)
      game.currentToken shouldBe 4
      game = game.moveCurrentToken(1)
      game.currentToken shouldBe 1
      game = game.moveCurrentToken(1)
      game.currentToken shouldBe 2
    }

    "have dice rolls between 1 and 6" in {
      val game = Game(mock[Board], Seq.empty, 0)
      val rolls = Seq.fill(100)(game.rollDice)

      rolls.filter(r => ((r>0)&&(r<7))).size shouldBe 100
    }

    "move current token by 4 squares" in {
      var game = Game(Board(100, Seq.empty), Seq.fill(2)(Token()), 0).start
      game.currentToken shouldBe 1
      val turn = game.currentToken
      game = game.moveCurrentToken(4)
      game.tokens(turn-1).square shouldBe 5
      game.currentToken shouldBe 2
    }

    "be won when token on square 97 moves 3 squares" in {
      var game = Game(Board(100, Seq.empty), Seq.fill(2)(Token()), 0).start
      game.currentToken shouldBe 1
      game = game.moveCurrentToken(96)
      game = game.moveCurrentToken(9)
      game.tokens(0).square shouldBe 97
      game.tokens(1).square shouldBe 10
      game = game.moveCurrentToken(3)
      game.tokens(0).square shouldBe 100
      game.winner.get shouldBe 1
    }

    "not be won when token on square 97 can't move 4 squares" in {
      var game = Game(Board(100, Seq.empty), Seq(Token()), 0).start
      game.currentToken shouldBe 1
      game = game.moveCurrentToken(96)
      game.tokens(0).square shouldBe 97
      game = game.moveCurrentToken(4)
      game.tokens(0).square shouldBe 97
      game.winner shouldBe None
    }


  }


}
