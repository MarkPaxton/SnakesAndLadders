
package uk.gov.sample.snakes_and_ladders.models

import uk.gov.hmrc.play.test.UnitSpec
import org.scalatest.mock.MockitoSugar

class GameSpec extends UnitSpec with MockitoSugar  {

  "Game" should {
    "start with Tokens on square 1" in {
      val board = Game(mock[Board], Seq.fill(3)(Token(0)))

      board.start.tokens.filter(_.square==1).size shouldBe 3
    }

  }


}
