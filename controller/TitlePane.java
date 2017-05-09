package controller;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;

import static controller.NameRepository.*;

public class TitlePane extends BorderPane
{
	public TitlePane(	MenuItem menuHome, MenuItem menuPlay, MenuItem menuCreate, MenuItem menuUpload, 
						MenuItem menuQuit, MenuItem menuInstructions, MenuItem menuAbout) 
	{
		MenuBar menuBar = new MenuBar();
		Menu menu = new Menu(MENU_OPT_1);
		Menu help = new Menu(MENU_OPT_2); 
		
		menu.getItems().addAll(menuHome, menuPlay, menuCreate, menuUpload, menuQuit);
		help.getItems().addAll(menuInstructions, menuAbout);
		
		menuBar.getMenus().addAll(menu, help);
		
		setTop(menuBar);
		setBottom(new ImagePane());
	}
}

class ImagePane extends StackPane 
{
	public ImagePane() 
	{
		//Created logo using http://logomakr.com
		ImageView imageView = new ImageView(new Image("image/Logo.png"));
		getChildren().add(imageView);
		getStyleClass().add("TitlePane");
		setPrefSize(800, 100);
	}
}