package net.martinkonicek.p99


class Crossword {

	val lines:List[Line]
	val words:List[String]
  
	def this(words:Seq[String], lines:Seq[Line]) = {
		this.words = words.toList
		this.lines = lines.toList
	}
}