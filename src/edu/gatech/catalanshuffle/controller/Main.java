package edu.gatech.catalanshuffle.controller;

import org.apache.commons.lang3.math.NumberUtils;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import edu.gatech.catalanshuffle.view.*;

// // todo: add catlan number output solution

public class Main extends Application {
	
	public static final int TICK_RATE = 400;
	public static final int INITIAL_N = 15;
	public static final String INITIAL_CANVAS = "Polygon Triangulation";
	
	BorderPane frame;
	CatalanModelCanvas canvas;
	Timeline timer;
	boolean timerTicking = true;
	double lambda = 1;
	 
    public static void main(String[] args) {
        launch(args);
    }
 
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Catalan Structure");
        Group root = new Group();
        frame = new BorderPane();
        setupCanvas(INITIAL_N, INITIAL_CANVAS);
        root.getChildren().add(frame);
        primaryStage.setScene(new Scene(root));
        frame.setBottom(getVariablePanel());
        
        primaryStage.show();
    }
    
    public void setupCanvas(int n, String type) {
    	if (timer != null) {
    		timer.stop();
    	}
        canvas = CatalanModelCanvasFactory(n, type);
        
        timer = new Timeline(new KeyFrame(Duration.millis(TICK_RATE), new TickCanvas(canvas, 1)));
        timer.setCycleCount(Timeline.INDEFINITE);
        
        frame.setCenter(canvas);
        frame.setRight(getControlPanel());
        
        if (timerTicking) {
        	timer.play();
        }
    }
    
    public HBox getVariablePanel() {
    	HBox hbox = new HBox();
    	hbox.setPadding(new Insets(15, 12, 15, 12));
    	hbox.setSpacing(10);
    	hbox.setStyle("-fx-background-color: #336699;");
    	
    	ComboBox<String> viewType = new ComboBox<>();
    	viewType.getItems().addAll(
        	"Polygon Triangulation",
        	"Single Dyck Path",
            "100 Dyck Paths",
            "Dyck Path Coupling",
			"Paralle Dyck Paths"
        );
    	viewType.setValue(viewType.getItems().get(0));
    	
    	Slider nSlider = new Slider(5, 50, INITIAL_N);
    	Label nLabel = new Label("n: ");
    	Label nValue = new Label(Integer.toString((int)nSlider.getValue()));
    	nLabel.setFont(new Font("Arial", 15));
    	nLabel.setTextFill(Color.WHITE);
    	nValue.setFont(new Font("Arial", 15));
    	nValue.setTextFill(Color.WHITE);
    	nSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
            	int n = (int)nSlider.getValue();
            	nValue.setText(String.format("%02d", n));
            	setupCanvas(n, viewType.getValue());
            }
        });
    	viewType.valueProperty().addListener(new ChangeListener<String>() {
            @Override 
            public void changed(@SuppressWarnings("rawtypes") ObservableValue ov, String t, String t1) {         
            	int n = (int)nSlider.getValue();
            	setupCanvas(n, viewType.getValue()); 
            }    
        });
    	
    	Slider tickSlider = new Slider(50, 999, TICK_RATE);
    	Label tickLabel = new Label("speed (millisecond): ");
    	Label tickValue = new Label(Integer.toString((int)tickSlider.getValue()));
    	tickLabel.setFont(new Font("Arial", 15));
    	tickLabel.setTextFill(Color.WHITE);
    	tickValue.setFont(new Font("Arial", 15));
    	tickValue.setTextFill(Color.WHITE);
    	tickSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
            	// reset timer when rate changes
            	int rate = (int)tickSlider.getValue();
            	timer.stop();
            	timer.getKeyFrames().clear();
            	timer.getKeyFrames().add(new KeyFrame(Duration.millis(rate), new TickCanvas(canvas, 1)));
            	timer.setCycleCount(Timeline.INDEFINITE);
            	tickValue.setText(String.format("%03d", rate));
            	if (timerTicking) {
            		timer.play();
            	}
            }
        });

		Slider pathNumSlider = new Slider(1, 25, INITIAL_N);
		Label pathNumLabel = new Label("Path Num: ");
		Label pathNumValue = new Label(Integer.toString((int)nSlider.getValue()));
		pathNumLabel.setFont(new Font("Arial", 15));
		pathNumLabel.setTextFill(Color.WHITE);
		pathNumValue.setFont(new Font("Arial", 15));
		pathNumValue.setTextFill(Color.WHITE);
		pathNumSlider.valueProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> ov,
								Number old_val, Number new_val) {
				int n = (int)pathNumSlider.getValue();
				pathNumValue.setText(String.format("%02d", n));
				System.out.print(n);
//				setupCanvas(n, viewType.getValue());
			}
		});

    	hbox.getChildren().addAll(viewType, nLabel, nValue, nSlider, tickLabel, tickValue, tickSlider, pathNumLabel, pathNumValue, pathNumSlider);
    	return hbox;
    }
    
    public VBox getControlPanel() {
    	VBox vbox = new VBox();
        vbox.setPadding(new Insets(15, 12, 15, 12));
        vbox.setSpacing(10);
        vbox.setStyle("-fx-background-color: #336699;");

        // pause automatic ticking
        Button pause = new Button("Pause");
        pause.setPrefSize(100, 20);
        pause.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	timerTicking = false;
            	timer.pause();
            }
        });
        // resume automatic ticking
        Button play = new Button("Play");
        play.setPrefSize(100, 20);
        play.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	timerTicking = true;
            	timer.play();
            }
        });
        // manually do one step random walk
        Button tick = new Button("Tick");
        tick.setPrefSize(100, 20);
        tick.setOnAction(new TickCanvas(canvas, 1));
        // manually do five steps random walk
        Button tick5 = new Button("Tick 5 Times");
        tick5.setPrefSize(100, 20);
        tick5.setOnAction(new TickCanvas(canvas, 5));
        // reset the structure to be in its original state
        Button reset = new Button("Reset");
        reset.setPrefSize(100, 20);
        reset.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	canvas.reset();
            }
        });

		// manually set to adjacency shuffle
		Button adj = new Button("Adj/Rand");
		adj.setPrefSize(100, 20);
		adj.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				canvas.setAdjToggle();
			}
		});

