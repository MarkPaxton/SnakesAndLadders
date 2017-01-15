package uk.gov.sample.snakes_and_ladders.models

/**
  * Created by mark on 15/01/17.
  */
case class Game(board:Board, tokens:Seq[Token]) {
  def start():Game = {
    return Game(this.board, tokens.map(c => c.copy(1)))
  }
}
