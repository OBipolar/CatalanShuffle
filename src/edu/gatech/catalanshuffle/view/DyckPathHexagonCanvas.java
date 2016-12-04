package edu.gatech.catalanshuffle.view;

import edu.gatech.catalanshuffle.model.DyckPath;
import edu.gatech.catalanshuffle.model.DyckPath.InitType;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.lang.reflect.Array;
import java.util.*;

import static edu.gatech.catalanshuffle.model.CatalanModel.rand;

public class DyckPathHexagonCanvas extends CatalanModelCanvas {
    private DyckPath[] model;
    private int pathIndex;

    public DyckPathHexagonCanvas(int n, double width, double height, double weightedLambda, int size) {
        super(n, width, height);
        this.model = new DyckPath[size + 1];
        model[0] = new DyckPath(n, InitType.HEXAGONBOTTOM);
        for (int i = 1; i < size + 1; i++) {
            model[i] = new DyckPath(n, InitType.BUTTOM, false, true, true, 1);
        }
        draw();
    }

    public void tick() {
        draw();
        pathIndex = rand.nextInt(model.length - 2) + 1;
        if (pathIndex == 0) {
            model[pathIndex].contextShuffle(model[pathIndex+1].getModel(), null, true);
        } else if (pathIndex == model.length - 2) {
            model[pathIndex].contextShuffle(null, model[pathIndex-1].getModel(), true);
        } else {
            model[pathIndex].contextShuffle(model[pathIndex+1].getModel(), model[pathIndex-1].getModel(), true);
        }
//        model[5].contextShuffle(model[6].getModel(), model[4].getModel());
//        model[5].shuffleOnce();
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
        double widthBase = width / 4;
        double heightBase = height / 6;
        int length = 2 * model[0].getN();
        double unitWidth = width / (2 * length);
        double unitHeight = height / (10 * model[0].getN());

        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, width, height);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(3);

        for (int i = 0; i < model.length - 1; i++) {
            DyckPath cur = model[i];
            double curHeight = height - i * unitHeight * 6- 2 * heightBase;
            for (int j = 0; j < length; j++) {
                double nextHeight = curHeight + (cur.getModel()[j] ? - 3*unitHeight : 3*unitHeight);
                if (i == pathIndex) {
                    gc.setStroke(Color.RED);
                } else {
                    gc.setStroke(new Color(0,0,1,0.3));
                }
                gc.strokeLine(j*unitWidth + widthBase, curHeight, (j+1)*unitWidth + widthBase, nextHeight);
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
