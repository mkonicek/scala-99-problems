package net.martinkonicek.p99

import scala.collection.mutable.ArrayBuffer

class Segment(val cells: List[Cell]) {
	
	def this(cells: Iterable[Cell]) = this(cells.toList)
	
	val intersections: ArrayBuffer[Intersection] = ArrayBuffer[Intersection]()
	
	def addIntersection(index: Int, otherSegment: Segment) = {
		intersections += new Intersection(this, index, otherSegment)
	}

	def length() = cells.length
}