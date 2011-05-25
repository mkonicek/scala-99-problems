package net.martinkonicek.p99

class Cell {
	
	var char:Character = '.'
	  
	def isEmpty = char != '.'
	  
	def clear = { char = '.' }
}