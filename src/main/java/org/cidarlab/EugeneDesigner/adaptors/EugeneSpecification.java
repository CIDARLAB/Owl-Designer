package org.cidarlab.EugeneDesigner.adaptors;

import java.util.List;

import org.cidarlab.EugeneDesigner.dom.Part;

public class EugeneSpecification {

	public static String createEugeneScript(
			List<Part> promoters,
			List<Part> ribozymes,
			List<Part> rbsList,
			List<Part> genes,
			List<Part> terminators,
			boolean withRibozyme,
			String designMethod) {
	    	
	        String script = "";
	        script += "//COMMON PARTS AND PROPERTIES\n"
	                + "Property name(txt);\n"
	                + "Property SO(txt);\n"
	                + "\n"
	                + "PartType Promoter(name, SO);\n"
	                + "PartType Ribozyme(name, SO);\n"
	                + "PartType RBS(name, SO);\n"
	                + "PartType CDS(name, SO);\n"
	                + "PartType Terminator(name, SO);\n";

	        int count = 1;
	        for (Part gene : genes) {
	            script += "CDS g" + count++ + "(.SEQUENCE(\"" + gene.getSequence() + "\"), .name(\"" + gene.getName() + "\"), .SO(\"SO_0000316\"));";
	            script += "\n";
	        }

	        script += "num N = " + genes.size() + ";\n";

	        count = 1;
	        for (Part promoter : promoters) {
	            script += "Promoter p" + count++ + "(.SEQUENCE(\"" + promoter.getSequence() + "\"), .name(\"" + promoter.getName() + "\"), .SO(\"SO_0000167\"));";
	            script += "\n";
	        }

	        count = 1;
	        for (Part ribozyme : ribozymes) {
	            script += "Ribozyme ri" + count++ + "(.SEQUENCE(\"" + ribozyme.getSequence() + "\"), .name(\"" + ribozyme.getName() + "\"), .SO(\"SO_0000627\"), .PIGEON(\"z ri 13\"));";
	            script += "\n";
	        }

	        count = 1;
	        for (Part rbs : rbsList) {
	            script += "RBS rbs" + count++ + "(.SEQUENCE(\"" + rbs.getSequence() + "\"), .name(\"" + rbs.getName() + "\"), .SO(\"SO_0000139\"));";
	            script += "\n";
	        }

	        count = 1;
	        for (Part terminator : terminators) {
	            script += "Terminator t" + count++ + "(.SEQUENCE(\"" + terminator.getSequence() + "\"), .name(\"" + terminator.getName() + "\"), .SO(\"SO_0000141\"));";
	            script += "\n";
	        }

	        script += "// Ribozyme (TRUE is with; FALSE is without); Default is TRUE.\n"
	                + "boolean riboz = " + withRibozyme + ";\n"
	                //=============================================================================
	                + "//==========================================\n"
	                + "\n"

	                //MONOCISTRONIC promoter-rbs-gene-terminator DEVICE TEMPLATE
	                
	                + "Device Monocistronic_prgt();\n"
	                + "Rule r1(on Monocistronic_prgt: ALL_FORWARD);\n"
	                + "\n"
	                + "for(num i=1; i<=N; i=i+1) {\n"
	                + "  if(riboz == false) {\n"
	                + "  	Monocistronic_prgt = Monocistronic_prgt + Promoter + RBS + CDS + Terminator;\n"
	                + "  } else {\n"
	                + "   Monocistronic_prgt = Monocistronic_prgt + Promoter + Ribozyme + RBS + CDS + Terminator;\n"
	                + "  }\n"
	                + "  Promoter${\"p\"+i};\n"
	                + "  Ribozyme${\"ri\"+i};\n"
	                + "  RBS${\"rbs\"+i};\n"
	                + "  CDS${\"g\"+i};\n"
	                + "  AND(r1, ${\"g\"+i} EXACTLY 1);\n"
	                + "  if(i>=2) {\n"
	                + "    AND(r1, ${\"g\"+(i-1)} BEFORE ${\"g\"+i}); \n"
	                + "  } \n"
	                + "  Terminator${\"t\"+i};\n"
	                + "}\n"
	                + "\n"
	                
	                // MONOCISTRONIC promoter-gene-terminator DEVICE TEMPLATE
	                
	                + "Device Monocistronic_pgt();\n"
	                + "Rule r2(on Monocistronic_pgt: ALL_FORWARD);\n"

	                + "for(num i=1; i<=N; i=i+1) {\n"
	                + "  if(riboz == false) {\n"
	                + "  	Monocistronic_pgt = Monocistronic_pgt + Promoter + CDS + Terminator;\n"
	                + "  } else {\n"
	                + "    Monocistronic_pgt = Monocistronic_pgt + Promoter + Ribozyme + CDS + Terminator;\n"
	                + "  }\n"
	                + "  Promoter${\"p\" + i};\n"
	                + "  Ribozyme${\"ri\"+i};\n"
	                + "  CDS${\"g\"+i}; AND(r2, ${\"g\"+i} EXACTLY 1);\n"
	                + "  if(i>=2) {\n"
	                + "    AND(r2, ${\"g\"+(i-1)} BEFORE ${\"g\"+i});\n" 
	                + "  }\n"
	                + "  Terminator${\"t\"+i};\n"
	                + "}\n"
				    
				    //POLYCISTRONIC DEVICE TEMPLATE
				    + "Device Polycistronic(Promoter);\n"
				
				    + "Rule R(on Polycistronic: ALL_FORWARD);\n"
				
				
				    + "for(num i=1; i<=N; i=i+1) {\n"
				    +   	"if (i == 10 || i == 20 || i == 30 || i == 40 || i == 50 || i == 60 || i == 70) {\n"
				    +   	    "Polycistronic = Polycistronic + RBS + CDS + Promoter;\n"
				    +           "CDS${\"g\"+i}; AND(R, ${\"g\"+i} EXACTLY 1);\n"
				    +           "RBS${\"rbs\" + i};\n"
				    +           "Promoter${\"p\"+i};\n"
				    +       "} else {\n"
				    +   	    "Polycistronic = Polycistronic + RBS + CDS;\n"
				    + "         RBS${\"rbs\" + i};\n"
				    + "         CDS${\"g\"+i}; AND(R, ${\"g\"+i} EXACTLY 1);\n"
				    + "      }\n"
				       
				    + "   if(i>=2) {\n"
				    + "     AND(R, ${\"g\"+(i-1)} BEFORE ${\"g\"+i});\n"
				    + "   }\n"
				  
				    + " }\n"
				    + "   Polycistronic = Polycistronic + Terminator;\n"
				    
		
				    // GENERATE the DEVICE
				    + " lod = product(" + designMethod + ");\n"
				    + " println(\"The total number of devices: \" + SIZEOF(lod));\n"
	        		+ " println(lod); ";
				
				        return script;
				    }
	
}
