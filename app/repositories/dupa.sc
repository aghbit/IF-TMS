//Map example
val list = "latowszedzie".toList
val listUpper = list.map(c => c.toUpper)

//Foreach example
list.foreach(c => println(c))
//Flatmap example
val list1 = list.map(c => Some(c)) ::: List(None, None, None)
list1.flatMap(o=>o)
list1.flatten
//Flatmap example
val list2 = List(1,2,3,4,5)
def g(int: Int):List[Int] = List(int-1, int, int+1)
list2.map(i => g(i))
list2.flatMap(i => g(i))
//Collect example
val list3 = List("l", "a", "t", "o", 2, 0, 1, 5)
val list4 = list3.collect {case s:String => s}
val list5 = list3.collect {case i:Int => i}
//Filter example
val list6 = List(1,2,3,4,5,6,7,8,9)
list6.filter(i=> i>4)
//GroupBy example
list6.groupBy(i => i%2)

//Fold Rigth example
val list7 = List(1,2,3,4)
list7.foldRight(0)((i, j)=> i+j)
list7.foldRight(1)((i,j)=> i*j)
list7.foldRight(List[Int]())((i,j)=> j ::: List(i))
list7.foldLeft(List[Int]())((j,i)=> j ::: List(i))




