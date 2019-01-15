import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.io.File;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage; 
/*
 * Pooja Nadkarni
 * Period 1
 * 4/1/18
 * 
 * This lab took me around four hours. I was able to complete most things
 * smoothly. However, I did have trouble understanding how to create the
 * WebView and the scroll for the window. once I was able to complete that
 * part of the program, the entire game was complete.
 * 
 */
public class P1_Nadkarni_Pooja_MinesweeperGUI extends Application {
	private int tileSize = 50;
	private double timeStart = 0.0;
	private GridPane grid = new GridPane();
	private P1_Nadkarni_Pooja_MinesweeperModel model = new P1_Nadkarni_Pooja_MinesweeperModel(8, 8, 10);
	private boolean isGameOver = false;
	private boolean isGameWon = false;
	private int rows = model.getNumRows();
	private int cols = model.getNumCols();
	private VBox vBox = new VBox();
	private Scene scene;
	private Label mines = new Label("");
	private Label time;
	private Image img2;
	private ImageView imgView2;
	private boolean first = true;
	private long timeNow = 0;
	private AnimationTimer timer = new AnimationTimer() {
		public void handle(long now) {
			if(first) {
				timeNow =  now;
				first = false;
			}
			time.setText("" + (((now - (timeNow)) * (Math.pow(10, -9)))));
		}
	};
	private boolean isFirstClick = true;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Minesweeper GUI");
		stage.setResizable(true);
		stage.sizeToScene();

		Group root = new Group();
		scene = new Scene(root, Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());
		
		stage.setScene(scene);
		ScrollPane scroll = new ScrollPane();
		scroll.setMaxSize(scene.getWidth(), scene.getHeight());
		vBox.setAlignment(Pos.CENTER);
		vBox.setPadding(new Insets(scene.getHeight() / 20, scene.getWidth() / 50, scene.getHeight() / 20, scene.getWidth() / 50));
	    vBox.setSpacing(scene.getWidth() / 50);
	    scroll.setContent(vBox);
		root.getChildren().add(scroll);
		
		//Menu Bar
		HBox hbox = new HBox();
		hbox.setAlignment(Pos.CENTER);
		hbox.setPadding(new Insets(scene.getHeight() / 8, scene.getWidth() / 50, scene.getHeight() / 8, scene.getWidth() / 50));
	    hbox.setSpacing(scene.getWidth() / 50);
		MenuBar menuBar = new MenuBar();
		Menu menu = new Menu("Game");
		MenuEventHandler handler = new MenuEventHandler();
		MenuItem beginnerGame = new MenuItem("New Beginner Game");
		beginnerGame.setOnAction(handler);
		MenuItem middleGame = new MenuItem("New Intermediate Game");
		middleGame.setOnAction(handler);
		MenuItem expertGame = new MenuItem("New Expert Game");
		expertGame.setOnAction(handler);
		MenuItem customGame = new MenuItem("New Custom Game");
		customGame.setOnAction(handler);
		MenuItem exit = new MenuItem("Exit");
		beginnerGame.setOnAction(handler);
		menu.getItems().addAll(beginnerGame, middleGame, expertGame, customGame, exit);
		menuBar.getMenus().add(menu);
		
			
		Menu menu2 = new Menu("Options");
		MenuItem setMines = new MenuItem("Set Number Of Mines");
		setMines.setOnAction(handler);
		menu2.getItems().addAll(setMines);
		menuBar.getMenus().add(menu2);
		
		
		Menu menu3 = new Menu("Help");
		MenuItem howTo = new MenuItem("How To Play");
		howTo.setOnAction(handler);
		MenuItem about = new MenuItem("About");
		about.setOnAction(handler);
		menu3.getItems().addAll(howTo, about);
		menuBar.getMenus().add(menu3);
		vBox.getChildren().add(hbox);
		vBox.getChildren().add(menuBar);
		
		//Board
		setModel(model);
		
		//Top Row
		VBox vbox3 = new VBox();
		Text text = new Text(10, 50, "Mines Remaining");
		text.setFont(new Font("Arial", 20));
		InnerShadow insideShadow = new InnerShadow();
		insideShadow.setColor(Color.AQUA);
		text.setEffect(insideShadow);
		mines = new Label("" + (model.totalMineCount() - model.totalMarkings()));
		vbox3.getChildren().addAll(text, mines);
		hbox.getChildren().add(vbox3);
		
		img2 = new Image("face_smile.gif");
		imgView2 = new ImageView(img2);				
		hbox.getChildren().add(imgView2);
		
		VBox vbox2 = new VBox();
		Text text2 = new Text(10, 50, "Time Elapsed");
		text2.setFont(new Font("Arial", 20));
		InnerShadow insideShadow2 = new InnerShadow();
		insideShadow2.setColor(Color.AQUA);
		text2.setEffect(insideShadow2);
		time = new Label("" + timeStart);
		vbox2.getChildren().addAll(text2, time);
		hbox.getChildren().add(vbox2);
		
