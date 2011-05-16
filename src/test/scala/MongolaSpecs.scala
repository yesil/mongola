import org.specs2.mutable._
import org.mongola._
import com.mongodb.DBCursor

class Mongola extends SpecificationWithJUnit {
  lazy val db = DyDB("mongola")
  implicit def Value2Double(str: MongolaDynamic) = 10d
  /*
  "MongoDB" should {
    "connect" ! {
      val m = new Mongo()
      val db = m.getDB("hemmen")
      db.getLastError().getException() must beNull
    }
  }
  */

  "Mongola" should {
    "execute db.users.find()" in {
      val result = db.users.find()
      while (result.hasNext) {
        val item = result.next
        println("%s %d,%d".format(item.last_name, item.location(0), item.location(0)))
      }
      result must not be empty
    }

    "execute db.users.find({'last_name': 'Smith'}) with scala Map" in {
      val result = db.users.find(Map("last_name" -> "Smith"))
      result must not be empty
    }
  }
}
