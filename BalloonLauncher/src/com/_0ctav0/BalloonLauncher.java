package com._0ctav0;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;

import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.*;
import javafx.concurrent.Task;
import javafx.scene.*;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

public final class BalloonLauncher extends Application {
	
	private static final double WIDTH = 800, HEIGHT = 600, WIDTH_BUTTON = 120;
	
	private GameLoop gameLoop;
	
	private Group root, gMainMenu, gNewGameMenu, gGame;
	private Scene scene;
	private Stage window;
	
	private Label lSec, lTemperature, lAltitude;
	private Button bContinue;
	
	private boolean isPlay = false, isPause = false;
	private double
			rocketX = WIDTH / 2, rocketY = HEIGHT - 190,
			massRocket = 100.0, 
			densityAir = 1.225, densityBallons = 0.179 + 0.05,
			temperature = 30.0, altitude = 0.1;
	private int countBalloons;
	private String gameTime = "";
	
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		root = new Group();
		scene = new Scene(root);
		window = primaryStage;
		window.setScene(scene);
		window.setTitle("BallonLauncher");
		window.setResizable(false);
		window.setWidth(WIDTH);
		window.setHeight(HEIGHT);
		
		
		initMainMenu();
		initNewGameMenu();
		
		showMainMenu();
		
		window.show();
	}
	
	private void initMainMenu() {
		
		bContinue = new Button("����������");
		bContinue.setPrefWidth(WIDTH_BUTTON);
		bContinue.setOnAction(e -> {
			showGame();
		});
		
		
		Button bRestart = new Button("����� ������");
		bRestart.setPrefWidth(WIDTH_BUTTON);
		bRestart.setOnAction(e -> {
			showNewGameMenu();	
		});
		
		Button bExit = new Button("�����");
		bExit.setPrefWidth(WIDTH_BUTTON);
		bExit.setOnAction(e -> {
			window.close();	
		});
		
		
		Rectangle rect = new Rectangle(0, 0, WIDTH, HEIGHT);
		rect.setFill(Color.GRAY);
		rect.setOpacity(0.5);

		VBox vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);
		vbox.setSpacing(20);
		vbox.setPrefSize(WIDTH, HEIGHT);
		vbox.getChildren().addAll(bContinue, bRestart, bExit);
		
		gMainMenu = new Group(rect, vbox);
	}
	
	private void showMainMenu() {
		
		bContinue.setVisible(isPlay);
		root.getChildren().remove(gNewGameMenu);
		root.getChildren().add(gMainMenu);
	}
	
	private void initNewGameMenu() {
		
		final TextField tfMassRocket = new TextField("100.0");
		tfMassRocket.setMaxWidth(WIDTH_BUTTON);
		
		Label lMassRocket = new Label("����� ������ (�)");
		
		final TextField tfTemperature = new TextField("15.0");
		tfTemperature.setMaxWidth(WIDTH_BUTTON);
		
		Label lTemperature = new Label("����������� ������� (��)");
		
		
		Button bStart = new Button("������� ������");
		bStart.setPrefWidth(WIDTH_BUTTON);
		bStart.setOnAction(e -> {
			try {
				massRocket = Double.parseDouble(tfMassRocket.getText());
				temperature = Double.parseDouble(tfTemperature.getText());
				// if no errors then play
				play();
			} catch (NumberFormatException ex) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("������!");
				alert.setHeaderText(null);
				alert.setContentText("����� ��������� ������� ���������");
				alert.showAndWait();
			}
		});
		
		Button bBack = new Button("�����");
		bBack.setPrefWidth(WIDTH_BUTTON);
		bBack.setOnAction(e -> {
			showMainMenu();
		});
		
		Rectangle rect = new Rectangle(0, 0, WIDTH, HEIGHT);
		rect.setFill(Color.GRAY);
		rect.setOpacity(0.5);
		
		
		GridPane gridPane = new GridPane();
		gridPane.setVgap(20);
		gridPane.setHgap(10);
		GridPane.setConstraints(lMassRocket, 2, 0);
		GridPane.setConstraints(tfMassRocket, 1, 0);
		GridPane.setConstraints(lTemperature, 2, 1);
		GridPane.setConstraints(tfTemperature, 1, 1);
		GridPane.setConstraints(bStart, 1, 2);
		GridPane.setConstraints(bBack, 1, 3);
		gridPane.getChildren().addAll(lMassRocket, tfMassRocket, lTemperature, tfTemperature, bStart, bBack);
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setPrefSize(WIDTH, HEIGHT);
		
		gNewGameMenu = new Group(rect, gridPane);
	}
	
	private void showNewGameMenu() {
		root.getChildren().remove(gMainMenu);
		root.getChildren().add(gNewGameMenu);
	}
	
	private void play() { ///////////////// PLAY
		
		System.out.println("������� ������ ������ = " + massRocket + " ����");
		isPlay = false;
		isPause = false;
		isPlay = true;
		if ( gameLoop != null ) {
			gameLoop.interrupt();
		}
		
		final Rectangle sky = new Rectangle(0, 0, WIDTH, HEIGHT);
		sky.setFill(Color.DEEPSKYBLUE);
		
		FileInputStream inputstream;
		Image image;
		final ImageView imgRocket;
		try {
			inputstream = new FileInputStream("rocket.png");
			image = new Image(inputstream);
			imgRocket = new ImageView(image);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		
		imgRocket.setX(rocketX - 25);
		//imgRocket.setX(WIDTH / 2 - image.getWidth());
		imgRocket.setY(rocketY);
		//imgRocket.setY(HEIGHT - image.getHeight() - 30);
		
		Path ground = new Path();
		
		MoveTo moveTo = new MoveTo(0, HEIGHT - 50);
		QuadCurveTo curveUpper = new QuadCurveTo(WIDTH / 2, HEIGHT - 100, WIDTH, HEIGHT - 50);
		VLineTo vLineRight = new VLineTo(HEIGHT);
		HLineTo hLineBottom = new HLineTo(0); 
		
		ground.getElements().addAll(moveTo, curveUpper, vLineRight, hLineBottom);
		
		Stop[] stops = new Stop[] {
				new Stop(0, Color.DARKGREEN),
				new Stop(0.5, Color.GREEN),
				new Stop(1, Color.LIGHTGREEN)
		};
		LinearGradient grColor = new LinearGradient(1.5, 1.5, 1.5, 0, true, CycleMethod.NO_CYCLE, stops);
		ground.setFill(grColor);
		
		
		Button bToMenu = new Button("� ����");
		bToMenu.setOpacity(0.5);
		bToMenu.setOnAction(e -> {
			setBlurOnChildren(true);
			isPause = true;
			showMainMenu();
		});
		
		Button bFillBalloons = new Button("��������� ����");
		bFillBalloons.setOnAction(e -> {
			fillBalloons();
			bFillBalloons.setVisible(false);
		});	
		
		lSec = new Label("00:00");
		lTemperature = new Label(Double.toString(temperature) + " �C");
		lAltitude = new Label(Double.toString(altitude) + " �.");
		
		
		HBox hBox = new HBox();
		hBox.getChildren().addAll(bToMenu, bFillBalloons);
		hBox.setSpacing(5);
		
		GridPane gridPane = new GridPane();
		GridPane.setConstraints(hBox, 0, 0);
		GridPane.setConstraints(lSec, 20, 0);
		GridPane.setConstraints(lTemperature, 21, 0);
		GridPane.setConstraints(lAltitude, 22, 0);
		gridPane.setHgap(10);
		gridPane.getChildren().addAll(hBox, lSec, lTemperature, lAltitude);
		

		gGame = new Group(sky, ground, imgRocket, gridPane);
		root.getChildren().add(gGame);
	}
	
	private void setBlurOnChildren(boolean b) {
		GaussianBlur blur = b ? new GaussianBlur(20) : null;
		for (Node e : gGame.getChildren() ) {
			e.setEffect(blur);
		}
	}
	
	private void showGame() {
		setBlurOnChildren(false);
		isPause = false;
		root.getChildren().clear();
		root.getChildren().addAll(gGame);
	}
	
	private void fillBalloons() {
		// start thread
		gameLoop = new GameLoop();
		countBalloons = (int) Math.ceil(massRocket / 10);
		Circle[] balloons = new Circle[countBalloons];
		Line[] balloonsLine = new Line[countBalloons];
		for ( int i = 0; i < countBalloons; i++ ) {
			balloons[i] = new Circle(1);
			balloons[i].setFill(Color.CORAL);
			if ( i < countBalloons / 2 ) {
				balloons[i].setCenterX( WIDTH / 2 - 200 + i * 25 );
			} else {
				balloons[i].setCenterX( WIDTH / 2 - 100 + i * 25 );
			}
			balloons[i].setCenterY( HEIGHT / 2 + i % 2 * 25 );
			balloonsLine[i] = new Line();
			balloonsLine[i].setStartX(rocketX);
			balloonsLine[i].setStartY(rocketY);
			balloonsLine[i].setEndX(balloons[i].getCenterX());
			balloonsLine[i].setEndY(balloons[i].getCenterY());
			ScaleTransition anim = new ScaleTransition();
			TranslateTransition anim2 = new TranslateTransition();
			anim.setDuration(Duration.millis(2000));
			anim.setNode(balloons[i]);
			anim.setByX(10);
			anim.setByY(10);
			anim2.setDuration(Duration.millis(2000));
			anim2.setNode(balloons[i]);
			anim2.setFromY(100);
			anim2.setToY(0);
			
			RotateTransition animLine = new RotateTransition();
			animLine.setDuration(Duration.millis(2000));
			animLine.setNode(balloonsLine[i]);
			animLine.setFromAngle(30);
			animLine.setToAngle(0);
			//animLine.setFromY(100);
			//animLine.setToY(0);
			
			anim.play();
			anim2.play();
			//animLine.play();
			
		}
		gGame.getChildren().addAll(balloonsLine);
		gGame.getChildren().addAll(balloons);
	}
	
	private final class GameLoop extends Thread {
		
		public GameLoop() {
			setDaemon(true);
			start();
		}
		
		@Override
		public void run() {
			GameLoopTask task = new GameLoopTask();
			task.run();
		}

		private final class GameLoopTask extends Task<Integer> {
			
			private Date timeBegin;
			
			public GameLoopTask() {
				timeBegin = new Date();
				lSec.setVisible(true);
			}

			@Override
			protected Integer call() throws Exception {
				 while ( isPlay ) {
					 while ( isPause ) {
						 Thread.sleep(1000);
						 System.out.println("pause");
					 }
					 while ( !isPause ) {
						 Thread.sleep(100);
						 System.out.println("play");
						 Date now = new Date();
						 Date diff = new Date(now.getTime() - timeBegin.getTime());
						 gameTime = String.format("%tM:%tS", diff, diff);
						 Platform.runLater(new Runnable() {
							@Override
							public void run() {
								lSec.setText(gameTime);
							}
						 });
					 } 
	        	 }
				return 0;
			}
		};
	}
	
	
}


