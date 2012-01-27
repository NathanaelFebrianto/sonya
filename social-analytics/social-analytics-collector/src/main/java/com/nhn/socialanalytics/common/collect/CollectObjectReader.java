package com.nhn.socialanalytics.common.collect;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CollectObjectReader {
	
	private List<CollectObject> colObjects = new ArrayList<CollectObject>();
	
	public CollectObjectReader(File file) {
		try {
			System.out.println("Objects to collect are loading...............");
			this.colObjects = this.loadCollectObjets(file);
			System.out.println("Objects to collect are loaded (" + colObjects.size() + " objects)");

		} catch (IOException e) {
			System.err.println("Error: file " + file + " doesn't exist");
			e.printStackTrace();
			System.exit(1);
		} catch (NullPointerException e) {
			System.err.println("Error: collector object file " + file + " doesn't have the right format");
			e.printStackTrace();
			System.exit(1);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	private List<CollectObject> loadCollectObjets(File file) throws IOException, Exception {
		List<CollectObject> colObjects = new ArrayList<CollectObject>();
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line;

		while ((line = reader.readLine()) != null) {
			if (line.startsWith("#")) {
				continue;
			}
			
			CollectObject colObject = new CollectObject();
			
			String columns[] = line.split("\t");
			
			for (int i = 0; i < columns.length; i++) {
				String column = columns[i];
				
				if (i == 0) {
					colObject.setSite(column);
				}
				else if (i == 1) {
					colObject.setObjectId(column);
				}
				else if (i == 2) {
					colObject.setObjectName(column);
				}
				else if (i == 3) {
					String[] keywords = column.split(",");
					colObject.setSearchKeywords(Arrays.asList(keywords));
				}
				else if (i == 4) {
					colObject.setMaxPage(Integer.valueOf(column));
				}
				else if (i == 5) {
					colObject.setHistoryBufferMaxRound(Integer.valueOf(column));
				}
				else if (i == 6) {
					String[] languages = column.split(",");
					colObject.setLanguages(Arrays.asList(languages));
				}
				else if (i == 7) {
					String[] featureDictionaries = column.split(",");
					for (int j = 0; j < featureDictionaries.length; j++) {
						String[] featureDictionary = featureDictionaries[j].split(":", 2);
						String lang = featureDictionary[0];
						String featureDic = featureDictionary[1];
						colObject.addFeatureDictionary(lang, featureDic);
					}
				}
				else if (i == 8) {
					colObject.setCompetitorDictionary(column);
				}
				else if (i == 9) {
					String[] attributes = column.split(",");
					for (int j = 0; j < attributes.length; j++) {
						String[] attributePairs = attributes[j].split(":");
						String key = attributePairs[0];
						String value = attributePairs[1];
						
						colObject.addExtendedAttribute(key, value);
					}
				}				
			}
			colObjects.add(colObject);			
			System.out.println(colObject);
		}
		
		return colObjects;
	}
	
	public List<CollectObject> getCollectObject(String site) {
		System.out.println("\nObjects to collect for site = " + site);
		List<CollectObject> siteColObjects = new ArrayList<CollectObject>();
		
		for (CollectObject colObject : colObjects) {
			if (site.equalsIgnoreCase(colObject.getSite())) {
				siteColObjects.add(colObject);
				System.out.println(colObject);
			}
		}
		
		return siteColObjects;
	}
	
	public static void main(String[] args) {
		CollectObjectReader colObjectReader = new CollectObjectReader(new File("./conf/collect_object.txt"));
		
		colObjectReader.getCollectObject("appstore");
	}
	
}
