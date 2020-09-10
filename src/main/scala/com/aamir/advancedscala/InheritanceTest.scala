object IT extends App {

   def me(x: =>Nothing): Nothing = {
     Vector.empty.asInstanceOf[Nothing]
   }

  me(throw  new Exception)
}