//        Label lambdaLabel = new Label("Lambda: ");
        TextField lambdaInput = new TextField();
        lambdaInput.setPrefSize(100, 20);
        lambdaInput.setPromptText("Lambda");
        if (lambda != 1) {
        	lambdaInput.setText(Double.toString(lambda));
        }
        Button applyLambda = new Button("Apply");
        applyLambda.setPrefSize(100, 20);
        applyLambda.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	String lambdaValue = lambdaInput.getText();
            	if (lambdaValue.length() == 0) {
            		lambda = 1;
            	}
            	else if (NumberUtils.isNumber(lambdaValue) && NumberUtils.toDouble(lambdaValue) > 0) {
            		lambda = NumberUtils.toDouble(lambdaValue);
            	}
            	else {
            		lambdaInput.setText(Double.toString(lambda));
            	}
            	canvas.setWeightedLambda(lambda);
            }
        });
//        CheckBox weighted = new CheckBox("weighted");
//        weighted.setTextFill(Color.WHITE);
//        weighted.selectedProperty().addListener(new ChangeListener<Boolean>() {
//            public void changed(ObservableValue<? extends Boolean> ov,
//                Boolean old_val, Boolean new_val) {
//                   canvas.setWeighted(new_val);
//            }
//        });
        
        vbox.getChildren().addAll(pause, play, tick, tick5, reset, adj, lambdaInput, applyLambda);
        return vbox;
    }
    
    private CatalanModelCanvas CatalanModelCanvasFactory(int n, String type) {
    	int width = 800;
    	int height = 450;
		boolean isAdj = false; // adjacent shuffle toggle (by default to random transposition)
		switch(type) {
			case "Polygon Triangulation":
    			return new PolygonTriangulationCanvas(n, width, height, lambda);
    		case "Single Dyck Path":
    			return new DyckPathCanvas(n, width, height, lambda);
    		case "100 Dyck Paths":
    			return new DyckPathCollectionCanvas(n, width, height, lambda, 100);
    		case "Dyck Path Coupling":
    			return new DyckPathCouplingCanvas(n, width, height, lambda, false, true);
			case "Paralle Dyck Paths":
				return new DyckPathParallelCanvas(n, width, height, lambda, 10);
			default:
    			return null;
    	}
    }
    
    private class TickCanvas implements EventHandler<ActionEvent> {
    	
    	private final CatalanModelCanvas canvas;
    	private final int itr;
    	
    	public TickCanvas(CatalanModelCanvas canvas, int itr) {
    		this.canvas = canvas;
    		this.itr = itr;
		}

		@Override
		public void handle(ActionEvent event) {
			for (int i = 0; i < itr; i++) {
				canvas.tick();
			}
		}
    }

}