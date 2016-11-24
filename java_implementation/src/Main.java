import java.util.*;
import java.io.*;

public class Main {
	private static Solver SOLVER;

	public static void main(String[] args) throws IOException {
		ReadFile("input.in");
	}

	private static void ReadFile(String path) throws IOException {
		// Load the file
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// Create the reader
		BufferedReader bufferedReader = new BufferedReader(fileReader);

		// Read the subgridSize
		int subgridSize = Integer.parseInt(bufferedReader.readLine());
		int n = subgridSize * subgridSize;
		String contents[] = new String[n];

		for (int i = 0; i < n; i++) {
			contents[i] = bufferedReader.readLine();
		}

		int grid[][] = new int[n][n];

		// Put to the 2D array
		for (int i = 0; i < n; i++) {
			String numbers[] = contents[i].split(" ");
			
			for (int j = 0; j < n; j++) {
				grid[i][j] = Integer.parseInt(numbers[j]);
			}
		}

		bufferedReader.close();

		SOLVER = new Solver(grid, subgridSize, n);
		SOLVER.solve();
	}
}