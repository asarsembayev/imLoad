package TestingPack;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class TestAstar {
	List<Point2D>openList; List<Point2D>closedList; List<Point2D>pathList;
	int robotPosX; int robotPosY; int xFinish; int yFinish;
	double hValue; double gValue; double fValue;
	Point neigh1, neigh2, neigh3, neigh4, neigh5, neigh6, neigh7, neigh8;
	Point lastPoint;
	//constructor
	public TestAstar(List<Point2D> openList,Point robotLocation, Point finishPoint){
		robotPosX = (int) robotLocation.getX(); robotPosY = (int) robotLocation.getY();
		lastPoint = robotLocation;
		xFinish = finishPoint.x; yFinish = finishPoint.y;
		System.out.println("robot location " + robotLocation);
		System.out.println("finish location " + finishPoint);
		hValue = countHvalue(robotPosX, robotPosY);
		System.out.println("the distance from robot to finish is " + hValue);
		//hValue = countDist();
		HashMap<Double, Point> fValsOfNeighbors = new HashMap<>();
	
		for(int i = 0; i < 1; i++){
			neigh1 = new Point(lastPoint.x - 1, lastPoint.y - 1);
			neigh2 = new Point(lastPoint.x, lastPoint.y - 1);
			neigh3 = new Point(lastPoint.x + 1, lastPoint.y - 1);
			neigh4 = new Point(lastPoint.x + 1, lastPoint.y);
			neigh5 = new Point(lastPoint.x + 1, lastPoint.y + 1);
			neigh6 = new Point(lastPoint.x, lastPoint.y + 1);
			neigh7 = new Point(lastPoint.x - 1, lastPoint.y + 1);
			neigh8 = new Point(lastPoint.x + 1, lastPoint.y);
			
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
			
			double fval1 = hval1 + gval1;
			double fval2 = hval2 + gval2;
			double fval3 = hval3 + gval3;
			double fval4 = hval4 + gval4;
			double fval5 = hval5 + gval5;
			double fval6 = hval6 + gval6;
			double fval7 = hval7 + gval7;
			double fval8 = hval8 + gval8;
			
			fValsOfNeighbors.put(fval1, neigh1); fValsOfNeighbors.put(fval2, neigh2); 
			fValsOfNeighbors.put(fval3, neigh3); fValsOfNeighbors.put(fval4, neigh4); 
			fValsOfNeighbors.put(fval5, neigh5); fValsOfNeighbors.put(fval6, neigh6); 
			fValsOfNeighbors.put(fval7, neigh7); fValsOfNeighbors.put(fval8, neigh8); 
			
			double [] neighVals = new double[8];
			neighVals[0] = fval1; neighVals[1] = fval2; neighVals[2] = fval3; neighVals[3] = fval4;
			neighVals[4] = fval5; neighVals[5] = fval6; neighVals[6] = fval7; neighVals[7] = fval8;
			double min = neighVals[0]; double max = neighVals[0];
			for(int k = 0; k<neighVals.length; k++){
				if(neighVals[k] < min){
					min = neighVals[k];
				}
				if(neighVals[k] > max){
					max = neighVals[k];
				}
			}
			System.out.println(min + " is minimum and maximum is " + max);
			Point bestNeighbor = new Point();
			Iterator<Double> id = fValsOfNeighbors.keySet().iterator();
			while(id.hasNext()){
				Double minNum = id.next();
				if(minNum == min){
					bestNeighbor = fValsOfNeighbors.get(minNum);
				} System.out.println("best nei is " + bestNeighbor + " dist is " + min);
			}
		}
	}
	
	private double countGvalue(int x, int y){
		double gVal = Math.sqrt(  
				(x-robotPosX)*(x-robotPosX) + (y - robotPosY)*(y-robotPosY)
				);
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

