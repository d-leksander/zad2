package controllers

import javax.inject._
import play.api.mvc._

@Singleton
class LoginController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def  login = Action {
    Ok(views.html.index("This i s a login action!"))
  }

  def  logout = Action {
    Ok(views.html.index("This is a logged out action"))
  }

}
