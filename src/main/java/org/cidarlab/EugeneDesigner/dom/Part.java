package org.cidarlab.EugeneDesigner.dom;

import org.biojava3.core.exceptions.CompoundNotFoundError;
import org.biojava3.core.sequence.DNASequence;

import lombok.Getter;

public class Part {
	
	@Getter private String name;
	@Getter private PartType partType;
	@Getter private String sequence;
	
	public Part(String name, PartType partType, String sequence) throws CompoundNotFoundError{
		this.name = name;
		this.partType = partType;	
		DNASequence seq = new DNASequence(sequence);
		this.sequence = seq.getSequenceAsString().toUpperCase();
		
	}

	@Override
	public String toString() {

		return "Part [name=" + name + ", partType=" + partType + ", sequence=" + sequence + "]";
	}
	
}
