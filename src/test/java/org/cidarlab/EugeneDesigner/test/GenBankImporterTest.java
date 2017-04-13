package org.cidarlab.EugeneDesigner.test;

import java.util.List;

import org.cidarlab.EugeneDesigner.adaptors.GenBankImporter;
import org.cidarlab.EugeneDesigner.dom.GenBankFeature;
import org.cidarlab.EugeneDesigner.util.Utilities;

import org.junit.Test;

public class GenBankImporterTest {
	
	@Test
	public void testGenBankImporter(){
		
		String filepath = Utilities.getResourcesFilepath() + "genbank.gb";
		List<String> _input = Utilities.getFileLines(filepath);
		String input = GenBankImporter.stringifyList(_input);
		System.out.println("==============INPUT STRING BEGINS=================");
		System.out.println(input);
		System.out.println("==============INPUT STRING ENDS=================");
		List<GenBankFeature> parts = GenBankImporter.analyzeGenBank(input);
		System.out.println("============parts imported=================");
		System.out.println(parts.size());
		for(GenBankFeature part : parts){
			if(part.getDnaSequence().length() > 100){
				System.out.println("Name: " + part.getName() + "\t" + "Sequence_substring(1-100): " + part.getDnaSequence().substring(0, 100));
			} else{
				System.out.println("Name: " + part.getName() + "\t" + "Sequence: " + part.getDnaSequence());
			}
		};
		
		System.out.println("Total sequence lenght is: " + parts.get(0).getFullSequence().length() + " bp.");
		//ExportGenBank.writeGenBank(parts);
	}
	
}
