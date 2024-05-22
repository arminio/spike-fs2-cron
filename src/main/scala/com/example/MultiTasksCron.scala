package com.example

import cats.effect.{IO, IOApp}
import cron4s.Cron
import fs2.*

import java.time.LocalTime
object MultiTasksCron extends IOApp.Simple {

  val everyFiveSeconds = Cron.unsafeParse("*/5 * * ? * *")
  // everyFiveSeconds: cron4s.package.CronExpr = CronExpr(
  //   seconds = */5,
  //   minutes = *,
  //   hours = *,
  //   daysOfMonth = ?,
  //   months = *,
  //   daysOfWeek = *
  // )

  val scheduledTasks = CronFs2EvenSeconds.cronScheduler.schedule(
    List(
      CronFs2EvenSeconds.evenSeconds -> Stream.eval(
        IO(println(LocalTime.now.toString + " task 1"))
      ),
      everyFiveSeconds -> Stream.eval(
        IO(println(LocalTime.now.toString + " task 2"))
      )
    )
  )
  // scheduledTasks: Stream[IO, Unit] = Stream(..)

  override def run: IO[Unit] =
    scheduledTasks.take(9).compile.drain
  // 22:34:52.001721651 task 1
  // 22:34:54.001702294 task 1
  // 22:34:55.002032079 task 2
  // 22:34:56.000919733 task 1
  // 22:34:58.000939089 task 1
  // 22:35:00.001158852 task 1
  // 22:35:00.001743567 task 2
  // 22:35:02.001037743 task 1
  // 22:35:04.001006980 task 1
}
