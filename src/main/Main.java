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
    Enemy[] enemies = new Enemy[54];
    ArrayList<Shot> shots = new ArrayList <> (  );
    boolean left,right;
    double time;
    double enemyTime;
    int score;

    AnimationTimer animationTimer = new AnimationTimer ( ) {
        @Override
        public void handle ( long l ) {
            time += 0.1;
            enemyTime += 0.1;

            if (enemyTime > 30) {
                enemyTime = 0;
                for (Enemy enemy : enemies) {
                    if (enemy != null)
                        enemy.setLayoutY ( enemy.getLayoutY () + 40 );
                }
            }

//            Iterator <Rectangle> i = shots.iterator();
//            Iterator <Enemy> j = enemies.iterator ();
//            while (i.hasNext()) {
//                Rectangle shot = i.next();
//                shot.setLayoutY ( shot.getLayoutY () - 10 );
//                if (shot.getLayoutY () < -100) {
//                    anchorPane.getChildren ().remove ( shot );
//                    i.remove ();
//                    continue;
//                }
//                while (j.hasNext ()) {
//                    Enemy enemy = j.next ();
//                    if (shot.getBoundsInParent ().intersects ( enemy.getImageView ().getBoundsInParent () )) {
//                        anchorPane.getChildren ().removeAll ( shot , enemy.getImageView () );
//                        i.remove ();
//                        j.remove ();
//                        break;
//                    }
//                }
//            }

            for (Shot shot : shots) {
                if (!shot.isDead ()) {
                    moveShoot ( shot );
                    collisionCheck ( shot );
                }
            }




            if (left)
                hero.setLayoutX ( hero.getLayoutX () - 6 );
            if (right)
                hero.setLayoutX ( hero.getLayoutX () + 6 );

        }
    };

    private void moveShoot ( Shot shot ) {
        shot.setLayoutY ( shot.getLayoutY () - 10 );
    }


    @Override
    public void start(Stage primaryStage) {

        anchorPane = new AnchorPane (  );
        primaryStage.setTitle("Space Invaders");
        ImageView background = new ImageView ( new Image ( "/resources/background.png" ) );
        background.setPreserveRatio ( true );
        background.setFitHeight ( 1200 );

        hero = new ImageView ( new Image ( "/resources/hero.png" ) );
        hero.setPreserveRatio ( true );
        hero.setFitWidth ( 50 );
        hero.setLayoutY ( 750 );

        anchorPane.getChildren ().addAll ( background, hero );

        int count = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 20 ; j++) {
                if (j == 6 || j == 13)
                    continue;
                Enemy enemy = new Enemy ( new Image ( "/resources/enemy" + i + ".png" ) , i , j );
                enemies[count++] = enemy;
                anchorPane.getChildren ().add ( enemy );
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
        } );

        scene.setOnKeyReleased ( event -> {
            if( event.getCode() == KeyCode.RIGHT)
                right = false;
            else if (event.getCode() == KeyCode.LEFT)
                left = false;
            else if(event.getCode() == KeyCode.SPACE)
                shoot ();
        } );

        animationTimer.start ();

    }

    private void shoot () {
        time = 0;
        Shot shot = new Shot (  );
        shots.add ( shot );
        shot.setWidth ( 5 );
        shot.setHeight ( 20 );
        shot.setFill ( Color.GREEN );
        anchorPane.getChildren ().add ( shot );
        shot.setLayoutX ( hero.getLayoutX () + hero.getFitWidth ()/2 );
        shot.setLayoutY ( hero.getLayoutY () - 10 );
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void collisionCheck (Shot shot) {
        for (int i = 0; i < enemies.length; i++) {
            if (enemies[i] != null) {
                if (shot.getBoundsInParent ().intersects ( enemies[i].getBoundsInParent () )) {
                    enemies[i].setVisible ( false );
                    enemies[i] = null;
                    System.out.println ( ++score );
                    shot.setDead ();
                    anchorPane.getChildren ().remove ( shot );
                    break;
                }
            }
        }
    }

}

class Enemy extends ImageView {
    public Enemy ( Image image, int i, int j ) {
        setImage ( image );
        setPreserveRatio ( true );
        setFitWidth ( 50 );
        setLayoutY ( (i+1) * getFitWidth () ); //getFitHeight doesn't exist now
        setLayoutX ( (j+1) * getFitWidth () );
    }

}

class Shot extends Rectangle {
    boolean isDead;

    public void setDead (  ) {
        isDead = true;
    }

    public boolean isDead () {
        return isDead;
    }
}
