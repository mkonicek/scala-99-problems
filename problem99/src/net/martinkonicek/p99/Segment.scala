package net.martinkonicek.p99

import scala.collection.mutable.ArrayBuffer

class Segment(val cells: List[Cell]) {
	
	def this(cells: Iterable[Cell]) = this(cells.toList)
	
	def length = cells.length
	
	def word = cells.map(_.char).mkString
	
	val intersections: ArrayBuffer[Intersection] = ArrayBuffer[Intersection]()
	
	def intersectingSites = intersections.map(_.target)
	
	def addIntersection(index: Int, otherSegment: Segment) = {
		val cell = this.cells(index)
		intersections += new Intersection(cell, this, index, otherSegment)
	}
}