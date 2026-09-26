import java.util.*;
 
// Prince wang

public class USAMTSP1 {
 
    static final int N = 5;
 
    // given digit in each square, -1 if none
    static int[][] given = {
        { -1, -1, -1,  1,  5 },
        {  6,  1, -1,  3,  2 },
        {  2, -1, -1,  0,  1 },
        {  4, -1, -1, -1, -1 },
        {  9,  4, -1,  8, -1 },
    };
 
    // true = square is circled (its number must be prime); false = must NOT be prime.
    static boolean[][] circled = {
        { false, false, false, true,  false },
        { true,  true,  false, true,  false },
        { true,  false, false, false, false },
        { false, false, true,  false, false },
        { false, false, false, false, true  },
    };
 
    // unit digit of the sum of the 5 numbers in each row / column. -1 = no clue given.
    static int[] rowClue = { 5, 4, 2, 3, 8 };
    static int[] colClue = { 7, 4, -1, 6, -1 };
 
    static int[][] tens  = new int[N][N];
    static int[][] units = new int[N][N];
    static boolean[][] rowUsed = new boolean[N][10]; // rowUsed[r][d]
    static boolean[][] colUsed = new boolean[N][10]; // colUsed[c][d]
    static boolean[] numberUsed = new boolean[100];  // whole-grid distinctness
 
    public static void main(String[] args) {

    
        boolean found = solve(0, args.length > 0 && args[0].equals("-all"));

 
        if (!found  ) {
            System.out.println("Doesn't work");
        }
    }
 

    static boolean solve(int cell, boolean findAll) {
        // if every square is filled check the final row and col
        if (cell == N * N) {
            if (checkRows() && checkCols()) {
                
                printGrid();
                return !findAll;
            }
            return false;
        }

        // Find the row and col for the current cell
        int r = cell / N, c = cell % N;
        int clueDigit = given[r][c];

        // Try each possible tens digit for this square
        for (int t = 0; t <= 9; t++) {
            if (rowUsed[r][t] || colUsed[c][t]) continue;
            // Try each possible units digit for this square
            for (int u = 0; u <= 9; u++) {
                if (u == t || rowUsed[r][u] || colUsed[c][u]) continue;
                if (clueDigit != -1 && t != clueDigit && u != clueDigit) continue;

                // build the 2-digit number
                int num = t * 10 + u;
                if (numberUsed[num]) continue;
                if (isPrime(num) != circled[r][c]) continue;

                
                tens[r][c] = t;
                units[r][c] = u;
                rowUsed[r][t] = true;  
                rowUsed[r][u] = true;
                colUsed[c][t] = true;  
                colUsed[c][u] = true;
                numberUsed[num] = true;

                // check the row clue
                boolean rowOk = true;
                if (c == N - 1 && rowClue[r] != -1) {
                    rowOk = rowSum(r) == rowClue[r];
                }

                // recursion for deeper solve
                if (rowOk && solve(cell + 1, findAll)) return true;

                // backtracking if solution is invalid
                rowUsed[r][t] = false; rowUsed[r][u] = false;
                colUsed[c][t] = false; colUsed[c][u] = false;
                numberUsed[num] = false;
            }
        }
        return false;
    }
    // checks if the row is valid
    static int rowSum(int r) {
        int sum = 0;
        for (int c = 0; c < N; c++) sum += tens[r][c] * 10 + units[r][c];
        return sum % 10;
    }
    // checks if the col is valid (helper)
    static int colSum(int c) {
        int sum = 0;
        for (int r = 0; r < N; r++) sum += tens[r][c] * 10 + units[r][c];
        return sum % 10;
    }
 
    static boolean checkRows() {
        for (int r = 0; r < N; r++) {
            if (rowClue[r] != -1 && rowSum(r) != rowClue[r])
                return false;
        }
        return true;
    }

    static boolean checkCols() {
        for (int c = 0; c < N; c++) {
            if (colClue[c] != -1 && colSum(c) != colClue[c]) 
                return false;
        }
        return true;
    }
    //prime checker
    static boolean isPrime(int n) {
        if (n < 2) 
            return false;
        for (int i = 2; (long) i * i <= n; i++) {
            if (n % i == 0) return false;
        }
        return true;
    }
    // printing
    static void printGrid() {
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                int num = tens[r][c] * 10 + units[r][c];
                if (circled[r][c]) {
                    System.out.print("(" + num + ")");
                } 
                else {
                    System.out.print(" " + num + " ");
                }
                System.out.print(" ");
            }
            System.out.println("   | row sum unit digit = " + rowSum(r));
        }

        System.out.print("Column sum unit digits: ");
        for (int c = 0; c < N; c++) {
            System.out.print(colSum(c) + "  ");
        }
        System.out.println();
        System.out.println();
    }
}
 


