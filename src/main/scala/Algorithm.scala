import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object Algorithm {


  def main(args: Array[String]) = {

    fizzBuzz(1,100,s => println(s));
    fizzBuzz(1,100,s => {});

    //method taking functions as parameter
    {
      val b = new Box(1)
      b.printMsg("Hello")
      b.update(i => i + 5)
      b.printMsg("Hello")
      println()

      b.update(increment)
      b.update(x => increment(x))
      b.update{x => increment(x)}
      b.update(increment(_))
      b.printMsg("result: ")
    }

  }

  def fizzBuzz(start: Int, end: Int,handleLine: String => Unit) = {
    for (i <- Range.inclusive(start, end)) {
      handleLine(
        if (i % 3 == 0 && i % 5 == 0) "FizzBuzz"
        else if (i % 3 == 0) "Fizz"
        else if (i % 5 == 0) "Buzz"
        else i.toString
      )
    }
  }

  class Box(var x: Int) {
    def update(f: Int => Int) = x = f(x)
    def printMsg(msg: String) = {
      println(msg + x)
    }
  }

  def increment(i: Int) = i + 1
}