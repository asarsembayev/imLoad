package TestingPack;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TestQuadrification {
	
	int cell_step = 0;
	int rgb = 245;
	
	Point lastPoint;
	Point neigh1, neigh2, neigh3, neigh4, neigh5, neigh6, neigh7, neigh8;
		
	List<Point2D>quadList = new ArrayList<Point2D>();
	
	List<Point2D>openList; List<Point2D>closedList = new ArrayList<Point2D>(); public List<Point2D>quadPathList = new ArrayList<Point2D>();
	HashMap<Double, Point> temporaryFValsOfNeighbors = new HashMap<>(); HashMap<Point, Double> temporaryGvalsOfNeighbors = new HashMap<>();
	
	Double[]hValsArr = new Double[8]; Double[]fValsArr = new Double[8]; Point[]neighs = new Point[8]; //?
	
	int robotPosX; int robotPosY; int xFinish; int yFinish;
	double hValue; double gValue; double fValue;
	

	Point bestNeighbor; Point currentObservedNeighbor;
	double min; int minInd = 0; 
	ArrayList<Double> tempFvalues = new ArrayList<>();
	ArrayList<Double> tempHvalues = new ArrayList<>();
	ArrayList<Point> tempNeighbors = new ArrayList<>();
	

	public TestQuadrification(BufferedImage bImage, List<Point2D> openList, List<Point2D> wallPositions, 
			List<Point2D> wallOfset, Dimension robSize, Point robotLocation, Point finishPoint) {
		//THIS IS CONSTRUCTOR TODO Auto-generated constructor stub
		long currentTime = System.currentTimeMillis();
		
		Dimension robDim = robSize;
		int imageBorderW = bImage.getWidth(); int imageBorderH = bImage.getHeight();
		int rWidth = Math.round((robDim.width + 9)/10 * 10);	int rheight = Math.round((robDim.height + 9)/10 * 10); //robots dimensions
		cell_step = rWidth;
		for(int i = 0; i < bImage.getWidth(); i++){
			for(int j = 0; j < bImage.getHeight(); j++){
				if(i!=0 && j != 0 && i != imageBorderW && j!=imageBorderH ){
					if(i%cell_step==0){
						if(j%cell_step==0){
							Point2D p = new Point();
							p.setLocation(i, j);
							quadList.add(p);
							bImage.setRGB(i, j, rgb);
						}
					}
				}
			}
		}
		//System.out.println("Cells of quadList are " + quadList.toString());
		//return bImage; 
		
		chechTheNeighbors(openList, robotLocation, finishPoint, wallOfset, wallPositions);
		
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - currentTime;
		System.out.println("time spent for exec Quad " + totalTime);
	}
	
	
	
	//constructor
	
	private void chechTheNeighbors(List<Point2D> openList,Point robotLocation, Point finishPoint, List<Point2D> wallOfset, List<Point2D> wallPositions){
		robotPosX = robotLocation.x; robotPosY = robotLocation.y;
		lastPoint = robotLocation;
		xFinish = finishPoint.x; yFinish = finishPoint.y;
		hValue = countHvalue(robotPosX, robotPosY); gValue = 0;
		while(true){
				//List<Point> TemporaryNeighborsList = new ArrayList<Point>(); DELETE MAYBE?
				neigh1 = new Point(lastPoint.x - cell_step, lastPoint.y - cell_step); 		neigh2 = new Point(lastPoint.x, lastPoint.y - cell_step);			neigh3 = new Point(lastPoint.x + cell_step, lastPoint.y - cell_step); 		neigh4 = new Point(lastPoint.x + cell_step, lastPoint.y);
				neigh5 = new Point(lastPoint.x + cell_step, lastPoint.y + cell_step); 		neigh6 = new Point(lastPoint.x, lastPoint.y + cell_step);			neigh7 = new Point(lastPoint.x - cell_step, lastPoint.y + cell_step); 		neigh8 = new Point(lastPoint.x - cell_step, lastPoint.y);
												
				double hval1 = countHvalue(neigh1.x, neigh1.y); 		double hval2 = countHvalue(neigh2.x, neigh2.y);				double hval3 = countHvalue(neigh3.x, neigh3.y); 		double hval4 = countHvalue(neigh4.x, neigh4.y);
				double hval5 = countHvalue(neigh5.x, neigh5.y); 		double hval6 = countHvalue(neigh6.x, neigh6.y);				double hval7 = countHvalue(neigh7.x, neigh7.y); 		double hval8 = countHvalue(neigh8.x, neigh8.y);
				
				double gval1 = countGvalue(neigh1.x, neigh1.y);		double gval2 = countGvalue(neigh2.x, neigh2.y);			double gval3 = countGvalue(neigh3.x, neigh3.y);		double gval4 = countGvalue(neigh4.x, neigh4.y);
				double gval5 = countGvalue(neigh5.x, neigh5.y);		double gval6 = countGvalue(neigh6.x, neigh6.y);		double gval7 = countGvalue(neigh7.x, neigh7.y);		double gval8 = countGvalue(neigh8.x, neigh8.y);
				
				double fval1 = hval1 + gval1;	double fval2 = hval2 + gval2;	double fval3 = hval3 + gval3;	double fval4 = hval4 + gval4;
				double fval5 = hval5 + gval5;	double fval6 = hval6 + gval6;	double fval7 = hval7 + gval7;	double fval8 = hval8 + gval8;
				
				hValsArr[0] = hval1; hValsArr[1] = hval2; hValsArr[2] = hval3; hValsArr[3] = hval4; hValsArr[4] = hval5; hValsArr[5] = hval6; hValsArr[6] = hval7; hValsArr[7] = hval8;
				fValsArr[0] = fval1;	fValsArr[1] = fval2;	fValsArr[2] = fval3;	fValsArr[3] = fval4;	fValsArr[4] = fval5;	fValsArr[5] = fval6;	fValsArr[6] = fval7;	fValsArr[7] = fval8;
				neighs[0] = neigh1;neighs[1] = neigh2;neighs[2] = neigh3;neighs[3] = neigh4;neighs[4] = neigh5;neighs[5] = neigh6;neighs[6] = neigh7;neighs[7] = neigh8;
				
				 //double min = neighVals[0]; double max = neighVals[0];				

				for(int i=0; i < neighs.length; i++){
					if(!wallOfset.contains(neighs[i]) && !wallPositions.contains(neighs[i])){
						tempNeighbors.add(neighs[i]);
						tempHvalues.add(hValsArr[i]);
						tempFvalues.add(fValsArr[i]);
					}
				}
				
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
								} else if(prevNeiHvalue == currentNeiHvalue && quadPathList.contains(currentObservedNeighbor)){
									bestNeighbor = bestNeighbor;
									lastPoint = bestNeighbor;
								}
								else if (prevNeiHvalue>currentNeiHvalue){
									bestNeighbor = currentObservedNeighbor;
									lastPoint = bestNeighbor;
									quadPathList.add(currentObservedNeighbor);
								}
							} else if(minNeiSum<2 && !quadPathList.contains(currentObservedNeighbor)){
								bestNeighbor = currentObservedNeighbor;
								lastPoint = bestNeighbor;
								quadPathList.add(currentObservedNeighbor);								
							} else if(minNeiSum<2 && quadPathList.contains(currentObservedNeighbor)){
								//search for the next minimum value
								tempFvalues.remove(minInd);
								tempHvalues.remove(minInd);
								tempNeighbors.remove(minInd);
								findMin(tempFvalues);
								min = tempFvalues.get(minInd);
								
								bestNeighbor = tempNeighbors.get(minInd);
								lastPoint = bestNeighbor;
								quadPathList.add(currentObservedNeighbor);								
							}
						}
						else if(currentNeighValue!=min){
							closedList.add(currentObservedNeighbor);
						}
				}//end of loop

				if( ( Math.abs(bestNeighbor.x - finishPoint.x) > 1 && Math.abs(bestNeighbor.x - finishPoint.x) < 20) 
						&& ( Math.abs(bestNeighbor.y - finishPoint.y) > 1 && Math.abs(bestNeighbor.y - finishPoint.y) < 20)){
					//System.out.println("path from quadPathList is " + quadPathList.toString());
					//System.out.println("path size " + pathList.size());
					break;
				}
				tempNeighbors.clear(); tempFvalues.clear(); tempHvalues.clear();
			}
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





