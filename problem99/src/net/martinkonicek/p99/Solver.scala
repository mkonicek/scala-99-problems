package net.martinkonicek.p99

import scala.collection.mutable.Map
import scala.collection.mutable.Set
import scala.collection.mutable.ArrayBuffer

class Solver(crossword:Crossword) {
	
	val segments = crossword.segments
	val freeSegments = Set[Segment]() ++ segments
	val words = crossword.words
	val freeWords = Set[String]() ++ words
	val candidates = Map[Segment, List[String]]()
	val wordLookup = new WordLookup(words)
	
  	def solve(): Option[Solution] = {
	  	initCandidates(segments)
		if (!checkSolvable) {
			return None		// maybe a NoSolution object with message?
		}
	  	val startMs = System.currentTimeMillis()
		val solution = solveRecursive(filledCount = 0)
		println((System.currentTimeMillis() - startMs) + " ms taken")
		return solution
	}
	
	def solveRecursive(filledCount: Int): Option[Solution] = {
		if (filledCount == segments.length) {
			return Some(new Solution(crossword))
		}
		// can replace with mutable heap
		val segment = freeSegments.maxBy(score)	
		if (candidates(segment).length == 0) {
			// segment is the one with least candidates
			return None
		}
		for (val candidateWord <- candidates(segment)) {
			placeWord(candidateWord, segment)
			val s = solveRecursive(filledCount + 1)
			if (s != None) {
				return s
			}
			removeWord(candidateWord, segment)
		}
		return None
	}
	
	def placeWord(word: String, segment: Segment) = {
		for ((cell, char) <- segment.cells.view.zip(word)) {
			cell.char = char
		}
		candidates(segment) = List()	// no candidates
		segment.intersectingLines.foreach(recalculateCandidates)
		freeSegments -= segment
		freeWords -= word
	}
	
	def removeWord(word: String, segment: Segment) = {
		segment.cells.foreach(_.clear)
		recalculateCandidates(segment)
		//line.intersectingLines.foreach(_.candidates = oldCandidates(_))
		segment.intersectingLines.foreach(recalculateCandidates)
		freeSegments += segment
		freeWords += word
	}
	
	def recalculateCandidates(segment: Segment) = {
		candidates(segment) = wordLookup.getMatchingWords(segment).filter(w => freeWords(w))
	}
	
	/** Prefers Segments that are likely to fail fast
	 *  (little candidates, many free intersections -> will restrict other Segments)
	 */
	def score(segment: Segment) = 
		candidates(segment).length*(-10) + freeIntersectionsCount(segment)*4 //+ line.length  
	
	/** Sanity check - all Segments must have at least one candidate word. */
	def checkSolvable =
		candidates.forall(_._2.length > 0)
	
	/** Number of free intersections in a Segment. */
	def freeIntersectionsCount(segment: Segment) =
		segment.intersections.count(_.isEmpty)  
	
	def initCandidates(segments: Iterable[Segment]) = {
		segments.foreach(recalculateCandidates)
	}
}