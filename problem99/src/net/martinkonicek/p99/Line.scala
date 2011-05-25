package net.martinkonicek.p99
import scala.collection.mutable.ArrayBuffer

class Line {
	
	val cells:ArrayBuffer[Cell] = ArrayBuffer[Cell]()
	
	val candidates:ArrayBuffer[String] = ArrayBuffer[String]()
	
	val intersections:ArrayBuffer[Intersection] = ArrayBuffer[Intersection]()

	def length() = chars.length
	
	def candidatesCount() = candidates.length
	
//	def this(cell : Cell) = {
//		chars = ArrayBuffer(cell)
//	}
}