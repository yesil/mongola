import org.specs2.mutable._
import org.mongola._

class Mongola extends SpecificationWithJUnit {

  "Mongola" should {
  	
  	// see people.json

    lazy val db = DB("mongola")

    "execute db.users.find()" in {
      val result = db.users.find()
      while (result.hasNext) {
        val item = result.next
        println("%s %s,%s".format(item.last_name, item.location(0), item.location(1)))
      }
      result must not be empty
    }

    "execute db.users.find({'last_name': 'Smith'}) with scala Map" in {
      val result = db.users.find(Map("last_name" -> "Smith"))
      result must not be empty
    }
    
     "support map function on collections" in {
      val result = db.users.find(Map("last_name" -> "Türkben"))
     	val person = result.next
    	val mappedList = person.addresses.map(item => item.street)
 	  	println(mappedList)
 	  	mappedList must not be empty
    } 
  }
}
