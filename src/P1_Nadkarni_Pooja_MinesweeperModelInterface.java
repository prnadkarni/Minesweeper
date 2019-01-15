
public interface P1_Nadkarni_Pooja_MinesweeperModelInterface {
	public boolean isMine(int row, int col);
	public int numNeighboringMines(int row, int col);
	public int totalMineCount();
	public void setNumRows(int rows);
	public void setNumCols(int col);
	public int getNumRows();
	public int getNumCols();
	public void reveal(int row, int col);
	public void setNumMines(int num);
	public void newGrid(int rows, int cols, int mines);
	public boolean isRevealed(int row, int col);
	public boolean isFlag(int row, int col);
	public boolean isQuestionMark(int row, int col);
	public void setFlag(int row, int col, boolean b);
	public void setQuestionMark(int row, int col, boolean b);
	public int totalMarkings();
	public int numCorrectFlags();
	public int numClearedCells();
}
