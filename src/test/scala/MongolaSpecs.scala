import org.specs2.mutable._
import org.mongola._
import com.mongodb.DBCursor

class Mongola extends SpecificationWithJUnit {
/*
  "MongoDB" should {
    "connect" ! {
      val m = new Mongo()
      val db = m.getDB("hemmen")
      db.getLastError().getException() must beNull
    }
  }
  */

  "MongoDB" should {
    "execute db.hemmen.find command" in {
      val db = DyDB("hemmen")
       val result = db.ads.find()
      while(result.hasNext){
        val item = result.next
        println(item.name)
        println(item.location(1))
      }
      true must beTrue
    }
  }
}
