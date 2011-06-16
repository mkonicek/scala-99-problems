package net.martinkonicek.p99

import scala.collection.mutable.ArrayBuffer

/** Represents one line in the crossword to put a word in. */
class Segment(val cells: List[Cell]) {
	
	def this(cells: Iterable[Cell]) = this(cells.toList)
	
	/** Length of this Segment. */
	def length = cells.length
	
	/** String content of this Segment (empty Cells represented by dots). */
	def word = cells.map(_.char).mkString
	
	/** Places a word into this Segment. */
	def setWord(word: String) = {
		for ((cell, char) <- cells.view.zip(word))
			cell.char = char
	}
	
	/** All Intersections of this Segment. */
	val intersections: ArrayBuffer[Intersection] = ArrayBuffer[Intersection]()
	
	/** The Segments this Segment intersects. */
	def intersectingSegments = intersections.map(_.target)
	
	/** Declares that this Segment and @param otherSegment intersect. */
	def addIntersection(index: Int, otherSegment: Segment) = {
		val cell = this.cells(index)
		intersections += new Intersection(cell, this, index, otherSegment)
	}
}