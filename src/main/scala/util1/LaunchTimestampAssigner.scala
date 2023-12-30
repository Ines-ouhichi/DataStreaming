package util1

import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks
import org.apache.flink.streaming.api.watermark.Watermark

import java.text.SimpleDateFormat
import java.util.{Calendar, TimeZone}

class LaunchTimestampAssigner extends AssignerWithPeriodicWatermarks[LaunchEvent] {
  private val maxOutOfOrderness = 1000L // 1 second

  override def extractTimestamp(element: LaunchEvent, previousElementTimestamp: Long): Long = {
    val dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"))
    val date = dateFormat.parse(element.date_utc)
    date.getTime}

  override def getCurrentWatermark(): Watermark = {
    new Watermark(Calendar.getInstance().getTimeInMillis - maxOutOfOrderness)
  }
}
