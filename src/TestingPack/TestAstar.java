package TestingPack;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class TestAstar {
	List<Point2D>closedList = new ArrayList<Point2D>(); public List<Point2D>pathList = new ArrayList<Point2D>();
	HashMap<Double, Point> temporaryFValsOfNeighbors = new HashMap<>(); HashMap<Point, Double> temporaryGvalsOfNeighbors = new HashMap<>();
	
	Double[]hValsArr = new Double[8]; Double[]fValsArr = new Double[8]; Point[]neighs = new Point[8]; //?
	
	int robotPosX; int robotPosY; int xFinish; int yFinish;
	double hValue; double gValue; double fValue;
	Point neigh1, neigh2, neigh3, neigh4, neigh5, neigh6, neigh7, neigh8;
	Point lastPoint;
	Point bestNeighbor; Point currentObservedNeighbor;
	double min; int minInd = 0; 
	ArrayList<Double> tempFvalues = new ArrayList<>();
	ArrayList<Double> tempHvalues = new ArrayList<>();
	ArrayList<Point> tempNeighbors = new ArrayList<>();
	//constructor
	public TestAstar(Point robotLocation, Point finishPoint, List<Point2D> wallOfset, List<Point2D> wallPositions){
		long currentTime = System.currentTimeMillis();
		robotPosX = robotLocation.x; robotPosY = robotLocation.y;
		lastPoint = robotLocation;
		xFinish = finishPoint.x; yFinish = finishPoint.y;
		hValue = countHvalue(robotPosX, robotPosY); gValue = 0;
	
		while(true){
				List<Point> TemporaryNeighborsList = new ArrayList<Point>();
				neigh1 = new Point(lastPoint.x - 1, lastPoint.y - 1); 		neigh2 = new Point(lastPoint.x, lastPoint.y - 1);			neigh3 = new Point(lastPoint.x + 1, lastPoint.y - 1); 		neigh4 = new Point(lastPoint.x + 1, lastPoint.y);
				neigh5 = new Point(lastPoint.x + 1, lastPoint.y + 1); 		neigh6 = new Point(lastPoint.x, lastPoint.y + 1);			neigh7 = new Point(lastPoint.x - 1, lastPoint.y + 1); 		neigh8 = new Point(lastPoint.x - 1, lastPoint.y);
												
				double hval1 = countHvalue(neigh1.x, neigh1.y); 		double hval2 = countHvalue(neigh2.x, neigh2.y);				double hval3 = countHvalue(neigh3.x, neigh3.y); 		double hval4 = countHvalue(neigh4.x, neigh4.y);
				double hval5 = countHvalue(neigh5.x, neigh5.y); 		double hval6 = countHvalue(neigh6.x, neigh6.y);				double hval7 = countHvalue(neigh7.x, neigh7.y); 		double hval8 = countHvalue(neigh8.x, neigh8.y);
				
				double gval1 = countGvalue(neigh1.x, neigh1.y);		double gval2 = countGvalue(neigh2.x, neigh2.y);			double gval3 = countGvalue(neigh3.x, neigh3.y);		double gval4 = countGvalue(neigh4.x, neigh4.y);
				double gval5 = countGvalue(neigh5.x, neigh5.y);		double gval6 = countGvalue(neigh6.x, neigh6.y);		double gval7 = countGvalue(neigh7.x, neigh7.y);		double gval8 = countGvalue(neigh8.x, neigh8.y);
				
				double fval1 = hval1 + gval1;	double fval2 = hval2 + gval2;	double fval3 = hval3 + gval3;	double fval4 = hval4 + gval4;
				double fval5 = hval5 + gval5;	double fval6 = hval6 + gval6;	double fval7 = hval7 + gval7;	double fval8 = hval8 + gval8;
				
				hValsArr[0] = hval1; hValsArr[1] = hval2; hValsArr[2] = hval3; hValsArr[3] = hval4; hValsArr[4] = hval5; hValsArr[5] = hval6; hValsArr[6] = hval7; hValsArr[7] = hval8;
				fValsArr[0] = fval1;	fValsArr[1] = fval2;	fValsArr[2] = fval3;	fValsArr[3] = fval4;	fValsArr[4] = fval5;	fValsArr[5] = fval6;	fValsArr[6] = fval7;	fValsArr[7] = fval8;
				neighs[0] = neigh1;neighs[1] = neigh2;neighs[2] = neigh3;neighs[3] = neigh4;neighs[4] = neigh5;neighs[5] = neigh6;neighs[6] = neigh7;neighs[7] = neigh8;
				
				for(int i=0; i < neighs.length; i++){
					if(!wallOfset.contains(neighs[i])){
						tempNeighbors.add(neighs[i]);
						tempHvalues.add(hValsArr[i]);
						tempFvalues.add(fValsArr[i]);
					}
				}
				
/*				for(int i=0; i < neighs.length; i++){
					if(!wallPositions.contains(neighs[i])){
						tempNeighbors.add(neighs[i]);
						tempHvalues.add(hValsArr[i]);
						tempFvalues.add(fValsArr[i]);
					}
				}*/

				findMin(tempFvalues);
				min = tempFvalues.get(minInd);
				
				bestNeighbor = new Point(); currentObservedNeighbor = new Point();
				int minNeiSum = 0;
				for(int looper = 0; looper<tempFvalues.size(); looper++){
					Double currentNeighValue = tempFvalues.get(looper);
					currentObservedNeighbor = tempNeighbors.get(looper);
					// IT COUNTS HEURISTICS AS THE SAME. CORRECT IT
						if(currentNeighValue==min){
							minNeiSum++;
							if(minNeiSum>=2){
								double prevNeiHvalue = tempHvalues.get(tempNeighbors.indexOf(bestNeighbor));
								double currentNeiHvalue = tempHvalues.get(looper);
								if(prevNeiHvalue<currentNeiHvalue){
									//System.out.println(" current H is higher ");
								} else if(prevNeiHvalue == currentNeiHvalue && pathList.contains(currentObservedNeighbor)){
									//System.out.println("min is " + min + " current is " + currentNeighValue);
									bestNeighbor = bestNeighbor;
									lastPoint = bestNeighbor;
									//pathList.add(bestNeighbor);
								}
								else if (prevNeiHvalue>currentNeiHvalue){
									//System.out.println("min is " + min + " current is " + currentNeighValue);
									bestNeighbor = currentObservedNeighbor;
									lastPoint = bestNeighbor;
									pathList.add(currentObservedNeighbor);
								}
							} else if(minNeiSum<2 && !pathList.contains(currentObservedNeighbor)){
								bestNeighbor = currentObservedNeighbor;
								lastPoint = bestNeighbor;
								pathList.add(currentObservedNeighbor);								
							} else if(minNeiSum<2 && pathList.contains(currentObservedNeighbor)){
								//search for the next minimum value
								tempFvalues.remove(minInd);
								tempHvalues.remove(minInd);
								tempNeighbors.remove(minInd);
								findMin(tempFvalues);
								min = tempFvalues.get(minInd);
								
								bestNeighbor = tempNeighbors.get(minInd);
								lastPoint = bestNeighbor;
								pathList.add(currentObservedNeighbor);								
							}
							//System.out.println("min is " + min + " current is " + currentNeighValue);
							
						}
						else if(currentNeighValue!=min){
							closedList.add(currentObservedNeighbor);
						}
				}//end of loop
				if(bestNeighbor.equals(finishPoint)){
					System.out.println("path is " + pathList.toString());
					//System.out.println("path size " + pathList.size());
					break;
				}
				tempNeighbors.clear(); tempFvalues.clear(); tempHvalues.clear();
			}
		
		
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - currentTime;
		System.out.println("time spent for exec Astar " + totalTime);
}
	
	private double findMin(ArrayList<Double> tempFvalues){
		double min = tempFvalues.get(0);
		for(int i = 0; i<tempFvalues.size(); i++){
			if(tempFvalues.get(i)<min){
				min = tempFvalues.get(i);
				minInd = i;
			}
		}
		return minInd;
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

