package core;

public class Process {
    private int pid;
    private int arrivalTime;
    private int burstTime;
    private int startTime;
    private int remainingTime;
    private int completionTime;
    private int priority;

    public Process(int pid, int arrivalTime, int burstTime) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.startTime = -1;
        this.remainingTime = burstTime;
        this.completionTime = 0;
        this.priority = pid;
    }

    public int getPid() {
        return pid;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public int getPriority() {
        return priority;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }
    public int getCompletionTime() {
        return completionTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public void setCompletionTime(int completionTime) {
        this.completionTime = completionTime;
    }
}
