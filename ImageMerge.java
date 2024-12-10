import java.util.*;

class Event {
    double x;
    int type; // 0 for start, 1 for end
    double[] rectangle; // [x, y, w, h]

    Event(double x, int type, double[] rectangle) {
        this.x = x;
        this.type = type;
        this.rectangle = rectangle;
    }
}

class Interval {
    double lo, hi;
    double[] rectangle;

    Interval(double lo, double hi, double[] rectangle) {
        this.lo = lo;
        this.hi = hi;
        this.rectangle = rectangle;
    }
}

class IntervalTree {
    private Node root;

    private class Node {
        Interval interval;
        double max;
        Node left, right;

        Node(Interval interval) {
            this.interval = interval;
            this.max = interval.hi;
        }
    }

    public void put(double lo, double hi, double[] rectangle) {
        root = put(root, new Interval(lo, hi, rectangle));
    }

    private Node put(Node x, Interval interval) {
        if (x == null) {
            return new Node(interval);
        }

        double cmp = interval.lo - x.interval.lo;
        if (cmp < 0) x.left = put(x.left, interval);
        else x.right = put(x.right, interval);

        x.max = Math.max(x.max, getMax(x));
        return x;
    }

    public void delete(double lo, double hi) {
        root = delete(root, lo, hi);
    }

    private Node delete(Node x, double lo, double hi) {
        if (x == null) return null;

        double cmp = lo - x.interval.lo;
        if (cmp < 0) x.left = delete(x.left, lo, hi);
        else if (cmp > 0) x.right = delete(x.right, lo, hi);
        else if (Double.compare(hi, x.interval.hi) == 0) {
            if (x.right == null) return x.left;
            if (x.left == null) return x.right;

            Node t = x;
            x = min(t.right);
            x.right = deleteMin(t.right);
            x.left = t.left;
        } else {
            x.right = delete(x.right, lo, hi);
        }

        x.max = getMax(x);
        return x;
    }

    private Node min(Node x) {
        return (x.left != null) ? min(x.left) : x;
    }

    private Node deleteMin(Node x) {
        if (x.left == null) return x.right;
        x.left = deleteMin(x.left);
        x.max = getMax(x);
        return x;
    }

    private double getMax(Node x) {
        double max = x.interval.hi;
        if (x.left != null) max = Math.max(max, x.left.max);
        if (x.right != null) max = Math.max(max, x.right.max);
        return max;
    }

    public List<double[]> query(double lo, double hi) {
        List<double[]> result = new ArrayList<>();
        query(root, lo, hi, result);
        return result;
    }

    private void query(Node x, double lo, double hi, List<double[]> result) {
        if (x == null) return;

        boolean goLeft = x.left != null && x.left.max >= lo;
        boolean goRight = x.right != null && x.right.max >= lo;

        if (goLeft) query(x.left, lo, hi, result);
        if (overlaps(x.interval.lo, x.interval.hi, lo, hi)) result.add(x.interval.rectangle);
        if (goRight && x.interval.lo <= hi) query(x.right, lo, hi, result);
    }

    private boolean overlaps(double lo1, double hi1, double lo2, double hi2) {
        return lo1 <= hi2 && hi1 >= lo2;
    }
}

class ImageMerge {
    private double[][] boundingBoxes;
    private double iouThreshold;
    private int[] parent;
    private int[] size;

    public ImageMerge(double[][] bbs, double iou_thresh) {
        this.boundingBoxes = bbs;
        this.iouThreshold = iou_thresh;
        int totalRectangles = bbs.length;
        this.parent = new int[totalRectangles];
        this.size = new int[totalRectangles];
        Arrays.fill(size, 1);
        for (int i = 0; i < totalRectangles; i++) {
            parent[i] = i;
        }
    }

    private int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    private void union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        if (rootX == rootY) return;

