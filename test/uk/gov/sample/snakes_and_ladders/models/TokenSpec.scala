
package uk.gov.sample.snakes_and_ladders.models

import org.scalatest.mock.MockitoSugar
import uk.gov.hmrc.play.test.UnitSpec

class TokenSpec extends UnitSpec with MockitoSugar  {

  "Token" should {
    "move 3 spaces from square 1 to 4" in {
      val board = Board(100, Seq.empty)
      var counter = Token(1, false)
      counter = counter.moveOnBoard(board, 3)
      counter.square shouldBe 4
    }

    "move 3 spaces from square 1 to 4 and then 4 spaces from 4 to 8" in {
      val board = Board(100, Seq.empty)
      var counter = Token(1, false)
      counter = counter.moveOnBoard(board, 3)
      counter.square shouldBe 4
      counter = counter.moveOnBoard(board, 4)
      counter.square shouldBe 8
    }
  }


  "move to square 2 when landing on a snake on square 12 ending on suqare 2" in {
    val board = Board(100, Seq(Hazard(12, 2)))
    var counter = Token(1, false)
    counter = counter.moveOnBoard(board, 11)
    counter.square shouldBe 2
  }

  "stay on square 2 when landing on a snake on square 12 ending on square 2" in {
    val board = Board(100, Seq(Hazard(12, 2)))
    var counter = Token(1, false)
    counter = counter.moveOnBoard(board, 1)
    counter.square shouldBe 2
  }


  "move to square 12 when landing on  on ladder on square 2 ending on suqare 12" in {
    val board = Board(100, Seq(Hazard(2, 12)))
    var counter = Token(1, false)
    counter = counter.moveOnBoard(board, 1)
    counter.square shouldBe 12
  }

  "stay on square 12 when landing on a ladder on square 2 ending on square 12" in {
    val board = Board(100, Seq(Hazard(2, 12)))
    var counter = Token(11, false)
    counter = counter.moveOnBoard(board, 1)
    counter.square shouldBe 12
  }
}
