package devinsideyou

object SubRoutine extends App {

  FarAway.code(false, true)
}

object FarAway {

  def code: (Boolean, Boolean) => Unit = {
    (first, second) =>

      println {
        if (first) "yeah sunny weather"
        else if (second) "yeah funny weather"
        else "inclement weather"
      }
  }
}

