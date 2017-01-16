package uk.gov.sample.snakes_and_ladders.models

/**
  * Created by mark on 15/01/17.
  */
case class Game(board:Board, tokens:Seq[Token], currentToken: Int) {
  private val random = scala.util.Random

  def start:Game = {
    return Game(this.board, tokens.map(c => c.copy(1)), 1)
  }

  def startWith(token:Int):Game = {
    return Game(this.board, tokens.map(c => c.copy(1)), token)
  }

  def rollDice: Int = {
    return random.nextInt(6)+1
  }

  def moveCurrentToken(distance:Int):Game = {
    val newTokens = tokens.patch(currentToken-1, Seq(tokens(currentToken-1).moveOnBoard(board, distance)), 1)
    return copy(tokens = newTokens, currentToken = (currentToken % tokens.length)+1)
  }

  def winner: Option[Int] = {
    return tokens.filter(_.square==board.size) match {
      case Seq(finished:Token, _*) => Some(tokens.indexOf(finished)+1)
      case Seq() => None
    }
  }
}
