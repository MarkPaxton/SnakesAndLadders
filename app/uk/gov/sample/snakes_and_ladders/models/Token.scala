package uk.gov.sample.snakes_and_ladders.models

/**
  * Created by mark on 14/01/17.
  */
case class Token(square: Int) {
  def moveOnBoard(board:Board, distance:Int):Token = {
    if(board.size>=square+distance) {
      return copy(square+distance)
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