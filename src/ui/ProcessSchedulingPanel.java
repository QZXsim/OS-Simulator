package ui;

import core.GanttData;
import core.Process;
import core.Scheduler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ProcessSchedulingPanel extends SimulationPanel {

    @Override
    protected void configureButtons() {
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int pid = Integer.parseInt(pidField.getText());
                    int arrival = Integer.parseInt(arrivalField.getText());
                    int burst = Integer.parseInt(burstField.getText());
                    processList.add(new Process(pid, arrival, burst));
                    outputArea.append("Added: PID=" + pid + ", Arrival=" + arrival + ", Burst=" + burst + "\n");
                    clearFields();
                    ganttChartPanel.repaint();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter valid numbers.");
                }
            }
        });

        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedAlgorithm = (String) algorithmComboBox.getSelectedItem();
                GanttData ganttData = null;

                List<Process> freshList = getFreshProcessList();

                if ("FCFS".equals(selectedAlgorithm)) {
                    ganttData = Scheduler.runFCFS(freshList);
                }
                else if ("SJF".equals(selectedAlgorithm)) {
                    ganttData = Scheduler.runSJF(freshList);
                }
                else if ("SRTF".equals(selectedAlgorithm)) {
                    ganttData = Scheduler.runSRTF(freshList);
                }
                else if ("Priority".equals(selectedAlgorithm)) {
                    ganttData = Scheduler.runPriority(freshList);
                }
                else if ("RR".equals(selectedAlgorithm)) {
                    ganttData = Scheduler.runRoundRobin(freshList);
                }

                if (ganttData != null) {
                    outputArea.append("\n" + selectedAlgorithm + " Scheduling Result:\n" + ganttData.getOutput() + "\n");
                    ganttEntities = ganttData.getGanttEntities();  // updated GanttEntity list for chart
                }

                scheduledProcessList.clear();
                scheduledProcessList.addAll(freshList);

                ganttChartPanel.repaint();
            }
        });
    }
    private List<Process> getFreshProcessList() {
        List<Process> copy = new ArrayList<>();
        for (Process p : processList) {
            // Create a new Process object with the same PID, Arrival, and Burst (fresh copy)
            copy.add(new Process(p.getPid(), p.getArrivalTime(), p.getBurstTime()));
        }
        return copy;
    }
}
