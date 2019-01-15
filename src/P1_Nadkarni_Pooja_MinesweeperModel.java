import java.util.Random;
/*
 * Pooja Nadkarni
 * Period 1
 * 3/22/18
 * 
 * This lab took me around 1.5 hours.
 * 
 * This program went well for me. I was able to complete every method, and I was able to 
 * think of a couple more methods that would be helpful in this Model.
 * 
 */
public class P1_Nadkarni_Pooja_MinesweeperModel implements P1_Nadkarni_Pooja_MinesweeperModelInterface {
	private Tile[][] grid = new Tile[0][0];
	private int numMines = 0;
	
	public P1_Nadkarni_Pooja_MinesweeperModel(int rows, int cols, int mines) {
		newGrid(rows, cols, mines);
	}
	
	@Override
	public boolean isMine(int row, int col) {
		return grid[row][col].isMine();
	}

	@Override
	public int numNeighboringMines(int row, int col) {
		return grid[row][col].getNumAdjBombs();
	}

	@Override
	public int totalMineCount() {
		return numMines;
	}

	@Override
	public void setNumRows(int rows) {
		newGrid(rows, getNumCols(), numMines);
	}

	@Override
	public void setNumCols(int cols) {
		newGrid(getNumRows(), cols, numMines);
	}

	@Override
	public int getNumRows() {
		return grid.length;
	}

	@Override
	public int getNumCols() {
		return grid[0].length;
	}

	@Override
	public void reveal(int row, int col) {
		if(!isMine(row, col) && !isRevealed(row, col) && grid[row][col].getNumAdjBombs() == 0) {
			grid[row][col].setRevealed(true);
			int[][] span = {{1, 0}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}, {0, 1}, {0, -1}};
			for(int i = 0; i < span.length; i++) {
				if(span[i][0] + row >= 0 && span[i][0] + row < getNumRows() && 
						span[i][1] + col >= 0 && span[i][1] + col < getNumCols()) {
					if(!grid[span[i][0] + row][span[i][1] + col].isRevealed() ) {					
						reveal(span[i][0] + row, span[i][1] + col);
					}
				}
			}	
		} else if(!isMine(row, col) && !isRevealed(row, col)) {
			grid[row][col].setRevealed(true);
		}							
	}

	@Override
	public void setNumMines(int num) {
		numMines = num;
	}
	
	public void setReveal(int row, int col, boolean b) {
		grid[row][col].setRevealed(b);
	}
	
	private void findNumAdjCells() {
		for(int n = 0; n < grid.length; n++) {
			for(int m = 0; m < grid[0].length; m++) {
				int row = n;
				int col = m;
				int totalAround = 0;
				int[][] span = {{1, 0}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}, {0, 1}, {0, -1}};
				for(int i = 0; i < span.length; i++) {
					if(span[i][0] + row >= 0 && span[i][0] + row < getNumRows() && 
							span[i][1] + col >= 0 && span[i][1] + col < getNumCols()) {
						if(grid[span[i][0] + row][span[i][1] + col].isMine()) {
							totalAround++;
						}
					}
				}
				grid[row][col].setNumAdjBombs(totalAround);
			}
		}		
	}
	
	@Override
	public void newGrid(int rows, int cols, int mines) {
		Random rand = new Random();
		grid = new Tile[rows][cols];
		for(int x = 0; x < grid.length; x++) {
			for(int y = 0; y < grid[0].length; y++) {
				grid[x][y] = new Tile();
			}
		}
		for(int n = 0; n < mines; n++) {
			int row = rand.nextInt(rows);
			int col = rand.nextInt(cols);
			if(!grid[row][col].isMine()) {
				grid[row][col].setMine(true);
			} else {
				n--;
			}
		}	
		numMines = mines;
		findNumAdjCells();
	}

	@Override
	public boolean isRevealed(int row, int col) {
		return grid[row][col].isRevealed();
	}

	@Override
	public boolean isFlag(int row, int col) {
		return grid[row][col].isFlagged();
	}

	@Override
	public boolean isQuestionMark(int row, int col) {
		return grid[row][col].isQuestionMarked();
	}

	@Override
	public void setFlag(int row, int col, boolean b) {
		grid[row][col].setFlagged(b);
	}

	@Override
	public void setQuestionMark(int row, int col, boolean b) {
		grid[row][col].setQuestionMarked(b);
	}
	
	class Tile {
		private int numAdjBombs = 0;
		private boolean isRevealed = false;
		private boolean isFlagged = false;
		private boolean isQuestionMarked = false;
		private boolean isMine = false;
		
		public Tile() {
			
		}
		
		public int getNumAdjBombs() {
			return numAdjBombs;
		}
		
		public void setNumAdjBombs(int num) {
			numAdjBombs = num;
		}
		
		public boolean isFlagged() {
			return isFlagged;
		}
		
		public void setFlagged(boolean b) {
			isFlagged = b;
		}
		
		public boolean isRevealed() {
			return isRevealed;
		}
		
		public void setRevealed(boolean b) {
			isRevealed = b;
		}
		
		public boolean isQuestionMarked() {
			return isQuestionMarked;
		}
		
		public void setQuestionMarked(boolean b) {
			isQuestionMarked = b;
		}
		
		public boolean isMine() {
			return isMine;
		}
		
		public void setMine(boolean b) {
			isMine = b;
		}
	}

	@Override
	public int totalMarkings() {
		int total = 0;
		for(int n = 0; n < grid.length; n++) {
			for(int m = 0; m < grid[0].length; m++) {
				if(grid[n][m].isFlagged()) {
					total++;
				}
			}
		}	
		return total;
	}

	@Override
	public int numCorrectFlags() {
		int total = 0;
		for(int n = 0; n < grid.length; n++) {
			for(int m = 0; m < grid[0].length; m++) {
				if(grid[n][m].isFlagged() && grid[n][m].isMine()) {
					total++;
				}
			}
		}	
		return total;
	}

	@Override
	public int numClearedCells() {
		int total = 0;
		for(int n = 0; n < grid.length; n++) {
			for(int m = 0; m < grid[0].length; m++) {
				if(grid[n][m].isRevealed()) {
					total++;
				}
			}
		}	
		return total;
	}

}
