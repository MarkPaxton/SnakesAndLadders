package uk.gov.sample.snakes_and_ladders.models

/**
  * Created by mark on 14/01/17.
  */
case class Board(size:Int, contents:Seq[Hazard]) {
  private val random = scala.util.Random
  private def getRandom():Int = {
    random.nextInt(size-2)  + 1
  }

  def withSnake():Board = {
    val available = Range(2,size-1).filterNot(contents.map(_.start).toSet)
    if(contents.length<size-1 && available.nonEmpty) {
      val start = available(random.nextInt(available.length))
      val end = start - Math.max(1, random.nextInt(start-1))
      return copy(contents = contents :+ Hazard(start, end))
    } else {
      return copy()
    }
  }

  def withLadder():Board = {
    val available = Range(2,size-1).filterNot(contents.map(_.start).toSet)
    if(contents.length<size-1 && available.nonEmpty) {
      val start = available(random.nextInt(available.length))
      val end = start + Math.min(size-1, random.nextInt(size-start))
      return copy(contents = contents :+ Hazard(start, end))
    } else {
      return copy()
    }
  }
}


object Board {
  def apply(): Board = {
    Board(100, Seq.empty)
  }
}
