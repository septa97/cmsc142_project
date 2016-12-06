import java.util.*;
import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import java.awt.*;
import java.awt.event.*;

public class Main {
	private static Solver SOLVER;
	private static int N, sub;
	private static int[][] board;
	private static boolean selectedY,selectedX;
	private static JFrame frame;
	private static Container c;
	private static JPanel optionsPanel;

	public static void main(String[] args) throws IOException {
		// Filter
		FileNameExtensionFilter filter = new FileNameExtensionFilter(".in files", "in");

		// File Chooser
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(filter);

		// ReadFile("input.in");
		
		// Create the main frame
		frame = new JFrame("SUDOKU");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(800,600));

		// Get the main container
		c = frame.getContentPane();
		c.setLayout(new BorderLayout());
		optionsPanel = new JPanel();
		optionsPanel.setLayout(new GridLayout(4,1,0,0));

		// Create the "Browse" button
		JButton browse = new JButton("Browse");
		browse.setPreferredSize(new Dimension(200,50));
		optionsPanel.add(browse);

		// Add an action listener to the "Browse" button
		browse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnVal = fileChooser.showOpenDialog(c);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File inputFile = fileChooser.getSelectedFile();

					try {
						ReadFile(inputFile);
						init();
					} catch (IOException exception) {
						exception.printStackTrace();
					}
				}
			}
		});

		c.add(optionsPanel,BorderLayout.EAST);
		
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private static void init() {
		// Create a solver instance
		SOLVER = new Solver(board, sub, N);

		// Create a 2D array of JButtons
		final JButton[][] panelButton = new JButton[N][N];

		// Create the checkbox panel
		JPanel cbPanel = new JPanel();

		// Create the checkbox for sudoku X and Y
      	final JCheckBox sudokuX = new JCheckBox("Sudoku X");
      	final JCheckBox sudokuY = new JCheckBox("Sudoku Y");

		cbPanel.add(sudokuX);
		cbPanel.add(sudokuY);
		
		// Create the solve panel
		JPanel solvePanel = new JPanel();
		
		// Create the "Solve" button
		JButton solve = new JButton("Solve");
		solve.setPreferredSize(new Dimension(200,100));
		solvePanel.add(solve);

		// Create the panel where the number of solutions will be displayed
		JPanel solutionPanel = new JPanel();
		solutionPanel.setLayout(null);

		// Create the label of the number of solutions
		JLabel sol = new JLabel();
		sol.setBounds(20,30,200,30);
		sol.setText("Number Of Solutions: ");
		

		final JLabel output = new JLabel("");
		output.setBounds(90,60,200,30);


		solutionPanel.add(sol);
		solutionPanel.add(output);

		// Create the panel for the sudoku board
		JPanel boardPanel = new JPanel();
		boardPanel.setLayout(new GridLayout(N,N));
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				final int a = i, b = j;
				panelButton[i][j] = new JButton(String.valueOf(board[i][j]).equals("0") ? "" : String.valueOf(board[i][j]));
				panelButton[i][j].setBackground(Color.BLACK);
				panelButton[i][j].setForeground(Color.WHITE);
				panelButton[i][j].setFont(new Font("Arial", Font.PLAIN, 40));
				
				
				final boolean x = sudokuX.isSelected();
				final boolean y = sudokuY.isSelected();
				
				
				if(String.valueOf(board[i][j]).equals("0")){
					// Add a listener whenever the button is clicked
					panelButton[i][j].addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e){	
							String temp = JOptionPane.showInputDialog("INPUT: ");
							board[a][b] = Integer.parseInt(temp);
							
							if (!SOLVER.valid(board,a,b,board[a][b],x,y)) {
								panelButton[a][b].setBackground(Color.RED);
							}
							else{
								panelButton[a][b].setBackground(Color.BLACK);
								
							}
							
							panelButton[a][b].setText(temp.equals("0") ? "" : temp);
							if (Solved(panelButton)) {
								JOptionPane.showMessageDialog(null, "SOLVED!", "SOLVED!", JOptionPane.INFORMATION_MESSAGE);
							}
						}
					});
				}		

				boardPanel.add(panelButton[i][j]);
			}
		}

		// Add a listener for the "Solve" button
		solve.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){	
				selectedX = sudokuX.isSelected();
				selectedY = sudokuY.isSelected();
				
				if (Solved(panelButton)) {
					JOptionPane.showMessageDialog(null, "ALREADY SOLVED!", "ALREADY SOLVED!", JOptionPane.INFORMATION_MESSAGE);
				}
				else if (Solvable(panelButton)) {
					String solutions = SOLVER.solve(selectedX, selectedY);
					output.setText(solutions);
				}
				
			}
		});

		c.add(boardPanel,BorderLayout.CENTER);

		optionsPanel.add(solutionPanel);
		optionsPanel.add(cbPanel);
		optionsPanel.add(solvePanel);

		frame.validate();
		frame.repaint();
	}

	private static boolean Solved(JButton[][] panelButton){
		for(int i=0;i<N;i++){
			for(int j=0;j<N;j++){
				if(panelButton[i][j].getBackground() == Color.RED || panelButton[i][j].getText().equals("")){
					return false;
				}
			}
		}
		return true;
	}
	private static boolean Solvable(JButton[][] panelButton){
		for(int i=0;i<N;i++){
			for(int j=0;j<N;j++){
				if(panelButton[i][j].getBackground() == Color.RED){
					return false;
				}
			}
		}

		return true;
	}


	private static void ReadFile(File file) throws IOException {
		// Load the file
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// Create the reader
		BufferedReader bufferedReader = new BufferedReader(fileReader);

		// Read the subgridSize
		int subgridSize = Integer.parseInt(bufferedReader.readLine());
		sub = subgridSize;
		int n = subgridSize * subgridSize;
		N = n;
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
		
		board = grid;

		bufferedReader.close();
	}
}
