package parser;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import model.Operator;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

//basato su SAX
public class ParserXML {
	
	public ArrayList<Operator> operators;
	
	private static boolean node_type;
	private static boolean parent_relationship;
	private static boolean relation_name;	
	
	private String tmp_node_type;
	private String tmp_parent_relationship;
	private String tmp_relation_name;
	
	private Operator tmp;
	
	private int id;
	private int id_parent;
	
	private HashMap<Integer, Boolean> subPlans;
	
	public ParserXML()
	{
		id=-1;
		id_parent=-1;
		operators = new ArrayList<Operator>();
		subPlans = new HashMap<Integer, Boolean>();
	}
	
	
	/*
	 * Logica del parse: ad ogno i nodo "plan" crea una struttura, nella quale vado ad inserire diversi campi
	 * ->tipo di operatore (node type)
	 * ->parent relationship
	 * ->tabella coinvolta (relation name)
	 *  
	 */
	public void parseDocument(String res)
	{
		
    try { 
    	
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser(); 
		DefaultHandler handler = new DefaultHandler() {
	  
				public void startElement(String uri, String localName,String qName, 
			                Attributes attributes) throws SAXException {
										
					if (qName.equalsIgnoreCase("PLAN")) {
						tmp = new Operator();
						id++;
						subPlans.put(id, false);
					}
					
					if (qName.equalsIgnoreCase("PLANS")) {
						
						//aggiorno lo stato delle subquery
						subPlans.remove(id);
						subPlans.put(id, true);
						
						tmp.setId(id);
						tmp.setIdParent(id_parent);
						operators.add(tmp);
						id_parent = id;
					}
					
					//per leggere i dati
					if (qName.equalsIgnoreCase("NODE-TYPE")) {
						node_type = true;
					}
					
					if (qName.equalsIgnoreCase("RELATION-NAME")) {
						relation_name = true;
					}
					
					if (qName.equalsIgnoreCase("PARENT-RELATIONSHIP")) {
						parent_relationship = true;
					}
					
			 
				}
			 
				public void endElement(String uri, String localName,
					String qName) throws SAXException {
					
					if (qName.equalsIgnoreCase("PLAN")) {
						if(subPlans.get(id) == false) //l'elemento � una foglia non ha sottopiani, lo devo aggiungere
						{
							subPlans.remove(id);
							subPlans.put(id, true);
							
							tmp.setId(id);
							tmp.setIdParent(id_parent);
							operators.add(tmp);
						}
					}
			 
				}
			 
				public void characters(char ch[], int start, int length) throws SAXException {
			 
										
					if (node_type) {
						String value = new String(ch, start, length);
						
						tmp_node_type = value;
						tmp.setNodeType(tmp_node_type);
						node_type = false;
					}
					
					if (parent_relationship) {
						String value = new String(ch, start, length);
						
						tmp_parent_relationship = value;
						tmp.setParentRelationship(tmp_parent_relationship);
						parent_relationship = false;
					}
					
					if (relation_name) {
						String value = new String(ch, start, length);
						
						tmp_relation_name = value;
						tmp.setRelationName(tmp_relation_name);
						relation_name = false;
					}
					
					
			  
				} 
	     };
 
	     saxParser.parse(res, handler);
	     
 
     } catch (Exception e) {
       e.printStackTrace();
     }
    	
    	
   }
 
}