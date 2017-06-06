package org.cidarlab.EugeneDesigner.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.cidarlab.EugeneDesigner.dom.FileType;
import org.cidarlab.EugeneDesigner.dom.Part;
import org.cidarlab.EugeneDesigner.dom.PartType;
import org.cidarlab.EugeneDesigner.util.InputFile;
import org.cidarlab.EugeneDesigner.util.Utilities;
import org.junit.Assert;
import org.junit.Test;

public class InputFileTest {

	@Test
	public void testInputFileTypes(){
		
		FileType ftype = null;
		List<Part> parts = new ArrayList<>();
		
		
		File gbk = new File(Utilities.getResourcesFilepath()+"genbank.gb");
		ftype = InputFile.checkType(gbk);
		Assert.assertEquals(FileType.GBK, ftype);
		parts = InputFile.inputToParts(gbk, PartType.CDS, true);
		System.out.println("Warning message is expected:");
		for(Part part: parts){
			System.out.println(part.toString());
		}
		
		File fasta = new File(Utilities.getResourcesFilepath()+"genes.fasta");
		ftype = InputFile.checkType(fasta);
		Assert.assertEquals(FileType.FASTA, ftype);
		parts = InputFile.inputToParts(fasta, PartType.CDS, false);
		for(Part part: parts){
			System.out.println(part.toString());
		}
		
		File sbol = new File(Utilities.getResourcesFilepath()+"SBOL.rdf");
		ftype = InputFile.checkType(sbol);
		Assert.assertEquals(FileType.SBOL, ftype);
	}
}
