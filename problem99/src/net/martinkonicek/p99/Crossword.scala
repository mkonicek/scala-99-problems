package net.martinkonicek.p99

object Crossword {
	/** Reads the Crossword from an input file (see the 'input' folder for examples). */
	def loadFromFile(fileName:String) = {
		new Loader(fileName).load()
	}
}

/** Represents the input and output. */
class Crossword(val words:List[String], val segments:List[Segment], matrix:Array[Array[Cell]]) {
	
	def this(words: Iterable[String], segs: Iterable[Segment], matrix: Array[Array[Cell]]) = 
		this(words.toList, segs.toList, matrix)
	
	/** Prints this Crossword to standard output, in the 'input file' format. */
	def print() = {
		matrix.foreach(matrixLine =>
			println(matrixLine.map(cell => if (cell == null) " " else cell.char).mkString)
		)
	}
}