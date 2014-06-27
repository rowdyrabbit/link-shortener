import filters.CorsFilter
import play.api.{Application, GlobalSettings}
import play.api.mvc.WithFilters

object Global extends WithFilters(new CorsFilter) with GlobalSettings {

}
