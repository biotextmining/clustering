package com.silicolife.textmining.clustering;

import static org.junit.Assert.assertTrue;

import java.util.Properties;

import org.junit.Test;

import com.silicolife.textmining.DatabaseConnectionInit;
import com.silicolife.textmining.clustering.carrotlinkage.CarrotClusterAlgorithmsEnum;
import com.silicolife.textmining.clustering.carrotlinkage.CarrotRunClusterAlgorithms;
import com.silicolife.textmining.core.datastructures.init.exception.InvalidDatabaseAccess;
import com.silicolife.textmining.core.datastructures.process.ir.configuration.IRSearchConfigurationImpl;
import com.silicolife.textmining.core.interfaces.core.dataaccess.exception.ANoteException;
import com.silicolife.textmining.core.interfaces.core.report.processes.ir.IIRSearchProcessReport;
import com.silicolife.textmining.core.interfaces.process.IR.IIRSearchConfiguration;
import com.silicolife.textmining.core.interfaces.process.IR.IQuery;
import com.silicolife.textmining.core.interfaces.process.IR.exception.InternetConnectionProblemException;
import com.silicolife.textmining.core.interfaces.process.cluestering.ICLusteringReport;
import com.silicolife.textmining.processes.ir.pubmed.PubMedSearch;

public class CarrotRunClusterAlgorithmsTest {

	@Test
	public void test() throws InvalidDatabaseAccess, ANoteException, InternetConnectionProblemException {
		DatabaseConnectionInit.init("localhost","3306","createdatest","root","admin");
		IQuery query = createQuery().getQuery();
		CarrotClusterAlgorithmsEnum algorithm = CarrotClusterAlgorithmsEnum.STC;
		ICLusteringReport report = CarrotRunClusterAlgorithms.execute(query, algorithm , new Properties());
		System.out.println(report.getClustering().getClusterLabels().size());
		assertTrue(report.isFinishing());
	}
	

	public static IIRSearchProcessReport createQuery() throws InvalidDatabaseAccess,
	ANoteException, InternetConnectionProblemException {
		System.out.println("Create Query");
		PubMedSearch pubmedSearch = new PubMedSearch();
		// Properties
		Properties propeties = new Properties();
		// The query name resulted
		String queryName = "Escherichia coli AND Stringent response Advanced";
		// Organism
		String organism = "Escherichia coli";
		// Keywords
		String keywords = "Stringent response";
		// Add Author Filter
		//propeties.put("authors", "");
		// Add Journal Filter
		//propeties.put("journal", "");
		// Add Data Range
		//// From Date
		propeties.put("fromDate", "2008");
		//// To Date
		propeties.put("toDate", "2014");
		// Article Details Content
		//// Abstract Available
		//propeties.put("articleDetails", "abstract");
		//// Free full text
		propeties.put("articleDetails", "freefulltext");
		//// Full Text available
		//propeties.put("articleDetails", "fulltextavailable");
		// Article Source
		//// Medline Only
		//propeties.put("ArticleSource", "med");
		//// Pubmed Central Only
		propeties.put("ArticleSource", "pmc");
		//// Both
		//propeties.put("ArticleSource", "medpmc");
		// Article Type
		//propeties.put("articletype", "Revision");

		IIRSearchConfiguration searchConfiguration = new IRSearchConfigurationImpl(keywords , organism , queryName, propeties );
		IIRSearchProcessReport report = pubmedSearch.search(searchConfiguration);
		return report;
	}

}
