package net.martinkonicek.p99

/** Solution containing filled Crossword. */
case class Solution(crossword: Crossword) {
	
	def print = crossword.print
	
	// could add metadata here, such as time spent, number of backtracks,...
}