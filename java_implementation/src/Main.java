import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main {
	private static int N;

	public static void main(String[] args) throws IOException {
		int[][] board = ReadFile("input.in");


		
		
		JFrame frame = new JFrame("SUDOKU");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(800,600));
		
		final JButton[][] panelButton = new JButton[N][N];

		final Container c = frame.getContentPane();
		c.setLayout(new BorderLayout());
		final JPanel optionsPanel = new JPanel();
		optionsPanel.setLayout(new GridLayout(3,1,0,0));


		JPanel cbPanel = new JPanel();


      	final JCheckBox sudokuX = new JCheckBox("Sudoku X");
      	final JCheckBox sudokuY = new JCheckBox("Sudoku Y");
      	
		cbPanel.add(sudokuX);
		cbPanel.add(sudokuY);
		
		JPanel solvePanel = new JPanel();
				
		JButton solve = new JButton("Solve");
		solve.setPreferredSize(new Dimension(200,180));
		solvePanel.add(solve);

		JPanel solutionPanel = new JPanel();
		solutionPanel.setLayout(null);

		JLabel sol = new JLabel();
		sol.setBounds(20,30,200,30);
		sol.setText("Number Of Solutions: ");
		

		JLabel output = new JLabel("69");
		output.setBounds(90,60,200,30);


		solutionPanel.add(sol);
		solutionPanel.add(output);


		JPanel boardPanel = new JPanel();
		boardPanel.setLayout(new GridLayout(N,N));
		
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				panelButton[i][j] = new JButton(String.valueOf(board[i][j]));
				boardPanel.add(panelButton[i][j]);
			}
		}		
				
		

		optionsPanel.add(solutionPanel);
		optionsPanel.add(cbPanel);
		optionsPanel.add(solvePanel);
		
		c.add(boardPanel,BorderLayout.CENTER);
		c.add(optionsPanel,BorderLayout.EAST);
		
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		
		
	}

	private static int[][] ReadFile(String path) throws IOException {
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
		N = subgridSize * subgridSize;
		String contents[] = new String[N];

		for (int i = 0; i < N; i++) {
			contents[i] = bufferedReader.readLine();
		}

		int board[][] = new int[N][N];

		// Put to the 2D array
		for (int i = 0; i < N; i++) {
			String numbers[] = contents[i].split(" ");
			
			for (int j = 0; j < N; j++) {
				board[i][j] = Integer.parseInt(numbers[j]);
			}
		}

		bufferedReader.close();

		return board;
	}
}
