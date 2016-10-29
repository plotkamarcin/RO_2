package classifier;

import java.util.Comparator;

public class DistanceMetric {

	private double MetricValue;
    public double getMetricValue() {
		return MetricValue;
	}


	public void setMetricValue(double metricValue) {
		MetricValue = metricValue;
	}


	public int getLabel() {
		return Label;
	}


	public void setLabel(int label) {
		Label = label;
	}


	private int Label;

	public DistanceMetric(){
		
	}
	public DistanceMetric(double value, int label){
        this.MetricValue=value;
        this.Label=label;
	}

	
}
