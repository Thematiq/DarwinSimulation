package app;

import engine.handlers.Simulation;

public class SimulationController extends Thread {

    protected Simulation sim;
    protected boolean running = false;
    protected final int sleepTime;

    public SimulationController(Simulation sim) { this(sim, 1000); }

    public SimulationController(Simulation sim, int sleepTime) {
        this.sim = sim;
        this.sleepTime = sleepTime;
    }

    public void setStatus(boolean running) {
        this.running = running;
        System.out.println("Changed running to " + String.valueOf(running));
    }

    @Override
    public void run() {
        System.out.println("Started!");
        while (true) {
            if (this.running) {
                this.sim.nextDay();
                try {
                    Thread.sleep(this.sleepTime);
                } catch(InterruptedException e ) {
                    e.printStackTrace();
                }
            }
        }
    }
}
