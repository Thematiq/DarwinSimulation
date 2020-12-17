package app.windows;

import engine.handlers.Simulation;
import engine.objects.Animal;
import engine.tools.Algebra;
import engine.tools.Parameters;
import engine.tools.Vector;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public abstract class AbstractSimulatorController {
    final Image animal = new Image("file:resources/animal.png");
    final Image flower = new Image("file:resources/flower.png");
    final Image icon = new Image("file:resources/icon.png");

    final boolean grid = false;
    final int dayLength = 100;

    Timeline timeline;
    int cellSize;
    Parameters params;

    public abstract void initSimulation(Parameters param);

    void run_drawer(Simulation sim, Canvas can) {
        GraphicsContext gc = can.getGraphicsContext2D();
        // Draw map
        Animal an;
        gc.setFill(Color.DARKORANGE);
        gc.fillRect(0, 0, this.cellSize * params.width, this.cellSize * params.height);
        gc.setFill(Color.DARKGREEN);
        int[] jungleSize = sim.jungleSize();
        for(int i = 0; i < 4; ++i) {
            jungleSize[i] *= this.cellSize;
        }
        gc.fillRect(jungleSize[0], jungleSize[1], jungleSize[2], jungleSize[3]);
        gc.setFill(Color.GREEN);

        for(int x = 0; x < params.width ; ++x) {
            for(int y = 0; y < params.height; ++y) {
                if (sim.isGrass(new Vector(x, y))) {
                    gc.drawImage(this.flower, this.cellSize * x, this.cellSize * y, this.cellSize, this.cellSize);
                }
                an = sim.animalAt(new Vector(x,y));
                if (an != null) {
                    this.draw_animal(gc, an);
                }
            }
        }

        if (this.grid) {
            gc.setLineWidth(1.0);
            gc.setStroke(Color.BLACK);
            for (int i = 0; i < params.width; ++i) {
                gc.strokeLine(this.cellSize * i, 0, this.cellSize * i, can.getHeight());
            }
            for (int i = 0; i < params.height; ++i) {
                gc.strokeLine(0, this.cellSize * i, can.getWidth(), this.cellSize * i);
            }
        }
    }

    void draw_animal(GraphicsContext gc, Animal a) {
        gc.save();
        gc.rotate(a.getOrient().getDegree());
        Vector pos = Algebra.rotateVector(a.getPos().mult(this.cellSize), a.getOrient().getDegree(), new Vector(this.cellSize/2, this.cellSize/2));
        gc.drawImage(this.animal, pos.x, pos.y, this.cellSize, this.cellSize);
        //Draw Energy bar
        gc.setLineWidth(8.0);
        gc.setStroke(Color.GREY);
        gc.strokeLine(pos.x, pos.y, pos.x + this.cellSize, pos.y);
        double energyPercent = ((double)a.getEnergy() / (this.params.startEnergy * 2));
        if (energyPercent > 1) {
            energyPercent = 1.0;
        }
        gc.setStroke(Color.BLUE);
        if (energyPercent > 0) {
            gc.strokeLine(pos.x, pos.y, pos.x + (this.cellSize * energyPercent), pos.y);
        }
        gc.restore();
    }
}
