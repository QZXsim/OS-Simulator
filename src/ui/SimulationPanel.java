package ui;

import core.Process;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class SimulationPanel extends JPanel {
    protected JTextField pidField, arrivalField, burstField;
    protected JTextArea outputArea;
    protected JButton addButton, runButton;
    protected List<Process> processList;
    protected JPanel ganttChartPanel;

    public SimulationPanel() {
        processList = new ArrayList<>();
        setLayout(new BorderLayout());

        // Top Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        inputPanel.add(new JLabel("PID:"));
        pidField = new JTextField();
        inputPanel.add(pidField);

        inputPanel.add(new JLabel("Arrival Time:"));
        arrivalField = new JTextField();
        inputPanel.add(arrivalField);

        inputPanel.add(new JLabel("Burst Time:"));
        burstField = new JTextField();
        inputPanel.add(burstField);

        addButton = new JButton("Add Process");
        inputPanel.add(addButton);

        runButton = new JButton("Run Algorithm");
        inputPanel.add(runButton);

        add(inputPanel, BorderLayout.NORTH);

        // Center Output Area
        outputArea = new JTextArea(10, 40);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom Gantt Chart Panel
        ganttChartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawGanttChart(g, processList);
            }
        };
        ganttChartPanel.setPreferredSize(new Dimension(400, 100));
        ganttChartPanel.setBackground(Color.WHITE);
        add(ganttChartPanel, BorderLayout.SOUTH);

        configureButtons();
    }

    protected abstract void configureButtons();

    protected void drawGanttChart(Graphics g, List<Process> processList) {
        if (processList == null || processList.isEmpty()) return;

        List<Process> sortedList = getSortedByArrival(processList);
        int x = 10, y = 20, scale = 30;
        int currentTime = 0;

        for (Process p : sortedList) {
            int startTime = Math.max(currentTime, p.getArrivalTime());
            int width = p.getBurstTime() * scale;

            g.setColor(Color.CYAN);
            g.fillRect(x, y, width, 30);
            g.setColor(Color.BLACK);
            g.drawRect(x, y, width, 30);
            g.drawString("P" + p.getPid(), x + width / 2 - 10, y + 20);
            g.drawString("" + startTime, x, y + 45);

            x += width;
            currentTime = startTime + p.getBurstTime();
        }
        g.drawString("" + currentTime, x, y + 45);
    }

    protected List<Process> getSortedByArrival(List<Process> list) {
        List<Process> sorted = new ArrayList<>(list);
        sorted.sort((p1, p2) -> Integer.compare(p1.getArrivalTime(), p2.getArrivalTime()));
        return sorted;
    }

    protected void clearFields() {
        pidField.setText("");
        arrivalField.setText("");
        burstField.setText("");
    }
}
