package core;

import ui.GanttEntity;

import java.util.*;

public class Scheduler {

    public static GanttData runFCFS(List<Process> processes) {
        List<Process> sorted = new ArrayList<>(processes);
        sorted.sort((p1, p2) -> Integer.compare(p1.getArrivalTime(), p2.getArrivalTime()));

        int currentTime = 0;
        StringBuilder sb = new StringBuilder();
        List<GanttEntity> ganttEntities = new ArrayList<>();
        sb.append("PID\tArrival\tBurst\tStart\tCompletion\n");

        for (Process p : sorted) {
            int start = Math.max(currentTime, p.getArrivalTime());
            int completion = start + p.getBurstTime();

            p.setStartTime(start);
            p.setCompletionTime(completion);

            sb.append(p.getPid()).append("\t")
                    .append(p.getArrivalTime()).append("\t")
                    .append(p.getBurstTime()).append("\t")
                    .append(start).append("\t")
                    .append(completion).append("\n");

            ganttEntities.add(new GanttEntity(p.getPid(), start, completion));

            currentTime = completion;
        }

        return new GanttData(sb.toString(), ganttEntities);
    }

    public static GanttData runSJF(List<Process> processes) {
        List<Process> sorted = new ArrayList<>(processes);
        List<Process> result = new ArrayList<>();
        int currentTime = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("PID\tArrival\tBurst\tStart\tCompletion\n");

        List<GanttEntity> ganttEntities = new ArrayList<>();

        while (!sorted.isEmpty()) {
            // Find all processes that have arrived
            List<Process> available = new ArrayList<>();
            for (Process p : sorted) {
                if (p.getArrivalTime() <= currentTime) {
                    available.add(p);
                }
            }

            if (available.isEmpty()) {
                // If no process has arrived, move time to the next arriving process
                int nextArrival = sorted.stream()
                        .mapToInt(Process::getArrivalTime)
                        .min()
                        .orElse(currentTime);
                currentTime = nextArrival;
                continue;
            }

            // Pick process with the shortest burst time
            Process next = available.stream()
                    .min(Comparator.comparingInt(Process::getBurstTime))
                    .get();

            sorted.remove(next);

            int start = Math.max(currentTime, next.getArrivalTime());
            int completion = start + next.getBurstTime();

            next.setStartTime(start);
            next.setCompletionTime(completion);
            result.add(next);

            sb.append(next.getPid()).append("\t")
                    .append(next.getArrivalTime()).append("\t")
                    .append(next.getBurstTime()).append("\t")
                    .append(start).append("\t")
                    .append(completion).append("\n");

            ganttEntities.add(new GanttEntity(next.getPid(), start, completion));

            currentTime = completion;
        }

        // Update the original list for Gantt Chart use
        processes.clear();
        processes.addAll(result);

        return new GanttData(sb.toString(), ganttEntities);
    }

    public static GanttData runSRTF(List<Process> processes) {
        List<Process> processList = new ArrayList<>(processes);
        int n = processList.size();
        int completed = 0;
        int currentTime = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("PID\tArrival\tBurst\tStart\tCompletion\n");

        List<GanttEntity> ganttEntities = new ArrayList<>();
        Process currentProcess = null;
        int startTime = 0;

        // Initialize remaining times
        for (Process p : processList) {
            p.setRemainingTime(p.getBurstTime());
            p.setStartTime(-1);
        }

        while (completed < n) {
            Process shortest = null;
            int minRemaining = Integer.MAX_VALUE;

            // Find the process with shortest remaining time
            for (Process p : processList) {
                if (p.getArrivalTime() <= currentTime && p.getRemainingTime() > 0) {
                    if (p.getRemainingTime() < minRemaining) {
                        minRemaining = p.getRemainingTime();
                        shortest = p;
                    }
                }
            }

            // Handle process change for Gantt chart
            if (shortest != currentProcess) {
                if (currentProcess != null) {
                    ganttEntities.add(new GanttEntity(
                            currentProcess.getPid(),
                            startTime,
                            currentTime
                    ));
                }
                currentProcess = shortest;
                startTime = currentTime;
            }

            // Execute the current process
            if (shortest != null) {
                if (shortest.getStartTime() == -1) {
                    shortest.setStartTime(currentTime);
                }
                shortest.setRemainingTime(shortest.getRemainingTime() - 1);

                // Check if process completed
                if (shortest.getRemainingTime() == 0) {
                    shortest.setCompletionTime(currentTime + 1);
                    completed++;
                    ganttEntities.add(new GanttEntity(
                            shortest.getPid(),
                            startTime,
                            currentTime + 1
                    ));
                    currentProcess = null;
                }
            }

            currentTime++;
        }

        // Generate output
        for (Process p : processList) {
            sb.append(p.getPid()).append("\t")
                    .append(p.getArrivalTime()).append("\t")
                    .append(p.getBurstTime()).append("\t")
                    .append(p.getStartTime()).append("\t")
                    .append(p.getCompletionTime()).append("\n");
        }

        return new GanttData(sb.toString(), ganttEntities);
    }

