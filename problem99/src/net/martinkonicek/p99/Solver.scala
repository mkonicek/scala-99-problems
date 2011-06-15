package net.martinkonicek.p99

import scala.collection.mutable.Map
import scala.collection.mutable.Set
import scala.collection.mutable.ArrayBuffer

class Solver(crossword:Crossword) {
	
	val lines = crossword.segments
	val freeLines = Set[Segment]()
	val words = crossword.words
	val candidates = Map[Segment, List[String]]()
	val wordLookup = new WordLookup(words)
	
  	def solve(): Option[Solution] = {
	  	initCandidates(lines)
		if (!checkSolvable) {
			return None		// maybe a NoSolution object with message?
		}
		return solveRecursive(0)	// named argument
	}
	
	def solveRecursive(filledCount: Int): Option[Solution] = {
		/*if (filledCount == lines.length) {
			return new Solution(crossword)
		}
		val startLine = freeLines.maxBy(lineScore)	// can replace with mutable heap
		if (startLine.candidatesCount == 0) {
			// counts on the fact that startLine is the one with least candidates
			return None
		}
		for (val candidateWord <- startLine.candidates) {
			val oldCandidates = startLine.intersectingLines.toMap(_.candidates)
			placeWord(candidateWord, startLine)
			val s = solveRecursive(filledCount + 1)
			if (s != None) {
				return s
			}
			removeWord(startLine, oldCandidates)
		}*/
		return None
	}
	
	def placeWord(word: String, line: Segment) = {
		for ((cell, char) <- line.cells.view.zip(word)) {
			cell.char = char
		}
		candidates(line) = List()	// no candidates
		line.intersectingLines.foreach(recalculateCandidates)
		freeLines -= line
	}
	
	def recalculateCandidates(line: Segment) = {
		candidates(line) = wordLookup.getMatchingWords(line)
	}
	
	def removeWord(line: Segment) = {
		line.cells.foreach(_.clear)
		recalculateCandidates(line)
		//line.intersectingLines.foreach(_.candidates = oldCandidates(_))
		line.intersectingLines.foreach(recalculateCandidates)
		freeLines += line
	}
	
	/** Prefers Segments that are likely to fail fast
	 *  (little candidates, many free intersections -> will restrict other Segments)
	 * */
	def score(line: Segment) = 
		candidates(line).length*(-10) + freeIntersectionsCount(line)*4 //+ line.length  
	
	/** Sanity check - all Segments must have at least one candidate word. */
	def checkSolvable =
		candidates.forall(_._2.length > 0)
	
	/** Number of free intersections in a Segment. */
	def freeIntersectionsCount(segment: Segment) =
		segment.intersections.count(_.isEmpty)  
	
	def initCandidates(segs: Iterable[Segment]) = {
		segs.foreach(recalculateCandidates)
	}
}

// fail fast:
		// always prefer lines with least candidates.
		// when 2 lines same number of candidates
		// then every empty intersection is score +1(, every filled intersection is +4)

		// main algorithm: take the line with the highest score,
		// try place all possible words and recurse.
		// if no word possible, backtrack

		// placing a word changes scores -
		// recalculate candidates for affected lines, 
		//     if only one then +100, if zero, backtrack
		// 
		// -> maintain a heap of current ordering,
		// update it when placing a word, and when backtracking

		// representation of lines: 
		// 1. lines are Lists of Cell objects. some cells are shared so 
		//    that filling a cell in one line also fills it in the other line
		// 2. lines are just Lists of chars + info about intersections
		//    so that when we write a char, we must also update it in
		//    all the intersecting lines
		// (can't tell what will be faster, maybe 1.)
		// (to be able to write the output we have to keep (x,y) for every cell)

		// lookup of candidate words:
		// build a matrix (not map) - (words which have char c at position i)
		// (wrap it into an object to hide the implementation)
		// (maybe convert all chars to ints in advance).
		// a helper method will be useful which takes a line (partially filled)
		// and returns all possible candidate words

		// loading the input:
		// go line by line. horizontal segments are new lines,
		// vertical segments are built by remembering "possible vertical"
		// for every dot from the previous line.