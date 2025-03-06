import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

// BattleShip by @Pv_McM

public class BattleShip {

    static int row;
    static int col;
    static int size;
    static boolean horizontal;

    static int remainParts;
    static int hits;
    static int misses;
    static int totalShots;

    static boolean isHit;
    static int lastHitRow;
    static int lastHitCol;

    // Grid size for the game
    static final int GRID_SIZE = 10;

    // Player 1's main grid containing their ships
    static char[][] player1Grid = new char[GRID_SIZE][GRID_SIZE];

    // Player 2's main grid containing their ships
    static char[][] player2Grid = new char[GRID_SIZE][GRID_SIZE];

    // Player 1's tracking grid to show their hits and misses
    static char[][] player1TrackingGrid = new char[GRID_SIZE][GRID_SIZE];

    // Player 2's tracking grid to show their hits and misses
    static char[][] player2TrackingGrid = new char[GRID_SIZE][GRID_SIZE];

    // Scanner object for user input
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=======================");
        System.out.println("=  BattleShip by McM  =");
        System.out.println("=======================");
        System.out.println();
        System.out.println("Choose your option:");
        System.out.println("1.PvP (Normal Match)");
        System.out.println("2.PvP (Prize Table)");
        System.out.println("3.Play vs AI (Easy mode)");
        System.out.println("4.Play vs AI (Hard mode)");
        System.out.println("5.Exit");

