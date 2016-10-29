package classifier;

import java.util.Comparator;

class DistanceMetricComparator implements Comparator<DistanceMetric>{

	@Override
	public int compare(DistanceMetric o1, DistanceMetric o2) {
		return Double.compare(o1.getMetricValue(), o2.getMetricValue());
		//return (Double)o1.getMetricValue()<o2.getMetricValue() ? -1 : o1.getMetricValue()==o2.getMetricValue()?0:1;
	}
	


}
