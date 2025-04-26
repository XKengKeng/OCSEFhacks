package src;

public class GameUI extends AppCompatActivity {
    private GameManager game;
    private TextView stepsText, plantStatusText;
    private ImageView plantImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        game = GameManager.getInstance();
        stepsText = findViewById(R.id.stepsText);
        plantStatusText = findViewById(R.id.plantStatus);
        plantImage = findViewById(R.id.plantImage);

        setupButtons();
        updateUI();
    }

    private void setupButtons() {
        findViewById(R.id.waterBtn).setOnClickListener(v -> {
            if (game.getPlayer().useWater()) {
                game.getPlantGirl().giveWater();
                updateUI();
            }
        });

        findViewById(R.id.shopBtn).setOnClickListener(v -> {
            startActivity(new Intent(this, ShopActivity.class));
        });
    }

    private void updateUI() {
        stepsText.setText("Steps: " + game.getPlayer().getTotalSteps());
        plantStatusText.setText(game.getPlantGirl().getStatus());
        updatePlantImage();
    }

    private void updatePlantImage() {
        int resId;
        if (game.getPlantGirl().isWilting()) {
            resId = R.drawable.plant_wilting;
        } else {
            switch (game.getPlantGirl().getGrowthStage()) {
                case 0: resId = R.drawable.plant_stage1; break;
                case 1: resId = R.drawable.plant_stage2; break;
                default: resId = R.drawable.plant_stage3;
            }
        }
        plantImage.setImageResource(resId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        game.updateGame();
        updateUI();
    }
}