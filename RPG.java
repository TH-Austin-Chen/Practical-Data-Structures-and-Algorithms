class RPG {
    private int[] defence; // Array storing the defence values of the dragon for each round
    private int[] attack; // Array storing the attack values of the hero for each round
    private Integer[][] memo; // Memoization table to store the maximum damage up to round n with and without boost

    // Constructor to initialize the RPG game with dragon's defence and hero's attack values
    public RPG(int[] defence, int[] attack) {
        this.defence = defence;
        this.attack = attack;
        // Initialize the memoization table, extra dimension for boosted state (0: not boosted, 1: boosted)
        this.memo = new Integer[defence.length + 1][2];
    }

    // Helper method to recursively calculate the maximum damage
    // Parameters: n (current round), boosted (if the hero is currently boosted or not)
    private int maxDamageUtil(int n, boolean boosted) {
        if (n == 0) {
            // Base case: if there are no rounds left, no damage can be inflicted
            return 0;
        }
        
        // Check if this state has already been computed to avoid re-computation
        if (memo[n][boosted ? 1 : 0] != null) {
            return memo[n][boosted ? 1 : 0];
        }
        
        // Calculate damage if attacking this round without any boost
        int damageAttack = attack[n-1] - defence[n-1] + maxDamageUtil(n-1, false);
        
        int damageBoost = 0;
        if (n > 1) { // Check if there's a next round to benefit from boosting
            // Calculate potential damage for boosting this round (for the next round)
            // We ensure that the next round's damage calculation takes the boost into account
            damageBoost = (2 * attack[n-1] - defence[n-1]) + maxDamageUtil(n-2, false);
        }
        
        // Store the maximum of attacking or boosting in the memoization table
        memo[n][boosted ? 1 : 0] = Math.max(damageAttack, damageBoost);
        return memo[n][boosted ? 1 : 0];
    }
    
    // Public method to start the calculation of maximum damage
    // It always starts with not boosted state
    public int maxDamage(int n) {
        return maxDamageUtil(n, false);
    }
    
    // Main method to test the RPG class
    public static void main(String[] args) {
        RPG sol = new RPG(new int[]{5, 4, 1, 7, 98, 2}, new int[]{200, 200, 200, 200, 200, 200});
        System.out.println(sol.maxDamage(6)); // Expected to print the maximum damage over 6 rounds
    }
}