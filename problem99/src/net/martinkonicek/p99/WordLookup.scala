/*package net.martinkonicek.p99

// STORE STRINGS EVERYWHERE, then try to switch to ints (indexes into global string array)
// to see the speedup

class WordLookup(words:Seq[String]) {
	// array indexed by character?
	//val charPosLookup = initCharPosLookup(words)
	// HOW TO MAKE CONSTRUCTOR TAKING SOME PARAMETERS AND FILLING SOME NEW FIELDS?
	//val wordArray = words.toArray
	
	def getMatchingWords(line:Line):Seq[String] = {
		line.candidates.filter(candidate => {
			// this could be optimized by checking only one intersection
			// if we know it has just changed
			line.intersections.all(i => candidate(i.pos) == i.char)
		})
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
}*/