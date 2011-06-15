package net.martinkonicek.p99

import scala.collection.mutable.Map
import scala.collection.mutable.Set
import scala.collection.mutable.ArrayBuffer

class Solver(crossword: Crossword, isDebugOutput: Boolean) {
	
	val segments = crossword.segments
	val freeSegments = Set[Segment]() ++ segments
	val words = crossword.words
	val freeWords = Set[String]() ++ words
	val candidates = Map[Segment, List[String]]()
	val wordLookup = new WordLookup(words)
	
	var cnt = 0
	
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
		if (isDebugOutput) {
			debugOutput()
		}
		
		if (filledCount == segments.length) {
			return Some(new Solution(crossword))
		}
		// can replace with mutable heap
		val segment = freeSegments.maxBy(score)	
		if (candidates(segment).length == 0) {
			// segment is the one with least candidates
			return None
		}
		for (val candidateWord <- candidates(segment).filter(freeWords(_))) {
			val oldWord = segment.word 
			placeWord(candidateWord, segment)
			val s = solveRecursive(filledCount + 1)
			if (s != None) {
				return s
			}
			removeWord(candidateWord, oldWord, segment)
		}
		return None
	}
	
	def placeWord(word: String, segment: Segment) = {
		assert(freeWords(word))
		assert(freeSegments(segment))
		
		setWord(word, segment)
		freeSegments -= segment
		freeWords -= word
		
		segments.foreach(recalculateCandidates)
		// recalculating candidates of intersecting words is enough
//		candidates(segment) = List()  // no candidates
//		segment.intersectingSites.foreach(recalculateCandidates)
	}
	
	def removeWord(word: String, oldWord: String, segment: Segment) = {
	  	assert(!freeWords(word))
	  	assert(!freeSegments(segment))
		
		freeSegments += segment
		freeWords += word
		setWord(oldWord, segment)
	  	
	  	segments.foreach(recalculateCandidates)
	  	// recalculating candidates of intersecting words is enough
//	  	recalculateCandidates(segment)
//		segment.intersectingSites.foreach(recalculateCandidates)
	}
	
	def setWord(word: String, segment: Segment) = {
		for ((cell, char) <- segment.cells.view.zip(word)) {
			cell.char = char
		}
	}
	
	def recalculateCandidates(segment: Segment) = {
		candidates(segment) = wordLookup.getMatchingWords(segment).filter(freeWords(_))
	}
	
	/** Prefers Sites that are likely to fail fast (have little candidates) */
	def score(segment: Segment) =
		candidates(segment).length*(-1)
	
	/** Sanity check - all Sites must have at least one candidate word. */
	def checkSolvable =
		candidates.forall(_._2.length > 0)
	
	/** Number of free intersections in a Site. */
	def freeIntersectionsCount(segment: Segment) =
		segment.intersections.count(_.isEmpty)  
	
	/** Calculates candidates for all Sites. */
	def initCandidates(segments: Iterable[Segment]) = {
		segments.foreach(recalculateCandidates)
	}
	
	/** Shows progress on every other step. */
	def debugOutput() = {
		cnt += 1
		if (cnt % 1 == 0) crossword.print
	}
}