package controller;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class InputMenu extends GridPane{
	
	public InputMenu(Node... buttons)
	{
		for(int i = 0; i < buttons.length; ++i)
		{
			StackPane cell = new StackPane();
			cell.getChildren().add(buttons[i]);
			this.add(cell, 0, i);
		}
	}
}
