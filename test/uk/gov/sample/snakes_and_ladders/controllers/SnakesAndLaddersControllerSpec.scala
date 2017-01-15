
package uk.gov.sample.snakes_and_ladders.controllers

import play.api.http.Status
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.play.test.{UnitSpec, WithFakeApplication}


class SnakesAndLaddersControllerSpec extends UnitSpec with WithFakeApplication{

  val fakeRequest = FakeRequest("GET", "/")


  "GET /" should {
    "return 200" in {
      val result = SnakesAndLaddersController.game_index(fakeRequest)
      status(result) shouldBe Status.OK
    }

    "return HTML" in {
      val result = SnakesAndLaddersController.game_index(fakeRequest)
      contentType(result) shouldBe Some("text/html")
      charset(result) shouldBe Some("utf-8")
    }


  }


}
