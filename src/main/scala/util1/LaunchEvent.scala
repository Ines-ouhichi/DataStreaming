package util1

import util1.Rocket

case class LaunchEvent(name: String, date_utc: String,rocket: String,landing_type:Option[String], success: Option[Boolean])

