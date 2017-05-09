package controller;

import static controller.NameRepository.CELL_COLOR;
import static controller.NameRepository.CELL_HEIGHT;
import static controller.NameRepository.CELL_PADDING;
import static controller.NameRepository.CELL_THICKNESS;
import static controller.NameRepository.CELL_WIDTH;

import java.util.Arrays;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class SudokuGrid extends GridPane
{
	Button[] 			inputButtons;	//Selection buttons for the game
	Sudokubutton[][] 	puzzleButtons;	//Sudoku grids
	int [][] 			sudokuPuzzle;	//Sudoku Data
	int [][]			backupSudokuPuzzle;
	int 				selected = 0;	//Currently selected button

	/*
	 * Checks if puzzle is completely solved
	 */
	public boolean isComplete()
	{
		for(int i = 0; i < 9; ++i){
			for(int j = 0; j < 9; ++j){
				if(0 == puzzleButtons[i][j].getValue()|| puzzleButtons[i][j].getConflict())
					return false;
			}
		}
		return true;
	}
	
	/*
	 * Check if puzzle is in edit state
	 */
	public boolean isEditState()
	{
		for(int i = 0; i < 9; ++i){
			for(int j = 0; j < 9; ++j){
				if(puzzleButtons[i][j].getPermanent())
					return false;
			}
		}
		return true;
	}
	
	/*
	 * Resets puzzle
	 */
	public void clearPuzzle()
	{
		for(int i = 0; i < 9; ++i)
		{
			for(int j =0; j < 9; ++j)
			{
				sudokuPuzzle[i][j] = 0;
				puzzleButtons[i][j].setValue(0);
				puzzleButtons[i][j].refreshImage();
			}
		}
	}
	
	/*
	 * Checks for any conflicts for provided grid position and value
	 */
	private void checkConflicts(int row, int col, int value)
	{
		boolean conflict = false;
		
		//Check row and col
		for(int i = 0; i < 9; ++i) {
			if(value == sudokuPuzzle[i][col] && row != i){
				puzzleButtons[i][col].setConflict(true);
				puzzleButtons[i][col].refreshImage();
				conflict = true;
			}
														
			if(value == sudokuPuzzle[row][i] && col != i){
				puzzleButtons[row][i].setConflict(true);
				puzzleButtons[row][i].refreshImage();
				conflict = true;
			}		
		}
		
		//Check box
		for(int i = (row/3)*3; i <= ((row/3)*3)+2; ++i){
			for(int j = (col/3)*3; j <= ((col/3)*3)+2; ++j){
				if(value == sudokuPuzzle[i][j] && !(row == i && col == j)){
					puzzleButtons[i][j].setConflict(true);
					puzzleButtons[i][j].refreshImage();													
					conflict = true;
				}
			}
		}
		
		puzzleButtons[row][col].setConflict(conflict);
		puzzleButtons[row][col].refreshImage();
	}
	
	/*
	 * Returns puzzle in string form used for exporting to file
	 */
	public String getAsString()
	{
		String output = "";
		for(int i = 0; i < 9; ++i)
		{
			for(int j =0; j < 9; ++j)
			{
				output += Integer.toString(sudokuPuzzle[i][j]) + " ";
			}
		}
		return output.trim();
	}
		
	/*
	 * Initial settings
	 */
	private void init()
	{
		sudokuPuzzle = new int[9][9];
		backupSudokuPuzzle = new int[9][9];
		
		inputButtons = new Button[10];
		for(int i = 0; i < 10; ++i){
			inputButtons[i] = new Button();
			inputButtons[i].setGraphic(new ImageView(new Image("image/Regular/Regular_" + Integer.toString(i+1) + ".png")));
			inputButtons[i].onMouseClickedProperty().set(new EventHandler<MouseEvent>(){
				@Override
				public void handle(MouseEvent event){
					int loc = Arrays.asList(inputButtons).indexOf(event.getSource());
					if(selected != (loc + 1)){
						if(0 != selected)
							inputButtons[selected - 1].setGraphic(new ImageView(new Image("image/Regular/Regular_" + Integer.toString(selected) + ".png")));
						inputButtons[loc].setGraphic(new ImageView(new Image("image/Selected/Selected_" + Integer.toString(loc+1) + ".png")));
						selected = loc + 1;
					}			
				} 
			});
		}
		
		puzzleButtons = new Sudokubutton[9][9];
		for(int i = 0; i < 9; ++i){
			for(int j = 0; j < 9; ++j){
				puzzleButtons[i][j] = new Sudokubutton(i, j);
				puzzleButtons[i][j].setGraphic(new ImageView(new Image("image/Empty.png")));
				puzzleButtons[i][j].onMouseClickedProperty().set(new EventHandler<MouseEvent>(){				
					@Override
					public void handle(MouseEvent event){
						
						for(int row = 0; row < 9; ++row){
							if(Arrays.asList(puzzleButtons[row]).contains(event.getSource())){
								int col = Arrays.asList(puzzleButtons[row]).indexOf(event.getSource());
								
								if(!puzzleButtons[row][col].getPermanent()){
									if(selected >= 1 && selected <= 9){
										int oldVal = sudokuPuzzle[row][col];
										puzzleButtons[row][col].setValue(selected);
										sudokuPuzzle[row][col] = selected;
										checkConflicts(row, col, selected);
										
										
										if(0 != oldVal){
											//Refresh duplicate markings
											for(int i = 0; i < 9; ++i){
												//Check row for duplicates 
												if(oldVal == sudokuPuzzle[i][col])
													checkConflicts(i, col,oldVal);
												
												//Check col for duplicates
												if(oldVal == sudokuPuzzle[row][i])
													checkConflicts(row, i, oldVal);
											}
											
											//Check square for duplicates
											for(int i = (row/3)*3; i <= ((row/3)*3)+2; ++i){
												for(int j = (col/3)*3; j <= ((col/3)*3)+2; ++j){
													if(oldVal == sudokuPuzzle[i][j])
														checkConflicts(i, j, oldVal);
												}
											}
										}

										if(isComplete() && !isEditState())
										{
											saveState();
											winGraphics();
										}
									}
									else
									{
										int oldVal = sudokuPuzzle[row][col];
										
										puzzleButtons[row][col].setValue(0);
										puzzleButtons[row][col].setConflict(false);
										puzzleButtons[row][col].refreshImage();
										sudokuPuzzle[row][col] = 0;
										
										//Refresh duplicate markings
										for(int i = 0; i < 9; ++i){
											//Check row for duplicates 
											if(oldVal == sudokuPuzzle[i][col])
												checkConflicts(i, col, oldVal);
											
											//Check col for duplicates
											if(oldVal == sudokuPuzzle[row][i])
												checkConflicts(row, i, oldVal);
										}
										
										//Check square for duplicates
										for(int i = (row/3)*3; i <= ((row/3)*3)+2; ++i){
											for(int j = (col/3)*3; j <= ((col/3)*3)+2; ++j){
												if(oldVal == sudokuPuzzle[i][j])
													checkConflicts(i, j, oldVal);
											}
										}
									}
								}
							}
						}
					}
				});	
			}
		}
	}
	
	/*
	 * Sudoku boxes painting
	 */
	private void createGrid()
	{
		for (int row = 0; row < 13; row++){
            for (int col = 0; col < 13; col++){
                StackPane cell = new StackPane();
                
                if(0 == row%4){
                	if(0 == col%4){
                		cell.getChildren().addAll(new Rectangle(CELL_THICKNESS , CELL_THICKNESS, CELL_COLOR));
                	}
                	else{
                		cell.getChildren().addAll(new Rectangle(CELL_WIDTH + CELL_PADDING, CELL_THICKNESS, CELL_COLOR));
                	}
                }
                else{
                	if(0 == col%4){
                		cell.getChildren().addAll(new Rectangle(CELL_THICKNESS,CELL_HEIGHT + CELL_PADDING, CELL_COLOR));
                	}
                	else{   
                		int r = row - (row > 8 ? 3 : row > 4 ? 2 : 1);
                		int c = col - (col > 8 ? 3 : col > 4 ? 2 : 1);
                		
                		cell.getChildren().add(puzzleButtons[r][c]);
                	}
                }  
                this.add(cell, col, row);
            }
        }
	}
	
	/*
	 * Saving a backup to revert to if user tries to edit
	 */
	private void backupPuzzle()
	{
		for(int i = 0; i < sudokuPuzzle.length; ++i)
			for(int j = 0; j < sudokuPuzzle[i].length; ++j){
				backupSudokuPuzzle[i][j] = sudokuPuzzle[i][j];
			}
	}
	
	/*
	 * Restores backup for edit mode
	 */
	private void restorePuzzle()
	{
		for(int i = 0; i < backupSudokuPuzzle.length; ++i)
			for(int j = 0; j < backupSudokuPuzzle[i].length; ++j){
				sudokuPuzzle[i][j] = backupSudokuPuzzle[i][j];
			}
	}
	
	/*
	 * Populates puzzle with input values (for play and upload options)
	 */
	private void setPuzzle(int [][] inputPuzzle)
	{
		for(int i = 0; i < 9; ++i){
			for(int j = 0; j < 9; ++j){
				sudokuPuzzle[i][j] = inputPuzzle[i][j];
				puzzleButtons[i][j].setValue(inputPuzzle[i][j]);
				puzzleButtons[i][j].refreshImage();
				checkConflicts(i, j, inputPuzzle[i][j]);
			}
		}
	
	}
	
	/*
	 * Graphics when user wins 
	 */
	private void winGraphics()
	{
		for(int i = 0; i < 3; ++i)
		{
			puzzleButtons[i][2].setYellow();
			puzzleButtons[i][6].setYellow();
			puzzleButtons[i+6][2].setYellow();
			puzzleButtons[i+6][6].setYellow();
		}
		puzzleButtons[1][4].setYellow();
		puzzleButtons[2][3].setYellow();
		puzzleButtons[2][5].setYellow();
		
		for(int i = 2; i < 7; ++i)
		{
			puzzleButtons[3][i].setRed();
			puzzleButtons[5][i].setRed();
		}
		puzzleButtons[4][4].setRed();
		
		for(int i = 6; i < 9; ++i)
		{
			puzzleButtons[i][i-3].setYellow();
		}
		
	}
	
	/*
	 * Saves puzzle in edit mode before going into play mode
	 */
	public void saveState()
	{
		backupPuzzle();
				
		for(int i = 0; i < 9; ++i)
			for(int j = 0; j < 9; ++j){
				if(!puzzleButtons[i][j].getPermanent() && 0 != puzzleButtons[i][j].getValue()){
					puzzleButtons[i][j].setValue(sudokuPuzzle[i][j]);
					puzzleButtons[i][j].setPermanent();
					puzzleButtons[i][j].refreshImage();
				}
			}	
	}

	/*
	 * Edit clicked
	 */
	public void editState()
	{
		restorePuzzle();
				
		for(int i = 0; i < 9; ++i)
			for(int j = 0; j < 9; ++j){
				if(0 != puzzleButtons[i][j].getValue()){
					puzzleButtons[i][j].setValue(sudokuPuzzle[i][j]);
					puzzleButtons[i][j].setEditable();
					puzzleButtons[i][j].refreshImage();
				}
			}	
	}
		
	/*
	 * Default constructor
	 */
	public SudokuGrid()
	{
		init();
		createGrid();
	}
	
	/*
	 * Overloaded constructor for upload and play
	 */
	public SudokuGrid(int [][] inputPuzzle)
	{
		init();
		setPuzzle(inputPuzzle);
		createGrid();
	}
	
	/*
	 * Input selection buttons for left pane
	 */
	public Button[] getInputButtons()
	{
		return inputButtons;
	}
}
