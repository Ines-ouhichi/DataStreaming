package util1

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction
import org.apache.flink.streaming.api.functions.source.SourceFunction.SourceContext
import org.json.{JSONArray, JSONException, JSONObject}

import java.io.{BufferedWriter, FileWriter}
import scala.collection.mutable.ArrayBuffer


class SpaceXApiSource extends RichParallelSourceFunction[LaunchEvent] {

  @volatile private var running = true

  override def run(ctx: SourceContext[LaunchEvent]): Unit = {
    while (running) {
      fetchSpaceXLaunches().foreach(ctx.collect)
      Thread.sleep(1000)
    }
  }

  override def cancel(): Unit = {
    running = false
  }

  private def fetchSpaceXLaunches(): Seq[LaunchEvent] = {
    val apiUrl = "https://api.spacexdata.com/v4/launches"
    val request = HttpRequest.newBuilder()
      .uri(URI.create(s"$apiUrl?limit=5")) // You can adjust the limit as needed
      .timeout(Duration.ofSeconds(10))
      .build()

    val client = HttpClient.newHttpClient()
    val response = client.send(request, HttpResponse.BodyHandlers.ofString())

    val launches = parseLaunches(response.body)
    launches.foreach(println)

    Seq.empty[LaunchEvent]
  }

  private def parseLaunches(json: String): Seq[LaunchEvent] = {
    val jsonArray = new JSONArray(json)
    (0 until jsonArray.length()).map { i =>
      val jsonObject = jsonArray.getJSONObject(i)

      val success = try {
        Some(jsonObject.getBoolean("success"))
      } catch {
        case e: JSONException =>
          // Handle the case where "success" is not a boolean or does not exist
          //println(s"Error parsing 'success' field: ${e.getMessage}")
          None
      }

      val landingType = try {
        Option(jsonObject.getString("landing_type"))
      } catch {
        case e: JSONException =>
          // Handle the case where "landing_type" is not a string or does not exist
          //println(s"Error parsing 'landing_type' field: ${e.getMessage}")
          None
      }

      LaunchEvent(
        name = jsonObject.getString("name"),
        date_utc = jsonObject.getString("date_utc"),
        rocket = jsonObject.getString("rocket"),
        landing_type = landingType,
        success = success
      )
    }
  }




  private def parseRocket(jsonRocket: JSONObject): Rocket = {
    try {
      Rocket(
        name = jsonRocket.getString("name"),
        `type` = jsonRocket.getString("type")
      )
    } catch {
      case e: JSONException =>
        // Handle the case where "name" or "type" fields are not present or not strings
        println(s"Error parsing 'rocket' field: ${e.getMessage}")
        Rocket(name = "", `type` = "")
    }
  }


}



