import java.util.*;
import java.io.*;

public class Main {
	public static void main(String[] args) throws IOException {
		State initialState = readFile(args[0]);
		
		State finalState = solve(initialState);
	
		if (finalState != null) System.out.println("SOLVED!");
		else System.out.println("NO SOLUTION!");
	}

	// Solve the input board
	private static State solve(State s) {
		Stack<State> stack = new Stack<State>();
		stack.push(s);

		while (!stack.empty()) {
			State top = stack.peek();
			top.printBoard();
			
			// try {
			// 	Thread.sleep(1000);
			// } catch (InterruptedException e) {
			// 	e.printStackTrace();
			// }

			if (top.isLeaf()) {
				if (top.isGoal()) {
					return top;
				}
				else {
					System.out.println("Backtracking...");
					stack.pop();
				}
			}
			else {
				stack.push(top.nextUntriedChild());
			}
		}

		return null;
	}

	// Read the file and return a State object as the initial state
	private static State readFile(String path) throws IOException {
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

		int n = subgridSize*subgridSize;
		String contents[] = new String[n];

		for (int i = 0; i < n; i++) {
			contents[i] = bufferedReader.readLine();
		}

		bufferedReader.close();
	
		int board[][] = new int[n][n];
		int children[] = new int[n];

		// Put to the 2D array
		for (int i = 0; i < n; i++) {
			String numbers[] = contents[i].split(" ");

			for (int j = 0; j < n; j++) {
				board[i][j] = Integer.parseInt(numbers[j]);
			}

			children[i] = i+1;
		}

		return new State(board, children, n);
	}
}