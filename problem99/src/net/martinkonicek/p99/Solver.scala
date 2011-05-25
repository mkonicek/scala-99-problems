package net.martinkonicek.p99

class Solver(crossword:Crossword) {
	
	val lines = crossword.lines
	val freeLines = Set[Line]()
	val words = crossword.words
	
  	def solve():Option[Solution] = {
		initCandidates()
		if (!checkSolvable) {
			return None			// maybe a NoSolution object with message?
		}
		val wordLookup = new WordLookup(words)
		solveRecursive(0)	// named argument
	}
	
	def solveRecursive(filledCount:Int):Option[Solution] = {
		if (filledCount == lines.length) {
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
		}
		return None
	}
	
	def placeWord(word:String, line:Line) = {
		// line.cells.zip(word) actually creates a new ArrayBuffer of pairs
		// does toSeq prevent this?
		for ((char, cell) <- word.zip(line.cells)) {
			cell.char = char
		}
		line.intersectingLines.foreach(recalculateCandidates)
		freeLines -= line
	}
	
	def recalculateCandidates(line:Line) = {
		line.candidates = wordLookup.getMatchingWords(line)
	}
	
	// use a TYPEDEF Candidates = ArrayBuffer[String] so that we don't
	// have to repeat it everywhere
	def removeWord(line:Line, oldCandidates:Map[Line,ArrayBuffer[String]]) = {
		line.cells.foreach(_.clear)
		line.intersectingLines.foreach(_.candidates = oldCandidates(_))
		freeLines += line
	}
	
	def lineScore(line:Line) = -line.candidatesCount*10 + line.freeIntersectionsCount*4 //+ line.length  
		
	def checkSolvable = !lines.exists(_.candidatesCount == 0)	
	
	def initCandidates() = {
		for (line <- lines) {
			line.candidates = words.filter(word => word.length == line.length)
		}
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