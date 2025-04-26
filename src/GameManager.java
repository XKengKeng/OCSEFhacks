package src;

public class GameManager {
    private Player player;
    private PlantGirl plantGirl;
    private Shop shop;
    private static GameManager instance;

    private GameManager() {
        this.player = new Player("Player");
        this.plantGirl = new PlantGirl();
        this.shop = new Shop();
    }

    public static GameManager getInstance() {
        if (instance == null) instance = new GameManager();
        return instance;
    }

    public void updateGame() {
        plantGirl.update();
        // Add daily events here
    }

    public Player getPlayer() { return player; }
    public PlantGirl getPlantGirl() { return plantGirl; }
    public Shop getShop() { return shop; }

    public void saveGame(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("GameData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("steps", player.getTotalSteps());
        editor.putInt("affection", plantGirl.getAffection());
        editor.apply();
    }

    public void loadGame(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("GameData", Context.MODE_PRIVATE);
        player.addSteps(prefs.getInt("steps", 0));
        plantGirl.setAffection(prefs.getInt("affection", 10));
    }
}