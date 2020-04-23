package controllers

import javax.inject._
import play.api.mvc._

@Singleton
class RegistrationController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def registrationUser = Action {
    Ok(views.html.index("This is a register action!"))
  }

}