package uk.gov.sample.snakes_and_ladders.models

/**
  * Created by mark on 14/01/17.
  */
case class Token(square: Int) {
  def moveOnBoard(board:Board, distance:Int):Token = {
    if(board.size>=square+distance) {
      var newSquare = square+distance
      val squareContents = board.contents.filter(_.start==newSquare)
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
  def apply(): Token = {
    Token(0)
  }
}