package ui;

import core.Process;
import core.Scheduler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter valid numbers.");
                }
            }
        });

        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String result = Scheduler.runFCFS(processList);
                outputArea.append("\nFCFS Scheduling Result:\n" + result + "\n");
                ganttChartPanel.repaint();
            }
        });
    }
}
