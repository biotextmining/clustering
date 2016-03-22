package com.silicolife.textmining.clustering.carrotlinkage;


import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.carrot2.core.Cluster;
import org.carrot2.core.Document;

import com.silicolife.textmining.core.datastructures.clustering.ClusterLabelImpl;
import com.silicolife.textmining.core.datastructures.clustering.ClusterProcessImpl;
import com.silicolife.textmining.core.datastructures.clustering.ClusteringReportImpl;
import com.silicolife.textmining.core.datastructures.init.InitConfiguration;
import com.silicolife.textmining.core.datastructures.utils.Utils;
import com.silicolife.textmining.core.interfaces.core.cluster.IClusterLabel;
import com.silicolife.textmining.core.interfaces.core.cluster.IClusterProcess;
import com.silicolife.textmining.core.interfaces.core.dataaccess.exception.ANoteException;
import com.silicolife.textmining.core.interfaces.core.document.IPublication;
import com.silicolife.textmining.core.interfaces.process.IR.IQuery;
import com.silicolife.textmining.core.interfaces.process.cluestering.ICLusteringReport;

public class CarrotRunClusterAlgorithms {

	public static ICLusteringReport execute(IQuery query,CarrotClusterAlgorithmsEnum algorithm, Properties properties) throws ANoteException  {
		long startTime = GregorianCalendar.getInstance().getTimeInMillis();
		List<Document> documents = new ArrayList<Document>();
		List<IPublication> pubs = query.getPublications();
		Map<Long, IPublication> mapPubs = new HashMap<Long, IPublication>();
		for (IPublication pub : pubs)
		{
			
			Document doc = new Document(pub.getTitle(),pub.getAbstractSection(),String.valueOf(pub.getId()));
			documents.add(doc);
			mapPubs.put(pub.getId(), pub);
		}
		String[] quer = new String[2];
		quer[0] = query.getKeywords();
		quer[1] = query.getOrganism();
		List<Cluster> clustersByTopic = algorithm.getClusters(documents, properties, quer);
		properties.put(ClusterAlgorithmsPropertiesNames.algorithm, algorithm.toString());
		String name = "Cluster "+algorithm.toString()+" Algorithm "+Utils.SimpleDataFormat.format(new Date());
		List<IClusterLabel> clusterLabels = new ArrayList<IClusterLabel>();
		for(Cluster cl:clustersByTopic)
		{
			Double score = cl.getScore();
			String label = cl.getLabel();
			List<Document> lableOlDcoument = cl.getAllDocuments();
			List<Long> documentsIDs = new ArrayList<Long>();
			for(Document doc:lableOlDcoument)
			{
				long pubID = Long.valueOf(doc.getContentUrl());
				documentsIDs.add(pubID);
			}
			clusterLabels.add(new ClusterLabelImpl( label, score, documentsIDs ));
		}
		IClusterProcess clustering = new ClusterProcessImpl(name, clusterLabels,properties);
		InitConfiguration.getDataAccess().createClustering(clustering);
		InitConfiguration.getDataAccess().registerQueryClustering(query,clustering);
		InitConfiguration.getDataAccess().addClusteringLabels(clustering);
		ICLusteringReport report = new ClusteringReportImpl(clustering,algorithm.toString(),query);
		long endTime = GregorianCalendar.getInstance().getTimeInMillis();
		report.setTime(endTime-startTime);
		return report;
	}



}
