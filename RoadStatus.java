import java.util.Arrays;

class RoadStatus {
    private int[] roadStatusArray = new int[3];
    private int signal;
    private int needChangeTime;
    private int systemTime;

    public RoadStatus() {
        Arrays.fill(roadStatusArray, 0);
        signal = 3;
        needChangeTime = -1;
        systemTime = -1;
    }

    public void addCar(int time, int id, int numOfCars) {
        while (time > systemTime) {
            if (systemTime == needChangeTime) {applySignalFunction(systemTime);}
            systemTime += 1;
            if (signal != 3) {roadStatusArray[signal] -= 1;}
        }
        roadStatusArray[id] += numOfCars;
        if (systemTime == needChangeTime || signal == 3) {applySignalFunction(time);}
    }

    public int[] roadStatus(int time) {
        while (time > systemTime) {
            if (systemTime == needChangeTime) {applySignalFunction(systemTime);}
            systemTime += 1;
            if (signal != 3) {roadStatusArray[signal] -= 1;}
        }
        return roadStatusArray.clone();
    }

    private void applySignalFunction(int time) {
        int maxCars = 0;
        int maxIndex = -1;
        for (int i = 0; i < 3; i++) {
            if (roadStatusArray[i] > maxCars) {
                maxCars = roadStatusArray[i];
                maxIndex = i;
            }
        }
        if (maxCars > 0) {signal = maxIndex;}
        else {signal = 3;}
        if (signal != 3) {needChangeTime = time + roadStatusArray[signal];} 
        else {needChangeTime = time;}
    }

    public static void main(String[] args) {
        // Example 1
        System.out.println("Example 1: ");
        RoadStatus sol1 = new RoadStatus();
        sol1.addCar(0, 0, 2);
        System.out.println("0: " + Arrays.toString(sol1.roadStatus(0)));
        sol1.addCar(0, 1, 3);
        System.out.println("0: " + Arrays.toString(sol1.roadStatus(0)));
        System.out.println("1: " + Arrays.toString(sol1.roadStatus(1)));
        sol1.addCar(2, 0, 4);
        for (int i = 2; i < 12; ++i)
            System.out.println(i + ": " + Arrays.toString(sol1.roadStatus(i)));
        // Example 2
        System.out.println("Example 2: ");
        RoadStatus sol2 = new RoadStatus();
        sol2.addCar(0, 0, 2);
        System.out.println("0: " + Arrays.toString(sol2.roadStatus(0)));
        sol2.addCar(0, 0, 1);
        System.out.println("0: " + Arrays.toString(sol2.roadStatus(0)));
        System.out.println("1: " + Arrays.toString(sol2.roadStatus(1)));
        sol2.addCar(2, 1, 2);
        for (int i = 2; i < 7; ++i)
            System.out.println(i + ": " + Arrays.toString(sol2.roadStatus(i)));
        // Example 3
        System.out.println("Example 3: ");
        RoadStatus sol3 = new RoadStatus();
        sol3.addCar(0, 0, 1);
        System.out.println("0: " + Arrays.toString(sol3.roadStatus(0)));
        System.out.println("1: " + Arrays.toString(sol3.roadStatus(1)));
        System.out.println("2: " + Arrays.toString(sol3.roadStatus(2)));
        sol3.addCar(3, 1, 1);
        System.out.println("3: " + Arrays.toString(sol3.roadStatus(3)));
        sol3.addCar(3, 1, 1);
        System.out.println("3: " + Arrays.toString(sol3.roadStatus(3)));
        sol3.addCar(4, 0, 2);
        for (int i = 4; i < 10; i++) {
            System.out.println(i + ": " + Arrays.toString(sol3.roadStatus(i)));
        }
    }
}