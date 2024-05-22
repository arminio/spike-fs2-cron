package com.example

import cats.effect.IOApp
import cats.effect.IO

object Main extends IOApp.Simple {

  // This is your new "main"!
  def run: IO[Unit] = {

    //    import cats.effect.IO

    import cats.effect.unsafe.implicits.global
    import fs2.Stream
    import java.time.LocalTime

    val printTime = Stream.eval(IO(println(LocalTime.now)))
    HelloWorld.say().flatMap(IO.println) *> printTime.compile.drain
  }

}
