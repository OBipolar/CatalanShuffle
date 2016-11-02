package edu.gatech.catalanshuffle.view;

import edu.gatech.catalanshuffle.model.DyckPath;
import edu.gatech.catalanshuffle.model.DyckPath.InitType;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.lang.reflect.Array;
import java.util.*;

public class DyckPathParallelCanvas extends CatalanModelCanvas {
    private DyckPath[] model;

    public DyckPathParallelCanvas(int n, double width, double height, double weightedLambda, int size) {
        super(n, width, height);
        this.model = new DyckPath[size];
        for (int i = 0; i < size; i++) {
            model[i] = new DyckPath(n, InitType.BUTTOM);
        }
        draw();
    }

    public void tick() {
        for (DyckPath p : model) {
            p.shuffleOnce();
        }
        draw();
    }

    public void setWeightedLambda(double weightedLambda) {
        for (DyckPath m : model) {
            m.setWeightedLambda(weightedLambda);
        }
    }

    public void setAdjToggle() {
        for (DyckPath m : model) {
            m.setAdjToggle();
        }
    }

    public void reset() {
        for (DyckPath p : model) {
            p.reset();
        }
        draw();
    }

    private void draw() {
        double width = getWidth();
        double height = getHeight();
        int length = 2 * model[0].getN();
        double unitWidth = width / length;
        double unitHeight = height / model[0].getN();

        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, width, height);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(5);

        for (int i = 0; i < model.length; i++) {
            DyckPath cur = model[i];
            double curHeight = height - i * unitHeight;
            for (int j = 0; j < length; j++) {
                double nextHeight = curHeight + (cur.getModel()[j] ? - unitHeight : unitHeight);
                gc.setStroke(new Color(0, 0, 1, i/(float)model.length));
                gc.strokeLine(j*unitWidth, curHeight, (j+1)*unitWidth, nextHeight);
                curHeight = nextHeight;
            }
        }
//        double unitOpacity = 1.0 / model.length;
//        double avgHeight = 0;
//
//        Map<List<Double>, Double> viewModel = new HashMap<>();
//        for (DyckPath path : model) {
//            double curHeight = height;
//            for (int i = 0; i < length; i++) {
//                double nextHeight = curHeight + (path.getModel()[i] ? -unitHeight : unitHeight);
//                List<Double> coor = new ArrayList<>();
//                coor.add(i*unitWidth);
//                coor.add(curHeight);
//                coor.add((i+1)*unitWidth);
//                coor.add(nextHeight);
//                if (viewModel.containsKey(coor)) {
//                    viewModel.put(coor, viewModel.get(coor) + unitOpacity);
//                }
//                else {
//                    viewModel.put(coor, unitOpacity);
//                }
//                curHeight = nextHeight;
//            }
//            avgHeight += path.testStatisticsValue(DyckPath.TestStatistics.PEEK);
//        }
//
//        GraphicsContext gc = getGraphicsContext2D();
//        gc.clearRect(0, 0, width, height);
//        gc.setLineWidth(5);
//
//        for (List<Double> coor : viewModel.keySet()) {
//            gc.setStroke(new Color(0, 0, 1, Math.min(1.0, viewModel.get(coor))));
//            gc.strokeLine(coor.get(0), coor.get(1), coor.get(2), coor.get(3));
//        }
//
//        avgHeight /= model.length;
//        gc.setStroke(Color.BLACK);
//        gc.setLineWidth(1);
//        gc.strokeText("Average Height: " + avgHeight, 10, 20);
    }
}
