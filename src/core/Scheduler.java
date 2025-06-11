package core;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {

    public static String runFCFS(List<Process> processes) {
        List<Process> sorted = new ArrayList<>(processes);
        sorted.sort((p1, p2) -> Integer.compare(p1.getArrivalTime(), p2.getArrivalTime()));

        int currentTime = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("PID\tArrival\tBurst\tStart\tCompletion\n");

        for (Process p : sorted) {
            int start = Math.max(currentTime, p.getArrivalTime());
            int completion = start + p.getBurstTime();
            sb.append(p.getPid()).append("\t")
                    .append(p.getArrivalTime()).append("\t")
                    .append(p.getBurstTime()).append("\t")
                    .append(start).append("\t")
                    .append(completion).append("\n");
            currentTime = completion;
        }

        return sb.toString();
    }
}
