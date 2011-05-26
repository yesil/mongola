package org.mongola

import com.mongodb.Mongo


/**
 * User: Ilyas Stéphane Türkben
 */


trait MongolaDynamic extends Dynamic {
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

class DBCollection(val underlying: com.mongodb.DBCollection) {

  def find(args: Any*) = new DBCursor(underlying.find())
  
}

class DBCursor(val underlying: com.mongodb.DBCursor) extends Iterator[DBObject] {
	
	def hasNext = underlying.hasNext()

  def next = new DBObject(underlying.next())

  override def isEmpty = underlying.count() == 0
}

class DBObjectList(val underlying: java.util.Iterator[java.lang.Object]) extends Iterator[DBObject] {
 
	def hasNext = underlying.hasNext()

	def next = new DBObject(underlying.next())
}

object dummyIterator extends java.util.Iterator[java.lang.Object]{

	override def hasNext = false
  
  override def next = Nil
  
  override def remove = {}
}

class DBObject(val underlying: Any) extends Dynamic with Seq[DBObject]  {
  
  override def toString = underlying.toString

	override def iterator =  underlying match {
      case dblist: com.mongodb.BasicDBList => new DBObjectList(dblist.iterator)
      case _ => new DBObjectList(dummyIterator)
  }
	

  override def isEmpty = underlying match {
      case dblist: com.mongodb.BasicDBList => dblist.isEmpty
      case _ => true
  }
  
  def apply(i: Int) = underlying match {
      case dblist: com.mongodb.BasicDBList => new DBObject(dblist.get(i))
      case _ => this
  }
  
  override def length = underlying match {
      case dblist: com.mongodb.BasicDBList => dblist.size
      case _ => 1
  }
    
  def applyDynamic(method: String)(args: Any*): DBObject = {
  	if(!underlying.isInstanceOf[AnyRef]) return this
    underlying match {
    	case s: String => this
      case dbobject: com.mongodb.DBObject => 
				val field = dbobject.get(method)
				field match {        
					case dblist: com.mongodb.BasicDBList =>  						
					args.length match {
						case  0 => new DBObject(dblist)
						case  _ => new DBObject(dblist.get(args(0).toString))
					}
				case _ => new DBObject(field)
			}
		}
	}
}