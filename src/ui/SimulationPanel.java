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
    protected JComboBox<String> algorithmComboBox;
    protected List<Process> processList;           // User-added processes
    protected List<Process> scheduledProcessList;  // Final scheduled result after Run
    protected List<GanttEntity> ganttEntities = new ArrayList<>();
    protected JPanel ganttChartPanel;

    public SimulationPanel() {
        processList = new ArrayList<>();
        scheduledProcessList = new ArrayList<>();
        setLayout(new BorderLayout());

        // Top Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));

        inputPanel.add(new JLabel("PID:"));
        pidField = new JTextField();
        inputPanel.add(pidField);

        inputPanel.add(new JLabel("Arrival Time:"));
        arrivalField = new JTextField();
        inputPanel.add(arrivalField);

        inputPanel.add(new JLabel("Burst Time:"));
        burstField = new JTextField();
        inputPanel.add(burstField);

        algorithmComboBox = new JComboBox<>(new String[]{"FCFS", "SJF", "SRTF", "Priority", "RR"});
        inputPanel.add(new JLabel("Algorithm:"));
        inputPanel.add(algorithmComboBox);

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
                drawGanttChart(g, ganttEntities);
            }
        };
        ganttChartPanel.setPreferredSize(new Dimension(400, 100));
        ganttChartPanel.setBackground(Color.WHITE);
        add(ganttChartPanel, BorderLayout.SOUTH);

        configureButtons();
    }

    protected abstract void configureButtons();

    protected void drawGanttChart(Graphics g, List<GanttEntity> entities) {
        if (entities == null || entities.isEmpty()) return;

        int x = 10, y = 20, scale = 30;

        for (GanttEntity e : entities) {
            int width = (e.getEndTime() - e.getStartTime()) * scale;

            g.setColor(Color.CYAN);
            g.fillRect(x, y, width, 30);
            g.setColor(Color.BLACK);
            g.drawRect(x, y, width, 30);
            g.drawString("P" + e.getPid(), x + width / 2 - 10, y + 20);
            g.drawString("" + e.getStartTime(), x, y + 45);

            x += width;
        }

        if (!entities.isEmpty()) {
            GanttEntity last = entities.get(entities.size() - 1);
            g.drawString("" + last.getEndTime(), x, y + 45);
        }
    }


    protected void clearFields() {
        pidField.setText("");
        arrivalField.setText("");
        burstField.setText("");
    }
}
