package com.teamthirty.buyhighselllow.Screens;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.gridlayout.widget.GridLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.teamthirty.buyhighselllow.Entities.Towers.*;
import com.teamthirty.buyhighselllow.Systems.PlayerSystem;
import com.teamthirty.buyhighselllow.Utilities.Difficulty;
import com.teamthirty.buyhighselllow.R;
import com.teamthirty.buyhighselllow.Utilities.TowerType;
import androidx.core.util.Pair;
import java.util.ArrayList;

public class GameScreen extends AppCompatActivity implements View.OnClickListener {
    private GridLayout mapLayout;
    private ArrayList<Pair<Integer, Integer>> path;
    private Button[][] mapArray;
    private TowerType towerType;
    private PlayerSystem playerSystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);
        Bundle extras = getIntent().getExtras();
        String playerName = extras.getString("name");
        Difficulty difficulty = (Difficulty) extras.get("difficulty");
        playerSystem = new PlayerSystem(difficulty);

        // User interface buttons
        Button redditDude = findViewById(R.id.RedditDude);
        Button tradingChad = findViewById(R.id.TradingChad);
        Button cryptoWhale = findViewById(R.id.CryptoWhale);
        // THIS IS HARD-CODED AND NEEDS TO GO
        path = new ArrayList<>();
        path.add(new Pair<>(3, 8));
        path.add(new Pair<>(3, 7));
        path.add(new Pair<>(3, 6));
        path.add(new Pair<>(3, 5));
        path.add(new Pair<>(3, 4));
        path.add(new Pair<>(3, 3));
        path.add(new Pair<>(3, 2));
        path.add(new Pair<>(3, 1));
        path.add(new Pair<>(3, 0));
        makeMap();

        redditDude.setOnClickListener(view -> setTowerType(TowerType.RedditDude));
        tradingChad.setOnClickListener(view -> setTowerType(TowerType.TradingChad));
        cryptoWhale.setOnClickListener(view -> setTowerType(TowerType.CryptoWhale));

        TextView playerNameText = findViewById(R.id.playerName);
        playerNameText.setText(playerName);

        TextView monumentHealthText = findViewById(R.id.monumentHealth);
        TextView playerCashText = findViewById(R.id.playerCash);
        int cash = 0;
        int monumentHealth = 0;
        switch (difficulty) {
        case HARD: // hard difficulty
            cash = 600;
            monumentHealth = 60;
            break;
        case STANDARD: // medium difficulty
            cash = 800;
            monumentHealth = 80;
            break;
        case EASY: // easy difficulty
        default:
            // default is easy mode
            cash = 1000;
            monumentHealth = 100;
            break;
        }
        playerSystem.setMoney(cash);

        monumentHealthText.setText("Monument HP: " + monumentHealth);
        playerCashText.setText("Player Cash: " + cash);
        playerCashText.setTextSize(15);

        for (Button[] buttonArray : mapArray) {
            for (Button button : buttonArray) {
                button.setOnClickListener(this);
            }
        }
    }

    private void setTowerType(TowerType newType) {
        towerType = newType;
    }

    private void makeMap() {
        // Layout object representing the map
        GridLayout mapLayout = findViewById(R.id.map);
        mapArray = new Button[mapLayout.getRowCount()][mapLayout.getColumnCount()];
        for (int i = 0; i < mapLayout.getRowCount(); i++) {
            for (int j = 0; j < mapLayout.getColumnCount(); j++) {
                Pair<Integer, Integer> temp = new Pair<>(i, j);
                if (!path.contains(temp)) {
                    mapArray[i][j] = makeMapButton(mapLayout, this, Color.GREEN, i, j);
                    mapLayout.addView(mapArray[i][j]);
                } else {
                    mapArray[i][j] = makeMapButton(mapLayout, this, Color.GRAY, i, j);
                    mapLayout.addView(mapArray[i][j]);
                }
            }
        }
        Pair<Integer, Integer> monumentLocation = path.get(0);
        int monumentRow = monumentLocation.first;
        int monumentColumn = monumentLocation.second;
        mapArray[monumentRow][monumentColumn].setBackgroundColor(Color.MAGENTA);
    }

    private Button makeMapButton(GridLayout mapLayout, Context context, int color, int row,
                                 int column) {
        GridLayout.LayoutParams buttonParams = new GridLayout.LayoutParams();
        buttonParams.height = 0;
        buttonParams.width = 0;
        buttonParams.rowSpec = GridLayout.spec(row, (float) 1);
        buttonParams.columnSpec = GridLayout.spec(column, (float) 1);

        //Buttons on the grid that handle tower press placement
        Button button = new Button(context);
        String id;
        if (row == 0) {
            id = "999" + column;
        } else {
            id = row + "000" + column;
        }
        button.setId(Integer.parseInt(id));
        button.setBackgroundColor(color);
        button.setLayoutParams(buttonParams);

        return button;
    }

    @Override
    public void onClick(View v) {
        String id = v.getId() + "";
        int row;
        int column;

        if (id.contains("999")) {
            row = 0;
            column = Integer.parseInt(id.substring(3));
        } else {
            int endOfRowIndex = id.indexOf('0');
            row = Integer.parseInt(id.substring(0, endOfRowIndex));
            column = Integer.parseInt(id.substring(endOfRowIndex + 3));
        }
        Tower tower = null;
        Pair<Integer, Integer> towerLocation = new Pair<>(row, column);

        if (path.contains(towerLocation)) {
            String errorMessage = "Cannot place tower on path!";
            int popUpDuration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, errorMessage, popUpDuration);
            toast.show();
        } else {
            if (towerType == null) {
                String errorMessage = "Select a tower type to place!";
                int popUpDuration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(this, errorMessage, popUpDuration);
                toast.show();
            } else {
                if (((ColorDrawable) mapArray[row][column].getBackground()).getColor()
                    != (Color.GREEN)) {
                    String errorMessage = "Cannot place tower on top of another tower!";
                    int popUpDuration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(this, errorMessage, popUpDuration);
                    toast.show();
                } else {
                    if (towerType.equals(TowerType.RedditDude)) {
                        tower = new RedditDude(towerLocation);
                    } else if (towerType.equals(TowerType.TradingChad)) {
                        tower = new TradingChad(towerLocation);
                    } else if (towerType.equals(TowerType.CryptoWhale)) {
                        tower = new CryptoWhale(towerLocation);
                    }

                    //playerSystem.addEntity(tower);
                    boolean towerBought = playerSystem.buyTower(tower, towerType, this);
                    if (towerBought) {
                        if (towerType.equals(TowerType.RedditDude)) {
                            mapArray[row][column].setBackgroundColor(Color.YELLOW);
                        } else if (towerType.equals(TowerType.TradingChad)) {
                            mapArray[row][column].setBackgroundColor(Color.BLUE);
                        } else if (towerType.equals(TowerType.CryptoWhale)) {
                            mapArray[row][column].setBackgroundColor(Color.BLACK);
                        }
                        TextView playerCashText = findViewById(R.id.playerCash);
                        playerCashText.setText("Player Cash: " + playerSystem.getMoney());
                    }
                }
            }
        }
    }
}