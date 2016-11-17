#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

void solve(int **, int, int);
void print_grid(int **, int, int, int, int, int, int, int);
int valid(int **, int, int, int, int, int);
void move_to_next_cell(int *, int *, int);
void move_to_previous_cell(int *, int *, int);
int row_same_number(int **, int, int, int, int);
int column_same_number(int **, int, int, int, int);
int subgrid_same_number(int **, int, int, int, int, int);


int main() {
	FILE *fp = fopen("input.in", "r");
	int puzzle_number, i, j, k;

	fscanf(fp, "%d", &puzzle_number);

	for (k = 0; k < puzzle_number; k++) {
		int subgrid_size, n, **grid;

		fscanf(fp, "%d", &subgrid_size);

		n = subgrid_size * subgrid_size;

		grid = (int **) malloc(sizeof(int *) * n);

		for (i = 0; i < n; i++) {
			grid[i] = (int *) malloc(sizeof(int) * n);

			for (j = 0; j < n; j++) {
				fscanf(fp, "%d", &grid[i][j]);				
			}
		}

		// Print the grid (before)
		printf("Before\n");
		for (i = 0; i < n; i++) {
			for (j = 0; j < n; j++) {
				printf("%3d", grid[i][j]);
			}
			printf("\n");
		}

		// Solve
		solve(grid, n, subgrid_size);

		// Print the grid (after)
		printf("After\n");
		for (i = 0; i < n; i++) {
			for (j = 0; j < n; j++) {
				printf("%3d", grid[i][j]);
			}
			printf("\n");
		}
	}

	return 0;
}


void solve(int **grid, int n, int subgrid_size) {
	int original_grid[n][n];
	int start, move;
	int nopts[n*n+2];
	int options[n*n+2][n+2];
	int i, j, k, x, y, candidate, solutions = 0;

	// Preserve a copy of the original grid
	for (i = 0; i < n; i++) {
		for (j = 0; j < n; j++) {
			original_grid[i][j] = grid[i][j];
		}
	}

	printf("\nNOPTS\n");
	// Initialize all the elements of the array "nopts" to zero
	for (i = 0; i < n*n+2; i++) {
		nopts[i] = 0;
		printf("%3d", nopts[i]);
	}

	printf("\nOPTIONS\n");
	// Initialize all the elements of the 2D array "options" to zero
	for (i = 0; i < n*n+2; i++) {
		for (j = 0; j < n+2; j++) {
			options[i][j] = 0;
			printf("%3d", options[i][j]);
		}
		printf("\n");
	}

	move = start = 0;
	nopts[start] = 1;		// Empty stack marker

	i = j = 0;

	while (i >= 0 && j >= 0) {
		// If solved, look for another possible solution
		if (!(i < n && j < n)) {
			solutions += 1;
			move_to_previous_cell(&i, &j, n);
			print_grid(grid, solutions, i, j, move, nopts[move], options[move][nopts[move]], n);
			// sleep(5);
			printf("Backtracking... (Solution found)\n");

			// Check if the cell is fixed
			while (original_grid[i][j] != 0) {
				move_to_previous_cell(&i, &j, n);
				move--;
			}

			// Find the next untried and possible move
			nopts[move]--;
			grid[i][j] = 0;

			while (nopts[move] > 0) {
				if (valid(grid, i, j, options[move][nopts[move]], n, subgrid_size)) {

					grid[i][j] = options[move][nopts[move]];
					move_to_next_cell(&i, &j, n);

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
				move_to_next_cell(&i, &j, n);
				continue;
			}

			nopts[move] = 0;

			// Generate the possible moves
			for (candidate = n; candidate >= 1; candidate--) {
				nopts[move]++;
				options[move][nopts[move]] = candidate;
			}

			// Find the possible move
			while (nopts[move] > 0) {
				// Check if the current cell is empty and if the current number is possible to be put into the current cell
				if (!grid[i][j] && valid(grid, i, j, options[move][nopts[move]], n, subgrid_size)) {
					grid[i][j] = options[move][nopts[move]];
					move_to_next_cell(&i, &j, n);

					break;
				}
				else {
					nopts[move]--;
				}
			}
		}
		else {
			// printf("Backtracking...\n");

			// Move to the previous cell
			grid[i][j] = 0;
			move_to_previous_cell(&i, &j, n);
			move--;

			// Check if the cell is fixed
			while (i >= 0 && j >= 0 && original_grid[i][j] != 0) {
				move_to_previous_cell(&i, &j, n);
				move--;
			}

			// No more solutions to be searched
			if (!(i >= 0 && j >= 0)) {
				break;
			}

			// Find the next untried and possible move
			nopts[move]--;
			grid[i][j] = 0;

			while (nopts[move] > 0) {
				if (valid(grid, i, j, options[move][nopts[move]], n, subgrid_size)) {
					grid[i][j] = options[move][nopts[move]];
					move_to_next_cell(&i, &j, n);

					break;
				}
				else {
					nopts[move]--;
				}
			}
		}

		// sleep(1);
	}

	printf("Solutions found: %d\n", solutions);
}

void print_grid(int **grid, int solutions, int i, int j, int move, int nopts_move, int options_move, int n) {
	int x, y;

	printf("\nSolutions: %d, Current Grid [%d, %d], move: %d, nopts[move]: %d, options[move][nopts[move]]: %d\n", solutions, i, j, move, nopts_move, options_move);
	for (x = 0; x < n; x++) {
		for (y = 0; y < n; y++) {
			printf("%3d", grid[x][y]);
		}
		printf("\n");
	}
}

int valid(int **grid, int i, int j, int num, int n, int subgrid_size) {
	if (
		!row_same_number(grid, i, num, j, n) &&
		!column_same_number(grid, j, num, i, n) &&
		!subgrid_same_number(grid, i, j, num, n, subgrid_size)
		) return 1;

	return 0;
}

void move_to_next_cell(int *i, int *j, int n) {
	if (*j == n-1) {
		(*i)++;
		*j = 0;
	}
	else {
		(*j)++;
	}
}

void move_to_previous_cell(int *i, int *j, int n) {
	if (*j == 0) {
		(*i)--;
		*j = n-1;
	}
	else {
		(*j)--;
	}
}

int row_same_number(int **grid, int row, int num, int current_column, int n) {
	// printf("Checking if %d has a row for conflict...\n", num);
	int i;

	for (i = 0; i < n; i++) {
		if (i != current_column) {
			if (grid[row][i] == num) return 1;
		}
	}

	return 0;
}

int column_same_number(int **grid, int col, int num, int current_row, int n) {
	// printf("Checking the column for conflict...\n");
	int i;

	for (i = 0; i < n; i++) {
		if (i != current_row) {
			if (grid[i][col] == num) return 1;
		}
	}

	return 0;
}

int subgrid_same_number(int **grid, int row, int col, int num, int n, int subgrid_size) {
	// printf("Checking the subgrid for conflict...\n");	
	int subgrid_row = row/subgrid_size;
	int subgrid_col = col/subgrid_size;
	int i, j;

	for (i = subgrid_row * subgrid_size; i < (subgrid_row + 1) * subgrid_size; i++) {
		for (j = subgrid_col * subgrid_size; j < (subgrid_col + 1) * subgrid_size; j++) {
			if (i != row && j != col) {
				if (grid[i][j] == num) return 1;
			}
		}
	}

	return 0;
}
