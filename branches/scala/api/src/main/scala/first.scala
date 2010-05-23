object first extends Application {
	println("Hello World!")
	val names = Array("John", "Mike","Alex")
	for (name <- names) {
		println(name)	 	  
	}
	names.foreach(println)  
	val towns = List("London","Paris","Odessa")
	towns.foreach(println)
	val map = Map("hey"->"Hey")
	if (map.contains("jack")) println(map("jack"))
			
			
}
