package extractor;

/**
 * Copied from the NIST Big-Data-Project into Versus-transformations for the GLCM Transformation
 */

/**
 *
 * @author Julien Amelot
 */

public class GLCM {

    public final int width;
    public final int height;
    public final int levels;
    public /*final*/ int[][] leveled;
    private double[][] GLCM;
    public final boolean symmetric;
    public final boolean normalize;
    public final int dx;
    public final int dy;
    //
    private Double energy;
    private Double contrast;
    private Double homogeneity;
    private Double correlation;

    public GLCM(int[][] leveled, int dx, int dy, int levels, boolean symmetric, boolean normalize) {

        this.leveled = leveled;
        this.levels = levels;
        this.symmetric = symmetric;
        this.normalize = normalize;
        width = leveled.length;
        height = leveled[0].length;
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Factory, to create from the
     * matrix instead of leveled
     * A new leveled matrix is computed. use GLCMs to create multiple GLCMs optimally
     * @return 
     */
    public static GLCM createGLCM(double[][] matrix, int dx, int dy, int levels, boolean symmetric, boolean normalize, double min, double max) {
        int[][] leveled = computeLeveled(matrix, levels, min, max);
        return new GLCM(leveled, dx, dy, levels, symmetric, normalize);
    }

    public double[][] getGLCM() {

        if (GLCM == null) {
            GLCM = computeGLCM();

        }
        return GLCM;
    }

    public double[][] computeGLCM() {
        if (GLCM == null) {
            GLCM = new double[levels][levels];

            int sum = 0;
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    //range
                    if (x + dx >= 0 && y + dy >= 0 && x + dx < width && y + dy < height) {
                        int v1 = leveled[x][y];
                        int v2 = leveled[x + dx][y + dy];
                        sum++;
                        GLCM[v1][v2]++;
                        if (symmetric) {
                            GLCM[v2][v1]++;
                        }
                    }
                }
            }

            //normalize
            if (normalize) {
                if (symmetric) {
                    sum *= 2;//was counted only once to optimize
                }
                for (int x = 0; x < levels; x++) {
                    for (int y = 0; y < levels; y++) {
                        GLCM[x][y] /= sum;
                    }
                }
            }
        }

        return GLCM;
    }

    public double computeContrast() {
        if (contrast == null) {
            if (!normalize) {
                throw new Error("must be normalized");
            }
            computeGLCM();

            contrast = 0d;

            for (int x = 0; x < levels; x++) {
                for (int y = 0; y < levels; y++) {
                    contrast += (x - y) * (x - y) * GLCM[x][y];
                }
            }
        }

        return contrast;
    }

    public double computeCorrelation() {
        if (correlation == null) {
            if (!normalize) {
                throw new Error("must be normalized");
            }
            computeGLCM();
            correlation = 0d;

            //
            double meanX = 0;
            double meanY = 0;

            for (int x = 0; x < levels; x++) {
                for (int y = 0; y < levels; y++) {
                    meanX += x * GLCM[x][y];
                    meanY += y * GLCM[x][y];
                }
            }

            //
            double stdX = 0;
            double stdY = 0;

            for (int x = 0; x < levels; x++) {
                for (int y = 0; y < levels; y++) {
                    stdX += (x - meanX) * (x - meanX) * GLCM[x][y];
                    stdY += (y - meanY) * (y - meanY) * GLCM[x][y];
                }
            }
            stdX = Math.sqrt(stdX);
            stdY = Math.sqrt(stdY);

            //
            for (int x = 0; x < levels; x++) {
                for (int y = 0; y < levels; y++) {
                    double num = (x - meanX) * (y - meanY) * GLCM[x][y];
                    double denum = stdX * stdY;
                    if (denum==0){
                    	return 0.0;
                    }
                    correlation += num / denum;
                }
            }
        }

        return correlation;
    }

    public double computeEnergy() {
        if (energy == null) {
            if (!normalize) {
                throw new Error("must be normalized");
            }
            computeGLCM();
            energy = 0d;

            for (int x = 0; x < levels; x++) {
                for (int y = 0; y < levels; y++) {
                    energy += GLCM[x][y] * GLCM[x][y];
                }
            }
        }

        return energy;
    }

    public double computeHomogeneity() {
        if (homogeneity == null) {
            if (!normalize) {
                throw new Error("must be normalized");
            }
            computeGLCM();
            homogeneity = 0d;

            for (int x = 0; x < levels; x++) {
                for (int y = 0; y < levels; y++) {
                    homogeneity += GLCM[x][y] / (1 + Math.abs(x - y));
                }
            }
        }
        return homogeneity;
    }

    /**
     * static so that it can be computed only once for an array of GLCM
     * @param matrix
     * @param levels
     * @param min
     * @param max
     * @return 
     */
    public static int[][] computeLeveled(double[][] matrix, int levels, double min, double max) {
        int[][] _leveled = new int[matrix.length][matrix[0].length];

        for (int x = 0; x < matrix.length; x++) {
            for (int y = 0; y < matrix[0].length; y++) {

                int l = (int) (Math.floor((matrix[x][y] - min) * levels / (double) max));
                if (l < 0) {
                    l = 0;
                } else if (l >= levels) {
                    l = levels - 1;
                }
                _leveled[x][y] = l;
            }
        }

        return _leveled;
    }

    /**
     * for GC
     */
    public void clean() {
        GLCM = null;
    }

    public void deepClean() {
        clean();
        leveled = null;
    }

    public void computeAllTextureFeatures() {
        computeContrast();
        computeCorrelation();
        computeEnergy();
        computeHomogeneity();
    }
}

