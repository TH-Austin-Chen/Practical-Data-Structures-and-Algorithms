import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays; // Used to print the arrays
import java.util.Stack;

import com.google.gson.*;

class test{
    public static void main(String[] args){
        Mafia sol = new Mafia();
        Gson gson = new Gson();
        try (FileReader reader = new FileReader("Mafia.json")){
            JsonArray all = gson.fromJson(reader, JsonArray.class);
            for(JsonElement caseInList : all){
                JsonArray a = caseInList.getAsJsonArray();
                int q_cnt = 0, wa = 0,ac = 0;
                for (JsonElement o : a) {
                    q_cnt++;
                    JsonObject person = o.getAsJsonObject();
                    JsonArray arg_lvl = person.getAsJsonArray("level");
                    JsonArray arg_rng = person.getAsJsonArray("range");
                    JsonArray arg_ans = person.getAsJsonArray("answer");
                    int LVL[] = new int[arg_lvl.size()];
                    int RNG[] = new int[arg_lvl.size()];
                    int Answer[] = new int[arg_ans.size()];
                    int Answer_W[] = new int[arg_ans.size()];
                    for(int i=0;i<arg_ans.size();i++){
                        Answer[i]=(arg_ans.get(i).getAsInt());
                        if(i<arg_lvl.size()){
                            LVL[i]=(arg_lvl.get(i).getAsInt());
                            RNG[i]=(arg_rng.get(i).getAsInt());
                        }
                    }
                    Answer_W = sol.result(LVL,RNG);
                    for(int i=0;i<arg_ans.size();i++){
                        if(Answer_W[i]==Answer[i]){
                            if(i==arg_ans.size()-1){
                                System.out.println(q_cnt+": AC");
                            }
                        }else {
                            wa++;
                            System.out.println(q_cnt+": WA");
                            break;
                        }
                    }

                }
                System.out.println("Score: "+(q_cnt-wa)+"/"+q_cnt);

            }
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


class Mafia {
    public int[] result(int[] levels, int[] ranges) {
        int n = levels.length;
        int[] leftBasedOnLevel = new int[n];
        int[] rightBasedOnLevel = new int[n];
        int[] attackBounds = new int[2 * n];
        Stack<Integer> stack = new Stack<>();

        // Determine potential attack bounds based on level
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && levels[i] > levels[stack.peek()]) {
                stack.pop();
            }
            leftBasedOnLevel[i] = stack.isEmpty() ? 0 : stack.peek() + 1;
            stack.push(i);
        }

        stack.clear();

        for (int i = n - 1; i >= 0; i--) {
            while (!stack.isEmpty() && levels[i] > levels[stack.peek()]) {
                stack.pop();
            }
            rightBasedOnLevel[i] = stack.isEmpty() ? n - 1 : stack.peek() - 1;
            stack.push(i);
        }

        // Calculate attack bounds based solely on range
        for (int i = 0; i < n; i++) {
            // Left bound based on range
            int leftBasedOnRange = Math.max(i - ranges[i], 0);
            // Right bound based on range
            int rightBasedOnRange = Math.min(i + ranges[i], n - 1);

            // Finalize the attack bounds taking into account both level and range
            attackBounds[2 * i] = Math.max(leftBasedOnLevel[i], leftBasedOnRange);
            attackBounds[2 * i + 1] = Math.min(rightBasedOnLevel[i], rightBasedOnRange);
        }

        return attackBounds;
    }

    public static void main(String[] args) {
        Mafia sol = new Mafia();
        System.out.println(Arrays.toString(
            sol.result(new int[]{11, 13, 11, 7, 15},
                       new int[]{1, 8, 1, 7, 2})));
        // Expected Output: [0, 0, 0, 3, 2, 3, 3, 3, 2, 4]
    }
}