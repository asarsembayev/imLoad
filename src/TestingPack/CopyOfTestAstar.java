package TestingPack;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class CopyOfTestAstar {
	List<Point2D>openList; List<Point2D>closedList = new ArrayList<Point2D>(); public List<Point2D>pathList = new ArrayList<Point2D>();
	HashMap<Double, Point> temporaryFValsOfNeighbors = new HashMap<>(); HashMap<Point, Double> temporaryGvalsOfNeighbors = new HashMap<>();
	int robotPosX; int robotPosY; int xFinish; int yFinish;
	double hValue; double gValue; double fValue;
	Point neigh1, neigh2, neigh3, neigh4, neigh5, neigh6, neigh7, neigh8;
	Point lastPoint;
	Point bestNeighbor; Point currentObservedNeighbor;
	//constructor
	public CopyOfTestAstar(List<Point2D> openList,Point robotLocation, Point finishPoint, List<Point2D> wallOfset){
		//robotPosX = (int) robotLocation.getX(); robotPosY = (int) robotLocation.getY(); 
		robotPosX = robotLocation.x; robotPosY = robotLocation.y;
		lastPoint = robotLocation;
		xFinish = finishPoint.x; yFinish = finishPoint.y;
		//System.out.println("robot location " + robotLocation);
		//System.out.println("finish location " + finishPoint);
		hValue = countHvalue(robotPosX, robotPosY); gValue = 0;
		//System.out.println("the distance from robot to finish is " + hValue);
		//hValue = countDist();
		
		while(!openList.isEmpty()){
				List<Point> TemporaryNeighborsList = new ArrayList<Point>();
				neigh1 = new Point(lastPoint.x - 1, lastPoint.y - 1); 				
				neigh2 = new Point(lastPoint.x, lastPoint.y - 1);
				neigh3 = new Point(lastPoint.x + 1, lastPoint.y - 1); 			
				neigh4 = new Point(lastPoint.x + 1, lastPoint.y);
				neigh5 = new Point(lastPoint.x + 1, lastPoint.y + 1); 		
				neigh6 = new Point(lastPoint.x, lastPoint.y + 1);
				neigh7 = new Point(lastPoint.x - 1, lastPoint.y + 1); 		
				neigh8 = new Point(lastPoint.x - 1, lastPoint.y);
				
								
				double hval1 = countHvalue(neigh1.x, neigh1.y); 		
				double hval2 = countHvalue(neigh2.x, neigh2.y);
				double hval3 = countHvalue(neigh3.x, neigh3.y); 		
				double hval4 = countHvalue(neigh4.x, neigh4.y);
				double hval5 = countHvalue(neigh5.x, neigh5.y); 		
				double hval6 = countHvalue(neigh6.x, neigh6.y);
				double hval7 = countHvalue(neigh7.x, neigh7.y); 	
				double hval8 = countHvalue(neigh8.x, neigh8.y);
				
				double gval1 = countGvalue(neigh1.x, neigh1.y);		
				double gval2 = countGvalue(neigh2.x, neigh2.y);
				double gval3 = countGvalue(neigh3.x, neigh3.y);		
				double gval4 = countGvalue(neigh4.x, neigh4.y);
				double gval5 = countGvalue(neigh5.x, neigh5.y);		
				double gval6 = countGvalue(neigh6.x, neigh6.y);
				double gval7 = countGvalue(neigh7.x, neigh7.y);		
				double gval8 = countGvalue(neigh8.x, neigh8.y);
				
				double fval1 = hval1 + gval1;			System.out.println("fval 1 " + fval1);
				double fval2 = hval2 + gval2;	System.out.println("fval 2 " + fval2);
				double fval3 = hval3 + gval3;		System.out.println("fval 3 " + fval3);
				double fval4 = hval4 + gval4;System.out.println("fval 4 " + fval4);
				double fval5 = hval5 + gval5;		System.out.println("fval 5 " + fval5);
				double fval6 = hval6 + gval6;System.out.println("fval 6 " + fval6);
				double fval7 = hval7 + gval7;		System.out.println("fval 7 " + fval7);
				double fval8 = hval8 + gval8;System.out.println("fval 8 " + fval8);
				
				temporaryFValsOfNeighbors.put(fval1, neigh1);	System.out.println("key1 " + temporaryFValsOfNeighbors.containsKey(fval1) + temporaryFValsOfNeighbors.containsValue(neigh1));
				temporaryFValsOfNeighbors.put(fval2, neigh2);	System.out.println("key2 " + temporaryFValsOfNeighbors.containsKey(fval2) + temporaryFValsOfNeighbors.containsValue(neigh2));				
				temporaryFValsOfNeighbors.put(fval3, neigh3);	System.out.println("key3 " + temporaryFValsOfNeighbors.containsKey(fval3) + temporaryFValsOfNeighbors.containsValue(neigh3));
				temporaryFValsOfNeighbors.put(fval4, neigh4);	System.out.println("key4 " + temporaryFValsOfNeighbors.containsKey(fval4) + temporaryFValsOfNeighbors.containsValue(neigh4));
				temporaryFValsOfNeighbors.put(fval5, neigh5);	System.out.println("key5 " + temporaryFValsOfNeighbors.containsKey(fval5) + temporaryFValsOfNeighbors.containsValue(neigh5));
				temporaryFValsOfNeighbors.put(fval6, neigh6);	System.out.println("key6 " + temporaryFValsOfNeighbors.containsKey(fval6) + temporaryFValsOfNeighbors.containsValue(neigh6));
				temporaryFValsOfNeighbors.put(fval7, neigh7);	System.out.println("key7 " + temporaryFValsOfNeighbors.containsKey(fval7) + temporaryFValsOfNeighbors.containsValue(neigh7));
				temporaryFValsOfNeighbors.put(fval8, neigh8);	System.out.println("key8 " + temporaryFValsOfNeighbors.containsKey(fval8) + temporaryFValsOfNeighbors.containsValue(neigh8));
				
				temporaryGvalsOfNeighbors.put(neigh1, gval1);
				temporaryGvalsOfNeighbors.put(neigh2, gval2);	
				temporaryGvalsOfNeighbors.put(neigh3, gval3);
				temporaryGvalsOfNeighbors.put(neigh4, gval4); 
				temporaryGvalsOfNeighbors.put(neigh5, gval5);
				temporaryGvalsOfNeighbors.put(neigh6, gval6);	
				temporaryGvalsOfNeighbors.put(neigh7, gval7);
temporaryGvalsOfNeighbors.put(neigh8, gval8); 
				
				//delete later
				System.out.println("size of neighbors " + temporaryFValsOfNeighbors.size());
				System.out.println("F values of neighbors are " + temporaryFValsOfNeighbors.toString());
				
				double [] neighVals = new double[8];
				neighVals[0] = fval1; neighVals[1] = fval2; neighVals[2] = fval3; neighVals[3] = fval4;
				neighVals[4] = fval5; neighVals[5] = fval6; neighVals[6] = fval7; neighVals[7] = fval8;
				
				double min; double max; //double min = neighVals[0]; double max = neighVals[0];
				Arrays.sort(neighVals);
				//System.out.println("neighbors " + Arrays.toString(neighVals));
				int minNflag = 0;
				min = neighVals[minNflag]; max = neighVals[7];
				//System.out.println("minimum is " + min + " and maximum is " + max);
				bestNeighbor = new Point(); currentObservedNeighbor = new Point();
				Iterator<Double> id = temporaryFValsOfNeighbors.keySet().iterator();
				//System.out.println("the size of openList before is " + openList.size());
				while(id.hasNext()){
					Double currentNeighValue = id.next();
					currentObservedNeighbor = temporaryFValsOfNeighbors.get(currentNeighValue);
					int ind = openList.indexOf(currentObservedNeighbor);
					if(currentNeighValue == min && wallOfset.contains(currentObservedNeighbor)){
/*							for(int ii = 0; ii<neighVals.length; ii++){
								if(wallOfset.contains(temporaryFValsOfNeighbors.get( neighVals[ii] ))){
									System.out.println(" yes it is");
									minNflag++;
								}else if(!wallOfset.contains(temporaryFValsOfNeighbors.get( neighVals[ii] ))){
									break;
								}
							}*/
							minNflag++;
							min = neighVals[minNflag];
/*							bestNeighbor = temporaryFValsOfNeighbors.get(min);
							lastPoint = bestNeighbor;*/
						
/*						else if(currentNeighValue != min){
							closedList.add(openList.get(ind));
							openList.remove(ind);
						}*/						
						System.out.println("contains ");
					}
					if(!wallOfset.contains(currentObservedNeighbor)){
						if(currentNeighValue == min && ind != -1){
							bestNeighbor = temporaryFValsOfNeighbors.get(currentNeighValue);
							gValue = temporaryGvalsOfNeighbors.get(bestNeighbor);
							//System.out.println("Found it "+openList.contains(bestNeighbor));
							lastPoint = bestNeighbor;
							openList.remove(ind);
							//System.out.println("Removed it ");
						}
						else if(currentNeighValue != min && ind != -1){
							/*					System.out.println("openList.get(ind) " + openList.get(ind));
							System.out.println("openList.get(ind) " + closedList.contains(openList.get(ind)));*/
							/*					System.out.println("openList.get(ind).equals(bestNeighbor) " + openList.get(ind).equals(bestNeighbor)); //check which one is right 101 or 102 line
							System.out.println("openList.get(ind).equals(bestNeighbor) " + (openList.get(ind) == bestNeighbor));*/
							if(!closedList.contains(openList.get(ind)) && !openList.get(ind).equals(bestNeighbor)){
								closedList.add(openList.get(ind));
							}
							//System.out.println("the size of closedList after is " + closedList.size());
							openList.remove(ind);
						}
					}
				}

				//System.out.println("the size of openList after is " + openList.size());
				//System.out.println("best nei is " + bestNeighbor + " dist is " + min);
				bestNeighbor = temporaryFValsOfNeighbors.get(min);
				lastPoint = bestNeighbor;
				pathList.add(bestNeighbor);
				System.out.println("added " + bestNeighbor);
				temporaryFValsOfNeighbors.clear();
				
				if(bestNeighbor.equals(finishPoint)){
					System.out.println("path is " + pathList.toString());
					System.out.println("path size " + pathList.size());
					break;
				}
			}
	}
	
	private double countGvalue(int x, int y){
		double gVal = Math.sqrt(  
				(x-lastPoint.x)*(x-lastPoint.x) + (y - lastPoint.y)*(y-lastPoint.y)
				);
		gVal = gValue + gVal;
		return gVal;
	}
	
	private double countHvalue(int x, int y){
		double hVal = Math.sqrt(  
				(x-xFinish)*(x-xFinish) + (y - yFinish)*(y-yFinish)
				);
		return hVal;
	}
	private double countDist(){
		double dist = Math.sqrt(  
				(robotPosX-xFinish)*(robotPosX-xFinish) + (robotPosY - yFinish)*(robotPosY-yFinish)
				);
		System.out.println("Distance is " + dist);
		return dist;
	}

}

