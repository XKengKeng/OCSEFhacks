package src;

public class Shop {
    private static final int[] PRICES = {500, 800, 1500}; // Water, Soil, Cake

    public boolean buyItem(Player player, int itemType) {
        if (player.getTotalSteps() >= PRICES[itemType]) {
            switch (itemType) {
                case 0: player.useWater(); break;
                case 1: player.useSoil(); break;
                case 2: player.useCake(); break;
            }
            player.addSteps(-PRICES[itemType]);
            return true;
        }
        return false;
    }

    public int getPrice(int itemType) {
        return PRICES[itemType];
    }
}