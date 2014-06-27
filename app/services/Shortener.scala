package services

import org.sedis._
import redis.clients.jedis._
import play.Play

object Shortener {

  val pool = new Pool(new JedisPool(
                new JedisPoolConfig(),
                Play.application().configuration().getString("redis.host"),
                Play.application().configuration().getInt("redis.port"),
                Play.application().configuration().getInt("redis.timeout")))

  def shortenUrl(url: String): String = {
    val key = encodeUrl(url)
    pool.withClient { client =>
      client.set(key, url)
    }
    key
  }

  def getUrl(key: String): Option[String] = {
    pool.withClient { client =>
      client.get(key)
    }
  }

  private def encodeUrl(url: String): String = {

    var total = 0
    for (char <- url.toCharArray) {
      total += char.toInt
    }

    BigInt(total).toString(36)
  }

}