        switch (scanner.nextInt()) {
            case 1: runNormal(); break;
            case 2: runPrizeTable(); break;
            case 3: runEasyAI(); break;
            case 4: runHardAI(); break;
            case 5: System.exit(0);
        }
    }

    static void runNormal(){
        initializeGrids();
        boolean player1Turn = true;

        while (!isGameOver()) {
            if (player1Turn) {
                System.out.println("Player 1's turn:");
                checkGrid(player2Grid);
                printGrid(player1TrackingGrid);
                playerTurn(player2Grid, player1TrackingGrid);
            } else {
                System.out.println("Player 2's turn:");
                checkGrid(player1Grid);
                printGrid(player2TrackingGrid);
                playerTurn(player1Grid, player2TrackingGrid);
            }
            player1Turn = !player1Turn;
        }

        System.out.println("Game Over!");
        if (player1Turn) {
            System.out.println("Player 2 won!");
        }
        else{
            System.out.println("Player 1 won!");
        }
    }

    static void runPrizeTable(){
        initializeGrids();
        boolean player1Turn = true;
        while (!isGameOver()) {
            if (player1Turn) {
                System.out.println("Player 1's turn:");
                checkGrid(player2Grid);
                printGrid(player1TrackingGrid);
                playerTurn(player2Grid, player1TrackingGrid);
            }
            else{
                System.out.println("Player 2's turn:");
                checkGrid(player1Grid);
                printGrid(player2TrackingGrid);
                playerTurn(player1Grid, player2TrackingGrid);
            }

            if(!isHit) player1Turn = !player1Turn;

        }
        System.out.println("Game Over!");
        if (player1Turn) {
            System.out.println("Player 1 won!");
        }
        else{
            System.out.println("Player 2 won!");
        }
    }

    static void runEasyAI(){
        initializeGrids();
        boolean player1Turn = true;
        while (!isGameOver()) {
            if (player1Turn) {
                System.out.println("Player 1's turn:");
                checkGrid(player2Grid);
                printGrid(player1TrackingGrid);
                playerTurn(player2Grid, player1TrackingGrid);
            }
            else{
                checkGrid(player1Grid);
                printGrid(player2TrackingGrid);
                makeEasyMoveAI(player1Grid, player2TrackingGrid);
            }
            player1Turn = !player1Turn;
        }
        System.out.println("Game Over!");
        if (player1Turn) {
            System.out.println("You lost!");
        }
        else{
            System.out.println("You won!");
        }
    }

    static void runHardAI(){
        initializeGrids();
        boolean player1Turn = true;
        while (!isGameOver()) {
            if (player1Turn) {
                System.out.println("Player 1's turn:");
                checkGrid(player2Grid);
                printGrid(player1TrackingGrid);
                playerTurn(player2Grid, player1TrackingGrid);
            }
            else{
                checkGrid(player1Grid);
                printGrid(player2TrackingGrid);
                if(!isHit) makeEasyMoveAI(player1Grid, player2TrackingGrid);
                else makeHardMoveAI(player1Grid, player2TrackingGrid);
            }
            player1Turn = !player1Turn;
        }
        System.out.println("Game Over!");
        if (player1Turn) {
            System.out.println("You lost!");
        }
        else{
            System.out.println("You won!");
        }
    }

    static List<int[]> hardMoveTargets = new ArrayList<>();

    static void makeHardMoveAI(char[][] opponentGrid, char[][] trackingGrid) {
        Random rand = new Random();
        int[][] directions = { {0, 1}, {0, -1}, {1, 0}, {-1, 0} };

        if(hardMoveTargets.isEmpty()) {
            for (int[] dir : directions) {
                int newRow = lastHitRow + dir[0];
                int newCol = lastHitCol + dir[1];
                if (newRow >= 0 && newRow < GRID_SIZE && newCol >= 0 && newCol < GRID_SIZE && (opponentGrid[newRow][newCol] == 's' || opponentGrid[newRow][newCol] == '~')) {
                    hardMoveTargets.add(new int[]{newRow, newCol});
                }
            }
        }

        if(hardMoveTargets.isEmpty()) {
            isHit = false;
            makeEasyMoveAI(opponentGrid, trackingGrid);
            return;
        }

        int randomHMove = rand.nextInt(hardMoveTargets.size());
        int[] hardMove = hardMoveTargets.get(randomHMove);
        int Row = hardMove[0];
        int Col = hardMove[1];

        if(opponentGrid[Row][Col] == 's') {
            System.out.println("Did Hit!\n");
            opponentGrid[Row][Col] = 'x';
            trackingGrid[Row][Col] = 'x';
            isHit = true;
            lastHitRow = Row;
            lastHitCol = Col;
            hardMoveTargets.clear();
            for (int[] dir : directions) {
                int newRow = lastHitRow + dir[0];
                int newCol = lastHitCol + dir[1];
                if (newRow >= 0 && newRow < GRID_SIZE && newCol >= 0 && newCol < GRID_SIZE && (opponentGrid[newRow][newCol] == 's' || opponentGrid[newRow][newCol] == '~')) {
                    hardMoveTargets.add(new int[]{newRow, newCol});
                }
            }
        }else{
            System.out.println("Did Miss!\n");
            opponentGrid[Row][Col] = '0';
            trackingGrid[Row][Col] = '0';
            hardMoveTargets.remove(hardMove);
        }
    }

    static void makeEasyMoveAI(char[][] opponentGrid, char[][] trackingGrid){
        isHit = false;
        int Row;
        int Col;
        do{
            Random rand = new Random();
            Row = rand.nextInt(opponentGrid.length);
            Col = rand.nextInt(opponentGrid.length);
        }while(opponentGrid[Row][Col] != '~');

        if(opponentGrid[(Row)][Col] == 's'){
            System.out.println("Did Hit!\n");
            opponentGrid[(Row)][Col] = 'x';
            trackingGrid[(Row)][Col] = 'x';
            isHit = true;
            lastHitRow = Row;
            lastHitCol = Col;
        }
        if(opponentGrid[(Row)][Col] == '~'){
            System.out.println("Did Miss!\n");
            opponentGrid[(Row)][Col] = '0';
            trackingGrid[(Row)][Col] = '0';
        }
    }

    static void initializeGrid(char[][] grid) {
        for(int x = 0; x < GRID_SIZE; x++){
            for(int y = 0; y < GRID_SIZE; y++){
                grid[x][y] = '~';
            }
        }
    }

    static void initializeGrids() {
        initializeGrid(player1Grid);
        initializeGrid(player2Grid);
        initializeGrid(player1TrackingGrid);
        initializeGrid(player2TrackingGrid);

        placeShips(player1Grid);
        placeShips(player2Grid);
    }

    static void placeShips(char[][] grid) {
        for(int x=5; x>=2; x--){
            do{
                Random rand = new Random();
                row = rand.nextInt(GRID_SIZE);
                col = rand.nextInt(GRID_SIZE);
                size = x;
                horizontal = rand.nextBoolean();
            } while(!canPlaceShip(grid ,row ,col ,size ,horizontal));

            for(int i=0; i<size; i++){
                if(horizontal){
                    grid[row][col+i] = 's';
                }
                else{
                    grid[row+i][col] = 's';
                }
            }
        }
    }

    static boolean canPlaceShip(char[][] grid, int row, int col, int size, boolean horizontal) {
        if(horizontal){
            if(col + size > grid.length) return false;
            for(int x=0; x<size; x++){
                int col_temp = col + x;
                if(grid[row][col_temp] != '~') return false;
            }
        }else{
            if(row + size > grid.length) return false;
            for(int x=0; x<size; x++){
                int row_temp = row + x;
                if(grid[row_temp][col] != '~') return false;
            }
        }
        return true;
    }

    static void playerTurn(char[][] opponentGrid, char[][] trackingGrid) {
        isHit = false;
        while(true){
            String input = scanner.next();
            if(!isValidInput(input)) {
                System.out.println("Invalid input! Try again!");
                continue;
            }
            char colChar = input.charAt(0);
            int Row = Character.getNumericValue(input.charAt(1));
            int Col = (int) (colChar - 'A');
            if(opponentGrid[(Row)][Col] == 'x' || opponentGrid[(Row)][Col] == '0'){
                System.out.println("Invalid move! Try again!");
                continue;
            }
            if(opponentGrid[(Row)][Col] == 's'){
                System.out.println("You Hit!\n");
                opponentGrid[(Row)][Col] = 'x';
                trackingGrid[(Row)][Col] = 'x';
                isHit = true;
                break;
            }
            if(opponentGrid[(Row)][Col] == '~'){
                System.out.println("You Miss!\n");
                opponentGrid[(Row)][Col] = '0';
                trackingGrid[(Row)][Col] = '0';
                break;
            }
        }
    }

    static void checkGrid(char[][] grid) {
        remainParts = 0;
        hits = 0;
        misses = 0;
        for(int x=0; x<GRID_SIZE; x++){
            for(int y=0; y<GRID_SIZE; y++){
                if(grid[x][y] == 's') remainParts++;
                if(grid[x][y] == 'x') hits++;
                if(grid[x][y] == '0') misses++;
            }
        }
        totalShots = hits + misses;
    }

    static boolean isGameOver() {
        return allShipsSunk(player1Grid) || allShipsSunk(player2Grid);
    }

    static boolean allShipsSunk(char[][] grid) {
        for(int x=0; x<GRID_SIZE; x++){
            for(int y=0; y<GRID_SIZE; y++){
                if(grid[x][y] == 's') return false;
            }
        }
        return true;
    }

    static boolean isValidInput(String input) {
        if(input.isEmpty()) return false;
        if(input.length() != 2) return false;
        return Character.isLetter(input.charAt(0)) && Character.isDigit(input.charAt(1));
    }

    static void printGrid(char[][] grid) {
        System.out.print("  ");
        for(int x=0; x<GRID_SIZE; x++){
            System.out.print((char)(x + 65) + " ");
        }
        System.out.println();
        for(int x=0; x<GRID_SIZE; x++){
            System.out.print(x + " ");
            for(int y=0; y<GRID_SIZE; y++){
                System.out.print(grid[x][y] + " ");
            }
            System.out.println();
        }
        System.out.println("Current table stats: hits: " + hits + " totalShots: " + totalShots + " remainParts: " + remainParts);
    }
}
