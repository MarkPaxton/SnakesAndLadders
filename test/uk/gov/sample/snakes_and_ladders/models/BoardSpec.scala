
package uk.gov.sample.snakes_and_ladders.models

import uk.gov.hmrc.play.test.{UnitSpec, WithFakeApplication}


class BoardSpec extends UnitSpec  {

  "Board" should {
    "have a default size of 100" in {
      val board = Board()
      board.size shouldBe 100
    }

    "add a valid Snake" in {
      val board = Board().withSnake()
      board.contents.length shouldBe 1

      board.contents.head.start should be > board.contents.head.end
      board.contents.head.start should be < 100
    }

    "add a valid Ladder" in {
      val board = Board().withLadder()
      board.contents.length shouldBe 1

      board.contents.head.start should be < board.contents.head.end
      board.contents.head.start should be > 1
    }

    "add no more than 99 Snakes or Ladders when adding Ladders" in {
      var board = Board()
      for(i <- Iterator.range(0, 120)) { board = board.withLadder() }
      board.contents.length should be <  99
    }

    "add no more than 99 Snakes or Ladders when adding Snakes" in {
      var board = Board()
      for(i <- Iterator.range(0, 120)) { board = board.withSnake() }
      board.contents.length should be <  99
    }
  }


}
