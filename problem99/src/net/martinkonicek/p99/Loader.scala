package net.martinkonicek.p99

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.ListBuffer
import scala.collection._

import scala.io._

/** Reads the Crossword from input file. */
class Loader(fileName:String) {
    
    /** Reads the Crossword from input file. */
	def load(): Crossword = {
		val fileLines = Source.fromFile(fileName).getLines
		val words = fileLines.takeWhile(_ != "").toList
		val matrix = readPlan(fileLines)
		val lines = buildLines(matrix)
		
		println("words: " + (words mkString "\n"))
		new Crossword(words, lines, matrix)
	}
	
	/** Reads the input into a two-dimensional array
	 *  which contains Cells at the positions where the input contained dots.
	 */
	def readPlan(input:Iterator[String]): Array[Array[Cell]] = {
		val inputLines = input.toList
		val matrix = Array.ofDim[Cell](inputLines.length, inputLines(0).length)
		for (val i <- 0 until inputLines.length) {
			for (val j <- 0 until inputLines(i).length) {
				if (inputLines(i)(j) == '.') {
					matrix(i)(j) = new Cell();
				}
			}
		}
		matrix
	}
	
	/** Extracts continuous horizontal and vertical Lines with Intersections
	 *  from the input matrix.
	 */
	def buildLines(matrix: Array[Array[Cell]]): List[Segment] = {
		val crosswordLines = new ListBuffer[Segment]
		for (val horizontalLine <- matrix) {
			crosswordLines ++= getSegmentsOnLine(horizontalLine)
		}
		for (val verticalLine <- getColumns(matrix)) {
			crosswordLines ++= getSegmentsOnLine(verticalLine)
		}
		fillIntersections(crosswordLines)
		return crosswordLines.toList
	}
	
	def getColumns(matrix: Array[Array[Cell]]): Iterable[Iterable[Cell]] = {
		for (val i <- 0 until matrix(0).length)
			yield getColumn(i, matrix)
	}
	
	def getColumn(j: Int, matrix: Array[Array[Cell]]): Iterable[Cell] = {
		for (val i <- 0 until matrix.length)
			yield matrix(i)(j)
	}
	
	def fillIntersections(lines: Iterable[Segment]) = {
		// flatMap (line,index,cell) -> group by (cell,(index,line)) -> filter cells having == 2 lines
		// (all cells should have only 1 or 2 lines!)
		// -> add (line,index,theOtherLine)
		// , add (theOtherLine,theOtherIndex,line)
	}
	
	// use Seq, not Interator (what is the difference?)
	
	def getSegmentsOnLine(cellLine: Iterable[Cell]): List[Segment] = {
		splitSegments(cellLine, null).filter(_.length > 1).map(new Segment(_)).toList
	}
	
	/** Splits a collection into continuous segments separated by 
	 *  continuous segments of separators.
	 *  {{{
  	 *  scala> splitSegments(List(0, 1, 2, 0, 0, 6, 7, 8), 0)
  	 *  List(List(1, 2), List(6, 7, 8))
  	 *  }}}
	 */
	def splitSegments[T](items: Iterable[T], separator: T): List[List[T]] = {
		val splits = new ListBuffer[List[T]]
		val iter = items.iterator
		while (iter.hasNext) {
			val cLine = iter.dropWhile(_ == separator).takeWhile(_ != separator).toList
			if (cLine.length > 0) splits += cLine
		}
		splits.toList
	}
}