package src;
public class ShopActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        GameManager game = GameManager.getInstance();
        TextView stepsText = findViewById(R.id.shopStepsText);
        stepsText.setText("Steps: " + game.getPlayer().getTotalSteps());

        setupShopItems();
    }

    private void setupShopItems() {
        int[] buttonIds = {R.id.buyWaterBtn, R.id.buySoilBtn, R.id.buyCakeBtn};
        for (int i = 0; i < buttonIds.length; i++) {
            final int itemType = i;
            findViewById(buttonIds[i]).setOnClickListener(v -> {
                if (game.getShop().buyItem(game.getPlayer(), itemType)) {
                    Toast.makeText(this, "Purchased!", Toast.LENGTH_SHORT).show();
                    finish(); // Close shop
                } else {
                    Toast.makeText(this, "Not enough steps!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}