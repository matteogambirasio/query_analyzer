package main;

import java.util.ArrayList;

import extra.TPCHUtils;
import model.Operator;
import network.Network;
import parser.ParserNetwork;
import parser.ParserSimpleXML;
import parser.ParserXML;

public class Main {
	
	
	public static void main(String[] args) {
		
		ParserXML parser = new ParserXML(); //parser che crea la struttura ad albero
		ParserSimpleXML parserSimple = new ParserSimpleXML(); //parser che non si preoccupa della struttura ma estrae gli operatori di una query
		ParserNetwork parsernetwork = new ParserNetwork();
		//ParserEncSchemes parserencschemes = new ParserEncSchemes();
		
		
		/* ANALISI DI TUTTE LE QUERY TPCH */
		//voglio sapere il numero di operatori distinti e con che frquenza compaiono
		TPCHUtils tpchUtils = new TPCHUtils();
		for(int t = 1;t<=TPCHUtils.tpch_num;t++)
		{
			ArrayList<Operator> queryOperators = parserSimple.parseDocument("res/"+t+".xml");
			tpchUtils.inflateOperators(queryOperators);			
		}
		System.out.println(tpchUtils.getAllOperators().toString());
		
		//nuovo metodo di parsing, stabilisce una gerarchia degli operatori
		/* MI LIMITO ALLA 22 */
		parser.parseDocument("res/22.xml");		
		for(int i = 0; i< parser.operators.size(); i++)
					System.out.println(parser.operators.get(i).toString());
		
		
		/* PARSING DEL NETWORK */
		// ok
		Network network = new Network(parsernetwork.parseDocument("config/netconfig.xml"));
		System.out.println(network.showNetwork());
		
		/* PARSING DELLA CONFIGURAZIONE DEGLI OPERATORI */
		/* bocciata....deve essere fatta runtime analizzando le caratteristiche (target e funzioni)
		EncSchemes encSchemes = new EncSchemes(parserencschemes.parseDocument("config/encschemes.xml"));
		System.out.println(encSchemes.showEncSchemes());
		*/
		
		/* ANALISI DELLA QUERY */
		/* da rivedere alla luve della nuova struttura ad albero
		Analyzer analyzer = new Analyzer();
		analyzer.Analyze(encSchemes, queryOperators, network);
		System.out.println("MIN COST: "+analyzer.getMinCost());
		System.out.println("OPERATIONS: "+analyzer.getOperations());
		*/
		
	}

}
