
package uk.gov.sample.snakes_and_ladders.models

import org.scalatest.mock.MockitoSugar
import uk.gov.hmrc.play.test.UnitSpec

class TokenSpec extends UnitSpec with MockitoSugar  {

  "Token" should {
    "start move 3 spaces from square 1 to 4" in {
      val board = Board(100, Seq.empty)
      var counter = Token(1)
      counter = counter.moveOnBoard(board, 3)
      counter.square shouldBe 4
    }


    "start move 3 spaces from square 1 to 4 and then 4 spaces from 4 to 8" in {
      val board = Board(100, Seq.empty)
      var counter = Token(1)
      counter = counter.moveOnBoard(board, 3)
      counter.square shouldBe 4
      counter = counter.moveOnBoard(board, 4)
      counter.square shouldBe 8
    }
  }


}
