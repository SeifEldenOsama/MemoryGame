package Memory2Game;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.StrokeTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


public class MainFrame extends Application{

	    private Group Root;
	    private Button[] B;
	    private int Counter = 0, Width = 200, Height = 150, Click = 0, TimeRemaining = 30;
	    private Button Start;
	    private ArrayList<Integer> MainList = new ArrayList<>();
	    private ArrayList<Integer> ClickedButtons = new ArrayList<>();
	    private ArrayList<Integer> Correct = new ArrayList<>();
	    private Timeline timeline;
	    private Label Time, Result;
	    private Rectangle R1;
	    private ScaleTransition scale;
	    private StrokeTransition stroke;
	    private Clip MyClip, MyClip1;
	@Override
	public void start(Stage stage)  {
		Root = new Group();
        Scene S1 = new Scene(Root, 700, 700, Color.BURLYWOOD);
        stage.setScene(S1);
        B = new Button[16];
        for (int i = 0; i < 16; i++) {
            if (Counter == 4) {
                Width = 200;
                Height += 100;
                Counter = 0;
            }
            B[i] = new Button();
            B[i].setLayoutX(Width);
            B[i].setLayoutY(Height);
            B[i].setPrefSize(70, 70);
            B[i].setFont(new Font("century gothic", 40));
            B[i].setBackground(Background.fill(Color.LIGHTGOLDENRODYELLOW));
            Root.getChildren().add(B[i]);
            Width += 80;
            Counter++;
        }

        Time = new Label("30");
        Time.setLayoutX(330);
        Time.setLayoutY(70);
        Time.setFont(new Font("century gothic", 40));
        Time.setTextFill(Color.RED);

        DropShadow Shadow = new DropShadow(10, 8, 8, Color.RED);
        Start = new Button("Start");
        Start.setLayoutX(260);
        Start.setLayoutY(550);
        Start.setPrefSize(200, 60);
        Start.setFont(new Font("Century gothic", 35));
        Start.setStyle("-fx-background-radius: 23px;-fx-background-color:black");
        Start.setTextFill(Color.CRIMSON);
        Start.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent) -> {
            Start.setEffect(Shadow);
        });
        Start.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent) -> {
            Start.setEffect(null);
        });

        Start.setOnAction((ActionEvent) -> {
            CountdownSound();
            Root.getChildren().removeAll(R1, Result);
            Start.setDisable(true);
            TimeRemaining = 30;
            Time.setText("30");
            Correct.clear();
            MakeGame();
            CountDown();
        });

        R1 = new Rectangle();
        R1.setLayoutX(230);
        R1.setLayoutY(250);
        R1.setWidth(250);
        R1.setHeight(100);
        R1.setFill(Color.BISQUE);
        R1.setStroke(Color.DARKBLUE);
        R1.setStrokeWidth(10);

        Result = new Label();
        Result.setLayoutX(275);
        Result.setLayoutY(270);
        Result.setFont(new Font("centuy gothic", 40));

        scale = new ScaleTransition(Duration.millis(2000));
        scale.setNode(R1);
        scale.setByX(0.1);
        scale.setByY(0.1);
        scale.setAutoReverse(true);
        scale.setCycleCount(1000);

        stroke = new StrokeTransition(Duration.millis(2000));
        stroke.setShape(R1);
        stroke.setFromValue(Color.DARKBLUE);
        stroke.setToValue(Color.CORAL);
        stroke.setAutoReverse(true);
        stroke.setCycleCount(1000);

        Root.getChildren().add(Start);
        Root.getChildren().add(Time);
        stage.setResizable(false);
        stage.show();
    }

    public void MakeGame() {
        for (int i = 1; i <= 8; i++) {
            MainList.add(i);
            MainList.add(i);
        }
        Random Rand = new Random();
        int RandomIndex;
        ArrayList<Integer> NewArrayList = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            B[i].setText("");
            B[i].setDisable(false);
            B[i].setBackground(Background.fill(Color.LIGHTGOLDENRODYELLOW));
            RandomIndex = Rand.nextInt(MainList.size());
            NewArrayList.add(MainList.get(RandomIndex));
            MainList.remove(MainList.get(RandomIndex));
        }
        for (int i = 0; i < 16; i++) {
            final int CurrentIndex = i;
            B[CurrentIndex].setOnAction((ActionEvent) -> {
                B[CurrentIndex].setDisable(true);
                B[CurrentIndex].setText(String.valueOf(NewArrayList.get(CurrentIndex)));
                ClickedButtons.add(CurrentIndex);
                Click++;
                if (Click == 2) {
                    if (NewArrayList.get(ClickedButtons.get(0)).equals(NewArrayList.get(ClickedButtons.get(1)))) {
                        Timeline timeline1 = new Timeline(
                                new KeyFrame(Duration.seconds(0.2), e -> {
                                    B[ClickedButtons.get(0)].setBackground(Background.fill(Color.GREENYELLOW));
                                    B[ClickedButtons.get(1)].setBackground(Background.fill(Color.GREENYELLOW));
                                    Correct.add(1);
                                })
                        );
                        timeline1.setOnFinished(((ActionEvent1) -> {
                            ClickedButtons.clear();
                            Click = 0;
                        }));
                        timeline1.play();
                    } else {
                        Timeline timeline1 = new Timeline(
                                new KeyFrame(Duration.seconds(0.2), e -> {
                                    B[ClickedButtons.get(0)].setDisable(false);
                                    B[ClickedButtons.get(1)].setDisable(false);
                                    B[ClickedButtons.get(0)].setText("");
                                    B[ClickedButtons.get(1)].setText("");
                                })
                        );
                        timeline1.setOnFinished(((ActionEvent1) -> {
                            ClickedButtons.clear();
                            Click = 0;
                        }));
                        timeline1.play();
                    }
                }
            });
        }
    }

    public void CountDown() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            TimeRemaining--;
            Time.setText(String.valueOf((TimeRemaining)));
            if (TimeRemaining == 0 && Correct.size() != 8) {
                Result.setText("You Lose!");
                Result.setTextFill(Color.CRIMSON);
                Root.getChildren().add(R1);
                Root.getChildren().add(Result);
                stroke.play();
                scale.play();
                Start.setDisable(false);
                for (int i = 0; i < 16; i++) {
                    B[i].setDisable(true);
                }
                ResultSound("C:\\seif\\LoseSound.wav");
                MyClip.stop();
                timeline.stop();
            }
            if (TimeRemaining != 0 && Correct.size() == 8) {
                Result.setText("You win!");
                Result.setTextFill(Color.GREEN);
                Root.getChildren().add(R1);
                Root.getChildren().add(Result);
                stroke.play();
                scale.play();
                Start.setDisable(false);
                ResultSound("C:\\seif\\WinSound.wav");
                MyClip.stop();
                timeline.stop();
            }
        }));
        timeline.setCycleCount(30);
        timeline.play();
    }

    public void CountdownSound() {
        try {
            File MyFile = new File("C:\\seif\\coundown.wav");
            AudioInputStream A1 = AudioSystem.getAudioInputStream(MyFile);
            MyClip = AudioSystem.getClip();
            MyClip.open(A1);
            MyClip.start();
        } catch (Exception E) {
            System.out.println(E.getMessage());
        }
    }

    public void ResultSound(String Path) {
        try {
            File MyFile = new File(Path);
            AudioInputStream A1 = AudioSystem.getAudioInputStream(MyFile);
            MyClip1 = AudioSystem.getClip();
            MyClip1.open(A1);
            MyClip1.start();
        } catch (Exception E) {
            System.out.println(E.getMessage());
        }
    }
}    

