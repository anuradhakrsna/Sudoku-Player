package application;

import static controller.NameRepository.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import controller.InputMenu;
import controller.SudokuGrid;
import controller.TitlePane;

/*
 * Stage
 * Scene
 * 
 * Button
 */
	
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class Main extends Application implements EventHandler<ActionEvent>
{
	Scene scene;
	BorderPane pane;
	SudokuGrid sg;	
	
	Button btnPlay, btnCreate, btnUpload, btnInstructions, btnQuit; 
	MenuItem menuHome, menuPlay, menuCreate, menuUpload, menuQuit, menuInstructions, menuAbout;
	Button btnSave, bntEdit, btnHome, btnClear, btnExport;
	
	public Main()
	{
		/*
		 * Main page buttons
		 */
		btnPlay 	= new Button(PLAY);
		btnCreate 	= new Button(CREATE);
		btnUpload 	= new Button(UPLOAD);
		btnInstructions	= new Button(INSTRUCTIONS);
		btnQuit 	= new Button(QUIT);
		
		btnPlay.setOnAction(this);
		btnCreate.setOnAction(this);
		btnUpload.setOnAction(this);
		btnInstructions.setOnAction(this);
		btnQuit.setOnAction(this);
		
		btnSave		= new Button(SAVEANDPLAY);
		bntEdit		= new Button(EDIT);
		btnHome		= new Button(HOME);
		btnClear	= new Button(CLEAR);		
		btnExport	= new Button(EXPORT);
		
		btnSave.setOnAction(this);
		bntEdit.setOnAction(this);
		btnHome.setOnAction(this);
		btnClear.setOnAction(this);
		btnExport.setOnAction(this);
		
		menuHome			= new MenuItem(HOME);
		menuPlay 			= new MenuItem(PLAY);
		menuCreate 			= new MenuItem(CREATE);
		menuUpload 			= new MenuItem(UPLOAD);
		menuQuit 			= new MenuItem(QUIT);
		menuInstructions 	= new MenuItem(INSTRUCTIONS);
		menuAbout 			= new MenuItem(ABOUT);
		
		menuHome.setOnAction(this);
		menuPlay.setOnAction(this);
		menuCreate.setOnAction(this);
		menuUpload.setOnAction(this);
		menuQuit.setOnAction(this);
		menuInstructions.setOnAction(this);
		menuAbout.setOnAction(this);
	}
	
	@Override
	public void handle(ActionEvent event) 
	{
		if(event.getSource() == menuHome || event.getSource() == btnHome)
		{
			pane.setCenter(new VBox(btnPlay, btnCreate, btnUpload, btnInstructions, btnQuit));
			pane.setLeft(null);
			pane.setRight(null);
			return;
		}
		else if(event.getSource() == btnPlay || event.getSource() == menuPlay)
		{
			int puzzleNum = 1;

			try (BufferedReader br = new BufferedReader(new FileReader("src/Resources/puzzleList.txt"))) {
				int lineCount = 0;
			    while (null != br.readLine()) {
			       ++lineCount;
			    }
			    Random rand = new Random();
			    puzzleNum = rand.nextInt(lineCount) + 1;     
				}
			catch(Exception e){
				e.printStackTrace();
			}

			try (BufferedReader br = new BufferedReader(new FileReader("src/Resources/puzzleList.txt"))) {
				String line = "";
				while (puzzleNum > 0 && null != (line = br.readLine())) {
				       --puzzleNum;
				    }
				String[] values = line.split("\\s+");
				if(9*9 != values.length){
					//invalid value
				}
				else{
					int[][] inputPuzzle = new int[9][9];
					for(int i = 0; i < 9; ++i){
						for(int j = 0; j < 9; ++j){
							inputPuzzle[i][j] = Integer.parseInt(values[i*9 + j]);
						}
					}
					sg = new SudokuGrid(inputPuzzle);
					sg.saveState();
					pane.setCenter(sg);
					pane.setRight(new VBox(btnHome, bntEdit, btnExport, btnQuit));
					pane.setLeft(new InputMenu(sg.getInputButtons()));		
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			return;
				
		}
		else if(event.getSource() == btnCreate || event.getSource() == menuCreate)
		{
			sg = new SudokuGrid();
			pane.setCenter(sg);
			pane.setRight(new VBox(btnHome, btnSave, btnClear, btnExport, btnQuit));
			pane.setLeft(new InputMenu(sg.getInputButtons()));
			return;
		}
		else if(event.getSource() == btnUpload || event.getSource() == menuUpload)
		{
			Stage stage = (Stage) pane.getScene().getWindow();
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Select Input Sudoku Puzzle File");
			fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"));
			File file = fileChooser.showOpenDialog(stage);

			if(null != file)
			{
				int[][] inputPuzzle = new int[9][9];
				try {
					Scanner scanner = new Scanner(file);
					int i = 0, j = 0;
					while(scanner.hasNextInt()){
						inputPuzzle[i][j] = scanner.nextInt();
						if(8 == j){
							if(8 == i){
								break;
							}
							else{
								j = 0;
								++i;
							}
						}
						else{
							++j;
						}
					}
					scanner.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}				
				
				sg = new SudokuGrid(inputPuzzle);
				pane.setCenter(sg);
				pane.setRight(new VBox(btnHome, btnSave, btnClear, btnExport, btnQuit));
				pane.setLeft(new InputMenu(sg.getInputButtons()));
			}
			
			return;
		}
		else if(event.getSource() == btnQuit  || event.getSource() == menuQuit)
		{			
		    Platform.exit();
		    return;
		}
		else if(event.getSource() == btnSave)
		{			
			sg.saveState();
			pane.setRight(new VBox(btnHome, bntEdit, btnExport, btnQuit));
			return;
		}
		else if(event.getSource() == bntEdit)
		{			
			if(!sg.isComplete()){
			sg.editState();
			pane.setRight(new VBox(btnHome, btnSave, btnClear, btnExport, btnQuit));
			}
			return;
		}
		else if(event.getSource() == btnClear)
		{			
			sg.clearPuzzle();
			return;
		}
		else if(event.getSource() == btnExport)
		{		
			Stage stage = (Stage) pane.getScene().getWindow();
			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"));
			File file = fileChooser.showSaveDialog(stage);
			
			if(null != file)
			{
				try {
					FileWriter fileWriter = new FileWriter(file);
					fileWriter.write(sg.getAsString());
					fileWriter.close();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			return;
		}
		else if(event.getSource() == menuInstructions || event.getSource() == btnInstructions)
		{		
			StackPane img = new StackPane();
			ImageView imageView = new ImageView(new Image("image/Instructions.png"));
			img.getChildren().add(imageView);
			img.getStyleClass().add("StackPaneCustom");
			
			pane.setCenter(img);
			pane.setLeft(null);
			pane.setRight(null);
		}
		else if(event.getSource() == menuAbout)
		{		
			StackPane img = new StackPane();
			ImageView imageView = new ImageView(new Image("image/About.png"));
			img.getChildren().add(imageView);
			img.getStyleClass().add("StackPaneCustom");
			
			pane.setCenter(img);
			pane.setLeft(null);
			pane.setRight(null);
		}
	}
	
	public void init(Stage primaryStage)
	{
		try 
		{			
			//Pane
			pane = new BorderPane();
			pane.setTop(new TitlePane(menuHome, menuPlay, menuCreate, menuUpload, menuQuit, menuInstructions, menuAbout));
			pane.setCenter(new VBox(btnPlay, btnCreate, btnUpload, btnInstructions, btnQuit));
			
			//Scene
			scene = new Scene(pane, 1000, 800);
			scene.getStylesheets().add("http://fonts.googleapis.com/css?family=Gafata");
			scene.getStylesheets().add("formatting.css");
			
			//Stage
			primaryStage.setScene(scene);
			primaryStage.setTitle(APPLICATION_TITLE);
			primaryStage.setResizable(false);
			primaryStage.show();		
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	

	
	@Override
	public void start(Stage primaryStage) throws Exception
	{
		init(primaryStage);
	}
	
	public static void main(String[] args) 
	{
		Application.launch(args);
	}

}