        if (size[rootX] < size[rootY]) {
            parent[rootX] = rootY;
            size[rootY] += size[rootX];
        } else {
            parent[rootY] = rootX;
            size[rootX] += size[rootY];
        }
    }

    private double calculateIoU(double[] box1, double[] box2) {
        double left = Math.max(box1[0], box2[0]);
        double right = Math.min(box1[0] + box1[2], box2[0] + box2[2]);
        double bottom = Math.max(box1[1], box2[1]);
        double top = Math.min(box1[1] + box1[3], box2[1] + box2[3]);

        if (right > left && top > bottom) {
            double intersection = (right - left) * (top - bottom);
            double union = box1[2] * box1[3] + box2[2] * box2[3] - intersection;
            return intersection / union;
        }
        return 0;
    }

    public double[][] mergeBox() {
        List<Event> events = new ArrayList<>();
        IntervalTree intervalTree = new IntervalTree();

        for (int i = 0; i < boundingBoxes.length; i++) {
            events.add(new Event(boundingBoxes[i][0], 0, boundingBoxes[i]));
            events.add(new Event(boundingBoxes[i][0] + boundingBoxes[i][2], 1, boundingBoxes[i]));
        }

        events.sort((e1, e2) -> Double.compare(e1.x, e2.x));

        for (Event event : events) {
            if (event.type == 0) {
                List<double[]> overlaps = intervalTree.query(event.rectangle[1], event.rectangle[1] + event.rectangle[3]);
                for (double[] overlap : overlaps) {
                    if (calculateIoU(event.rectangle, overlap) >= iouThreshold) {
                        int idx1 = Arrays.asList(boundingBoxes).indexOf(event.rectangle);
                        int idx2 = Arrays.asList(boundingBoxes).indexOf(overlap);
                        union(idx1, idx2);
                    }
                }
                intervalTree.put(event.rectangle[1], event.rectangle[1] + event.rectangle[3], event.rectangle);
            } else {
                intervalTree.delete(event.rectangle[1], event.rectangle[1] + event.rectangle[3]);
            }
        }

        Map<Integer, List<double[]>> groups = new HashMap<>();
        for (int i = 0; i < boundingBoxes.length; i++) {
            int root = find(i);
            groups.putIfAbsent(root, new ArrayList<>());
            groups.get(root).add(boundingBoxes[i]);
        }

        List<double[]> mergedBoxes = new ArrayList<>();
        for (List<double[]> group : groups.values()) {
            double x1 = Double.MAX_VALUE, y1 = Double.MAX_VALUE;
            double x2 = Double.MIN_VALUE, maxY = Double.MIN_VALUE;
            for (double[] box : group) {
                x1 = Math.min(x1, box[0]);
                y1 = Math.min(y1, box[1]);
                x2 = Math.max(x2, box[0] + box[2]);
                maxY = Math.max(maxY, box[1] + box[3]);
            }
            mergedBoxes.add(new double[]{x1, y1, x2 - x1, maxY - y1});
        }

        mergedBoxes.sort((a, b) -> {
            if (a[0] != b[0]) return Double.compare(a[0], b[0]);
            if (a[1] != b[1]) return Double.compare(a[1], b[1]);
            if (a[2] != b[2]) return Double.compare(a[2], b[2]);
            return Double.compare(a[3], b[3]);
        });

        return mergedBoxes.toArray(new double[0][]);
    }
    
    // Methods draw() and main() are untouched as requested
    // public static void draw(double[][] bbs)
    // {
    //     // ** NO NEED TO MODIFY THIS FUNCTION, WE WON'T CALL THIS **
    //     // ** DEBUG ONLY, USE THIS FUNCTION TO DRAW THE BOX OUT** 
    //     StdDraw.setCanvasSize(960,540);
    //     for(double[] box : bbs)
    //     {
    //         double half_width = (box[2]/2.0);
    //         double half_height = (box[3]/2.0);
    //         double center_x = box[0]+ half_width;
    //         double center_y = box[1] + half_height;
    //         //StdDraw use y = 0 at the bottom, 1-center_y to flip
    //         StdDraw.rectangle(center_x, 1-center_y, half_width,half_height);
    //     }
    // }

    public static void main(String[] args) {
        ImageMerge sol = new ImageMerge(
                new double[][]{
                        {0.02,0.01,0.1,0.05},{0.0,0.0,0.1,0.05},{0.04,0.02,0.1,0.05},{0.06,0.03,0.1,0.05},{0.08,0.04,0.1,0.05},
                        {0.24,0.01,0.1,0.05},{0.20,0.0,0.1,0.05},{0.28,0.02,0.1,0.05},{0.32,0.03,0.1,0.05},{0.36,0.04,0.1,0.05},
                },
                0.5
        );
        double[][] temp = sol.mergeBox();
        // ImageMerge.draw(temp);
    } 
}