    public static GanttData runPriority(List<Process> processes) {
        List<Process> sorted = new ArrayList<>(processes);
        List<Process> result = new ArrayList<>();
        int currentTime = 0, completed = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("PID\tArrival\tBurst\tPriority\tStart\tCompletion\n");

        List<GanttEntity> ganttEntities = new ArrayList<>();

        while (completed != sorted.size()) {
            List<Process> available = new ArrayList<>();
            for (Process p : sorted) {
                if (p.getArrivalTime() <= currentTime && !result.contains(p)) {
                    available.add(p);
                }
            }

            if (available.isEmpty()) {
                currentTime++;
                continue;
            }

            Process next = available.stream()
                    .min(Comparator.comparingInt(Process::getPriority))
                    .get();

            int start = Math.max(currentTime, next.getArrivalTime());
            int completion = start + next.getBurstTime();

            next.setStartTime(start);
            next.setCompletionTime(completion);
            result.add(next);

            sb.append(next.getPid()).append("\t")
                    .append(next.getArrivalTime()).append("\t")
                    .append(next.getBurstTime()).append("\t")
                    .append(next.getPriority()).append("\t")
                    .append(start).append("\t")
                    .append(completion).append("\n");

            ganttEntities.add(new GanttEntity(next.getPid(), start, completion));

            currentTime = completion;
            completed++;
        }

        processes.clear();
        processes.addAll(result);

        return new GanttData(sb.toString(), ganttEntities);
    }


    public static final int DEFAULT_QUANTUM = 2;  // Default quantum size

    public static GanttData runRoundRobin(List<Process> processes) {
        return runRoundRobin(processes, DEFAULT_QUANTUM);
    }

    public static GanttData runRoundRobin(List<Process> processes, int quantum) {
        if (processes.isEmpty()) {
            return new GanttData("No processes to schedule", Collections.emptyList());
        }

        // Create a copy and sort by arrival time
        List<Process> processList = new ArrayList<>(processes);
        processList.sort(Comparator.comparingInt(Process::getArrivalTime));

        Queue<Process> queue = new LinkedList<>();
        int currentTime = 0;
        int nextProcessIndex = 0;
        int[] remainingTime = new int[processList.size()];
        int[] startTime = new int[processList.size()];
        Arrays.fill(startTime, -1);

        // Initialize remaining times
        for (int i = 0; i < processList.size(); i++) {
            remainingTime[i] = processList.get(i).getBurstTime();
        }

        List<GanttEntity> ganttEntities = new ArrayList<>();
        StringBuilder result = new StringBuilder();
        result.append("PID\tArrival\tBurst\tStart\tCompletion\n");

        while (true) {
            // Add arriving processes to the queue
            while (nextProcessIndex < processList.size() &&
                    processList.get(nextProcessIndex).getArrivalTime() <= currentTime) {
                queue.add(processList.get(nextProcessIndex));
                nextProcessIndex++;
            }

            if (queue.isEmpty()) {
                if (nextProcessIndex < processList.size()) {
                    // Jump to next arrival time if queue is empty
                    currentTime = processList.get(nextProcessIndex).getArrivalTime();
                    continue;
                } else {
                    break; // All processes completed
                }
            }

            Process current = queue.poll();
            int processIndex = processList.indexOf(current);

            // Set start time if not set before
            if (startTime[processIndex] == -1) {
                startTime[processIndex] = currentTime;
                current.setStartTime(currentTime);
            }

            // Execute for quantum or remaining time, whichever is smaller
            int executionTime = Math.min(quantum, remainingTime[processIndex]);
            int executionStart = currentTime;
            currentTime += executionTime;
            remainingTime[processIndex] -= executionTime;

            // Record this execution segment
            ganttEntities.add(new GanttEntity(
                    current.getPid(),
                    executionStart,
                    currentTime
            ));

            // Add arriving processes during this execution
            while (nextProcessIndex < processList.size() &&
                    processList.get(nextProcessIndex).getArrivalTime() <= currentTime) {
                queue.add(processList.get(nextProcessIndex));
                nextProcessIndex++;
            }

            // Re-add to queue if not finished
            if (remainingTime[processIndex] > 0) {
                queue.add(current);
            } else {
                current.setCompletionTime(currentTime);
            }
        }

        // Build the result string in process order
        for (Process p : processList) {
            result.append(p.getPid()).append("\t")
                    .append(p.getArrivalTime()).append("\t")
                    .append(p.getBurstTime()).append("\t")
                    .append(p.getStartTime()).append("\t")
                    .append(p.getCompletionTime()).append("\n");
        }

        return new GanttData(result.toString(), ganttEntities);
    }

}
