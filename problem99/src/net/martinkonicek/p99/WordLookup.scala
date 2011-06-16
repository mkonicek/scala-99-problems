package net.martinkonicek.p99

/** Fast lookup of words matching a (partially filled) Segment. */
class WordLookup(words: List[String]) {

  	val wordsByLength = words.groupBy(_.length)
	
  	/** Returns all words that fit to a (partially filled) Segment. */
	def getMatchingWords(segment: Segment) = {
  		// Simple implementation, but totally sufficient.
  		// Faster implementation could index the words by triples
		// (word_len,char,position)->(sorted list of word indices).
  	    // Given n filled intersections, it would do n lookups and 
  	    // intersect n (very short, usually <2 elements) sorted lists.
  		wordsByLength(segment.length).filter(word =>
			segment.intersections.forall(i => i.isEmpty || i.char == word(i.pos))
		)
	}
}