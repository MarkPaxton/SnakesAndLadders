package uk.gov.sample.snakes_and_ladders.models

import play.api.libs.json.Json

case class Token(square: Int, robot: Boolean) {
  def moveOnBoard(board: Board, distance: Int): Token = {
    if (board.size >= square + distance) {
      var newSquare = square + distance
      val squareContents = board.contents.filter(_.start == newSquare)
      if (squareContents.nonEmpty) {
        newSquare = squareContents.head.end
      }
      return copy(newSquare)
    } else {
      return copy()
    }
  }
}

object Token {
  implicit val format = Json.format[Token]

  def apply(): Token = {
    Token(0, false)
  }

  def apply(robot: Boolean): Token = {
    Token(0, robot)
  }
}