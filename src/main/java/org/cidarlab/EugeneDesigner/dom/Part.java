package org.cidarlab.EugeneDesigner.dom;

import org.biojava3.core.exceptions.CompoundNotFoundError;
import org.biojava3.core.sequence.DNASequence;

import lombok.Getter;

public class Part {
	
	@Getter private String name;
	@Getter private PartType partType;
	@Getter private String sequence;
	@Getter private Orientation orientation;
	
	public Part(String name, PartType partType, boolean isReverse, String sequence) throws CompoundNotFoundError{
		this.name = name;
		this.partType = partType;	
		if(isReverse){
			this.orientation = Orientation.REVERSE;
		} else {
			this.orientation = Orientation.FORWARD;
		}
		DNASequence _seq = new DNASequence(sequence);
		this.sequence = _seq.getSequenceAsString();
	}
		
	@Override
	public String toString() {

		return "Part [name=" + name + ", partType=" + partType + ", orientation=" + orientation + ", sequence=" + sequence + "]";
	}
	
}
