package main.scala

import java.text.SimpleDateFormat
import java.util.Calendar

import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.WindowFunction
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector
//LaunchAverager,

import org.apache.flink.api.common.serialization.SimpleStringEncoder
import org.apache.flink.core.fs.Path
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink
import org.apache.flink.streaming.api.functions.sink.filesystem.bucketassigners.DateTimeBucketAssigner

import util1.{ LaunchEvent, LaunchTimestampAssigner, SpaceXApiSource}

object AverageSpaceXLaunches {

  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env.getConfig.setAutoWatermarkInterval(1000L)

    val spaceXLaunches: DataStream[LaunchEvent] = env
      .addSource(new SpaceXApiSource)
      .assignTimestampsAndWatermarks(new LaunchTimestampAssigner)

    spaceXLaunches.print("Raw Events")

    //val avgLaunchesPerMinute: DataStream[(String, Long)] = spaceXLaunches
      //.keyBy(_.name)
      //.timeWindow(Time.minutes(1))
      //.apply(new LaunchAverager)


    //avgLaunchesPerMinute.print("Average Launches Per Minute")

    spaceXLaunches.print("Raw Events")

    // Define the file sink path
    val outputPath = "C:/Users/inesl/Documents/output"


    // Create a StreamingFileSink for CSV output
    val fileSink: StreamingFileSink[String] = StreamingFileSink
      .forRowFormat(new Path(outputPath), new SimpleStringEncoder[String]("UTF-8"))
      .withBucketAssigner(new DateTimeBucketAssigner[String]("yyyy-MM-dd--HHmm"))
      .build()

    // Write the raw events to the CSV file using the file sink
    spaceXLaunches.map(_.toString).addSink(fileSink)


    env.execute("Display raw SpaceX launch events")
  }
}
