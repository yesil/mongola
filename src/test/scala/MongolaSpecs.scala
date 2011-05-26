import org.specs2.mutable._
import org.mongola._

class Mongola extends SpecificationWithJUnit {

  "Mongola" should {
  	
  	// see people.json

    lazy val db = DB("mongola")

    "execute db.users.find()" in {
      val result = db.users.find()
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
 	  	mappedList must not be empty
    } 
    
      "support access by index" in {
      val result = db.users.find(Map("last_name" -> "Türkben"))
     	val person = result.next
    	val addresses = person.addresses
    	val firstAddress1 = addresses(0)
    	val firstAddress2 = person.addresses(0)
 	  	firstAddress1 must beEqualTo(firstAddress2)
 	  	firstAddress2 must not be equalTo(addresses)
 	  	firstAddress1.street.toString must beEqualTo("Edouard Herriot")
    } 
    
    "support x.y.z like access for inexisting property" in {
      val result = db.users.find(Map("last_name" -> "Türkben"))
     	val person = result.next
    	val last2digit = person.addresses(0).zip.last.two.digit
 	  	last2digit.toString must beEqualTo("")
    } 
  }
}
