//I worked on the homework assignment alone, using only course materials.

import javafx.animation.Timeline;
import javafx.animation.KeyValue;
import javafx.animation.KeyFrame;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.Font;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;

/**
 * PokeBattle.java The Pokemon Battle Game
 * @author Hemang Dash
 * @version 1.0.0
 **/

public class PokeBattle extends Application {

    /**
     * The main method runs the game by overriding the launch() method of the
     * Application class
     * @param args Command Line Arguments
     * @throws FileNotFoundException if the Image file is not found
     */

    public static void main(final String[] args) throws FileNotFoundException {

        launch(args);

    }

    private static final String RED_BAR = "red-bar";
    private static final String YELLOW_BAR = "yellow-bar";
    private static final String ORANGE_BAR = "orange-bar";
    private static final String GREEN_BAR = "green-bar";
    private static final String[] BAR_COLORS = {RED_BAR, ORANGE_BAR, YELLOW_BAR, GREEN_BAR};

    private final Pokemon[] oppPokemons = initOpp();
    private int[] aliveOppPokemons = {1, 1, 1};
    private int o = 0;
    private Pokemon currentOppPokemon = oppPokemons[o];
    private final Pokemon[] userPokemons = initUser();
    private final int[] aliveUserPokemons = {1, 1, 1};
    private int u = 0;
    private Pokemon currentUserPokemon = userPokemons[u];
    private final String[] userPokemonNames = {userPokemons[0].getName(), userPokemons[1].getName(),
            userPokemons[2].getName()};
    private Timeline timeline = new Timeline();
    private int train = 2;
    private int health = 1;
    private int poison = 1;
    private int load = 0;
    private int difficultyLevel = 0;

    /**
     * The start method This overrides the start() method of the Application class.
     * @param mainStage The main stage of the game
     * @throws FileNotFoundException if the Image file is not found
     */

    public void start(final Stage mainStage) throws FileNotFoundException {

        if (load == 0) {

            load = 1;
            new InitStage();

        } else if (load == 1) {

            load = 2;
            new InfoStage();

        } else {

            if (o < 3) {
                if (currentOppPokemon.isFainted()) {
                    aliveOppPokemons[o] = 0;
                    if (o != 2) {
                        o++;
                    }
                    currentOppPokemon = oppPokemons[o];
                }
            }

            if (u < 3) {
                if (currentUserPokemon.isFainted()) {
                    aliveUserPokemons[u] = 0;
                    for (int i = 0; i < 3; i++) {
                        if (aliveUserPokemons[i] == 1) {
                            u = i;
                            break;
                        }
                    }
                    currentUserPokemon = userPokemons[u];
                }
            }

            if (!anyAlive(aliveOppPokemons)) {
                final Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Game Over");
                alert.setHeaderText("You Won!");
                alert.setContentText("You have beaten the Computer!");
                alert.setOnHidden(eventExit -> System.exit(0));
                alert.show();
            }

            if (!anyAlive(aliveUserPokemons)) {
                final Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Game Over");
                alert.setHeaderText("Computer Won!");
                alert.setContentText("The Computer has beaten you.");
                alert.setOnHidden(eventExit -> System.exit(0));
                alert.show();
            }

            mainStage.setTitle("PokeBattle!");

            final GridPane oppGrid = new GridPane();
            oppGrid.setHgap(10);
            oppGrid.setVgap(10);
            oppGrid.setPadding(new Insets(0, 0, 0, 0));
            oppGrid.setStyle(
                    "-fx-border-color: #000000;" + "-fx-border-width: 5;" + "-fx-background-color: lightsalmon;");

            final Move[] oppMoves = currentOppPokemon.getMoves();

            final Image oppImage = new Image(new FileInputStream("opp" + currentOppPokemon.getName() + ".jpg"));
            final ImageView oppImageView = new ImageView(oppImage);
            oppImageView.setFitHeight(300);
            oppImageView.setFitWidth(300);

            final Label oppPokemon = new Label();
            oppPokemon.setText(currentOppPokemon.getName());
            oppPokemon.setMinWidth(100);
            oppPokemon.setFont(new Font("Arial", 15));
            oppPokemon.setAlignment(Pos.CENTER_LEFT);

            final Label oppLevel = new Label();
            oppLevel.setText("Lv" + currentOppPokemon.getLevel());
            oppLevel.setMinWidth(100);

            final Label oppHPLabel = new Label("HP:");
            oppHPLabel.setMinWidth(25);
            final ProgressBar oppPB = new ProgressBar();
            oppPB.progressProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(final ObservableValue<? extends Number> observable, final Number oldValue,
                        final Number newValue) {
                    final double progress = newValue.doubleValue();
                    if (progress < 0.25) {
                        setBarStyleClass(oppPB, RED_BAR);
                    } else if (progress < 0.5) {
                        setBarStyleClass(oppPB, ORANGE_BAR);
                    } else if (progress < 0.75) {
                        setBarStyleClass(oppPB, YELLOW_BAR);
                    } else {
                        setBarStyleClass(oppPB, GREEN_BAR);
                    }
                }

                private void setBarStyleClass(final ProgressBar oppPB, final String barStyleClass) {
                    oppPB.getStyleClass().removeAll(BAR_COLORS);
                    oppPB.getStyleClass().add(barStyleClass);
                }
            });
            oppPB.setProgress(currentOppPokemon.getCurrentHP() / 100);
            final HBox oppHP = new HBox();
            oppHP.setSpacing(1);
            oppHP.setPrefWidth(200);
            oppHP.getChildren().addAll(oppHPLabel, oppPB);
            final Label oppHPFraction = new Label();
            oppHPFraction.setText((int) Math.round(currentOppPokemon.getCurrentHP()) + "/"
                    + (int) Math.round(currentOppPokemon.getMaxHP()));
            if (currentOppPokemon.getCurrentHP() <= 0) {
                oppPB.setProgress(0);
                oppHPFraction.setText("0/10");
            }
            oppHPFraction.setMinWidth(100);

            final VBox oppStats = new VBox();
            oppStats.setAlignment(Pos.CENTER_LEFT);
            oppStats.getChildren().addAll(oppLevel, oppHP, oppHPFraction);
            oppStats.setPrefWidth(50);

            final HBox oppInfo = new HBox();
            oppInfo.setAlignment(Pos.CENTER_LEFT);
            oppInfo.getChildren().addAll(oppPokemon, oppStats);
            oppInfo.setPrefWidth(200);

            final HBox opp = new HBox();
            opp.setSpacing(150);
            opp.getChildren().addAll(oppInfo, oppImageView);

            oppGrid.add(opp, 15, 0);

            final GridPane userGrid = new GridPane();
            userGrid.setHgap(10);
            userGrid.setVgap(10);
            userGrid.setPadding(new Insets(0, 100, 0, 100));
            userGrid.setStyle(
                    "-fx-border-color: #000000;" + "-fx-border-width: 5;" + "-fx-background-color: lightcyan;");

            final Move[] userMoves = currentUserPokemon.getMoves();

            final Image userImage = new Image(new FileInputStream("user" + userPokemonNames[u] + ".jpg"));
            final ImageView userImageView = new ImageView(userImage);
            userImageView.setFitHeight(300);
            userImageView.setFitWidth(300);

