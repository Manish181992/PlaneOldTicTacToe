package in.co.manishkumar.pot3;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity implements DialogInterface.OnClickListener{
    private static final int PLAYER_1 = 1;
    private static final int PLAYER_2 = 2;

    private static final int IMG_PLAYER_1 = android.R.drawable.ic_delete;
    private static final int IMG_PLAYER_2 = R.drawable.circle_black;
    private static final int IMG_EMPTY = R.drawable.circle_white;
    private static final int BG_ACTIVE = R.drawable.border_bg_green;
    private static final int BG_INACTIVE = R.drawable.border_bg_white;

    private TextView labelPlayer1;
    private TextView labelPlayer2;
    private int[][] grid = new int[3][3];

    private int currentPlayer = PLAYER_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        setupReferences();
    }

    public void restart(View view) {
        recreate();
    }

    public void cellTap(View view) {
        ImageButton cell = (ImageButton) view;

        int row = (int) cell.getTag(R.string.key_row);
        int col = (int) cell.getTag(R.string.key_col);
        grid[row][col] = currentPlayer;

        if (currentPlayer == PLAYER_1) {
            cell.setImageResource(IMG_PLAYER_1);
            currentPlayer = PLAYER_2;
        } else {
            cell.setImageResource(IMG_PLAYER_2);
            currentPlayer = PLAYER_1;
        }

        highlightCurrentPlayer();
        int winner = getWinner();
        if(winner != 0){
            announceWinner(winner);
        }
    }

    private void setupReferences() {
        labelPlayer1 = (TextView) findViewById(R.id.label_player1);
        labelPlayer2 = (TextView) findViewById(R.id.label_player2);

        Resources resources = getResources();
        String packageName = getApplicationContext().getPackageName();
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                grid[row][col] = 0;

                int id = resources.getIdentifier("cell_" + row + col, "id", packageName);
                ImageButton cell = (ImageButton) findViewById(id);
                cell.setTag(R.string.key_row, row);
                cell.setTag(R.string.key_col, col);
                cell.setImageResource(IMG_EMPTY);
            }
        }
    }

    private void highlightCurrentPlayer() {
        if (currentPlayer == PLAYER_1) {
            labelPlayer2.setBackgroundResource(BG_INACTIVE);
            labelPlayer1.setBackgroundResource(BG_ACTIVE);
        } else {
            labelPlayer1.setBackgroundResource(BG_INACTIVE);
            labelPlayer2.setBackgroundResource(BG_ACTIVE);
        }
    }

    private int getWinner() {

        int winner = 0;
        // check rows
        for (int row = 0; row < 3; row++) {
            if ((grid[row][0] != 0) && (grid[row][0] == grid[row][1]) && (grid[row][1] == grid[row][2])) {
                winner = grid[row][0];
            }
        }

        // check columns
        for (int col = 0; col < 3; col++) {
            if ((grid[0][col] != 0) && (grid[0][col] == grid[1][col]) && (grid[1][col] == grid[2][col])) {
                winner = grid[0][col];
            }
        }

        // check diagonals
        if((grid[0][0] != 0) && (grid[0][0] == grid[1][1]) && (grid[1][1] == grid[2][2])){
            winner = grid[0][0];
        }

        if((grid[0][2] != 0) && (grid[0][2] == grid[1][1]) && (grid[1][1] == grid[2][0])){
            winner = grid[0][2];
        }

        return winner;
    }

    private void announceWinner(int winner){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Player " + winner + " won!!")
                .setCancelable(false)
                .setPositiveButton("New Game", this);
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        restart(null);
    }
}
