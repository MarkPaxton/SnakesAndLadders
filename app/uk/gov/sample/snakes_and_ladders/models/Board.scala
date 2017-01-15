package uk.gov.sample.snakes_and_ladders.models

/**
  * Created by mark on 14/01/17.
  */
case class Board(size:Int, contents:Seq[Any])


object Board {
  def apply(): Board = {
    Board(100, Seq.empty)
  }
}
