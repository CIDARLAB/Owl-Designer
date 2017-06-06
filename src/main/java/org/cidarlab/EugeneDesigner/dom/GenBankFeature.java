package org.cidarlab.EugeneDesigner.dom;

import org.biojava3.core.sequence.DNASequence;

import lombok.Getter;
import lombok.Setter;

public class GenBankFeature extends GenBankFile {

	@Getter @Setter private String name;
	@Getter @Setter private boolean reverseComplement;
	@Getter @Setter private boolean isSource;
	@Getter @Setter private int startx;
	@Getter @Setter private int endx;
	@Getter private String dnaSequence;
	@Getter @Setter private String featureType;
	@Getter @Setter private String sourceId;
	@Getter @Setter private String genBankId;
	@Getter @Setter private String partId;
	@Getter @Setter private String proteinSequence;
   
	   public GenBankFeature() {
	       
	   }
	   
	   public GenBankFeature(String name, boolean reverseComplement, boolean isSource, int startx, int endx, String dnaSequence, String featureType, String sourceId, String genBankId, String partId, String proteinSequence) {
	       super();
		   this.name = name;
	       this.reverseComplement = reverseComplement;
	       this.isSource = isSource;
	       this.startx = startx;
	       this.endx = endx;
	       this.dnaSequence = dnaSequence;
	       this.featureType = featureType;
	       this.sourceId = sourceId;
	       this.genBankId = genBankId;
	       this.partId = partId;
	       this.proteinSequence = proteinSequence;
	   }
	   
		public void setDnaSequence(String dnaSequence, boolean isReverseComplement) {
			DNASequence _seq = new DNASequence(dnaSequence);
			if(isReverseComplement){
				String revComp = _seq.getReverseComplement().getSequenceAsString();
				this.dnaSequence = revComp;
			} else {
				this.dnaSequence = _seq.getSequenceAsString();
			}
		}

	@Override
	public String toString() {
		return "GenBankFeature [name=" + name + ", reverseComplement=" + reverseComplement + ", isSource=" + isSource
				+ ", startx=" + startx + ", endx=" + endx + ", dnaSequence=" + dnaSequence + ", featureType="
				+ featureType + ", sourceId=" + sourceId + ", genBankId=" + genBankId + ", partId=" + partId
				+ ", proteinSequence=" + proteinSequence + "]";
	}
	   
}