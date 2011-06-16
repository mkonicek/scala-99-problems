package net.martinkonicek.p99

import scala.collection.mutable.Map
import scala.collection.mutable.Set
import scala.collection.mutable.ArrayBuffer

/** Finds one suitable solution if it exists, by backtracking.  */
class Solver(crossword: Crossword, isDebugOutput: Boolean) {
	
	val segments = crossword.segments
	private val freeSegments = Set[Segment]() ++ segments	 // all segments initially free
	private val freeWords = Set[String]() ++ crossword.words // all words initially free
	/** Current candidate words for each segment. */
	private val candidates = Map[Segment, List[String]]()
	private val wordLookup = new WordLookup(crossword.words)
	
  	def solve(): Option[Solution] = {
	  	initCandidates(segments)
		if (!checkSolvable) {
			return None
		}
		return solveRecursive(filledCount = 0)
	}
	
	private def solveRecursive(filledCount: Int): Option[Solution] = {
		if (isDebugOutput) {
			debugOutput()
		}
		if (filledCount == segments.length) {
			return Some(new Solution(crossword))
		}
		// Heuristic - try segments with least possible candidates first.
		val segment = freeSegments.minBy(candidates(_).length)
		if (candidates(segment).length == 0) {
			// there exists a segment with no candidates
			return None
		}
		for (candidate <- candidates(segment).filter(freeWords(_))) {
			val oldWord = segment.word 
			placeWord(candidate, segment)
			val s = solveRecursive(filledCount + 1)
			if (s != None) {
				return s
			}
			removeWord(candidate, oldWord, segment)
		}
		return None
	}
	
	private def placeWord(word: String, segment: Segment) = {
		assert(freeWords(word))
		assert(freeSegments(segment))
		
		segment.setWord(word)
		freeSegments -= segment
		freeWords -= word
		
		segments.foreach(recalculateCandidates)
	}
	
	private def removeWord(word: String, oldWord: String, segment: Segment) = {
	  	assert(!freeWords(word))
	  	assert(!freeSegments(segment))
		
	  	segment.setWord(oldWord)
		freeSegments += segment
		freeWords += word
	  	
	  	segments.foreach(recalculateCandidates)
	}
	
	/** Updates matching candidate words based on contents of Segment. */
	private def recalculateCandidates(segment: Segment) = {
		candidates(segment) = wordLookup.getMatchingWords(segment).filter(freeWords(_))
	}
	
	/** Sanity check - all Sites must have at least one candidate word. */
	private def checkSolvable =
		candidates.forall(_._2.length > 0)
	
	/** Number of free intersections in a Site. */
	private def freeIntersectionsCount(segment: Segment) =
		segment.intersections.count(_.isEmpty)  
	
	/** Calculates candidates for all Sites. */
	private def initCandidates(segments: Iterable[Segment]) =
		segments.foreach(recalculateCandidates)
	
		
	private var debugCounter = 0
	/** Shows progress on every other step. */
	private def debugOutput() = {
		debugCounter += 1
		if (debugCounter % 1 == 0) crossword.print
	}
}