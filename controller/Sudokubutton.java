package controller;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class Sudokubutton extends Button
{
	private	int 	value;
	private boolean permanent;
	private boolean conflict;
		
	public Sudokubutton(int x, int y)
	{
		value = 0;
		permanent = conflict = false;	
		
		this.onMouseEnteredProperty().set(new EventHandler<MouseEvent>() 
		{
			@Override
			public void handle(MouseEvent event) 
			{
				if(!permanent)
				{
					if(0 == value)
					{
						setGraphic(new ImageView(new Image("image/Selected_Empty.png")));
					}
					else
					{
						setGraphic(new ImageView(new Image("image/Selected/Selected_" + Integer.toString(value) + ".png")));
					}
				}
			} 
		});
		
		this.onMouseExitedProperty().set(new EventHandler<MouseEvent>()  
		{
			@Override
			public void handle(MouseEvent event) 
			{
				refreshImage();
			} 
		});
	}
	
	public void setValue(int val)
	{
		value = val;
	}
	
	public int getValue()
	{
		return value;
	}
	
	public void setPermanent()
	{
		permanent = true;
	}
	
	public boolean getPermanent()
	{
		return permanent;
	}
	
	public void setEditable()
	{
		permanent = false;
	}
	
	public void setConflict(boolean con)
	{
		conflict = con;
	}
	
	public boolean getConflict()
	{
		return conflict;
	}

	public void refreshImage()
	{
		if(!permanent)
		{
			if(0 == value)
			{
				setGraphic(new ImageView(new Image("image/Empty.png")));
			}
			else if(conflict)	
			{
				setGraphic(new ImageView(new Image("image/Error/Error_" + Integer.toString(value) + ".png")));
			}
			else
			{
				setGraphic(new ImageView(new Image("image/Regular/Regular_" + Integer.toString(value) + ".png")));
			}
		}
		else
		{
			setGraphic(new ImageView(new Image("image/Fixed/Fixed_" + Integer.toString(value) + ".png")));
		}
	}
	
	public void setRed()
	{
		if(0 != value)
			setGraphic(new ImageView(new Image("image/Error/Error_" + Integer.toString(value) + ".png")));
	}
	
	public void setYellow()
	{
		if(0 != value)
			setGraphic(new ImageView(new Image("image/Selected/Selected_" + Integer.toString(value) + ".png")));
	}
}
