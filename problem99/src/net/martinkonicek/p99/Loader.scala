package net.martinkonicek.p99

import scala.collection.mutable.ListBuffer
import scala.collection._
import scala.io._
import Utils._

/** Reads the Crossword from input file. */
class Loader(fileName:String) {
    
    /** Reads the Crossword from input file. */
	def load(): Crossword = {
		val fileLines = Source.fromFile(fileName).getLines
		val words = fileLines.takeWhile(_ != "").toList
		val matrix = readPlan(fileLines)
		val segments = buildSegments(matrix)
		new Crossword(words, segments, matrix)
	}
	
	/** Reads the input into a two-dimensional array
	 *  which contains Cells at the positions where the input contained dots.
	 */
	def readPlan(input: Iterator[String]): Array[Array[Cell]] = {
		val inputLines = input.toList
		val matrix = Array.ofDim[Cell](inputLines.length, inputLines(0).length)
		for (val i <- 0 until inputLines.length) {
			for (val j <- 0 until inputLines(i).length) {
				if (inputLines(i)(j) == '.')
					matrix(i)(j) = new Cell();
			}
		}
		matrix
	}
	
	/** Extracts the crossword structure
	 *  (i.e. horizontal & vertical Segments with Intersections) 
	 *  from the from the input matrix.
	 */
	def buildSegments(matrix: Array[Array[Cell]]): List[Segment] = {
		val crosswordSegments = new ListBuffer[Segment]
		for (val horizontalLine <- matrix) {
			crosswordSegments ++= getSegments(horizontalLine)
		}
		for (val verticalLine <- getColumns(matrix)) {
			crosswordSegments ++= getSegments(verticalLine)
		}
		fillIntersections(crosswordSegments)
		return crosswordSegments.toList
	}
	
	def getColumns(matrix: Array[Array[Cell]]): Iterable[Iterable[Cell]] = {
		for (val i <- 0 until matrix(0).length)
			yield getColumn(i, matrix)
	}
	
	def getColumn(j: Int, matrix: Array[Array[Cell]]): Iterable[Cell] = {
		for (val i <- 0 until matrix.length)
			yield matrix(i)(j)
	}
	
	/** Finds where Segments intersect and adds Intersections to them.
	 */
	def fillIntersections(segs: Iterable[Segment]) = {
		// List(seg,cell,index) -> List(cell,List(seg,cell,index))
	  	val grouped = indexedCells(segs).toList.groupBy(_._2).toList
	  	assert(grouped.count(_._2.length > 2) == 0)
	  	for (segCellIdxList <- grouped.map(cellGroup => cellGroup._2).filter(_.length == 2)) {
	  		assert(segCellIdxList.length == 2)
	  		val (seg, cell, index) = segCellIdxList(0)
	  		val (seg2, cell2, index2) = segCellIdxList(1)
	  		assert(cell == cell2)
	  		seg.addIntersection(index, seg2)
	  		seg2.addIntersection(index2, seg)
	  	}
	}
	
	def indexedCells(segs: Iterable[Segment]) = {
		for (val seg <- segs; val cellIdx <- seg.cells.zipWithIndex)
			yield (seg, cellIdx._1, cellIdx._2)
	}
	
	def getSegments(cellLine: Iterable[Cell]): List[Segment] = {
		Utils.splitSegments(cellLine, null).filter(_.length > 1).map(new Segment(_)).toList
	}
}