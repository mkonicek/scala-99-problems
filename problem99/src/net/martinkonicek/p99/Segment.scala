package net.martinkonicek.p99

import scala.collection.mutable.ArrayBuffer

class Segment(val cells: List[Cell]) {
	
	def this(cells: Iterable[Cell]) = this(cells.toList)
	
	val candidates: ArrayBuffer[String] = ArrayBuffer[String]()
	
	val intersections: ArrayBuffer[Intersection] = ArrayBuffer[Intersection]()

	def length() = cells.length
	
	def candidatesCount() = candidates.length
}