		stage.show();
	}

	
	private class MenuEventHandler implements EventHandler <ActionEvent> {
		public void handle(ActionEvent event) {
			if(((MenuItem) event.getSource()).getText().equals("New Beginner Game")){
				isGameOver = false;
				time.setText("0.0");
				isGameWon = false;			
				isFirstClick = true;
				imgView2.setImage(new Image("face_smile.gif"));
				model = new P1_Nadkarni_Pooja_MinesweeperModel(8, 8, 10);
				setModel(model);
			}  else if(((MenuItem) event.getSource()).getText().equals("New Intermediate Game")){
				isGameOver = false;
				time.setText("0.0");
				isGameWon = false;
				isFirstClick = true;
				imgView2.setImage(new Image("face_smile.gif"));
				model = new P1_Nadkarni_Pooja_MinesweeperModel(16, 16, 40);
				setModel(model);
			} else if(((MenuItem) event.getSource()).getText().equals("New Expert Game")){
				isGameOver = false;
				isGameWon = false;
				time.setText("0.0");
				isFirstClick = true;
				imgView2.setImage(new Image("face_smile.gif"));
				model = new P1_Nadkarni_Pooja_MinesweeperModel(16, 31, 99);
				setModel(model);
			} else if(((MenuItem) event.getSource()).getText().equals("New Custom Game")){
				isGameOver = false;
				time.setText("0.0");
				isGameWon = false;
				isFirstClick = true;
				imgView2.setImage(new Image("face_smile.gif"));
				TextInputDialog input = new TextInputDialog();
				input.setHeaderText("How many rows would you like?");
				input.showAndWait();
				String numRowsString = input.getEditor().getText();
				int numRows = 0;
				try {
					numRows = Integer.parseInt(numRowsString);	
				} catch (NumberFormatException e) {
					Alert a = new Alert(AlertType.ERROR, "Error: Please enter an integer for the number of mines", ButtonType.OK);
					a.showAndWait();
					return;
				}
				
				TextInputDialog input2 = new TextInputDialog();
				input2.setHeaderText("How many columns would you like?");
				input2.showAndWait();
				String numColsString = input2.getEditor().getText();
				int numCols = 0;
				try {
					numCols = Integer.parseInt(numColsString);	
				} catch (NumberFormatException e) {
					Alert a = new Alert(AlertType.ERROR, "Error: Please enter an integer for the number of mines", ButtonType.OK);
					a.showAndWait();
					return;
				}
				
				TextInputDialog input3 = new TextInputDialog();
				input3.setHeaderText("How many mines would you like?");
				input3.showAndWait();
				String numMinesString = input3.getEditor().getText();
				int numMines = 0;
				try {
					numMines = Integer.parseInt(numMinesString);	
				} catch (NumberFormatException e) {
					Alert a = new Alert(AlertType.ERROR, "Error: Please enter an integer for the number of mines", ButtonType.OK);
					a.showAndWait();
					return;
				}
				if(numRows < 1 || numCols < 1 || numMines < 1) {
					Alert a = new Alert(AlertType.ERROR, "Error: The number of mines, columns, and rows must be greater than 1", ButtonType.OK);
					a.showAndWait();
				} else {
					model = new P1_Nadkarni_Pooja_MinesweeperModel(numRows, numCols, numMines);
					setModel(model);
				}
			} else if(((MenuItem) event.getSource()).getText().equals("Set Number Of Mines")) {
				TextInputDialog input3 = new TextInputDialog();
				input3.setHeaderText("How many mines would you like?");
				input3.showAndWait();
				String numMinesString = input3.getEditor().getText();
				int numMines = 0;
				try {
					numMines = Integer.parseInt(numMinesString);	
				} catch (NumberFormatException e) {
					Alert a = new Alert(AlertType.ERROR, "Error: Please enter an integer for the number of mines", ButtonType.OK);
					a.showAndWait();
				}
				if(numMines > 1) {
					model.newGrid(model.getNumRows(), model.getNumCols(), numMines);
					mines.setText("" + (model.totalMineCount() - model.totalMarkings()));
				} else if(numMines != 0){
					Alert a = new Alert(AlertType.ERROR, "Error: There must be more than 1 mine", ButtonType.OK);
					a.showAndWait();
				}
			} else if(((MenuItem) event.getSource()).getText().equals("About")) {
				Alert a = new Alert(AlertType.NONE, "MINESWEEPER v. 1 was developed in March 2018 by Pooja Nadkarni", ButtonType.OK);
				a.showAndWait();
			} else if(((MenuItem) event.getSource()).getText().equals("How To Play")) {
				Stage stage2 = new Stage();
				Group root2 = new Group();
				Scene scene2 = new Scene(root2, Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());
				ScrollPane scroll = new ScrollPane();
				WebView browser = new WebView();
				WebEngine webEngine = browser.getEngine();
				System.out.println("here1");
				scroll.setContent(browser);
				root2.getChildren().add(scroll);
				webEngine.load("https://docs.google.com/document/d/1aVHqE1z-dQBvzmnUpQwKKTnSw0Vuwl3PVZ6W_pgg_dE/edit?usp=sharing");
				stage2.setScene(scene2);
				stage2.show();			
				System.out.println("here2");
			} 
		}
	}
	
	public void setModel(P1_Nadkarni_Pooja_MinesweeperModel model) {	
		vBox.getChildren().remove(grid);
		grid = new GridPane();
		grid.setColumnSpan(grid, model.getNumCols());
		grid.setRowSpan(grid, model.getNumRows());
		if(isGameWon) {
			imgView2.setImage(new Image("face_win.gif"));
		}
		if(isGameOver) {
			imgView2.setImage(new Image("face_dead.gif"));
			for(int i = 0; i < model.getNumRows(); i++) {
				for(int x = 0; x < model.getNumCols(); x++) {		
					if(model.isMine(i, x)) {
						if(model.isFlag(i, x)) {
							Image img = new Image("bomb_revealed.gif");
							ImageView imgView = new ImageView(img);	
							grid.add(imgView, i, x);
						} else if(isGameOver){
							Image img = new Image("bomb_wrong.gif");
							ImageView imgView = new ImageView(img);	
							grid.add(imgView, i, x);
						}
					} else {
						if(model.isRevealed(i, x)) {
							Image img = new Image("num_" + model.numNeighboringMines(i, x) + ".gif");
							ImageView imgView = new ImageView(img);	
							grid.add(imgView, i, x);		
						} else {
							Image img = new Image("blank.gif");
							ImageView imgView = new ImageView(img);	
							grid.add(imgView, i, x);		
						}
					}					
				}
			}
		} else {
			for(int i = 0; i < model.getNumRows(); i++) {
				for(int x = 0; x < model.getNumCols(); x++) {				
					if(model.isRevealed(i, x)) {
						 if (model.isMine(i, x)){
							Image img = new Image("bomb_death.gif");
							ImageView imgView = new ImageView(img);	
							MyMouseHandler handler = new MyMouseHandler();
							imgView.setOnMouseClicked(handler);
							grid.add(imgView, i, x);
						} else {						
							Image img = new Image("num_" + model.numNeighboringMines(i, x) + ".gif");
							ImageView imgView = new ImageView(img);	
							MyMouseHandler handler = new MyMouseHandler();
							imgView.setOnMouseClicked(handler);
							grid.add(imgView, i, x);											
						}
					} else {
						if(model.isFlag(i, x)) {
							Image img = new Image("bomb_flagged.gif");
							ImageView imgView = new ImageView(img);	
							MyMouseHandler handler = new MyMouseHandler();
							imgView.setOnMouseClicked(handler);
							grid.add(imgView, i, x);
						} else {
							Image img = new Image("blank.gif");
							ImageView imgView = new ImageView(img);	
							MyMouseHandler handler = new MyMouseHandler();
							imgView.setOnMouseClicked(handler);
							grid.add(imgView, i, x);
						}						
					}
				}
			}
			
		}
		vBox.getChildren().add(grid);
		mines.setText("" + (model.totalMineCount() - model.totalMarkings()));
	}
	
	
	private class MyMouseHandler implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent event) {
			if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
				int row = 0;
				int col = 0;				
				if(event.getButton() == MouseButton.PRIMARY) {
					if(isFirstClick) {
						timer.start();
						isFirstClick = false;
					}
					col = grid.getRowIndex((ImageView) event.getSource());
					row = grid.getColumnIndex((ImageView) event.getSource());
					if(!model.isMine(row, col)) {
						if(!model.isRevealed(row, col)) {
							model.reveal(row, col);
							setModel(model);
							if(model.numCorrectFlags() == model.totalMineCount()) {
								model.reveal(row, col);
								isGameWon = true;
								timer.stop();
								setModel(model);
								Alert a = new Alert(AlertType.NONE, "CONGRATULATIONS! YOU HAVE SUCCESSFULLY CLEARED ALL OF THE MINES", ButtonType.OK);
								a.showAndWait();
							}
						} else {
							Alert a = new Alert(AlertType.ERROR, "Error: You have already revealed this tile", ButtonType.OK);
							a.showAndWait();
						}						
					} else {
						model.reveal(row, col);
						isGameOver = true;
						timer.stop();
						first = true;
						setModel(model);						
						Alert a = new Alert(AlertType.NONE, "Oh no! You revealed a mine. Game Over.", ButtonType.OK);
						a.showAndWait();
					}
				} else if(event.getButton() == MouseButton.SECONDARY) {
					col = grid.getRowIndex((ImageView) event.getSource());
					row = grid.getColumnIndex((ImageView) event.getSource());
					model.setFlag(row, col, !model.isFlag(row, col));
					setModel(model);					
					if(model.numCorrectFlags() == model.totalMineCount()) {
						model.reveal(row, col);
						timer.stop();
						isGameWon = true;						
						setModel(model);
						Alert a = new Alert(AlertType.NONE, "CONGRATULATIONS! YOU HAVE SUCCESSFULLY CLEARED ALL OF THE MINES", ButtonType.OK);
						a.showAndWait();
					}
				}
			}

		}
	}
}
