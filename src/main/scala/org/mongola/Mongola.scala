package org.mongola

import com.mongodb.{DBObject, DBCursor, DBCollection, Mongo}

/**
 * Created by IntelliJ IDEA.
 * User: ilyas
 * Date: 5/14/11
 * Time: 6:33 PM
 * To change this template use File | Settings | File Templates.
 */

trait MongolaDynamic extends Dynamic  {
  def applyDynamic(method:String)(args:Any*):MongolaDynamic = throw new Exception(method + " must be implemented !")
  def hasNext = false
  def next:MongolaDynamic = null
}

class DyDB(val db:String)(val m:Mongo = new Mongo()) extends MongolaDynamic {
  lazy val database = m.getDB(db)
  override def applyDynamic(method:String)(args:Any*):MongolaDynamic =
    method match {
    case _ => new DyCollection(database.getCollection(method))
  }
}

object DyDB {
  def apply(db:String) = new DyDB(db)(new Mongo)
}

class DyCollection(val col:DBCollection) extends MongolaDynamic  {
  override def applyDynamic(method:String)(args:Any*):MongolaDynamic = method match {
    case "find" => args match {
      case _=> new DyCursor(col.find())
    }
  }
}

class DyString(val v:String) extends MongolaDynamic {
 override def toString = v.toString
}

class DyObject(val dbo:DBObject) extends MongolaDynamic {
  override def applyDynamic(method:String)(args:Any*):MongolaDynamic = {
    val v = dbo.get(method)
    v match {
      case s:String => new DyString(s)
      case o:DBObject => args(0) match {
        case i:Int => val  iv = o.get(i.toString)
          iv match {
            case o:DBObject => new DyObject(o)
            case d:java.lang.Double => new DyString(d.toString)
            case _ => new DyString(iv.toString)
          }
        case _ => new DyObject(o)
      }
    }
  }
}

class DyCursor(val cur:DBCursor) extends MongolaDynamic with Iterator[DyObject] {
  override def hasNext = cur.hasNext
  override def next = new DyObject(cur.next)
}