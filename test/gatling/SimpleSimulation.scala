package gatling

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class SimpleSimulation extends Simulation {

  val httpConf = http
    .baseURL("http://localhost:9000") // Here is the root for all relative URLs
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // Here are the common headers
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  val headers_10 = Map("Content-Type" -> """application/x-www-form-urlencoded""") // Note the headers specific to a given request

  object Active {
    val active = exec(http("request_1")
      .get("/"))
      .pause(1)
  }

  val scn = scenario("SimpleScenario").exec(Active.active)
  val users = scenario("Users").exec(Active.active)


  setUp(
    scn.inject(atOnceUsers(100)).protocols(httpConf),
    users.inject(rampUsers(10000) over 10).protocols(httpConf)
  )
}