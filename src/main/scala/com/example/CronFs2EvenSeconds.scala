package com.example

import cats.effect.{IO, IOApp}
import fs2.Stream

import java.time.LocalTime

object CronFs2EvenSeconds extends IOApp.Simple {

  import cron4s.Cron
  import eu.timepit.fs2cron.cron4s.Cron4sScheduler

  val cronScheduler = Cron4sScheduler.systemDefault[IO]
  // cronScheduler: eu.timepit.fs2cron.Scheduler[IO, cron4s.expr.CronExpr] = eu.timepit.fs2cron.cron4s.Cron4sScheduler$$anon$1@3b612fa5

  val printTime = Stream.eval(IO(println(LocalTime.now)))

  val evenSeconds = Cron.unsafeParse("*/2 * * ? * *")
  // evenSeconds: cron4s.package.CronExpr = CronExpr(
  //   seconds = */2,
  //   minutes = *,
  //   hours = *,
  //   daysOfMonth = ?,
  //   months = *,
  //   daysOfWeek = *
  // )

  val scheduled = cronScheduler.awakeEvery(evenSeconds) >> printTime
  // scheduled: Stream[[x]IO[x], Unit] = Stream(..)

  def run: IO[Unit] =
    scheduled.take(3).compile.drain
  // 22:34:46.145109392
  // 22:34:48.001058522
  // 22:34:50.001971813

}
