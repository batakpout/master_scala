object question4 extends App {

  val nums: List[Int] = List(1,2,3,4)

  def f(num:Int,nums:List[Int]) = nums.flatMap(List.fill(num)(_))

  println(f( num = 3, nums))

}
