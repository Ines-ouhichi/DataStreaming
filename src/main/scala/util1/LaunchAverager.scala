package util1

import org.apache.flink.streaming.api.scala.function.WindowFunction
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

class LaunchAverager extends WindowFunction[LaunchEvent, (String, Long), String, TimeWindow] {
  override def apply(
                      key: String,
                      window: TimeWindow,
                      input: Iterable[LaunchEvent],
                      out: Collector[(String, Long)]): Unit = {

    val count = input.size
    out.collect((key, count))
  }
}
