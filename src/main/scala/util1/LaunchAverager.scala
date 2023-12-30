package util1

import org.apache.flink.streaming.api.scala.function.WindowFunction
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector
import org.apache.flink.api.common.functions.MapFunction
import org.slf4j.{Logger, LoggerFactory}

class LaunchAverager extends WindowFunction[LaunchEvent, (String, Long), String, TimeWindow] {
  private val logger: Logger = LoggerFactory.getLogger(classOf[LaunchAverager])

  override def apply(
                      key: String,
                      window: TimeWindow,
                      input: Iterable[LaunchEvent],
                      out: Collector[(String, Long)]): Unit = {

    val count = input.size

    logger.info(s"Key: $key, Window: ${window.getStart}-${window.getEnd}, Count: $count")

    println(s"Key: $key, Window: ${window.getStart}-${window.getEnd}, Count: $count")
    out.collect((key, count))
  }
}
