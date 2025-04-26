package src;

import java.util.Calendar;

public class PlantGirl {
    // Growth stages
    private int growthStage;
    private static final int MAX_GROWTH_STAGE = 3;
    
    // Affection system
    private int affectionLevel;
    private static final int MAX_AFFECTION = 100;
    
    // Class constructor
    public PlantGirl() {
        this.affectionLevel = 10; // Starting affection
        this.currentHealth = HealthState.NORMAL;
        this.lastCareTime = Calendar.getInstance();
        this.daysWithoutCare = 0;
        this.growthStage = 0;
    }
    
    //Character status update
    public void updateStatus(Calendar currentTime) {
        // Calculate days since last care
        long diffInMillis = currentTime.getTimeInMillis() - lastCareTime.getTimeInMillis();
        int daysPassed = (int) (diffInMillis / (1000 * 60 * 60 * 24));
        
        if (daysPassed > 0) {
            daysWithoutCare += daysPassed;
            lastCareTime = currentTime;
            
            // Update health based on neglect
            if (daysWithoutCare >= 3) {
                currentHealth = HealthState.WILTING;
                affectionLevel = Math.max(0, affectionLevel - (10 * (daysWithoutCare - 2)));
            } else if (daysWithoutCare >= 1) {
                currentHealth = HealthState.NORMAL;
                affectionLevel = Math.max(0, affectionLevel - 5);
            }
            
            // Check for growth progression
            if (affectionLevel >= (growthStage + 1) * 25 && growthStage < MAX_GROWTH_STAGE) {
                growthStage++;
            }
        }
        
        // Update health state based on affection
        if (currentHealth != HealthState.WILTING) {
            if (affectionLevel >= 75) {
                currentHealth = HealthState.THRIVING;
            } else if (affectionLevel >= 30) {
                currentHealth = HealthState.NORMAL;
            }
        }
    }

    // Growth stages with drawable resources
    public enum GrowthStage {
        SEEDLING(R.drawable.plant_stage1, 0, 25),
        ADULT(R.drawable.plant_stage2, 25, 60),
        BLOOMING(R.drawable.plant_stage3, 60, 100);
        
        private final int drawableId;
        private final int minAffection;
        private final int maxAffection;
        
        GrowthStage(int drawableId, int min, int max) {
            this.drawableId = drawableId;
            this.minAffection = min;
            this.maxAffection = max;
        }
        
        public int getDrawableId() { return drawableId; }
        
        public static GrowthStage forAffection(int affection) {
            for (GrowthStage stage : values()) {
                if (affection >= stage.minAffection && affection < stage.maxAffection) {
                    return stage;
                }
            }
            return BLOOMING; // Default to highest stage
        }
    }
    
    // Modified to include stage visuals
    public int getCurrentDrawableId() {
        GrowthStage stage = GrowthStage.forAffection(affectionLevel);
        
        // Apply health modifiers
        if (currentHealth == HealthState.WILTING) {
            return R.drawable.plant_wilting;
        } else if (currentHealth == HealthState.REVIVING) {
            return R.drawable.plant_reviving;
        }
        
        return stage.getDrawableId();
    }
    // Health states
    public enum HealthState {
        THRIVING("Thriving", R.drawable.plant_happy),
        NORMAL("Normal", R.drawable.plant_normal),
        WILTING("Wilting", R.drawable.plant_wilting),
        REVIVING("Reviving", R.drawable.plant_reviving);
        
        private String displayName;
        private int drawableId;
        
        HealthState(String name, int id) {
            this.displayName = name;
            this.drawableId = id;
        }
        
        public String getDisplayName() { return displayName; }
        public int getDrawableId() { return drawableId; }
    }
    
    private HealthState currentHealth;
    private Calendar lastCareTime;
    private int daysWithoutCare;
    

    
    
    
    // Interaction methods
    public void giveWater() {
        affectCareAction(5);
    }
    
    public void giveSoil() {
        affectCareAction(8);
    }
    
    public void giveCake() {
        affectCareAction(15);
    }
    
    private void affectCareAction(int affectionGain) {
        affectionLevel = Math.min(MAX_AFFECTION, affectionLevel + affectionGain);
        daysWithoutCare = 0;
        lastCareTime = Calendar.getInstance();
        
        if (currentHealth == HealthState.WILTING) {
            currentHealth = HealthState.REVIVING;
        } else if (affectionLevel >= 75) {
            currentHealth = HealthState.THRIVING;
        } else {
            currentHealth = HealthState.NORMAL;
        }
    }
    
    // Dialogue system
    public String getDialogue() {
        if (currentHealth == HealthState.WILTING) {
            return "*Weak whisper* I need your care...";
        }
        
        String[] baseDialogue = {
            "The sunlight feels nice today...",
            "Did you know I can photosynthesize happiness?",
            "*Rustling leaves*",
            "Your steps make me grow stronger"
        };
        
        String[] highAffectionDialogue = {
            "I bloom just for you...",
            "Your care means everything to me",
            "*Leaves brush against your hand*",
            "Let's stay together forever"
        };
        
        if (affectionLevel > 70) {
            return highAffectionDialogue[(int)(Math.random() * highAffectionDialogue.length)];
        }
        return baseDialogue[(int)(Math.random() * baseDialogue.length)];
    }
    
    // Getters
    public int getAffectionLevel() { return affectionLevel; }
    public HealthState getHealthState() { return currentHealth; }
    public int getGrowthStage() { return growthStage; }
    public int getDaysWithoutCare() { return daysWithoutCare; }
    
    // For saving game state
    public String serialize() {
        return affectionLevel + "," +
               currentHealth.name() + "," +
               lastCareTime.getTimeInMillis() + "," +
               daysWithoutCare + "," +
               growthStage;
    }
    
    public static PlantGirl deserialize(String data) {
        String[] parts = data.split(",");
        PlantGirl plant = new PlantGirl();
        plant.affectionLevel = Integer.parseInt(parts[0]);
        plant.currentHealth = HealthState.valueOf(parts[1]);
        
        Calendar lastCare = Calendar.getInstance();
        lastCare.setTimeInMillis(Long.parseLong(parts[2]));
        plant.lastCareTime = lastCare;
        
        plant.daysWithoutCare = Integer.parseInt(parts[3]);
        plant.growthStage = Integer.parseInt(parts[4]);
        
        return plant;
    }
}