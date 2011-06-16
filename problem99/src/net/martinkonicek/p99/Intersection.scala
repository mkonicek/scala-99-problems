package net.martinkonicek.p99

/** Intersection of two Segments. */
class Intersection(
    val cell: Cell, val owner: Segment, val pos: Int, val target: Segment) {
	
  	def isEmpty = cell.isEmpty
  	
  	def char = cell.char
}