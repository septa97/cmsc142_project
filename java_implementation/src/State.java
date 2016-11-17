public class State {
	private int board[][];
	private int children[];
	private int n;

	public State(int board[][], int children[], int n) {
		this.board = new int[n][n];

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				this.board[i][j] = board[i][j];
			}
		}

		this.children = new int[n];

		for (int i = 0; i < n; i++) {
			this.children[i] = children[i];
		}

		this.n = n;
	}

	public boolean isLeaf() {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (board[i][j] == 0) {
					for (int k = 0; k < n; k++) {
						if (children[k] != 0 &&
							!rowSameNumber(i, children[k], j) &&
							!columnSameNumber(j, children[k], i) &&
							!subgridSameNumber(i, j, children[k], (int)Math.sqrt(n))
							) return false;
					}

					return true;
				}
			}
		}

		return true;
	}

	public boolean isGoal() {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (board[i][j] == 0) return false;
			}
		}

		return true;
	}

	public State nextUntriedChild() {
		boolean assigned = false;

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (board[i][j] == 0) {
					for (int k = 0; k < n; k++) {
						if (children[k] != 0 &&
							!rowSameNumber(i, children[k], j) &&
							!columnSameNumber(j, children[k], i) &&
							!subgridSameNumber(i, j, children[k], (int)Math.sqrt(n))
							) {
							board[i][j] = children[k];
							
							assigned = true;
							break;
						}
						else {
							children[k] = 0;
						}
					}

					if (assigned) break;
				}
			}

			if (assigned) break;
		}

		int newChildren[] = new int[n];

		for (int j = 0; j < n; j++) {
			newChildren[j] = j+1;
		}

		return new State(board, newChildren, n);
	}

	private boolean rowSameNumber(int row, int num, int currentColumn) {
		for (int i = 0; i < n; i++) {
			if (i != currentColumn && board[row][i] == num) {
				return true;
			}
		}

		return false;
	}

	private boolean columnSameNumber(int col, int num, int currentRow) {
		for (int i = 0; i < n; i++) {
			if (i != currentRow && board[i][col] == num) {
				return true;
			}
		}

		return false;
	}

	private boolean subgridSameNumber(int row, int col, int num, int subgridSize) {
		int subgridRow = row/subgridSize;
		int subgridCol = col/subgridSize;
		int i, j;

		for (i = subgridRow * subgridSize; i < (subgridRow + 1) * subgridSize; i++) {
			for (j = subgridCol * subgridSize; j < (subgridCol + 1) * subgridSize; j++) {
				if (i != row && j != col && board[i][j] == num) {
					return true;
				}
			}
		}

		return false;
	}

	public void printBoard() {
		System.out.println("Board");
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				System.out.printf("%3d", board[i][j]);
			}
			System.out.print("\n");
		}
	}
}