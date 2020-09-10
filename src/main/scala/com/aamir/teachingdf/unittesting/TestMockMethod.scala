package teachingdf.unittesting

trait TestMockMethod {
  def method1(name: String): String
  def method2(name: String, i: Int): String
}
class TestMockMethodImpl extends TestMockMethod  {
  def method1(str: String) = {
    println("-----------------shuldn't come here------------------")
    ""
  }

  def method2(str: String, i: Int) = {
    println("-----------------inside method2------------------")

    val res = method1(str)
    println(res + "----")
    val x = 10
    res
  }
}

