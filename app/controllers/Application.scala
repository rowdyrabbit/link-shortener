package controllers

import play.api.mvc._
import play.api.libs.json.{JsString, Json}
import services.Shortener
import scala.concurrent._

object Application extends Controller {
  implicit val context = scala.concurrent.ExecutionContext.Implicits.global

  def index = Action {BadRequest("Not Found") }

  def shorten = Action.async { implicit request =>
    val json = request.body.asJson.get
    val urlToShorten = (json \ "url").as[JsString].value

    future {
      Ok(Json.obj("shortenedUrl" -> ("http://localhost:9000/" + Shortener.shortenUrl(urlToShorten))))
    }
  }

  def redirect(id: String) = Action.async { implicit request =>
    //look up the url based on the id, if we get something, then redirect, else 404
      future {
        val url = Shortener.getUrl(id)
        url match {
          case Some(urlStr) => Redirect(urlStr)
          case _ => BadRequest("URL Not Found.")
        }
    }
  }

  def preflight(all: String) = Action {
    Ok("").withHeaders("Access-Control-Allow-Origin" -> "*",
      "Allow" -> "*",
      "Access-Control-Allow-Methods" -> "POST, GET, PUT, DELETE, OPTIONS",
      "Access-Control-Allow-Headers" -> "Origin, X-Requested-With, Content-Type, Accept, Referrer, User-Agent");
  }



}