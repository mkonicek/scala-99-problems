package net.martinkonicek.p99

// STORE STRINGS EVERYWHERE, then try to switch to ints (indexes into global string array)
// to see the speedup

class WordLookup(words: List[String]) {
	// array indexed by character?
	//val charPosLookup = initCharPosLookup(words)
  
  	val wordsByLength = words.groupBy(_.length)
	
	def getMatchingWords(segment: Segment) = {
  		wordsByLength(segment.length).filter(word =>
			segment.intersections.forall(i => i.isEmpty || i.char == word(i.pos))
		)
		// narrow down line's candidates by matching them to newly filled cells
		//line.candidates.intersectSorted(line.intersections.map(i =>
		//    charPosLookup(i.pos)(i.char)))
	}
	
	// in utils
	/** All arguments have to be sorted seqs.
	 *  Should be faster than standard intersect, which builds a Map.
	 */
	/*def intersectSorted(first:Seq[String], seqs:Seq[Seq[String]]) = {
		seqs.foldLeft(first, (res, seq) => intersectSorted(res, seq))
	}
	
	def intersectSorted(first:Seq[String], second:Seq[String]) = {
		// how to yield (like in C#) without allocating a collection?
	}
	
	def initCharPosLookup(words:Seq[String]) = {
		
	}*/
}