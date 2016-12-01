package classifier;

public class Result {
	private int originalLabel;
	private int classifiedLabel;

	public int getOriginalLabel() {
		return originalLabel;
	}

	public void setOriginalLabel(int originalLabel) {
		this.originalLabel = originalLabel;
	}

	public int getClassifiedLabel() {
		return classifiedLabel;
	}

	public void setClassifiedLabel(int classifiedLabel) {
		this.classifiedLabel = classifiedLabel;
	}

	public Result(int o, int c) {
		this.originalLabel = o;
		this.classifiedLabel = c;
	}
}