            final Label userPokemon = new Label();
            userPokemon.setText(currentUserPokemon.getName());
            userPokemon.setMinWidth(100);
            userPokemon.setFont(new Font("Arial", 15));
            userPokemon.setAlignment(Pos.TOP_LEFT);

            final Label userLevel = new Label();
            userLevel.setText("Lv" + currentUserPokemon.getLevel());
            userLevel.setMinWidth(100);

            final Label userHPLabel = new Label("HP:");
            userHPLabel.setMinWidth(25);
            final ProgressBar userPB = new ProgressBar();
            userPB.progressProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(final ObservableValue<? extends Number> observable, final Number oldValue,
                        final Number newValue) {
                    final double progress = newValue.doubleValue();
                    if (progress < 0.25) {
                        setBarStyleClass(userPB, RED_BAR);
                    } else if (progress < 0.5) {
                        setBarStyleClass(userPB, ORANGE_BAR);
                    } else if (progress < 0.75) {
                        setBarStyleClass(userPB, YELLOW_BAR);
                    } else {
                        setBarStyleClass(userPB, GREEN_BAR);
                    }
                }

                private void setBarStyleClass(final ProgressBar userPB, final String barStyleClass) {
                    userPB.getStyleClass().removeAll(BAR_COLORS);
                    userPB.getStyleClass().add(barStyleClass);
                }
            });
            userPB.setProgress(currentUserPokemon.getCurrentHP() / 100);
            final HBox userHP = new HBox();
            userHP.setSpacing(1);
            userHP.setPrefWidth(100);
            userHP.getChildren().addAll(userHPLabel, userPB);
            final Label userHPFraction = new Label();
            userHPFraction.setText((int) Math.round(currentUserPokemon.getCurrentHP()) + "/"
                    + (int) Math.round(currentUserPokemon.getMaxHP()));
            if (currentUserPokemon.getCurrentHP() <= 0) {
                userPB.setProgress(0);
                userHPFraction.setText("0/10");
            }
            userHPFraction.setMinWidth(100);

            final VBox userStats = new VBox();
            userStats.setAlignment(Pos.CENTER_LEFT);
            userStats.getChildren().addAll(userLevel, userHP, userHPFraction);
            userStats.setPrefWidth(50);

            final HBox userInfo = new HBox();
            userInfo.setAlignment(Pos.CENTER_LEFT);
            userInfo.getChildren().addAll(userPokemon, userStats);
            userInfo.setPrefWidth(200);

            final HBox user = new HBox();
            user.setSpacing(150);
            user.getChildren().addAll(userImageView, userInfo);

            final Label message = new Label();
            message.setText("What should " + currentUserPokemon.getName() + " do?");
            message.setFont(new Font("Arial", 20));
            message.setMinWidth(300);
            message.setMinHeight(200);
            message.setAlignment(Pos.CENTER_LEFT);

            final Button userOption1 = new Button();
            final Button userOption2 = new Button();
            final Button userOption3 = new Button();
            final Button userOption4 = new Button();

            userOption1.setText("FIGHT");
            userOption1.setMinWidth(150);
            userOption1.setOnAction(eventFight -> {
                    userOption1.setText(userMoves[0].getName());
                    userOption1.setOnAction(eventMove1 -> {
                            message.setText(currentUserPokemon.getName() + " used " + userMoves[0].getName() + ".");
                            Timeline oppAnimation = new Timeline();
                            oppAnimation.getKeyFrames().addAll(
                                    new KeyFrame(Duration.millis(0), evt -> oppImageView.setVisible(false)),
                                    new KeyFrame(Duration.millis(150), evt -> oppImageView.setVisible(true)),
                                    new KeyFrame(Duration.millis(300), evt -> oppImageView.setVisible(false)),
                                    new KeyFrame(Duration.millis(450), evt -> oppImageView.setVisible(true)),
                                    new KeyFrame(Duration.millis(500), evt -> oppImageView.setVisible(false)),
                                    new KeyFrame(Duration.millis(650), evt -> oppImageView.setVisible(true)));
                            oppAnimation.play();
                            oppAnimation.setOnFinished(eventAfterOppAnimation -> {
                                    double damage = attack(userMoves[0], currentUserPokemon, currentOppPokemon, 'u');
                                    Timeline moveTimeline = new Timeline();
                                    moveTimeline.getKeyFrames().addAll(
                                            new KeyFrame(Duration.millis(0),
                                                    new KeyValue(oppPB.progressProperty(),
                                                            (currentOppPokemon.getCurrentHP() / 100))),
                                            new KeyFrame(Duration.millis(1500), new KeyValue(oppPB.progressProperty(),
                                                    ((currentOppPokemon.getCurrentHP() - damage) / 100))));
                                    currentOppPokemon.setCurrentHP(currentOppPokemon.getCurrentHP() - damage);
                                    moveTimeline.play();
                                    moveTimeline.setOnFinished(eventMove -> {
                                            oppHPFraction.setText((int) Math.round(currentOppPokemon.getCurrentHP())
                                                    + "/"
                                                    + (int) Math.round(currentOppPokemon.getMaxHP()));
                                            if (currentOppPokemon.getCurrentHP() <= 0) {
                                                oppPB.setProgress(0);
                                                oppHPFraction.setText("0/10");
                                                message.setText(currentOppPokemon.getName() + " fainted.");
                                                Timeline faintMessage = new Timeline();
                                                faintMessage.getKeyFrames().addAll(
                                                        new KeyFrame(Duration.seconds(0),
                                                                eventFaint -> oppImageView.setVisible(false)),
                                                        new KeyFrame(Duration.seconds(1.5),
                                                                eventFaint -> oppImageView.setVisible(false)));
                                                faintMessage.play();
                                                faintMessage.setOnFinished(eventAfterFaint -> {
                                                        try {
                                                            start(mainStage);
                                                        } catch (Exception e) {
                                                            System.out.println(e.getMessage());
                                                        }
                                                    });
                                            } else {
                                                Random rand = new Random();
                                                Move oppMove = oppMoves[rand.nextInt(4)];
                                                double oppDamage = attack(oppMove, currentOppPokemon,
                                                    currentUserPokemon, 'o');
                                                message.setText(currentOppPokemon.getName()
                                                    + " used " + oppMove.getName() + ".");
                                                Timeline userAnimation = new Timeline();
                                                userAnimation.getKeyFrames().addAll(
                                                        new KeyFrame(Duration.millis(0),
                                                                evt -> userImageView.setVisible(false)),
                                                        new KeyFrame(Duration.millis(150),
                                                                evt -> userImageView.setVisible(true)),
                                                        new KeyFrame(Duration.millis(300),
                                                                evt -> userImageView.setVisible(false)),
                                                        new KeyFrame(Duration.millis(450),
                                                                evt -> userImageView.setVisible(true)),
                                                        new KeyFrame(Duration.millis(500),
                                                                evt -> userImageView.setVisible(false)),
                                                        new KeyFrame(Duration.millis(650),
                                                                evt -> userImageView.setVisible(true)));
                                                userAnimation.play();
                                                userAnimation.setOnFinished(eventAfterUserAnimation -> {
                                                        Timeline oppMoveTimeline = new Timeline();
                                                        oppMoveTimeline.getKeyFrames().addAll(
                                                                new KeyFrame(Duration.millis(0),
                                                                        new KeyValue(userPB.progressProperty(),
                                                                                (currentUserPokemon.getCurrentHP()
                                                                                        / currentUserPokemon.getMaxHP()
                                                                                        ))),
                                                                new KeyFrame(Duration.millis(1500),
                                                                        new KeyValue(userPB.progressProperty(),
                                                                                ((currentUserPokemon.getCurrentHP()
                                                                                    - oppDamage)
                                                                                        / currentUserPokemon.getMaxHP()
                                                                                        ))));
                                                        currentUserPokemon.setCurrentHP(
                                                            currentUserPokemon.getCurrentHP() - oppDamage
                                                        );
                                                        oppMoveTimeline.play();
                                                        oppMoveTimeline.setOnFinished(eventOppMove -> {
                                                                if (currentUserPokemon.isFainted()) {
                                                                    Timeline faintMessage = new Timeline();
                                                                    faintMessage.getKeyFrames()
                                                                            .addAll(new KeyFrame(Duration.seconds(0),
                                                                                    eventFaint ->
                                                                                        userImageView.setVisible(false)
                                                                                        ),
                                                                                    new KeyFrame(Duration.seconds(1.5),
                                                                                            eventFaint ->
                                                                                            userImageView.setVisible(
                                                                                                false
                                                                                                )
                                                                                            ));
                                                                    message.setText(currentUserPokemon.getName()
                                                                        + " fainted.");
                                                                    userPB.setProgress(0);
                                                                    faintMessage.play();
                                                                    faintMessage.setOnFinished(eventAfterFaint -> {
                                                                            try {
                                                                                start(mainStage);
                                                                            } catch (Exception e) {
                                                                                System.out.println(e.getMessage());
                                                                            }
                                                                        });
                                                                } else {
                                                                    try {
                                                                        start(mainStage);
                                                                    } catch (Exception e) {
                                                                        System.out.println(e.getMessage());
                                                                    }
                                                                }
                                                            });
                                                    });
                                            }
                                        });
                                });
                        });

                    userOption2.setText(userMoves[1].getName());
                    userOption2.setOnAction(eventMove2 -> {
                            message.setText(currentUserPokemon.getName() + " used " + userMoves[1].getName() + ".");
                            Timeline oppAnimation = new Timeline();
                            oppAnimation.getKeyFrames().addAll(
                                    new KeyFrame(Duration.millis(0), evt -> oppImageView.setVisible(false)),
                                    new KeyFrame(Duration.millis(150), evt -> oppImageView.setVisible(true)),
                                    new KeyFrame(Duration.millis(300), evt -> oppImageView.setVisible(false)),
                                    new KeyFrame(Duration.millis(450), evt -> oppImageView.setVisible(true)),
                                    new KeyFrame(Duration.millis(500), evt -> oppImageView.setVisible(false)),
                                    new KeyFrame(Duration.millis(650), evt -> oppImageView.setVisible(true)));
                            oppAnimation.play();
                            oppAnimation.setOnFinished(eventAfterOppAnimation -> {
                                    double damage = attack(userMoves[1], currentUserPokemon, currentOppPokemon, 'u');
                                    Timeline moveTimeline = new Timeline();
                                    moveTimeline.getKeyFrames().addAll(
                                            new KeyFrame(Duration.millis(0),
                                                    new KeyValue(oppPB.progressProperty(),
                                                            (currentOppPokemon.getCurrentHP() / 100))),
                                            new KeyFrame(Duration.millis(1500), new KeyValue(oppPB.progressProperty(),
                                                    ((currentOppPokemon.getCurrentHP() - damage) / 100))));
                                    currentOppPokemon.setCurrentHP(currentOppPokemon.getCurrentHP() - damage);
                                    moveTimeline.play();
                                    moveTimeline.setOnFinished(eventMove -> {
                                            oppHPFraction.setText((int) Math.round(currentOppPokemon.getCurrentHP())
                                                    + "/"
                                                    + (int) Math.round(currentOppPokemon.getMaxHP()));
                                            if (currentOppPokemon.getCurrentHP() <= 0) {
                                                oppPB.setProgress(0);
                                                oppHPFraction.setText("0/10");
                                                message.setText(currentOppPokemon.getName() + " fainted.");
                                                Timeline faintMessage = new Timeline();
                                                faintMessage.getKeyFrames().addAll(
                                                        new KeyFrame(Duration.seconds(0), eventFaint ->
                                                            oppImageView.setVisible(false)),
                                                        new KeyFrame(Duration.seconds(1.5),
                                                                eventFaint -> oppImageView.setVisible(false)));
                                                faintMessage.play();
                                                faintMessage.setOnFinished(eventAfterFaint -> {
                                                        try {
                                                            start(mainStage);
                                                        } catch (Exception e) {
                                                            System.out.println(e.getMessage());
                                                        }
                                                    });
                                            } else {
                                                Random rand = new Random();
                                                Move oppMove = oppMoves[rand.nextInt(4)];
                                                double oppDamage = attack(
                                                    oppMove,
                                                    currentOppPokemon,
                                                    currentUserPokemon,
                                                    'o'
                                                    );
                                                message.setText(currentOppPokemon.getName() + " used "
                                                    + oppMove.getName() + ".");
                                                Timeline userAnimation = new Timeline();
                                                userAnimation.getKeyFrames().addAll(
                                                        new KeyFrame(Duration.millis(0), evt ->
                                                            userImageView.setVisible(false)),
                                                        new KeyFrame(Duration.millis(150), evt ->
                                                            userImageView.setVisible(true)),
                                                        new KeyFrame(Duration.millis(300), evt ->
                                                            userImageView.setVisible(false)),
                                                        new KeyFrame(Duration.millis(450), evt ->
                                                            userImageView.setVisible(true)),
                                                        new KeyFrame(Duration.millis(500), evt ->
                                                            userImageView.setVisible(false)),
                                                        new KeyFrame(Duration.millis(650), evt ->
                                                            userImageView.setVisible(true)));
                                                userAnimation.play();
                                                userAnimation.setOnFinished(eventAfterUserAnimation -> {
                                                        Timeline oppMoveTimeline = new Timeline();
                                                        oppMoveTimeline.getKeyFrames().addAll(
                                                                new KeyFrame(Duration.millis(0),
                                                                        new KeyValue(userPB.progressProperty(),
                                                                                (currentUserPokemon.getCurrentHP()
                                                                                / 100))),
                                                                new KeyFrame(Duration.millis(1500),
                                                                    new KeyValue(userPB.progressProperty(),
                                                                        ((currentUserPokemon.getCurrentHP()
                                                                            - oppDamage) / 100))));
                                                        currentUserPokemon.setCurrentHP(
                                                            currentUserPokemon.getCurrentHP() - oppDamage
                                                        );
                                                        oppMoveTimeline.play();
                                                        oppMoveTimeline.setOnFinished(eventOppMove -> {
                                                                if (currentUserPokemon.isFainted()) {
                                                                    Timeline faintMessage = new Timeline();
                                                                    faintMessage.getKeyFrames()
                                                                            .addAll(new KeyFrame(Duration.seconds(0),
                                                                                    eventFaint ->
                                                                                        userImageView.setVisible(
                                                                                            false
                                                                                            )),
                                                                                    new KeyFrame(Duration.seconds(1.5),
                                                                                    eventFaint ->
                                                                                        userImageView.setVisible(
                                                                                            false
                                                                                            )));
                                                                    message.setText(currentUserPokemon.getName()
                                                                        + " fainted.");
                                                                    userPB.setProgress(0);
                                                                    faintMessage.play();
                                                                    faintMessage.setOnFinished(eventAfterFaint -> {
                                                                            try {
                                                                                start(mainStage);
                                                                            } catch (Exception e) {
                                                                                System.out.println(e.getMessage());
                                                                            }
                                                                        });
                                                                } else {
                                                                    try {
                                                                        start(mainStage);
                                                                    } catch (Exception e) {
                                                                        System.out.println(e.getMessage());
                                                                    }
                                                                }
                                                            });
                                                    });
                                            }
                                        });
                                });
                        });

                    userOption3.setText(userMoves[2].getName());
                    userOption3.setOnAction(eventMove3 -> {
                            message.setText(currentUserPokemon.getName() + " used " + userMoves[2].getName() + ".");
                            Timeline oppAnimation = new Timeline();
                            oppAnimation.getKeyFrames().addAll(
                                    new KeyFrame(Duration.millis(0), evt -> oppImageView.setVisible(false)),
                                    new KeyFrame(Duration.millis(150), evt -> oppImageView.setVisible(true)),
                                    new KeyFrame(Duration.millis(300), evt -> oppImageView.setVisible(false)),
                                    new KeyFrame(Duration.millis(450), evt -> oppImageView.setVisible(true)),
                                    new KeyFrame(Duration.millis(500), evt -> oppImageView.setVisible(false)),
                                    new KeyFrame(Duration.millis(650), evt -> oppImageView.setVisible(true)));
                            oppAnimation.play();
                            oppAnimation.setOnFinished(eventAfterOppAnimation -> {
                                    double damage = attack(userMoves[2], currentUserPokemon, currentOppPokemon, 'u');
                                    Timeline moveTimeline = new Timeline();
                                    moveTimeline.getKeyFrames().addAll(
                                            new KeyFrame(Duration.millis(0),
                                                    new KeyValue(oppPB.progressProperty(),
                                                            (currentOppPokemon.getCurrentHP() / 100))),
                                            new KeyFrame(Duration.millis(1500), new KeyValue(oppPB.progressProperty(),
                                                    ((currentOppPokemon.getCurrentHP() - damage) / 100))));
                                    currentOppPokemon.setCurrentHP(currentOppPokemon.getCurrentHP() - damage);
                                    moveTimeline.play();
                                    moveTimeline.setOnFinished(eventMove -> {
                                            oppHPFraction.setText((int) Math.round(currentOppPokemon.getCurrentHP())
                                                + "/"
                                                + (int) Math.round(currentOppPokemon.getMaxHP()));
                                            if (currentOppPokemon.getCurrentHP() <= 0) {
                                                oppPB.setProgress(0);
                                                oppHPFraction.setText("0/10");
                                                message.setText(currentOppPokemon.getName() + " fainted.");
                                                Timeline faintMessage = new Timeline();
                                                faintMessage.getKeyFrames().addAll(
                                                        new KeyFrame(Duration.seconds(0),
                                                            eventFaint -> oppImageView.setVisible(false)),
                                                        new KeyFrame(Duration.seconds(1.5),
                                                                eventFaint -> oppImageView.setVisible(false)));
                                                faintMessage.play();
                                                faintMessage.setOnFinished(eventAfterFaint -> {
                                                        try {
                                                            start(mainStage);
                                                        } catch (Exception e) {
                                                            System.out.println(e.getMessage());
                                                        }
                                                    });
                                            } else {
                                                Random rand = new Random();
                                                Move oppMove = oppMoves[rand.nextInt(4)];
                                                double oppDamage = attack(
                                                    oppMove,
                                                    currentOppPokemon,
                                                    currentUserPokemon,
                                                    'o'
                                                    );
                                                message.setText(currentOppPokemon.getName()
                                                    + " used " + oppMove.getName() + ".");
                                                Timeline userAnimation = new Timeline();
                                                userAnimation.getKeyFrames().addAll(
                                                        new KeyFrame(Duration.millis(0),
                                                            evt -> userImageView.setVisible(false)),
                                                        new KeyFrame(Duration.millis(150),
                                                            evt -> userImageView.setVisible(true)),
                                                        new KeyFrame(Duration.millis(300),
                                                            evt -> userImageView.setVisible(false)),
                                                        new KeyFrame(Duration.millis(450),
                                                            evt -> userImageView.setVisible(true)),
                                                        new KeyFrame(Duration.millis(500),
                                                            evt -> userImageView.setVisible(false)),
                                                        new KeyFrame(Duration.millis(650),
                                                            evt -> userImageView.setVisible(true)));
                                                userAnimation.play();
                                                userAnimation.setOnFinished(eventAfterUserAnimation -> {
                                                        Timeline oppMoveTimeline = new Timeline();
                                                        oppMoveTimeline.getKeyFrames().addAll(
                                                                new KeyFrame(Duration.millis(0),
                                                                    new KeyValue(userPB.progressProperty(),
                                                                            (currentUserPokemon.getCurrentHP()
                                                                                / 100))),
                                                                new KeyFrame(Duration.millis(1500),
                                                                    new KeyValue(userPB.progressProperty(),
                                                                            ((currentUserPokemon.getCurrentHP()
                                                                                - oppDamage) / 100))));
                                                        currentUserPokemon.setCurrentHP(
                                                            currentUserPokemon.getCurrentHP() - oppDamage
                                                        );
                                                        oppMoveTimeline.play();
                                                        oppMoveTimeline.setOnFinished(eventOppMove -> {
                                                                if (currentUserPokemon.isFainted()) {
                                                                    Timeline faintMessage = new Timeline();
                                                                    faintMessage.getKeyFrames()
                                                                            .addAll(new KeyFrame(Duration.seconds(0),
                                                                                    eventFaint ->
                                                                                        userImageView.setVisible(
                                                                                            false
                                                                                            )),
                                                                                    new KeyFrame(Duration.seconds(1.5),
                                                                                    eventFaint ->
                                                                                        userImageView.setVisible(
                                                                                            false
                                                                                            )));
                                                                    message.setText(currentUserPokemon.getName()
                                                                        + " fainted.");
                                                                    userPB.setProgress(0);
                                                                    faintMessage.play();
                                                                    faintMessage.setOnFinished(eventAfterFaint -> {
                                                                            try {
                                                                                start(mainStage);
                                                                            } catch (Exception e) {
                                                                                System.out.println(e.getMessage());
                                                                            }
                                                                        });
                                                                } else {
                                                                    try {
                                                                        start(mainStage);
                                                                    } catch (Exception e) {
                                                                        System.out.println(e.getMessage());
                                                                    }
                                                                }
                                                            });
                                                    });
                                            }
                                        });
                                });
                        });

                    userOption4.setText(userMoves[3].getName());
                    userOption4.setOnAction(eventMove4 -> {
                            message.setText(currentUserPokemon.getName() + " used " + userMoves[3].getName() + ".");
                            Timeline oppAnimation = new Timeline();
                            oppAnimation.getKeyFrames().addAll(
                                    new KeyFrame(Duration.millis(0), evt -> oppImageView.setVisible(false)),
                                    new KeyFrame(Duration.millis(150), evt -> oppImageView.setVisible(true)),
                                    new KeyFrame(Duration.millis(300), evt -> oppImageView.setVisible(false)),
                                    new KeyFrame(Duration.millis(450), evt -> oppImageView.setVisible(true)),
                                    new KeyFrame(Duration.millis(500), evt -> oppImageView.setVisible(false)),
                                    new KeyFrame(Duration.millis(650), evt -> oppImageView.setVisible(true)));
                            oppAnimation.play();
                            oppAnimation.setOnFinished(eventAfterOppAnimation -> {
                                    double damage = attack(userMoves[3], currentUserPokemon, currentOppPokemon, 'u');
                                    Timeline moveTimeline = new Timeline();
                                    moveTimeline.getKeyFrames().addAll(
                                            new KeyFrame(Duration.millis(0),
                                                    new KeyValue(oppPB.progressProperty(),
                                                            (currentOppPokemon.getCurrentHP() / 100))),
                                            new KeyFrame(Duration.millis(1500), new KeyValue(oppPB.progressProperty(),
                                                    ((currentOppPokemon.getCurrentHP() - damage) / 100))));
                                    currentOppPokemon.setCurrentHP(currentOppPokemon.getCurrentHP() - damage);
                                    moveTimeline.play();
                                    moveTimeline.setOnFinished(eventMove -> {
                                            oppHPFraction.setText((int) Math.round(currentOppPokemon.getCurrentHP())
                                                + "/"
                                                + (int) Math.round(currentOppPokemon.getMaxHP()));
                                            if (currentOppPokemon.getCurrentHP() <= 0) {
                                                oppPB.setProgress(0);
                                                oppHPFraction.setText("0/10");
                                                message.setText(currentOppPokemon.getName() + " fainted.");
                                                Timeline faintMessage = new Timeline();
                                                faintMessage.getKeyFrames().addAll(
                                                        new KeyFrame(Duration.seconds(0), eventFaint ->
                                                            oppImageView.setVisible(
                                                                false
                                                                )),
                                                        new KeyFrame(Duration.seconds(1.5),
                                                                eventFaint -> oppImageView.setVisible(false)));
                                                faintMessage.play();
                                                faintMessage.setOnFinished(eventAfterFaint -> {
                                                        try {
                                                            start(mainStage);
                                                        } catch (Exception e) {
                                                            System.out.println(e.getMessage());
                                                        }
                                                    });
                                            } else {
                                                Random rand = new Random();
                                                Move oppMove = oppMoves[rand.nextInt(4)];
                                                double oppDamage = attack(
                                                    oppMove,
                                                    currentOppPokemon,
                                                    currentUserPokemon,
                                                    'o'
                                                    );
                                                message.setText(currentOppPokemon.getName()
                                                    + " used " + oppMove.getName() + ".");
                                                Timeline userAnimation = new Timeline();
                                                userAnimation.getKeyFrames().addAll(
                                                        new KeyFrame(Duration.millis(0),
                                                            evt -> userImageView.setVisible(false)),
                                                        new KeyFrame(Duration.millis(150),
                                                            evt -> userImageView.setVisible(true)),
                                                        new KeyFrame(Duration.millis(300),
                                                            evt -> userImageView.setVisible(false)),
                                                        new KeyFrame(Duration.millis(450),
                                                            evt -> userImageView.setVisible(true)),
                                                        new KeyFrame(Duration.millis(500),
                                                            evt -> userImageView.setVisible(false)),
                                                        new KeyFrame(Duration.millis(650),
                                                            evt -> userImageView.setVisible(true)));
                                                userAnimation.play();
                                                userAnimation.setOnFinished(eventAfterUserAnimation -> {
                                                        Timeline oppMoveTimeline = new Timeline();
                                                        oppMoveTimeline.getKeyFrames().addAll(
                                                                new KeyFrame(Duration.millis(0),
                                                                    new KeyValue(userPB.progressProperty(),
                                                                        (currentUserPokemon.getCurrentHP()
                                                                            / 100))),
                                                                new KeyFrame(Duration.millis(1500),
                                                                    new KeyValue(userPB.progressProperty(),
                                                                        ((currentUserPokemon.getCurrentHP()
                                                                            - oppDamage) / 100))));
                                                        currentUserPokemon.setCurrentHP(
                                                            currentUserPokemon.getCurrentHP() - oppDamage
                                                        );
                                                        oppMoveTimeline.play();
                                                        oppMoveTimeline.setOnFinished(eventOppMove -> {
                                                                if (currentUserPokemon.isFainted()) {
                                                                    Timeline faintMessage = new Timeline();
                                                                    faintMessage.getKeyFrames()
                                                                            .addAll(new KeyFrame(Duration.seconds(0),
                                                                                    eventFaint ->
                                                                                        userImageView.setVisible(
                                                                                            false
                                                                                            )),
                                                                                    new KeyFrame(Duration.seconds(1.5),
                                                                                    eventFaint ->
                                                                                        userImageView.setVisible(
                                                                                            false
                                                                                            )));
                                                                    message.setText(currentUserPokemon.getName()
                                                                        + " fainted.");
                                                                    userPB.setProgress(0);
                                                                    faintMessage.play();
                                                                    faintMessage.setOnFinished(eventAfterFaint -> {
                                                                            try {
                                                                                start(mainStage);
                                                                            } catch (Exception e) {
                                                                                System.out.println(e.getMessage());
                                                                            }
                                                                        });
                                                                } else {
                                                                    try {
                                                                        start(mainStage);
                                                                    } catch (Exception e) {
                                                                        System.out.println(e.getMessage());
                                                                    }
                                                                }
                                                            });
                                                    });
                                            }
                                        });
                                });
                        });

                    message.setText("Choose a move.");
                });

            userOption2.setText("BAG");
            userOption2.setMinWidth(150);
            userOption2.setOnAction(eventBag -> {
                    userOption1.setText("Training Kit");
                    userOption1.setOnAction(eventTrain -> {
                            if (train > 0) {
                                currentUserPokemon.setLevel(currentUserPokemon.getLevel() + 10);
                                currentUserPokemon.setAtk(currentUserPokemon.getAtk() + 25);
                                train--;
                                Alert alert = new Alert(AlertType.INFORMATION);
                                alert.setTitle("Training Kits");
                                if (train == 1) {
                                    alert.setHeaderText("1 Training Kit Remaining");
                                } else if (train == 0) {
                                    alert.setHeaderText("0 Training Kits Remaining");
                                }
                                alert.setContentText("You have used up " + (2 - train) + " of your 2 training kits.");
                                alert.showAndWait();
                            } else {
                                Alert alert = new Alert(AlertType.ERROR);
                                alert.setTitle("No Training Kit");
                                alert.setHeaderText("Zero Training Kits Available");
                                alert.setContentText("You have used up all your training kits.");
                                alert.showAndWait();
                            }
                            message.setText("You chose to use a Training Kit.");
                            try {
                                start(mainStage);
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        });

                    userOption2.setText("Health Potion");
                    userOption2.setOnAction(eventPotion -> {
                            if (health > 0) {
                                currentUserPokemon.setMaxHP(currentUserPokemon.getMaxHP() + 50);
                                currentUserPokemon.setCurrentHP(currentUserPokemon.getCurrentHP() + 50);
                                health--;
                                Alert alert = new Alert(AlertType.INFORMATION);
                                alert.setTitle("Health Potion");
                                if (train == 0) {
                                    alert.setHeaderText("0 Health Potions Remaining");
                                }
                                alert.setContentText("You have used up your 1 health potion.");
                                alert.showAndWait();
                            } else {
                                Alert alert = new Alert(AlertType.ERROR);
                                alert.setTitle("No Health Potion");
                                alert.setHeaderText("Zero Health Potions Available");
                                alert.setContentText("You have used up all your health potions.");
                                alert.showAndWait();
                            }
                            message.setText("You chose to use a Health Potion.");
                            try {
                                start(mainStage);
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        });

                    userOption3.setText("Identity Card");
                    userOption3.setOnAction(eventIdentity -> {
                            TextInputDialog nameBox = new TextInputDialog("Name");
                            nameBox.setTitle("Identity Card");
                            nameBox.setHeaderText("Enter a new name for your Pokemon:");
                            nameBox.showAndWait();
                            String name = nameBox.getEditor().getText();
                            currentUserPokemon.setName(name);
                            Alert alert = new Alert(AlertType.INFORMATION);
                            alert.setTitle("Identity Card");
                            alert.setHeaderText("Name Successfully Changed");
                            alert.setContentText("Your Pokemon now has a new identity!");
                            alert.showAndWait();
                            try {
                                start(mainStage);
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        });

                    userOption4.setText("Secret Poison");
                    userOption4.setOnAction(eventSecretPoison -> {
                            if (poison > 0) {
                                currentOppPokemon.setCurrentHP(currentOppPokemon.getCurrentHP() - 35);
                                currentUserPokemon.setLevel(currentUserPokemon.getLevel() - 20);
                                if (currentUserPokemon.getLevel() < 1) {
                                    currentUserPokemon.setLevel(1);
                                }
                                poison--;
                                Alert alert = new Alert(AlertType.INFORMATION);
                                alert.setTitle("Secret Poison");
                                if (train == 0) {
                                    alert.setHeaderText("0 Secret Poisons Remaining");
                                }
                                alert.setContentText("You have used up your 1 secret poison.");
                                alert.showAndWait();
                            } else {
                                Alert alert = new Alert(AlertType.ERROR);
                                alert.setTitle("No Secret Poison");
                                alert.setHeaderText("Zero Secret Poison Available");
                                alert.setContentText("You have used up all your secret poisons.");
                                alert.showAndWait();
                            }
                            message.setText("You chose to use a Health Potion.");
                            try {
                                start(mainStage);
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        });

                    message.setText("Choose an Item.");
                });

            userOption3.setText("POKeMON");
            userOption3.setMinWidth(150);
            userOption3.setOnAction(eventPokemon -> {
                    if (onlyOnePokemon()) {
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Only one Pokemon");
                        alert.setHeaderText("Only one Pokemon is alive!");
                        alert.setContentText("Your lone warrior survives.");
                        alert.showAndWait();
                    } else {
                        if (u < 2) {
                            u++;
                            currentUserPokemon = userPokemons[u];
                        } else {
                            u = 0;
                            currentUserPokemon = userPokemons[u];
                        }
                    }
                    try {
                        start(mainStage);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                });

            userOption4.setText("RUN");
            userOption4.setMinWidth(150);
            userOption4.setOnAction(eventRun -> {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Game Over");
                    alert.setHeaderText("Computer Won!");
                    alert.setContentText("The Computer has beaten you.");
                    alert.showAndWait();
                    System.exit(0);
                });

            final HBox userOptions1 = new HBox();
            userOptions1.setSpacing(1);
            userOptions1.getChildren().addAll(userOption1, userOption2);

            final HBox userOptions2 = new HBox();
            userOptions2.setSpacing(1);
            userOptions2.getChildren().addAll(userOption3, userOption4);

            final VBox userOptions = new VBox();
            userOptions.setSpacing(1);
            userOptions.getChildren().addAll(userOptions1, userOptions2);
            userOptions.setAlignment(Pos.CENTER_RIGHT);

            final Line separation = new Line(5, 12, 5, 220);
            separation.setStrokeWidth(5);

            final HBox messageAndOptions = new HBox();
            messageAndOptions.setSpacing(70);
            messageAndOptions.getChildren().addAll(message, separation, userOptions);

            userGrid.add(user, 2, 0);

            final GridPane optionsGrid = new GridPane();
            optionsGrid.setHgap(10);
            optionsGrid.setVgap(10);
            optionsGrid.setPadding(new Insets(0, 100, 0, 100));
            optionsGrid.add(messageAndOptions, 0, 0);
            optionsGrid.setStyle("-fx-border-color: #000000;" + "-fx-border-width: 5;");

            final VBox layout = new VBox();
            layout.setSpacing(60);
            layout.getChildren().addAll(oppGrid, userGrid, optionsGrid);
            layout.getStylesheets().add(getClass().getResource("progress.css").toExternalForm());

            final Scene scene = new Scene(layout, 1000, 1000);
            mainStage.setScene(scene);
            mainStage.show();
        }

    }

    /**
     * This class is the loading screen of the Game. It appears with the Logo and a
     * Loading bar.
     */

    public class InitStage extends Stage {

        private final VBox root = new VBox();

        InitStage() throws FileNotFoundException {

            final ProgressBar bar = new ProgressBar();

            final Image logoImage = new Image(new FileInputStream("pokemon.jpg"));
            final ImageView logoImageView = new ImageView(logoImage);
            logoImageView.setFitHeight(350);
            logoImageView.setFitWidth(350);

            Timeline timeline1 = new Timeline(
                    new KeyFrame(Duration.millis(0), new KeyValue(bar.progressProperty(), 0)),
                    new KeyFrame(Duration.millis(3000), new KeyValue(bar.progressProperty(), 1)));

            root.setAlignment(Pos.CENTER);
            root.setSpacing(10);
            root.getChildren().addAll(logoImageView, bar);
            root.getStylesheets().add(getClass().getResource("progress.css").toExternalForm());

            this.setScene(new Scene(root, 450, 450));
            this.show();
            timeline1.play();

            timeline1.setOnFinished(event -> {
                    try {
                        start(this);
                        this.close();
                    } catch (Exception e) {
                        System.out.println("e.getMessage()");
                    }
                });

        }

    }

    /**
     * This class is the information screen of the Game.
     */

    public class InfoStage extends Stage {

        InfoStage() {
            this.setTitle("Information");
            final Label pokeBattle = new Label();
            pokeBattle.setText("      PokeBattle");
            pokeBattle.setFont(new Font("Arial", 30));
            pokeBattle.setAlignment(Pos.CENTER);

            final Label blankLabel = new Label();
            blankLabel.setMinWidth(50);

            final Text information = new Text(
                    " This is the PokeBattle game"
                    + "\n developed by Hemang Dash for a"
                    + "\n Homework Assignment for CS 1331"
                    + "\n class at the Georgia Institute of"
                    + "\n Technology.");
            information.setFont(new Font(15));
            information.setTextAlignment(TextAlignment.CENTER);

            final Text knownThings = new Text("         Some things to be known:");
            knownThings.setFont(new Font(15));
            knownThings.setTextAlignment(TextAlignment.CENTER);

            final Text known1 = new Text("       1. You and the computer will\n        both get 3 pokemons each.");
            known1.setFont(new Font(15));
            known1.setTextAlignment(TextAlignment.CENTER);

            final Text known2 = new Text("  2. You have 2 training kits, 1\n  health potion and 1 secret poison.");
            known2.setFont(new Font(15));
            known2.setTextAlignment(TextAlignment.CENTER);

            final Text known3 = new Text("    3. You always get the first move.");
            known3.setFont(new Font(15));
            known3.setTextAlignment(TextAlignment.CENTER);

            final Text known4 = new Text("4. You can also choose which Pokemon\nyou wish to fight with.");
            known4.setFont(new Font(15));
            known4.setTextAlignment(TextAlignment.CENTER);

            final VBox known = new VBox();
            known.setSpacing(3);
            known.getChildren().addAll(knownThings, known1, known2, known3, known4);

            final Label difficulty = new Label("Choose difficulty:");
            difficulty.setFont(new Font(15));
            difficulty.setAlignment(Pos.CENTER);

            final Button easy = new Button();
            easy.setText("EASY");
            easy.setOnAction(eventEasy -> {
                    difficultyLevel = 1;
                    try {
                        start(this);
                    } catch (Exception e) {
                        System.out.println("e.getMessage()");
                    }
                });

            final Button medium = new Button();
            medium.setText("MEDIUM");
            medium.setOnAction(eventMedium -> {
                    difficultyLevel = 2;
                    try {
                        start(this);
                    } catch (Exception e) {
                        System.out.println("e.getMessage()");
                    }
                });

            final Button hard = new Button();
            hard.setText("HARD");
            hard.setOnAction(eventHard -> {
                    difficultyLevel = 2;
                    try {
                        start(this);
                    } catch (Exception e) {
                        System.out.println("e.getMessage()");
                    }
                });

            final VBox root = new VBox();
            root.getChildren().addAll(pokeBattle, information, known);
            root.setSpacing(20);

            final HBox layout1 = new HBox();
            layout1.getChildren().addAll(blankLabel, root);

            final Label blankLabel2 = new Label();
            blankLabel2.setMinWidth(20);

            final HBox buttons = new HBox();
            buttons.setSpacing(5);
            buttons.getChildren().addAll(blankLabel2, difficulty, easy, medium, hard);

            final VBox layout = new VBox();
            layout.setSpacing(25);
            layout.getChildren().addAll(layout1, buttons);
            layout.getStylesheets().add(getClass().getResource("progress.css").toExternalForm());

            final Scene scene = new Scene(layout, 400, 400);
            this.setScene(scene);
            this.show();
        }

    }

    /**
     * This method initializes the Pokemons available.
     * @return array containing all available Pokemons
     */

    public Pokemon[] initPokemon() {

        // 1
        final Move thunderboltMove = new Move("Thunderbolt", 90, "ELECTRIC");
        final Move pikachuQuickAttackMove = new Move("Quick Attack", 40, "NORMAL");
        final Move ironTailMove = new Move("Iron Tail", 100, "STEEL");
        final Move voltTackleMove = new Move("Volt Tackle", 120, "ELECTRIC");
        final Move[] pikachuMoves = {thunderboltMove, pikachuQuickAttackMove, ironTailMove, voltTackleMove};
        final Pokemon pikachu = new Pokemon("Pikachu", 3, 100, 10, "ELECTRIC", pikachuMoves);

        // 2
        final Move fireSpinMove = new Move("Fire Spin", 35, "FIRE");
        final Move charizardAirSlashMove = new Move("Air Slash", 75, "FLYING");
        final Move infernoMove = new Move("Inferno", 100, "FIRE");
        final Move flareBlitzMove = new Move("Flare Blitz", 120, "FIRE");
        final Move[] charizardMoves = {fireSpinMove, charizardAirSlashMove, infernoMove, flareBlitzMove};
        final Pokemon charizard = new Pokemon("Charizard", 25, 100, 25, "FIRE", charizardMoves);

        // 3
        final Move squirtleWaterGunMove = new Move("Water Gun", 40, "WATER");
        final Move tacklMove = new Move("Tackle", 40, "NORMAL");
        final Move waterfallMove = new Move("Waterfall", 80, "WATER");
        final Move squirtleHydroPumpmMove = new Move("Hydro Pump", 110, "WATER");
        final Move[] squirtleMoves = {squirtleWaterGunMove, tacklMove, waterfallMove, squirtleHydroPumpmMove};
        final Pokemon squirtle = new Pokemon("Squirtle", 10, 100, 10, "WATER", squirtleMoves);

        // 4
        final Move poundMove = new Move("Pound", 40, "NORMAL");
        final Move doubleSlapMove = new Move("Double Slap", 15, "NORMAL");
        final Move jigglypuffBodySlamMove = new Move("Body Slam", 85, "NORMAL");
        final Move doubleEdgeMove = new Move("Double-Edge", 120, "NORMAL");
        final Move[] jigglypuffMoves = {poundMove, doubleSlapMove, jigglypuffBodySlamMove, doubleEdgeMove};
        final Pokemon jigglypuff = new Pokemon("Jigglypuff", 5, 100, 5, "NORMAL", jigglypuffMoves);

        // 5
        final Move gustMove = new Move("Gust", 40, "FLYING");
        final Move pidgeyQuickAttackMove = new Move("Quick Attack", 40, "NORMAL");
        final Move wingAttackMove = new Move("Wing Attack", 60, "FLYING");
        final Move pidgeyAirSlashMove = new Move("Air Slash", 75, "AirSlash");
        final Move[] pidgeyMoves = {gustMove, pidgeyQuickAttackMove, wingAttackMove, pidgeyAirSlashMove};
        final Pokemon pidgey = new Pokemon("Pidgey", 7, 100, 3, "FLYING", pidgeyMoves);

        // 6
        final Move biteMove = new Move("Bite", 60, "NORMAL");
        final Move crunchMove = new Move("Crunch", 80, "DARK");
        final Move hyperFangMove = new Move("Hyper Fang", 80, "NORMAL");
        final Move suckerPunchMove = new Move("Sucker Punch", 70, "DARK");
        final Move[] rattataMoves = {biteMove, crunchMove, hyperFangMove, suckerPunchMove};
        final Pokemon rattata = new Pokemon("Rattata", 27, 100, 15, "NORMAL", rattataMoves);

        // 7
        final Move vineWhipMove = new Move("Vine Whip", 45, "GRASS");
        final Move razorLeafMove = new Move("Razor Leaf", 55, "GRASS");
        final Move takeDownMove = new Move("Take Down", 90, "NORMAL");
        final Move solarBeamMove = new Move("Solar Beam", 120, "GRASS");
        final Move[] bulbasaurMoves = {vineWhipMove, razorLeafMove, takeDownMove, solarBeamMove};
        final Pokemon bulbasaur = new Pokemon("Bulbasaur", 20, 100, 15, "GRASS", bulbasaurMoves);

        // 8
        final Move confusionMove = new Move("Confusion", 50, "PSYCHIC");
        final Move magicalLeafMove = new Move("Magical Leaf", 60, "GRASS");
        final Move futureSightMove = new Move("Future Sight", 120, "PSYCHIC");
        final Move leafStormMove = new Move("Leaf Storm", 130, "GRASS");
        final Move[] celebiMoves = {confusionMove, magicalLeafMove, futureSightMove, leafStormMove};
        final Pokemon celebi = new Pokemon("Celebi", 60, 100, 35, "GRASS", celebiMoves);

        // 9
        final Move iceShardMove = new Move("Ice Shard", 40, "WATER");
        final Move brineMove = new Move("Brine", 65, "WATER");
        final Move laprasBodySlamMove = new Move("Body Slam", 85, "NORMAL");
        final Move iceBeamMove = new Move("Ice Beam", 90, "WATER");
        final Move[] laprasMoves = {iceShardMove, brineMove, laprasBodySlamMove, iceBeamMove};
        final Pokemon lapras = new Pokemon("Lapras", 18, 100, 28, "WATER", laprasMoves);

        // 10
        final Move emberMove = new Move("Ember", 40, "FIRE");
        final Move incinerateMove = new Move("Incinerate", 60, "FIRE");
        final Move flamethrowerMove = new Move("Flamethrower", 90, "FIRE");
        final Move fireBlastMove = new Move("Fire Blast", 110, "FIRE");
        final Move[] vulpixMoves = {emberMove, incinerateMove, flamethrowerMove, fireBlastMove};
        final Pokemon vulpix = new Pokemon("Vulpix", 55, 100, 14, "FIRE", vulpixMoves);

        // 11
        final Move twisterMove = new Move("Twister", 40, "FLYING");
        final Move extremeSpeedMove = new Move("Extreme Speed", 85, "FLYING");
        final Move flyMove = new Move("Fly", 90, "FLYING");
        final Move outrageMove = new Move("Outrage", 120, "FLYING");
        final Move[] rayquazaMoves = {twisterMove, extremeSpeedMove, flyMove, outrageMove};
        final Pokemon rayquaza = new Pokemon("Rayquaza", 45, 100, 30, "FLYING", rayquazaMoves);

        // 12
        final Move mudkipWaterGunMove = new Move("Water Gun", 40, "WATER");
        final Move mudSlapMove = new Move("Mud Slap", 20, "NORMAL");
        final Move whirlPoolMove = new Move("Whirlpool", 35, "WATER");
        final Move mudkipHydroPumpMove = new Move("Hydro Pump", 110, "WATER");
        final Move[] mudkipMoves = {mudkipWaterGunMove, mudSlapMove, whirlPoolMove, mudkipHydroPumpMove};
        final Pokemon mudkip = new Pokemon("Mudkip", 2, 100, 5, "WATER", mudkipMoves);

        final Pokemon[] pokemons = {pikachu, charizard, squirtle, jigglypuff, pidgey, rattata, bulbasaur, celebi,
            lapras, vulpix, rayquaza, mudkip};

        return pokemons;

    }

    /**
     * This method initializes the User Pokemons randomly.
     * @return array containing User Pokemons
     */

    public Pokemon[] initUser() {

        final Pokemon[] pokemons = initPokemon();
        final Pokemon[] userPokemons1 = new Pokemon[3];
        final int[] duplicates = new int[3];
        int flag = 0;

        final Random rand = new Random();
        for (int i = 0; i < 3; i++) {
            duplicates[i] = rand.nextInt(pokemons.length);
        }

        while (flag == 0) {
            flag = 1;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < i; j++) {
                    if (duplicates[i] == duplicates[j]) {
                        duplicates[i] = rand.nextInt(pokemons.length);
                        flag = 0;
                    }
                }
            }
        }

        for (int i = 0; i < 3; i++) {
            userPokemons1[i] = pokemons[duplicates[i]];
        }

        return userPokemons1;

    }

    /**
     * This method initializes the Opponent Pokemons randomly.
     * @return array containing Opponent Pokemons
     */

    public Pokemon[] initOpp() {

        final Pokemon[] pokemons = initPokemon();
        final Pokemon[] oppPokemons1 = new Pokemon[3];
        final Random rand = new Random();
        final int[] duplicates = new int[3];
        int flag = 0;

        for (int i = 0; i < 3; i++) {
            duplicates[i] = rand.nextInt(pokemons.length);
        }

        while (flag == 0) {
            flag = 1;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < i; j++) {
                    if (duplicates[i] == duplicates[j]) {
                        duplicates[i] = rand.nextInt(pokemons.length);
                        flag = 0;
                    }
                }
            }
        }

        for (int i = 0; i < 3; i++) {
            oppPokemons1[i] = pokemons[duplicates[i]];
        }

        return oppPokemons1;

    }

    /**
     * This method calculates the damage caused by a Move.
     * @param move     Move used
     * @param pokemon1 The Pokemon which attacks
     * @param pokemon2 The Pokemon getting attacked
     * @param type     User or Opponent
     * @return Damage caused
     */

    public double attack(final Move move, final Pokemon pokemon1, final Pokemon pokemon2, final char type) {

        final double effect = pokemon2.compareType(move);
        double damage = effect * (move.getPower()) * (pokemon1.getAtk() / 100.0)
                + ((pokemon1.getLevel() - pokemon2.getLevel()) / 5);
        if (damage < 0) {
            damage *= (-1);
        }
        if (type == 'o') {
            if (difficultyLevel == 3) {
                damage += 25;
            } else if (difficultyLevel == 2) {
                damage += 10;
            }
        }

        return damage;

    }

    /**
     * This method checks if any Pokemon in the passed array is alive or not.
     * @param alive User or Opponent Pokemons
     * @return True or False
     */

    public boolean anyAlive(final int[] alive) {

        boolean result = false;

        for (int i = 0; i < 3; i++) {
            if (alive[i] == 1) {
                result = true;
                break;
            }
        }

        return result;
    }

    /**
     * This method checks if only one Pokemon is alive.
     * @return True or False
     */

    public boolean onlyOnePokemon() {

        boolean result = false;

        int count = 0;
        for (int i = 0; i < 3; i++) {
            if (aliveUserPokemons[i] == 1) {
                count++;
            }
        }

        if (count == 1) {
            result = true;
        }

        return result;

    }

}