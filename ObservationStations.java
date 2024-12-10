import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import edu.princeton.cs.algs4.Point2D;
import java.util.Stack;
import java.util.Comparator;
import java.util.Collections;


class ObservationStationAnalysis {
    // List to hold the stations
    private ArrayList<Point2D> stations;

    // Constructor to initialize the list of stations
    public ObservationStationAnalysis(ArrayList<Point2D> stations) {
        this.stations = stations;
    }

    // Method to find the point with the lowest y-coordinate (and lowest x-coordinate if there's a tie)
    private static Point2D findLowestPoint(ArrayList<Point2D> points) {
        return Collections.min(points, Point2D.Y_ORDER);
    }

    // Method to perform the Graham Scan algorithm and return the vertices of the convex hull
    private ArrayList<Point2D> grahamScan(ArrayList<Point2D> points) {
        if (points.size() < 3) return new ArrayList<>(points); // Convex hull is not defined for fewer than 3 points

        // Find the point with the lowest y-coordinate to use as a reference for sorting
        Point2D lowest = findLowestPoint(points);

        // Sort points based on polar angle with respect to 'lowest'. Break ties by distance.
        points.sort(new Comparator<Point2D>() {
            @Override
            public int compare(Point2D p1, Point2D p2) {
                double dx1 = p1.x() - lowest.x();
                double dy1 = p1.y() - lowest.y();
                double dx2 = p2.x() - lowest.x();
                double dy2 = p2.y() - lowest.y();

                double angle1 = Math.atan2(dy1, dx1);
                double angle2 = Math.atan2(dy2, dx2);

                if (angle1 < angle2) return -1;
                else if (angle1 > angle2) return 1;
                else {
                    double dist1 = dx1 * dx1 + dy1 * dy1;
                    double dist2 = dx2 * dx2 + dy2 * dy2;
                    return Double.compare(dist1, dist2);
                }
            }
        });

        // Initialize a stack to store the vertices of the convex hull
        Stack<Point2D> hull = new Stack<>();
        hull.push(points.get(0));
        hull.push(points.get(1));

        // Iterate through the sorted list of points to construct the convex hull
        for (int i = 2; i < points.size(); i++) {
            Point2D top = hull.pop();
            while (!hull.isEmpty() && Point2D.ccw(hull.peek(), top, points.get(i)) <= 0) {
                top = hull.pop();
            }
            hull.push(top);
            hull.push(points.get(i));
        }

        return new ArrayList<>(hull);
    }

    // Method to find the farthest two stations in the list
    public Point2D[] findFarthestStations() {
        ArrayList<Point2D> hull = grahamScan(stations);
        double maxDistance = 0;
        Point2D[] farthest = new Point2D[2];

        // Compare each pair of points in the convex hull to find the farthest pair
        for (int i = 0; i < hull.size(); i++) {
            for (int j = i + 1; j < hull.size(); j++) {
                double distance = hull.get(i).distanceTo(hull.get(j));
                if (distance > maxDistance) {
                    maxDistance = distance;
                    farthest[0] = hull.get(i);
                    farthest[1] = hull.get(j);
                }
            }
        }

        // Sort the farthest pair based on the specified criteria
        Comparator<Point2D> byPolarRadius = Comparator.comparingDouble(Point2D::r);
        Comparator<Point2D> byYCoordinate = Comparator.comparingDouble(Point2D::y);
        Comparator<Point2D> byPolarRadiusThenY = byPolarRadius.thenComparing(byYCoordinate);

        if (byPolarRadiusThenY.compare(farthest[0], farthest[1]) > 0) {
            Point2D temp = farthest[0];
            farthest[0] = farthest[1];
            farthest[1] = temp;
        }

        return farthest;
    }

    // Method to calculate the area covered by the convex hull
    public double coverageArea() {
        ArrayList<Point2D> hull = grahamScan(stations);
        double area = 0.0;
        for (int i = 0; i < hull.size(); i++) {
            int j = (i + 1) % hull.size();
            area += hull.get(i).x() * hull.get(j).y();
            area -= hull.get(j).x() * hull.get(i).y();
        }
        area = Math.abs(area) / 2.0;
        return area;
    }

    public void addNewStation(Point2D newStation) {
        stations.add(newStation);
    }
    
    public static void main(String[] args) throws Exception {

        ArrayList<Point2D> stationCoordinates = new ArrayList<>();
        stationCoordinates.add(new Point2D(0, 0));
        stationCoordinates.add(new Point2D(2, 0));
        stationCoordinates.add(new Point2D(3, 2));
        stationCoordinates.add(new Point2D(2, 6));
        stationCoordinates.add(new Point2D(0, 4));
        stationCoordinates.add(new Point2D(1, 1));
        stationCoordinates.add(new Point2D(2, 2));

        ObservationStationAnalysis Analysis = new ObservationStationAnalysis(stationCoordinates);
        System.out.println("Farthest Station A: "+Analysis.findFarthestStations()[0]);
        System.out.println("Farthest Station B: "+Analysis.findFarthestStations()[1]);
        System.out.println("Coverage Area: "+Analysis.coverageArea());
        
        System.out.println("Add Station (10, 3): ");
        Analysis.addNewStation(new Point2D(10, 3));
        
        System.out.println("Farthest Station A: "+Analysis.findFarthestStations()[0]);
        System.out.println("Farthest Station B: "+Analysis.findFarthestStations()[1]);
        System.out.println("Coverage Area: "+Analysis.coverageArea());
    }
}
