package com.Solution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ColorGrid {

	//4*5 - for example
	static int[][] colorGrid = {{1,1,1,2,1},{3,1,1,2,1},{3,2,2,2,1},{1,4,2,2,2}};	// input
	static Map<Integer, List<Integer>> map = new HashMap<>();
	static int row = colorGrid.length;
	static int column = colorGrid.length == 0 ? 0 : colorGrid[0].length;
	static int noOfColors = 4;
	static int subCluster = -1;
	
	public static void main(String[] args) {
		
		/*
		 * check for each color and print the max color with cluster (positions are considered like below).
		 * 
		 * 	for 4*5 grid,
		 * 
		 *      1  2  3  4  5
		 *      6  7  8  9 10
		 *     11 12 13 14 15
		 *     16 17 18 19 20
		 */
		List<Integer> maxCluster=null;
		int maxColour=-1;
		int max=0;
		for(int k=1;k<=noOfColors;k++) {
			forColour(k);		
			for(Entry<Integer, List<Integer>> entry : map.entrySet()) {
				int subClusterSize = entry.getValue().size();
				if(subClusterSize > max) {
					max=subClusterSize;
					maxCluster=entry.getValue();
					maxColour=k;
				}
			}
			map.clear();				
		}
		System.out.println("Max Colour: "+ maxColour + " and cluster is :" +maxCluster);
	}
	
	
	
	private static void forColour(int color) {
		
		int currentNo=1;
		for(int i=0; i<row; i++) {
			
			for(int j=0; j<column; j++) {
				// check left & then above			
				currentNo = (i*column)+(j+1);
				
				if(colorGrid[i][j] == color) {
					if(i==0 && j==0) {
						assignToDiffCluster(subCluster, currentNo);
						continue;
					}else if(i==0) {
						if(colorGrid[i][j-1] == color) {
							assignToCluster(i, j-1, currentNo);							
						}else {
							assignToDiffCluster(--subCluster, currentNo);
						}
					}else {
						if(j == 0) {
							// check above
							if(colorGrid[i-1][j] == color) 
								assignToCluster(i-1, j, currentNo);
							else {
								// different sub cluster
								assignToDiffCluster(--subCluster, currentNo);
							}
						}else {
							// check left
							if(colorGrid[i][j-1] == color) {
								assignToCluster(i, j-1, currentNo);
								// check above & override if not
								if(colorGrid[i-1][j] == color) {
									// merge sub cluster
									int currentSubCluster=findSubCluster((i*column)+j+1);
									int aboveSubCluster=findSubCluster(((i-1)*column)+j+1);
									if(currentSubCluster != aboveSubCluster) {
										List<Integer> list = map.remove(aboveSubCluster);
										map.get(currentSubCluster).addAll(list);
									}
								}
									
							}else {
								// check above
								if(colorGrid[i-1][j] == color) 
									assignToCluster(i-1, j, currentNo);
								else {
									// different sub cluster
									assignToDiffCluster(--subCluster, currentNo);									
								}
							}
						}
					}
				}				
			}
		}	
	}

	private static void assignToCluster(int i, int j, int currentNo) {
		int no = (i*column)+j+1;
		int key=findSubCluster(no);
		map.get(key).add(currentNo);	
	}
	
	private static void assignToDiffCluster(int cluster, int currNo) {
		List<Integer> list = new ArrayList<>();
		list.add(currNo);
		map.put(--subCluster, list);
	}
	
	private static int findSubCluster(int subClusterNo) {
		int key=0;
		for(Entry<Integer, List<Integer>> entry : map.entrySet()) {
			if(entry.getValue().contains(subClusterNo)) {
				key=entry.getKey();
				break;	
			}
		}
		return key;
	}	
	
	
}
