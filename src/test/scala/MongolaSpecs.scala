import org.specs2.mutable._
import org.mongola._

class Mongola extends SpecificationWithJUnit {

  "Mongola" should {

    val json = """
/** users indexes **/
db.getCollection("users").ensureIndex({
  "_id": 1
},[

]);

/** users records **/
db.getCollection("users").insert({
  "_id": ObjectId("4dd18c4ea975bf3405000000"),
  "last_name": "Smith",
  "location": [
    46.27921295166,
    6.9267511367798
  ]
});
db.getCollection("users").insert({
  "_id": ObjectId("4dd18cb4a975bfed14000000"),
  "last_name": "Türkben",
  "location": [
    46.27921295166,
    7.9267511367798
  ]
});
      """

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
  }
}
