package src;
import java.util.Calendar;

public class Player {
    // Basic player stats
    private String name;
    private int totalSteps;
    private int todaySteps;
    private Calendar lastStepUpdate;
    private int pendingSteps;
    
    // Inventory
    private int water;
    private int soil;
    private int cakes;
    
    // Game progress
    private int totalDays;

    //Tutorial
    private boolean hasSeenTutorial;
    
    // Step bonuses
    private static final int[] STEP_BONUS_THRESHOLDS = {1000, 2000, 5000};
    private static final int[] STEP_BONUS_VALUES = {150, 300, 800};
    
    //Player constructor
    public Player(String name) {
        this.name = name;
        this.totalSteps = 0;
        this.todaySteps = 0;
        this.lastStepUpdate = Calendar.getInstance();
        
        // Starting inventory
        this.water = 3;
        this.soil = 2;
        this.cakes = 0;
        
        this.totalDays = 1;
        this.hasSeenTutorial = false;
    }
    //Import steps
    public void importStepsFromHealthApp(int healthAppSteps) {
        int newSteps = healthAppSteps - this.totalSteps;
        if (newSteps > 0) {
            pendingSteps += newSteps;
        }
    }
    
    public void processPendingSteps() {
        if (pendingSteps > 0) {
            addSteps(pendingSteps);
            pendingSteps = 0;
        }
    }
    // Core step functionality
    public void addSteps(int newSteps) {
        Calendar today = Calendar.getInstance();
        
        // Reset daily steps if it's a new day
        if (!isSameDay(today, lastStepUpdate)) {
            todaySteps = 0;
            totalDays++;
        }
        
        todaySteps += newSteps;
        totalSteps += newSteps;
        lastStepUpdate = today;
        
        checkStepBonuses();
    }
    
    private boolean isSameDay(Calendar day1, Calendar day2) {
        return day1.get(Calendar.YEAR) == day2.get(Calendar.YEAR) &&
               day1.get(Calendar.DAY_OF_YEAR) == day2.get(Calendar.DAY_OF_YEAR);
    }
    
    private void checkStepBonuses() {
        for (int i = 0; i < STEP_BONUS_THRESHOLDS.length; i++) {
            if (totalSteps == STEP_BONUS_THRESHOLDS[i]) {
                totalSteps += STEP_BONUS_VALUES[i];
                break; // Only apply one bonus at a time
            }
        }
    }
    
    // Inventory management
    public boolean useWater() {
        if (water > 0) {
            water--;
            return true;
        }
        return false;
    }
    
    public boolean useSoil() {
        if (soil > 0) {
            soil--;
            return true;
        }
        return false;
    }
    
    public boolean useCake() {
        if (cakes > 0) {
            cakes--;
            return true;
        }
        return false;
    }
    
    // Shop purchases
    public boolean buyWater(int cost) {
        if (totalSteps >= cost) {
            totalSteps -= cost;
            water++;
            return true;
        }
        return false;
    }
    
    public boolean buySoil(int cost) {
        if (totalSteps >= cost) {
            totalSteps -= cost;
            soil++;
            return true;
        }
        return false;
    }
    
    public boolean buyCake(int cost) {
        if (totalSteps >= cost) {
            totalSteps -= cost;
            cakes++;
            return true;
        }
        return false;
    }
    
    // Getters
    public String getName() { return name; }
    public int getTotalSteps() { return totalSteps; }
    public int getTodaySteps() { return todaySteps; }
    public int getWaterCount() { return water; }
    public int getSoilCount() { return soil; }
    public int getCakeCount() { return cakes; }
    public int getTotalDays() { return totalDays; }
    public boolean hasSeenTutorial() { return hasSeenTutorial; }
    
    // Setters
    public void markTutorialSeen() { hasSeenTutorial = true; }
    
    // Save game state (for Android SharedPreferences)
    public String serialize() {
        return name + "," + 
               totalSteps + "," + 
               todaySteps + "," + 
               lastStepUpdate.getTimeInMillis() + "," + 
               water + "," + 
               soil + "," + 
               cakes + "," + 
               totalDays + "," + 
               hasSeenTutorial;
    }
    
    // Load game state
    public static Player deserialize(String data) {
        String[] parts = data.split(",");
        Player player = new Player(parts[0]);
        player.totalSteps = Integer.parseInt(parts[1]);
        player.todaySteps = Integer.parseInt(parts[2]);
        
        Calendar lastUpdate = Calendar.getInstance();
        lastUpdate.setTimeInMillis(Long.parseLong(parts[3]));
        player.lastStepUpdate = lastUpdate;
        
        player.water = Integer.parseInt(parts[4]);
        player.soil = Integer.parseInt(parts[5]);
        player.cakes = Integer.parseInt(parts[6]);
        player.totalDays = Integer.parseInt(parts[7]);
        player.hasSeenTutorial = Boolean.parseBoolean(parts[8]);
        
        return player;
    }
}