import java.util.Arrays;

public class ImageSegmentation {
    private int[][] image;
    private int[] parent;
    private int[] size;
    private int segmentCount;
    private int largestColor;
    private int largestSize;

    public ImageSegmentation(int N, int[][] inputImage) {
        image = new int[N][N];
        for (int i = 0; i < N; i++) {
            System.arraycopy(inputImage[i], 0, image[i], 0, N);
        }
        int totalPixels = N * N;
        parent = new int[totalPixels];
        size = new int[totalPixels];
        Arrays.fill(size, 1);
        segmentCount = 0;
        largestColor = 0;
        largestSize = 0;

        for (int i = 0; i < totalPixels; i++) {
            parent[i] = i;
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int current = i * N + j;
                if (image[i][j] != 0) {
                    segmentCount++;
                    if (i > 0 && image[i - 1][j] == image[i][j]) union(current, (i - 1) * N + j);
                    if (j > 0 && image[i][j - 1] == image[i][j]) union(current, i * N + (j - 1));
                }
            }
        }
    }

    private int find(int p) {
        while (p != parent[p]) {
            parent[p] = parent[parent[p]]; // Path compression
            p = parent[p];
        }
        return p;
    }

    private void union(int p, int q) {
        int rootP = find(p);
        int rootQ = find(q);
        if (rootP == rootQ) return;

        if (size[rootP] < size[rootQ]) {
            parent[rootP] = rootQ;
            size[rootQ] += size[rootP];
        } else {
            parent[rootQ] = rootP;
            size[rootP] += size[rootQ];
        }
        segmentCount--;
    }

    public int countDistinctSegments() {
        return segmentCount;
    }

    public int[] findLargestSegment() {
        int N = image.length;
        int[] segmentSizes = new int[N * N];
        Arrays.fill(segmentSizes, 0);

        // Calculate the size of each segment
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int current = i * N + j;
                if (image[i][j] != 0) {
                    int root = find(current);
                    segmentSizes[root]++;
                }
            }
        }

        int maxSegmentSize = 0;
        int smallestColor = Integer.MAX_VALUE;

        // Find the largest segment considering the smallest color index
        for (int i = 0; i < N * N; i++) {
            if (segmentSizes[i] > maxSegmentSize) {
                maxSegmentSize = segmentSizes[i];
                smallestColor = image[i / N][i % N];
            } else if (segmentSizes[i] == maxSegmentSize && image[i / N][i % N] < smallestColor) {
                smallestColor = image[i / N][i % N];
            }
        }

        largestSize = maxSegmentSize;
        largestColor = smallestColor;

        return new int[]{largestSize, largestColor};
    }

    public static void main(String args[]) {

        // Example 1:
        int[][] inputImage1 = {
            {0, 0, 0},
            {0, 1, 1},
            {0, 0, 1}
        };

        System.out.println("Example 1:");

        ImageSegmentation s = new ImageSegmentation(3, inputImage1);
        System.out.println("Number of Distinct Segments: " + s.countDistinctSegments());

        int[] largest = s.findLargestSegment();
        System.out.println("Size of the Largest Segment: " + largest[0]);
        System.out.println("Color of the Largest Segment: " + largest[1]);


        // Example 2:
        int[][] inputImage2 = {
               {0, 0, 0, 3, 0},
               {0, 2, 3, 3, 0},
               {1, 2, 2, 0, 0},
               {1, 2, 2, 1, 1},
               {0, 0, 1, 1, 1}
        };

        System.out.println("\nExample 2:");

        s = new ImageSegmentation(5, inputImage2);
        System.out.println("Number of Distinct Segments: " + s.countDistinctSegments());

        largest = s.findLargestSegment();
        System.out.println("Size of the Largest Segment: " + largest[0]);
        System.out.println("Color of the Largest Segment: " + largest[1]);

    }

}