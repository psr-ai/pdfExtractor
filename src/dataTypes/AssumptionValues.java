package dataTypes;

public class AssumptionValues {
	public static final float distanceBetweenSimilarLines = 2;
	public static final float verticalDistanceBetweenSimilarLines = 2;
	public static final float increasingLengthOfLinesForBetterIntersection = 1;
	public static final float wordsCollaboratingToCellFactor = (float) 1.85;
	
	public static float min(float x1, float x2){		
		if (x1<x2) return x1;
		else return x2;
	}
	
	public static float max(float x1, float x2){
		if (x1>x2) return x1;
		else return x2;
	}
}
