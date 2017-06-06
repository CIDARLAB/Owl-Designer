package org.cidarlab.EugeneDesigner.util;

import java.util.ArrayList;
import java.util.List;

import org.cidarlab.EugeneDesigner.dom.GenBankFeature;
import org.cidarlab.EugeneDesigner.dom.Part;
import org.cidarlab.EugeneDesigner.dom.PartType;

public class FeatureListToPartList {
	
	public static List<Part> convert(List<GenBankFeature> gbkFeatures){
		
		List<Part> lp = new ArrayList<Part>();
		
		for(GenBankFeature gbf: gbkFeatures){
			switch(gbf.getFeatureType().toLowerCase()){
				case "promoter":
					Part promoter = new Part(gbf.getName(),PartType.PROMOTER,gbf.isReverseComplement(),gbf.getDnaSequence());
					lp.add(promoter);
					break;
				case "cds":
					Part cds = new Part(gbf.getName(),PartType.CDS,gbf.isReverseComplement(),gbf.getDnaSequence());
					lp.add(cds);
					break;
				case "gene":case "source":
					//skip
					break;
				case "rbs":
					Part rbs = new Part(gbf.getName(),PartType.RBS,gbf.isReverseComplement(),gbf.getDnaSequence());
					lp.add(rbs);
					break;
				case "terminator":
					Part terminator = new Part(gbf.getName(),PartType.TERMINATOR,gbf.isReverseComplement(),gbf.getDnaSequence());
					lp.add(terminator);
					break;
				case "ncrna":case "ribozyme":
					Part ribozyme = new Part(gbf.getName(),PartType.RIBOZYME,gbf.isReverseComplement(),gbf.getDnaSequence());
					lp.add(ribozyme);
					break;
				default:
					System.err.println("Warning! filtering out an unsupported annotation: "+gbf.getFeatureType() + ", when converting parts to Part objects");
					break;
			}
		}
		
		return lp;
	}
	
	
	
}
