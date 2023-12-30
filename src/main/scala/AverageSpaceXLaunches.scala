package main.scala

import java.text.SimpleDateFormat
import java.util.Calendar
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.sink.SinkFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.WindowFunction
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector
import java.io.{BufferedWriter, FileWriter}
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._

import java.io.{File, FileWriter}
//LaunchAverager,

import org.apache.flink.api.common.serialization.SimpleStringEncoder
import org.apache.flink.core.fs.Path
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink
import org.apache.flink.streaming.api.functions.sink.filesystem.bucketassigners.DateTimeBucketAssigner

import util1.{ LaunchEvent, LaunchTimestampAssigner, SpaceXApiSource}

class CsvLaunchEventSink(filePath: String) extends SinkFunction[LaunchEvent] {
  override def invoke(value: LaunchEvent): Unit = {
    val writer = new FileWriter(filePath, true)  // Open the file in append mode
    try {
      val absolutePath = new File(filePath).getAbsolutePath
      println(s"Writing to CSV file: $absolutePath")
      writer.write(s"${value.name},${value.date_utc},${value.rocket},${value.landing_type.getOrElse("None")},${value.success.getOrElse("None")}\n")
    } finally {
      writer.close()
    }
  }

}

object AverageSpaceXLaunches {

  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env.getConfig.setAutoWatermarkInterval(1000L)

    val spaceXLaunches: DataStream[LaunchEvent] = env
      .addSource(new SpaceXApiSource)
      .assignTimestampsAndWatermarks(new LaunchTimestampAssigner)


    //val avgLaunchesPerMinute: DataStream[(String, Long)] = spaceXLaunches
      //.keyBy(_.name)
      //.timeWindow(Time.minutes(1))
      //.apply(new LaunchAverager)


    //avgLaunchesPerMinute.print("Average Launches Per Minute")

    spaceXLaunches.print("Raw Events")



    env.execute("collecting launch data")
  }


}


