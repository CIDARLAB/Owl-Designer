package org.cidarlab.EugeneDesigner.test;

import java.io.File;
import java.util.List;

import org.cidarlab.EugeneDesigner.adaptors.GenBankImporter;
import org.cidarlab.EugeneDesigner.dom.GenBankFeature;
import org.cidarlab.EugeneDesigner.dom.Part;
import org.cidarlab.EugeneDesigner.util.FeatureListToPartList;
import org.cidarlab.EugeneDesigner.util.InputFile;
import org.cidarlab.EugeneDesigner.util.Utilities;
import org.junit.Test;

public class FangGenBankImportTest {

	@Test
	public void testFangGenBankImporter(){
		
		String filepath = Utilities.getResourcesFilepath() + "Fang_bgc.gbk";
		
		File file = new File(filepath);
		System.out.println("Input file type is: "+InputFile.checkType(file).toString());
		
		List<String> _input = Utilities.getFileLines(filepath);
		String input = GenBankImporter.stringifyList(_input);
		System.out.println("==============INPUT STRING BEGINS=================");
		System.out.println(input);
		List<GenBankFeature> parts = GenBankImporter.analyzeGenBank(input, true);
		System.out.println("\n============parts imported=================");
		System.out.println(parts.size());
		for(GenBankFeature part : parts){
			if(part.getDnaSequence().length() > 100){
				System.out.println("Name: " + part.getName() + ",\t" + "Sequence_substring(1-100): " + part.getDnaSequence().substring(0, 100) + ",\t" + "isReverse: "+part.isReverseComplement());
			} else{
				System.out.println("Name: " + part.getName() + ",\t" + "Sequence: " + part.getDnaSequence() + ",\t" + "isReverse: "+part.isReverseComplement());
			}
			//System.out.println("Checking full construct sequence length:\n"+ part.getFullSequence().length() + " bp.");
		};
		
		System.out.println("Total sequence lenght is: " + parts.get(0).getFullSequence().length() + " bp.");
		//ExportGenBank.writeGenBank(parts);
		
		System.out.println("\n==============GbkFeature To Part=================");
		List<Part> partList = FeatureListToPartList.convert(parts);
		for(Part part: partList){
			if(part.getSequence().length() > 100){
				System.out.println("Part [name=" + part.getName() + ", partType=" +part.getPartType().toString() + ", orientation=" + part.getOrientation().toString() + ", sequence_substring(1-100)=" + part.getSequence().substring(0, 99));
			} else{
				System.out.println("Part [name=" + part.getName() + ", partType=" +part.getPartType().toString() + ", orientation=" + part.getOrientation().toString() + ", sequence=" + part.getSequence());
			}
		}
	}
	
}
