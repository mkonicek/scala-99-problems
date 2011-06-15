package net.martinkonicek.p99

case class Solution(crossword: Crossword) {
  
	def print = crossword.print
	
	var metadata: Metadata = null
	
	class Metadata(var milisTaken: Int) {
	}
}