
package uk.gov.sample.snakes_and_ladders.models

import uk.gov.hmrc.play.test.{UnitSpec, WithFakeApplication}


class BoardSpec extends UnitSpec  {

  "Board" should {
    "have a default size of 100" in {
      val board = Board()
      board.size shouldBe 100
    }

  }


}
