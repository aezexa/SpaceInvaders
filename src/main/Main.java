package main;

import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends Application {

    AnchorPane anchorPane;
    ImageView hero;
    ArrayList<Enemy> enemies = new ArrayList <> (  );
    ArrayList<Rectangle> shots = new ArrayList <> (  );
    boolean left,right,space;
    double time;

    AnimationTimer animationTimer = new AnimationTimer ( ) {
        @Override
        public void handle ( long l ) {
            time += 0.1;
//            for (Rectangle shot : shots) {
//                shot.setLayoutY ( shot.getLayoutY () - 10 );
//                for (Enemy enemy : enemies) {
//                    if (shot.getBoundsInParent ().intersects ( enemy.getImageView ().getBoundsInParent () )) {
//                        anchorPane.getChildren ( ).removeAll ( shot , enemy.getImageView () );
//                        enemy.setDead ( true );
//                    }
//                }
//                enemies.removeIf ( enemy -> enemy.isDead );
//            }

            Iterator <Rectangle> i = shots.iterator();
            Iterator <Enemy> j = enemies.iterator ();
            while (i.hasNext()) {
                Rectangle shot = i.next();
                shot.setLayoutY ( shot.getLayoutY () - 10 );
                if (shot.getLayoutY () < -100) {
                    anchorPane.getChildren ().remove ( shot );
                    i.remove ();

                }
                while (j.hasNext ()) {
                    Enemy enemy = j.next ();
                    if (shot.getBoundsInParent ().intersects ( enemy.getImageView ().getBoundsInParent () )) {
                        anchorPane.getChildren ().removeAll ( shot , enemy.getImageView () );
                        i.remove ();
                        j.remove ();
                        break;
                    }
                }
            }

            if (left)
                hero.setLayoutX ( hero.getLayoutX () - 6 );
            if (right)
                hero.setLayoutX ( hero.getLayoutX () + 6 );
            if (space && time > 2)
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

        anchorPane.getChildren ().addAll ( background, hero );

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 20 ; j++) {
                if (j == 6 || j == 13)
                    continue;
                Enemy enemy = new Enemy ( new ImageView ( new Image ( "/resources/enemy" + i + ".png" ) ) , i , j );
                enemies.add ( enemy );
                anchorPane.getChildren ().add ( enemy.getImageView () );
            }
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
        time = 0;
        Rectangle rectangle = new Rectangle (  );
        shots.add ( rectangle );
        rectangle.setWidth ( 5 );
        rectangle.setHeight ( 20 );
        rectangle.setFill ( Color.GREEN );
        anchorPane.getChildren ().add ( rectangle );
        rectangle.setLayoutX ( hero.getLayoutX () + hero.getFitWidth ()/2 );
        rectangle.setLayoutY ( hero.getLayoutY () - 10 );
    }

    public static void main(String[] args) {
        launch(args);
    }

}

class Enemy extends Pane {
    ImageView imageView;
    boolean isDead;

    public Enemy ( ImageView imageView , int i, int j ) {
        imageView.setPreserveRatio ( true );
        imageView.setFitWidth ( 50 );
        imageView.setLayoutY ( (i+1) * imageView.getFitWidth () ); //getFitHeight doesn't exist now
        imageView.setLayoutX ( (j+1) * imageView.getFitWidth () );
        this.imageView = imageView;
    }

    public ImageView getImageView () {
        return imageView;
    }

    public void setDead ( boolean dead ) {
        isDead = dead;
    }

    public boolean isDead () {
        return isDead;
    }
}
