package org.cidarlab.EugeneDesigner.adaptors;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.biojava3.core.exceptions.CompoundNotFoundError;
import org.cidarlab.EugeneDesigner.dom.Part;
import org.cidarlab.EugeneDesigner.dom.PartType;
import org.sbolstandard.core2.Component;
import org.sbolstandard.core2.ComponentDefinition;
import org.sbolstandard.core2.Location;
import org.sbolstandard.core2.OrientationType;
import org.sbolstandard.core2.SBOLDocument;
import org.sbolstandard.core2.SBOLValidationException;
import org.sbolstandard.core2.Sequence;
import org.sbolstandard.core2.SequenceOntology;

public class SbolImporter {
	
	public static List<Part> sbolToParts(SBOLDocument sbol, List<Part> parts) throws MalformedURLException, URISyntaxException, SBOLValidationException {
		
		final URI ribozymeSeqOntology = new URL("http://identifiers.org/so/SO:0000374").toURI();
		
		String name = null;
		for(ComponentDefinition cd: sbol.getRootComponentDefinitions()){
			
			name = cd.getName();
			System.out.println("\nDevice [name="+ name +", sequence="+cd.getSequenceByEncoding(Sequence.IUPAC_DNA).getElements()+"]");
			System.out.println(" ");
			for(Component c: cd.getSortedComponents()){
				
				//Part p = new Part(name, partType, isReverse, sequence);
				if(c.getDefinition().containsRole(SequenceOntology.PROMOTER)){
					Part part = new Part(c.getDisplayId(),PartType.PROMOTER, isReverse(cd, c), c.getDefinition().getSequenceByEncoding(Sequence.IUPAC_DNA).getElements());
					parts.add(part);
				}
				if(c.getDefinition().containsRole(SequenceOntology.CDS)){
					Part part = new Part(c.getDisplayId(),PartType.CDS, isReverse(cd, c), c.getDefinition().getSequenceByEncoding(Sequence.IUPAC_DNA).getElements());
					parts.add(part);
				}
				if(c.getDefinition().containsRole(SequenceOntology.RIBOSOME_ENTRY_SITE)){
					Part part = new Part(c.getDisplayId(),PartType.RBS, isReverse(cd, c), c.getDefinition().getSequenceByEncoding(Sequence.IUPAC_DNA).getElements());
					parts.add(part);
				}
				if(c.getDefinition().containsRole(SequenceOntology.TERMINATOR)){
					Part part = new Part(c.getDisplayId(),PartType.TERMINATOR, isReverse(cd, c), c.getDefinition().getSequenceByEncoding(Sequence.IUPAC_DNA).getElements());
					parts.add(part);
				}
				if(c.getDefinition().containsRole(ribozymeSeqOntology)){
					Part part = new Part(c.getDisplayId(),PartType.RIBOZYME, isReverse(cd, c), c.getDefinition().getSequenceByEncoding(Sequence.IUPAC_DNA).getElements());
					parts.add(part);
				}
				
			}
			name=null;
		}
		
		return parts;		
	}
	
	public static List<Part> addPartFromSbol(SBOLDocument sbolPart, List<Part> parts) throws MalformedURLException, URISyntaxException, CompoundNotFoundError{
		
		ComponentDefinition cd = sbolPart.getComponentDefinitions().iterator().next();
		
		//Part p = new Part(name, partType, isReverse, sequence);
		if(cd.containsRole(SequenceOntology.PROMOTER)){
			Part part = new Part(cd.getDisplayId(),PartType.PROMOTER, false, cd.getSequenceByEncoding(Sequence.IUPAC_DNA).getElements());
			parts.add(part);
		}
		if(cd.containsRole(SequenceOntology.CDS)){
			Part part = new Part(cd.getDisplayId(),PartType.CDS, false, cd.getSequenceByEncoding(Sequence.IUPAC_DNA).getElements());
			parts.add(part);
		}
		if(cd.containsRole(SequenceOntology.RIBOSOME_ENTRY_SITE)){
			Part part = new Part(cd.getDisplayId(),PartType.RBS, false, cd.getSequenceByEncoding(Sequence.IUPAC_DNA).getElements());
			parts.add(part);
		}
		if(cd.containsRole(SequenceOntology.TERMINATOR)){
			Part part = new Part(cd.getDisplayId(),PartType.TERMINATOR, false, cd.getSequenceByEncoding(Sequence.IUPAC_DNA).getElements());
			parts.add(part);
		}
		if(cd.containsRole(new URL("http://identifiers.org/so/SO:0000374").toURI())){
			Part part = new Part(cd.getDisplayId(),PartType.RIBOZYME, false, cd.getSequenceByEncoding(Sequence.IUPAC_DNA).getElements());
			parts.add(part);
		}
		
		return parts;
	}
	
	
	private static boolean isReverse(ComponentDefinition compDef, Component comp){
		
		Iterator<Location> iter = compDef.getSequenceAnnotation(comp).getLocations().iterator();
		Location first = (Location) iter.next();
		boolean isReverse = false;
		
		if(first.getOrientation() == OrientationType.INLINE){
			isReverse = false;
		}
		if(first.getOrientation() == OrientationType.REVERSECOMPLEMENT){
			
			isReverse = true;
		}
		return isReverse;
	}
}
