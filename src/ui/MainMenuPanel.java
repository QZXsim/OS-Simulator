package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuPanel extends JPanel {

    public MainMenuPanel(MainFrame frame) {
        setLayout(new GridLayout(5, 1, 10, 10));

        JLabel titleLabel = new JLabel("OS Simulator Main Menu", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel);

        JButton processSchedulingButton = new JButton("Process Scheduling");
        JButton memoryManagementButton = new JButton("Memory Management (Coming Soon)");
        JButton diskSchedulingButton = new JButton("Disk Scheduling (Coming Soon)");
        JButton exitButton = new JButton("Exit");

        add(processSchedulingButton);
        add(memoryManagementButton);
        add(diskSchedulingButton);
        add(exitButton);

        processSchedulingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setContentPane(new ProcessSchedulingPanel());
                frame.revalidate();
            }
        });

        memoryManagementButton.addActionListener(e ->
                JOptionPane.showMessageDialog(frame, "Memory Management Simulation coming soon!")
        );

        diskSchedulingButton.addActionListener(e ->
                JOptionPane.showMessageDialog(frame, "Disk Scheduling Simulation coming soon!")
        );

        exitButton.addActionListener(e -> System.exit(0));
    }
}
