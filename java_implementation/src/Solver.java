public class Solver {
	private int originalGrid[][];
	private int subgridSize;
	private int n;
	private boolean x,y;

	public Solver(int originalGrid[][], int subgridSize, int n) {
		this.originalGrid = originalGrid;
		this.subgridSize = subgridSize;
		this.n = n;
	}
	
	public int[][] getOriginalgrid(){
		return originalGrid;
	}

	public int solve(boolean selectedX,boolean selectedY) {
		int grid[][] = new int[n][n];
		int nopts[] = new int[n*n+2];
		int options[][] = new int[n*n+2][n+2];
		int start, move;
		int solutions = 0;
		int temp[];
		this.x = selectedX;
		this.y = selectedY;

		// Create a copy of the original grid
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				grid[i][j] = originalGrid[i][j];
			}
		}
		
		// Check if X is valid 
		if (selectedX && !checkIfXValid(grid)) {
			System.out.println("X is not valid!");
			return 0;
		}

		// Check if Y is valid
		if (selectedY && !checkIfYValid(grid)) {
			System.out.println("Y is not valid!");
			return 0;
		}
		

		move = start = 0;
		nopts[start] = 1;	// Empty stack marker

		int i = 0, j = 0;

		while (i >= 0 && j >= 0) {
			// If solved, look for another possible solution
			if (!(i < n && j < n)) {
				solutions++;
				temp = moveToPreviousCell(i, j);
				i = temp[0];
				j = temp[1];
				System.out.println("Backtracking... (Solution found!)");

				// Check if the cell is fixed
				while (originalGrid[i][j] != 0) {
					temp = moveToPreviousCell(i, j);
					i = temp[0];
					j = temp[1];
					move--;
				}

				// Find the next untried and possible move
				nopts[move]--;
				grid[i][j] = 0;

				while (nopts[move] > 0) {
					if (valid(grid, i, j, options[move][nopts[move]],x,y)) {
						grid[i][j] = options[move][nopts[move]];
						temp = moveToNextCell(i, j);
						i = temp[0];
						j = temp[1];

						break;
					}
					else {
						nopts[move]--;
					}
				}
			}

			if (nopts[move] > 0) {
				move++;

				if (grid[i][j] != 0) {
					nopts[move] = n + 1 - grid[i][j];
					temp = moveToNextCell(i, j);
					i = temp[0];
					j = temp[1];
					continue;
				}

				nopts[move] = 0;

				// Generate the possible moves
				for (int candidate = n; candidate >= 1; candidate--) {
					nopts[move]++;
					options[move][nopts[move]] = candidate;
				}

				// Find the possible move
				while (nopts[move] > 0) {
					// Check if the current cell is empty and if the current number is possible to be put into the current cell
					if (grid[i][j] == 0 && valid(grid, i, j, options[move][nopts[move]],x,y)) {
						grid[i][j] = options[move][nopts[move]];
						temp = moveToNextCell(i, j);
						i = temp[0];
						j = temp[1];

						break;
					}
					else {
						nopts[move]--;
					}
				}
			}
			else {
				System.out.println("Backtracking...");

				// Move to the previous cell
				grid[i][j] = 0;
				temp = moveToPreviousCell(i, j);
				i = temp[0];
				j = temp[1];
				move--;

				// Check if the cell is fixed
				while (i >= 0 && j >= 0 && originalGrid[i][j] != 0) {
					temp = moveToPreviousCell(i, j);
					i = temp[0];
					j = temp[1];
					move--;
				}

				// No more solutions to be searched
				if (!(i >= 0 && j >= 0)) break;

				// Find the next untried and possible move
				nopts[move]--;
				grid[i][j] = 0;

				while (nopts[move] > 0) {
					if (valid(grid, i, j, options[move][nopts[move]],x,y)) {
						grid[i][j] = options[move][nopts[move]];
						temp = moveToNextCell(i, j);
						i = temp[0];
						j = temp[1];

						break;
					}
					else {
						nopts[move]--;
					}
				}
			}
		}

		System.out.println("Solutions found: " + solutions);
		return solutions;
	}

	private boolean valid(int grid[][], int i, int j, int num,boolean x,boolean y) {
		if (
			!rowSameNumber(grid, i, num, j) &&
			!columnSameNumber(grid, j, num, i) &&
			!subgridSameNumber(grid, i, j, num) &&
			(x && (i == j || i+j == n-1) ? !XSameNumber(grid, i, j, num) : true) &&
			(y && ((i == j && j <= n/2) || (i+j == n-1 && j >= n/2) || (i > n/2 && j == n/2)) ? !YSameNumber(grid, i, j, num) : true)
			) return true;

		return false;
	}

	private boolean checkIfXValid(int grid[][]) {
		for (int a = 0; a < n; a++) {
			for (int b = 0; b < n; b++) {
				if (a == b || a+b == n-1) {
					int x, y;
					int subgridRow = a / subgridSize;
					int subgridCol = b / subgridSize;

					for (int i = 0; i < n; i++) {
						for (int j = 0; j < n; j++) {
							x = i / subgridSize;
							y = j / subgridSize;

							if (grid[i][j] != 0 && i == j && a == b && (subgridRow != x && subgridCol != y)) {
								System.out.println("Checking if " + grid[a][b] + " is equal to " + grid[i][j]);
								if (grid[i][j] == grid[a][b]) return false;
							}
							else if (grid[i][j] != 0 && i+j == n-1 && a+b == n-1 && (subgridRow != x && subgridCol != y)) {
								System.out.println("Checking if " + grid[a][b] + " is equal to " + grid[i][j]);
								if (grid[i][j] == grid[a][b]) return false;
							}
						}
					}
				}
			}
		}

		return true;
	}

	private boolean checkIfYValid(int grid[][]) {
		if (subgridSize % 2 == 0) {
			return false;
		}
		
		for (int a = 0; a < n; a++) {
			for (int b = 0; b < n; b++) {
				if ((a == b && b <= n/2) || (a+b == n-1 && b >= n/2) || (a > n/2 && b == n/2)) {
					int x, y;
					int subgridRow = a / subgridSize;
					int subgridCol = b / subgridSize;

					for (int i = 0; i < n; i++) {
						for (int j = 0; j < n; j++) {
							x = i / subgridSize;
							y = j / subgridSize;

							if (grid[i][j] != 0 &&
								(i == j && j <= n/2 || i > n/2 && j == n/2) &&
								(a == b && b <= n/2 || a > n/2 && b == n/2) &&
								(subgridRow != x && subgridCol != y))
							{
								if (grid[i][j] == grid[a][b]) return false;
							}
							else if (grid[i][j] != 0 &&
								(i+j == n-1 && j >= n/2 || i > n/2 && j == n/2) &&
								(a+b == n-1 && b >= n/2 || a > n/2 && b == n/2) &&
								(subgridRow != x && subgridCol != y))
							{
								if (grid[i][j] == grid[a][b]) return false;
							}
						}
					}
				}
			}
		}

		return true;
	}

	private boolean YSameNumber(int grid[][], int row, int column, int num) {
		int x, y;
		int subgridRow = row / subgridSize;
		int subgridCol = column / subgridSize;

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				x = i / subgridSize;
				y = j / subgridSize;

				if (grid[i][j] != 0 && (i == j && j <= n/2 || i > n/2 && j == n/2) && (subgridRow != x && subgridCol != y)) {
					if (grid[i][j] == num) return true;
				}
				else if (grid[i][j] != 0 && (i+j == n-1 && j >= n/2 || i > n/2 && j == n/2) && (subgridRow != x && subgridCol != y)) {
					if (grid[i][j] == num) return true;
				}
			}
		}

		return false;
	}

	private boolean XSameNumber(int grid[][], int row, int column, int num) {
		int x, y;
		int subgridRow = row / subgridSize;
		int subgridCol = column / subgridSize;

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				x = i / subgridSize;
				y = j / subgridSize;

				if (grid[i][j] != 0 && i == j && (subgridRow != x && subgridCol != y)) {
					if (grid[i][j] == num) return true;
				}
				else if (grid[i][j] != 0 && i+j == n-1 && (subgridRow != x && subgridCol != y)) {
					if (grid[i][j] == num) return true;
				}
			}
		}

		return false;
	}

	private boolean rowSameNumber(int grid[][], int row, int num, int currentColumn) {
		for (int i = 0; i < n; i++) {
			if (i != currentColumn && grid[row][i] == num) {
				return true;
			}
		}

		return false;
	}

	private boolean columnSameNumber(int grid[][], int col, int num, int currentRow) {
		for (int i = 0; i < n; i++) {
			if (i != currentRow && grid[i][col] == num) {
				return true;
			}
		}

		return false;
	}

	private boolean subgridSameNumber(int grid[][], int row, int col, int num) {
		int subgridRow = row / subgridSize;
		int subgridCol = col / subgridSize;

		for (int i = subgridRow * subgridSize; i < (subgridRow + 1) * subgridSize; i++) {
			for (int j = subgridCol * subgridSize; j < (subgridCol + 1) * subgridSize; j++) {
				if (i != row && j != col && grid[i][j] == num) {
					return true;
				}
			}
		}

		return false;
	}

	private int[] moveToPreviousCell(int i, int j) {
		if (j == 0) {
			i--;
			j = n-1;
		}
		else {
			j--;
		}

		return new int[] {i, j};
	}

	private int[] moveToNextCell(int i, int j) {
		if (j == n-1) {
			i++;
			j = 0;
		}
		else {
			j++;
		}

		return new int[] {i, j};
	}

	private void printGrid(int grid[][], int solutions) {
		System.out.println("Current number of Solutions: " + solutions);
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				System.out.printf("%3d", grid[i][j]);
			}
			System.out.print("\n");
		}
	}
}
