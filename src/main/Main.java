package main;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

public class Main extends Application {

    AnchorPane anchorPane;
    ImageView hero;
    Enemy[] enemies = new Enemy[54];
    ArrayList<Shot> shots = new ArrayList <> (  );
    boolean left,right;
    double time;
    double enemyTime;
    int score;
    Label scoreLabel;
    public static AudioClip shotSound = new AudioClip ( new File ( "src/resources/shot.wav" ).toURI ().toString () );
    public static AudioClip destroyedSound = new AudioClip ( new File ( "src/resources/destroyed.wav" ).toURI ().toString () );

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

            for (Shot shot : shots) {
                if (!shot.isDead ()) {
                    moveShoot ( shot );
                    collisionCheck ( shot );
                }
                if (shot.getLayoutY () < -100)
                    shot.setDead ();
            }

            shots.removeIf ( Shot::isDead );

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

        scoreLabel = new Label ( "Points : " );
        scoreLabel.setLayoutX ( 15 );
        scoreLabel.setLayoutY ( 15 );
        scoreLabel.setStyle ( "-fx-font-size: 20");
        scoreLabel.setTextFill ( Color.GREEN );
        scoreLabel.getStylesheets ().add ( "/resources/fontStyle.css" );

        anchorPane.getChildren ().addAll ( background, scoreLabel, hero );



        int count = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 20 ; j++) {
                if (j == 6 || j == 13)
                    continue;
                Enemy enemy = new Enemy ( new Image ( "/resources/enemy" + i + ".png" ) , i , j );
                setEnemyColors (i , enemy);
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

    private void setEnemyColors ( int i, Enemy enemy) {
        if (i == 0)
            setEnemyLighting ( Color.GREEN , enemy );
        else if (i == 1)
            setEnemyLighting ( Color.RED , enemy );
    }

    private void setEnemyLighting (Color color, Enemy enemy) {
        Lighting lighting = new Lighting();
        lighting.setDiffuseConstant(1.0);
        lighting.setSpecularConstant(0.0);
        lighting.setSpecularExponent(0.0);
        lighting.setSurfaceScale(0.0);
        lighting.setLight(new Light.Distant(45, 45, color));

        enemy.setEffect ( lighting );
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
        shotSound.play ();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void collisionCheck (Shot shot) {
        for (int i = 0; i < enemies.length; i++) {
            if (enemies[i] != null) {
                if (shot.getBoundsInParent ().intersects ( enemies[i].getBoundsInParent () )) {
                    enemies[i].setVisible ( false );
//                    if (enemies[i].getLevel () == 0)
//                        scoreLabel.setText ( "Points : " + (score += 498 * 3) );
//                    else if (enemies[i].getLevel () == 1)
//                        scoreLabel.setText ( "Points : " + (score += 498 * 2) );
//                    else
//                        scoreLabel.setText ( "Points : " + (score += 498) );
                    scoreLabel.setText ( "Points : " + ++score );
                    enemies[i] = null;
                    shot.setDead ();
                    anchorPane.getChildren ().remove ( shot );
                    destroyedSound.play ();
                    break;
                }
            }
        }
    }

}

class Enemy extends ImageView {
    private int level;

    public Enemy ( Image image, int i, int j ) {
        level = i;
        setImage ( image );
        setPreserveRatio ( true );
        setFitWidth ( 50 );
        setLayoutY ( (i+1) * getFitWidth () ); //getFitHeight doesn't exist now
        setLayoutX ( (j+1) * getFitWidth () );
    }

    public int getLevel () {
        return level;
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
