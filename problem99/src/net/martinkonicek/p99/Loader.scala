package net.martinkonicek.p99
import scala.collection.mutable.ArrayBuffer

import scala.io._

object Loader {  
	def loadFromFile(fileName:String) {
		val lines = Source.fromFile(fileName).getLines
		val words = lines.takeWhile{ _ != "" }.toList
		val crosswordLines = readPlan(lines)		// new Crossword(words, crosswordLines)		
		/* dream syntax:
		 * with file(fileName).getLines {
			val words = takeWhile { != "" }
			val plan = readPlan
		}*/
		println("words: " + (words mkString "\n"))
		println("plan: " + plan)
	}
	
	//     .
	// .......
	//  .    .
	// ...  ..
	
	def readPlan(input:Iterator[String]):ArrayBuffer[Line] = {		val inputLines = input.toList ;		val matrix = Array.ofDim[Cell](inputLines.length, inputLines(0).length) ;		val crosswordLines = new ArrayBuffer[Line];		for (val i <- 0 to inputLines.length) {			for (val j <- 0 to inputLines(i).length) {				if (inputLines(i)(j) == '.') {					matrix(i)(j) = new Cell();				}			}		};		for (val horizontalLine <- matrix) {			crosswordLines += getLinesSplit(horizontalLine)		};		for (val verticalLine <- getColumns(matrix)) {			crosswordLines += getLinesSplit(verticalLine)		};				fillIntersections(crosswordLines)		return crosswordLines
	};		def getColumns(matrix:Array[Array[Cell]]):Iterator[Iterator[Cell]] = {		for (val i <- 0 to matrix(0).length)			yield getColumn(i, matrix)	};		def getColumn(i:Int, matrix:Array[Array[Cell]]):Iterator[Cell] = {		for (val j <- 0 to matrix.length)			yield matrix(i)(j)	};		def fillIntersections(lines:ArrayBuffer[Line]) = {		// flatMap (line,index,cell) -> group by (cell,(index,line)) -> filter cells having == 2 lines		// (all cells should have only 1 or 2 lines!)		// -> add (line,index,theOtherLine)		// , add (theOtherLine,theOtherIndex,line)	}		// use Seq, not Interator (what is the difference?)	def getLinesSplit(cells:Iterator[Cell]):ArrayBuffer[Line] = {		val allLines = new ArrayBuffer[Line];		var curLine:Line = null;		for (val cell <- cells) {			// line starts			if (curLine == null && cell != null) {				curLine = new Line();				curLine.chars += cell;			};			// line continues			if (curLine != null && cell != null) {				curLine.chars += cell;			};			// line ends			if (curLine != null && cell == null) {				if (curLine.chars.length > 1) {					allLines += curLine;					curLine = null;				}			}		};		if (curLine != null) {			allLines += curLine 		};		allLines	}}