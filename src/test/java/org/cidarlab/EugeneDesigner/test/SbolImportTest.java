package org.cidarlab.EugeneDesigner.test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.cidarlab.EugeneDesigner.adaptors.SbolImporter;
import org.cidarlab.EugeneDesigner.dom.Part;
import org.cidarlab.EugeneDesigner.util.Utilities;
import org.junit.Test;
import org.sbolstandard.core2.SBOLConversionException;
import org.sbolstandard.core2.SBOLDocument;
import org.sbolstandard.core2.SBOLReader;
import org.sbolstandard.core2.SBOLValidationException;

public class SbolImportTest {

	@Test
	public void testSbolImport() throws SBOLValidationException, IOException, SBOLConversionException, URISyntaxException{
		
		File sbol = new File(Utilities.getResourcesFilepath()+"SBOL.xml");
		
		SBOLDocument sd = SBOLReader.read(sbol);
		
		if(sd.isCompliant()){
			System.out.println("SBOL file is compliant");
		}
		List<Part> parts = new ArrayList<>();
		parts = SbolImporter.sbolToParts(sd, parts);
		
		for(Part part: parts){
			System.out.println(part);
		}
	}
	
}
