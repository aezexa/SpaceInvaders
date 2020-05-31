package main;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {

    AnchorPane anchorPane;
    ImageView hero;
    ArrayList<ImageView> enemies = new ArrayList <> (  );
    ArrayList<Rectangle> shots = new ArrayList <> (  );
    boolean left,right,space;

    AnimationTimer animationTimer = new AnimationTimer ( ) {
        @Override
        public void handle ( long l ) {
            for (Rectangle shot : shots) {
                shot.setLayoutY ( shot.getLayoutY () - 10 );
                for (ImageView enemy : enemies) {
                    if (shot.getBoundsInParent ().intersects ( enemy.getBoundsInParent () ))
                        anchorPane.getChildren ().remove ( enemy );
                }
            }

            shots.removeIf ( shot -> shot.getLayoutY ( ) < 0 );
            if (left)
                hero.setLayoutX ( hero.getLayoutX () - 6 ); //first off I didn't have booleans, and these properties, changed in scene.setOnKey..., but it was super slow
            if (right)
                hero.setLayoutX ( hero.getLayoutX () + 6 );
            if (space)
                shoot ();
        }
    };

    @Override
    public void start(Stage primaryStage) {

        anchorPane = new AnchorPane (  );
        primaryStage.setTitle("Hello World");
        ImageView background = new ImageView ( new Image ( "/resources/background.png" ) );
        background.setPreserveRatio ( true );
        background.setFitHeight ( 1200 );

        hero = new ImageView ( new Image ( "/resources/hero.png" ) );
        hero.setPreserveRatio ( true );
        hero.setFitWidth ( 50 );
        hero.setLayoutY ( 750 );

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 20 ; j++) {
                if (j == 6 || j == 13)
                    continue;
                ImageView enemy = new ImageView ( new Image ( "/resources/enemy" + i + ".png" ) );
                enemy.setPreserveRatio ( true );
                enemy.setFitWidth ( 50 );
                enemy.setLayoutY ( (i+1) * enemy.getFitWidth () ); //getFitHeight doesn't exist now
                enemy.setLayoutX ( (j+1) * enemy.getFitWidth () );
                enemies.add ( enemy );
            }
        }

        anchorPane.getChildren ().addAll ( background, hero );
        for (ImageView enemy : enemies) {
            anchorPane.getChildren ().add ( enemy );
        }
        Scene scene = new Scene ( anchorPane,1200,800 );
        primaryStage.setScene(scene);
        primaryStage.show();
        scene.setOnKeyPressed ( event -> {
            if( event.getCode() == KeyCode.RIGHT) {
                right = true;
            } else if (event.getCode() == KeyCode.LEFT)
                left = true;
            else if(event.getCode() == KeyCode.SPACE)
                space = true;
        } );

        scene.setOnKeyReleased ( event -> {
            if( event.getCode() == KeyCode.RIGHT)
                right = false;
            else if (event.getCode() == KeyCode.LEFT)
                left = false;
            else if(event.getCode() == KeyCode.SPACE)
                space = false;
        } );

        animationTimer.start ();

    }

    private void shoot () {
        Rectangle rectangle = new Rectangle (  );
        shots.add ( rectangle );
        rectangle.setWidth ( 2 );
        rectangle.setHeight ( 10 );
        rectangle.setFill ( Color.GREEN );
        anchorPane.getChildren ().add ( rectangle );
        rectangle.setLayoutX ( hero.getLayoutX () + hero.getFitWidth ()/2 );
        rectangle.setLayoutY ( hero.getLayoutY () - 10 );
    }

    public static void main(String[] args) {
        launch(args);
    }

}
