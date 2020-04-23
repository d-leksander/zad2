package controllers

import javax.inject.Inject
import play.api.mvc.{AbstractController, ControllerComponents}
import models.{User, UserRepository}
import models.{Admin, AdminRepository}

import scala.concurrent.ExecutionContext

class UserController @Inject()(userRepository: UserRepository, adminRepository: AdminRepository, cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {


  def createUser = Action {
    Ok(views.html.index("createUser action"))
  }
  def createAdmin = Action {
    Ok(views.html.index("createAdmin action"))
  }

  def allUsers(id: Int) = Action.async { implicit request =>
    val us = userRepository.list()
    us.map(us => {
      Ok(s"Users: $us")
    })
  }
  def adminList(id: Int) = Action.async { implicit request =>
    val admin = adminRepository.list()
    admin.map(admin => {
      Ok(s"AdminsList: $admin")
    })
  }

  def updateUser(id: Int) = Action {
    Ok(views.html.index("This is  updateUser action"))
  }
  def deleteUser()(id: Int) = Action {
    Ok(views.html.index("This is deleteUser action"))
  }

}
