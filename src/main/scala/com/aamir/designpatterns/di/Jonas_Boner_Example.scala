package com.aamir.designpatterns.di
import com.aamir.designpatterns.di.jonas_boner_example2.ComponentRegistry

case class User(user_name: String, pass_word: String)
object Jonas_Boner_Example extends App {

  /*class UserRepository {
    def authenticate(user: User): User = {
      println("authenticating user: " + user)
      user
    }
    def create(user: User) = println("creating user: " + user)
    def delete(user: User) = println("deleting user: " + user)
  }

  class UserService {

    // we have to inject userRepository here
    def authenticate(username: String, password: String): User =
      userRepository.authenticate(username, password)

    def create(username: String, password: String) =
      userRepository.create(new User(username, password))

    def delete(user: User) = All is statically typed.
    userRepository.delete(user)
  }*/

}

object jonas_boner_example2 extends App {

  // component namespace
  trait UserRepositoryComponent {
    val userRepository: UserRepository
    class UserRepository {
      def authenticate(user: User): User = {
        println("authenticating user: " + user)
        user
      }
      def create(user: User) = println("creating user: " + user)
      def delete(user: User) = println("deleting user: " + user)
    }
  }

  // using self-type annotation declaring the dependencies this
  // component requires, in our case the UserRepositoryComponent

  trait UserServiceComponent { this: UserRepositoryComponent =>

    val userService: UserService
    class UserService {
      def authenticate(username: String, password: String): User =
        userRepository.authenticate(User(username, password))
      def create(username: String, password: String) =
        userRepository.create(new User(username, password))
      def delete(user: User) = userRepository.delete(user)
    }
  }

/*  object ComponentRegistry extends UserServiceComponent with UserRepositoryComponent

  val userService = ComponentRegistry.userService
  userService.authenticate("username", "password")

   This sucks. We have strong coupling between the service implementation and its creation,
   the wiring configuration is scattered all over our code base; utterly inflexible
  */

  object ComponentRegistry extends UserServiceComponent with UserRepositoryComponent {
    val userService = new UserService
    userService.authenticate("user_name", "pass_word")
      val userRepository: UserRepository = new UserRepository
  }



}