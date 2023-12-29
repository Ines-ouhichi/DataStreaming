package util1

import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks
import org.apache.flink.streaming.api.watermark.Watermark

import java.util.Calendar

class LaunchTimestampAssigner extends AssignerWithPeriodicWatermarks[LaunchEvent] {
  private val maxOutOfOrderness = 1000L // 1 second

  override def extractTimestamp(element: LaunchEvent, previousElementTimestamp: Long): Long = {
    element.date_utc.toLong  }

  override def getCurrentWatermark(): Watermark = {
    new Watermark(Calendar.getInstance().getTimeInMillis - maxOutOfOrderness)
  }
}
