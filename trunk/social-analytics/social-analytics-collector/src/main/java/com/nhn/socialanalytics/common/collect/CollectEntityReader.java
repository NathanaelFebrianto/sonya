package com.nhn.socialanalytics.common.collect;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CollectEntityReader {
	
	private List<CollectEntity> colEntities = new ArrayList<CollectEntity>();
	
	public CollectEntityReader(File file) {
		try {
			System.out.println("Entities to collect are loading...............");
			this.colEntities = this.loadCollectEntities(file);
			System.out.println("Entities to collect are loaded (" + colEntities.size() + " entities)");

		} catch (IOException e) {
			System.err.println("Error: file " + file + " doesn't exist");
			e.printStackTrace();
			System.exit(1);
		} catch (NullPointerException e) {
			System.err.println("Error: " + file + " doesn't have the right format");
			e.printStackTrace();
			System.exit(1);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	private List<CollectEntity> loadCollectEntities(File file) throws IOException, Exception {
		List<CollectEntity> entities = new ArrayList<CollectEntity>();
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line;

		while ((line = reader.readLine()) != null) {
			if (line.startsWith("#")) {
				continue;
			}
			
			CollectEntity entity = new CollectEntity();
			
			String columns[] = line.split("\t");
			
			for (int i = 0; i < columns.length; i++) {
				String column = columns[i];
				
				if (i == 0) {
					entity.setSite(column);
				}
				else if (i == 1) {
					entity.setEntityId(column);
				}
				else if (i == 2) {
					entity.setEntityName(column);
				}
				else if (i == 3) {
					String[] keywords = column.split(",");
					entity.setSearchKeywords(Arrays.asList(keywords));
				}
				else if (i == 4) {
					entity.setMaxPage(Integer.valueOf(column));
				}
				else if (i == 5) {
					entity.setHistoryBufferMaxRound(Integer.valueOf(column));
				}
				else if (i == 6) {
					String[] languages = column.split(",");
					entity.setLanguages(Arrays.asList(languages));
				}
				else if (i == 7) {
					String[] featureDictionaries = column.split(",");
					for (int j = 0; j < featureDictionaries.length; j++) {
						String[] featureDictionary = featureDictionaries[j].split(":", 2);
						String lang = featureDictionary[0];
						String featureDic = featureDictionary[1];
						entity.addFeatureDictionary(lang, featureDic);
					}
				}
				else if (i == 8) {
					entity.setCompetitorDictionary(column);
				}
				else if (i == 9) {
					String[] attributes = column.split(",");
					for (int j = 0; j < attributes.length; j++) {
						String[] attributePairs = attributes[j].split(":");
						String key = attributePairs[0];
						String value = attributePairs[1];
						
						entity.addExtendedAttribute(key, value);
					}
				}				
			}
			entities.add(entity);			
			System.out.println(entity);
		}
		
		return entities;
	}
	
	public List<CollectEntity> getCollectEntities(String site) {
		System.out.println("\nEntities to collect for site = " + site);
		List<CollectEntity> siteColEntities = new ArrayList<CollectEntity>();
		
		for (CollectEntity entity : colEntities) {
			if (site.equalsIgnoreCase(entity.getSite())) {
				siteColEntities.add(entity);
				System.out.println(entity);
			}
		}
		
		return siteColEntities;
	}
	
	public static void main(String[] args) {
		CollectEntityReader colEntityReader = new CollectEntityReader(new File("./conf/collect_entity.txt"));
		
		colEntityReader.getCollectEntities("appstore");
	}
	
}
