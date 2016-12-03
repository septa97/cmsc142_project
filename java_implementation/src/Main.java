import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main {
	private static Solver SOLVER;
	private static int N,sub;
	private static int[][] board;
	private static boolean selectedY,selectedX;

	public static void main(String[] args) throws IOException {
		ReadFile("input.in");
		
		SOLVER = new Solver(board, sub, N);
		
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
		

		final JLabel output = new JLabel("");
		output.setBounds(90,60,200,30);


		solutionPanel.add(sol);
		solutionPanel.add(output);


		JPanel boardPanel = new JPanel();
		boardPanel.setLayout(new GridLayout(N,N));
		
		final int a,b;
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				panelButton[i][j] = new JButton(String.valueOf(board[i][j]));
				panelButton[i][j].addMouseListener(new MouseListener(){
					public void mouseClicked(MouseEvent e){	
						String temp = JOptionPane.showInputDialog("INPUT: ");
					//	board[i][j] = Integer.parseInt(temp);
					//	panelButton.setText(temp);
					}
					public void mouseEntered(MouseEvent e){
						
					}
					public void mouseExited(MouseEvent e){
						
					}
					public void mousePressed(MouseEvent e){

					}
					public void mouseReleased(MouseEvent e){
						
					}
				});
				boardPanel.add(panelButton[i][j]);
			}
		}
			

			
		solve.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e){	
				selectedX = sudokuX.isSelected();
				selectedY = sudokuY.isSelected();
				
				int solutions =  SOLVER.solve(selectedX,selectedY);
				output.setText(solutions + "");
			}
			public void mouseEntered(MouseEvent e){
				
			}
			public void mouseExited(MouseEvent e){
				
			}
			public void mousePressed(MouseEvent e){

			}
			public void mouseReleased(MouseEvent e){
				
			}
		});		
				
		

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
