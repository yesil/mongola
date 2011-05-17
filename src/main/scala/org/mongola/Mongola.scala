package org.mongola

import com.mongodb.Mongo


/**
 * Created by IntelliJ IDEA.
 * User: ilyas
 * Date: 5/14/11
 * Time: 6:33 PM
 * To change this template use File | Settings | File Templates.
 */


trait MongolaDynamic extends Dynamic {
  def hasNext: Boolean

  def next: DBObject

  def isEmpty: Boolean
}

class DB(val db: String)(val m: Mongo = new Mongo()) extends Dynamic {
  lazy val database = m.getDB(db)

  def applyDynamic(method: String)(args: Any*) =
    method match {
      case _ => new DBCollection(database.getCollection(method))
    }
}

object DB {
  def apply(db: String) = new DB(db)(new Mongo)
}

class DBCollection(val underlying: com.mongodb.DBCollection) extends Dynamic {

  def applyDynamic(method: String)(args: Any*): MongolaDynamic = method match {
    case "find" => args match {
      case _ => new DBCursor(underlying.find())
    }
  }
}


class DBObject(val underlying: Any) extends Dynamic {
  override def toString = underlying.toString

  def applyDynamic(method: String)(args: Any*): DBObject = {
    underlying match {
      case o: com.mongodb.DBObject =>
        val v = o.get(method)
        v match {
          case s: String => new DBObject(s)
          case o: com.mongodb.DBObject => args(0) match {
            case i: Int => val iv = o.get(i.toString)
            iv match {
              case o: com.mongodb.DBObject => new DBObject(o)
              case _ => new DBObject(iv)
            }
            case _ => new DBObject(o)
          }
        }
      case _ => this
    }
  }
}

class DBCursor(val underlying: com.mongodb.DBCursor) extends Iterator[DBObject] with MongolaDynamic {
  override def hasNext = underlying.hasNext()

  override def next = new DBObject(underlying.next())

  override def isEmpty = underlying.count() == 0
}