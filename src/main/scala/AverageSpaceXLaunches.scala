package main.scala

import java.text.SimpleDateFormat
import java.util.Calendar

import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.WindowFunction
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector
import util1.{LaunchAverager, LaunchEvent, LaunchTimestampAssigner, SpaceXApiSource}

object AverageSpaceXLaunches {

  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env.getConfig.setAutoWatermarkInterval(1000L)

    val spaceXLaunches: DataStream[LaunchEvent] = env
      .addSource(new SpaceXApiSource)
      .assignTimestampsAndWatermarks(new LaunchTimestampAssigner)

    val avgLaunchesPerMinute: DataStream[(String, Long)] = spaceXLaunches
      .keyBy(_.name)
      .timeWindow(Time.minutes(1))
      .apply(new LaunchAverager)

    avgLaunchesPerMinute.print()

    env.execute("Compute average SpaceX launches per minute")
  }
}
