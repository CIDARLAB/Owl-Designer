package org.cidarlab.EugeneDesigner.test;


import org.cidarlab.EugeneDesigner.util.CommandlineParser;
import org.cidarlab.EugeneDesigner.util.Utilities;
import org.junit.Assert;
import org.junit.Test;

import com.beust.jcommander.JCommander;

public class CommandLineTest {

	@Test
	public void testCommandlineParser(){
		
		CommandlineParser clp = new CommandlineParser();
		
		String[] argv = { "-p", Utilities.getResourcesFilepath()+"promoters.fasta", "-r", Utilities.getResourcesFilepath()+"rbs.fasta",
						  "-g", Utilities.getResourcesFilepath()+"genes.fasta", "-t", Utilities.getResourcesFilepath()+"terminators.fasta",
						  "-design", "Monocistronic_prgt", "-addRibozymes", "true", "-project", "Foundry-2016", "-o", "testOutput", "-e", "testErrors" 
						};
		
		JCommander jc = new JCommander(clp, argv);
		jc.setProgramName("EugeneDesigner");
		jc.usage();
		
		Assert.assertEquals(clp.getDesignMethod(), "Monocistronic_prgt");
		Assert.assertEquals(clp.getProjectName(), "Foundry-2016");
		Assert.assertTrue(clp.isWithRybozyme());
		System.out.println("Project name: " + clp.getProjectName());
		System.out.println("Design method: " + clp.getDesignMethod());
		System.out.println("With ribozymes: " + clp.isWithRybozyme());
		
	}
	
}
