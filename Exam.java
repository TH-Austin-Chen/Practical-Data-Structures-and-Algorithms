import java.util.*;

class Exam {
    public static List<int[]> getPassedList(Integer[][] scores) {
        final int students = scores[0].length;
        final int subjects = scores.length;
        int[] totalScores = new int[students];
        int[] subjectsQualified = new int[students];

        for (int subject = 0; subject < subjects; subject++) {
            final int finalSubject = subject; // Make the variable effectively final for use in lambda
            PriorityQueue<Integer> pq = new PriorityQueue<>((a, b) -> Integer.compare(scores[finalSubject][b], scores[finalSubject][a]));
            for (int i = 0; i < students; i++) {
                pq.add(i);
            }

            int topPercentCount = (int) Math.ceil(students * 0.2);
            List<Integer> topStudents = new ArrayList<>();
            for (int i = 0; i < topPercentCount && !pq.isEmpty(); i++) {
                topStudents.add(pq.poll());
            }

            // Handle ties
            if (!pq.isEmpty()) {
                int lastScore = scores[finalSubject][topStudents.get(topStudents.size() - 1)];
                while (!pq.isEmpty() && scores[finalSubject][pq.peek()] == lastScore) {
                    topStudents.add(pq.poll());
                }
            }

            // Update total scores and qualification count for top students
            for (int student : topStudents) {
                totalScores[student] += scores[finalSubject][student];
                subjectsQualified[student]++;
            }
        }

        // Filter students who are top in all subjects and prepare final list
        List<int[]> resultList = new ArrayList<>();
        for (int i = 0; i < students; i++) {
            if (subjectsQualified[i] == subjects) {
                resultList.add(new int[]{i, totalScores[i]});
            }
        }

        // Sort by total score descending, then by ID ascending
        resultList.sort((a, b) -> b[1] != a[1] ? Integer.compare(b[1], a[1]) : Integer.compare(a[0], b[0]));

        return resultList;
    }

    public static void main(String[] args) {
        List<int[]> ans = getPassedList(new Integer[][]{
            {67, 82, 98, 32, 65, 76, 87, 12, 43, 75, 25},
            {42, 90, 80, 12, 76, 58, 95, 30, 67, 78, 10}
        });
        for (int[] student : ans) {
            System.out.print(Arrays.toString(student) + " ");
        }
        System.out.println();

        ans = getPassedList(new Integer[][]{
            {67, 82, 64, 32, 65, 76},
            {42, 90, 80, 12, 76, 58}
        });
        for (int[] student : ans) {
            System.out.print(Arrays.toString(student) + " ");
        }
        System.out.println();
    }
}
