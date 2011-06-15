package net.martinkonicek.p99

import scala.collection.mutable.ArrayBuffer

object Crossword {
	def loadFromFile(fileName:String) = {
		new Loader(fileName).load()
	}
}

class Crossword(words:List[String], segments:List[Segment], matrix:Array[Array[Cell]]) {
	
	def this(words:Iterable[String], segs:Iterable[Segment], matrix:Array[Array[Cell]]) = 
		this(words.toList, segs.toList, matrix)
	
	def print() = {
		var i = 97
		for (val s <- segments) {
			s.cells.foreach(c => c.char = i.toChar)
			s.intersections.foreach(t => t.owner.cells(t.ownerPos).char = 'x')
			i += 1
		}
		matrix.foreach(matrixLine =>
			println(matrixLine.map(cell => if (cell == null) " " else cell.char).mkString)
		)
	}
}