package org.cidarlab.EugeneDesigner.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.cidarlab.EugeneDesigner.adaptors.EugeneGateSpecification;
import org.cidarlab.EugeneDesigner.adaptors.SbolImporter;
import org.cidarlab.EugeneDesigner.adaptors.SynBioHubAdaptor;
import org.cidarlab.EugeneDesigner.dom.Part;
import org.cidarlab.EugeneDesigner.util.Utilities;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.sbolstandard.core2.SBOLDocument;

public class SynBioHubDemo {
	
    private SynBioHubAdaptor instance;
    
    public SynBioHubDemo() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        instance = new SynBioHubAdaptor("https://synbiohub.programmingbiology.org/");
    }
    
    @After
    public void tearDown() {
    }
	
    @Test
    public void testSynBioHubDemo() throws Exception {
        
    	String collectionURI = "https://synbiohub.programmingbiology.org/public/Demo/Demo_collection/1";
    	
    	// PROMOTERS empty
    	List<Part> promoterList = new ArrayList<>();
    	
    	//  get RIBOZYMES
    	System.out.println("OWL is fetching Ribozyme components from SynBioHub...");
        List<String> resultRibozymes = instance.getRibozymeURIsFromCollection(collectionURI);
        
        System.out.println("\nOWL imported the following Ribozyme Components:");
        List<Part> ribozymeList = new ArrayList<>();
        for (String ribozyme : resultRibozymes) {
            SBOLDocument sbol = instance.getSBOL(ribozyme);
            ribozymeList = SbolImporter.addPartFromSbol(sbol, ribozymeList);
        }
        
        for(Part part: ribozymeList){
        	System.out.println(part);
        }
    	
        // get RBS
        System.out.println("================================================");
    	System.out.println("OWL is fetching RBS components from SynBioHub...");
        List<String> resultRBSs = instance.getRBSURIsFromCollection(collectionURI);
        
        System.out.println("\nOWL imported the following RBS Components:");
        List<Part> rbsList = new ArrayList<>();
        for (String rbs : resultRBSs) {
            SBOLDocument sbol = instance.getSBOL(rbs);
            rbsList = SbolImporter.addPartFromSbol(sbol, rbsList);
        }
        
        for(Part part: rbsList){
        	System.out.println(part);
        }
        
        // get CDS
        System.out.println("================================================");
    	System.out.println("OWL is fetching CDS components from SynBioHub...");
        List<String> resultCDSs = instance.getCDSURIsFromCollection(collectionURI);
        
        System.out.println("\nOWL imported the following CDS Components:");
        List<Part> cdsList = new ArrayList<>();
        for (String cds : resultCDSs) {
            SBOLDocument sbol = instance.getSBOL(cds);
            cdsList = SbolImporter.addPartFromSbol(sbol, cdsList);
        }
        
        for(Part part: cdsList){
        	System.out.println(part);
        }
        
        // get TERMINATORS
        System.out.println("================================================");
    	System.out.println("OWL is fetching TERMINATOR components from SynBioHub...");
        List<String> resultTerminators = instance.getTerminatorURIsFromCollection(collectionURI);
        
        System.out.println("\nOWL imported the following TERMINATOR Components:");
        List<Part> terminatorList = new ArrayList<>();
        for (String terminator : resultTerminators) {
            SBOLDocument sbol = instance.getSBOL(terminator);
            terminatorList = SbolImporter.addPartFromSbol(sbol, terminatorList);
        }
        
        for(Part part: terminatorList){
        	System.out.println(part);
        }
     
        // Eugene specification
        
        System.out.println("=================================================");
        
        System.out.println("// OWL generates Eugene specification");
        String script = EugeneGateSpecification.createEugeneScript(promoterList, ribozymeList, rbsList, cdsList, terminatorList, true, "Cello_gate");
       
        System.out.println(script);
        
        File file = new File(Utilities.getResourcesFilepath()+"GateSpecification.eug");
        Utilities.writeToFile(file, script);
    } 
}
