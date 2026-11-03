
package osvision;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class RoundRobin extends JFrame {

    DefaultTableModel inputModel, resultModel;
    JTable inputTable, resultTable;
    JTextField quantumField;
    JLabel avgWT, avgTAT;
    JPanel chartPanel;

    Color bg = new Color(240, 243, 252);
    Color navy = new Color(45, 55, 90);
    Color blue = new Color(100, 130, 220);
    Color purple = new Color(145, 115, 205);

    class Job {
        String id;
        int at, bt, remaining, ct, tat, wt;

        Job(String id, int at, int bt) {
            this.id = id;
            this.at = at;
            this.bt = bt;
            remaining = bt;
        }
    }

    public RoundRobin() {

        setTitle("OS Vision - Round Robin Scheduling");
        setSize(1050, 780);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JLabel title = new JLabel("ROUND ROBIN SCHEDULING");
        title.setFont(new Font("Arial", Font.BOLD, 27));
        title.setForeground(navy);

        JLabel subtitle = new JLabel(
                "Preemptive CPU Scheduling | Time Sharing Algorithm"
        );
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);

        JPanel heading = new JPanel(new GridLayout(2, 1, 5, 5));
        heading.setBackground(bg);
        heading.add(title);
        heading.add(subtitle);

        // Process input
        inputModel = new DefaultTableModel(
                new String[]{"Process ID", "Arrival Time", "Burst Time"}, 0
        );

        inputModel.addRow(new Object[]{"P1", 0, 5});
        inputModel.addRow(new Object[]{"P2", 1, 3});
        inputModel.addRow(new Object[]{"P3", 2, 2});
        inputModel.addRow(new Object[]{"P4", 3, 1});

        inputTable = new JTable(inputModel);
        styleTable(inputTable);

        JPanel inputCard = createCard("Process Input", blue);
        inputCard.add(new JScrollPane(inputTable), BorderLayout.CENTER);

        // Quantum section
        quantumField = new JTextField("2", 5);

        JPanel quantumPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        quantumPanel.setBackground(new Color(231, 224, 250));
        quantumPanel.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        JLabel quantumLabel = new JLabel("Time Quantum:");
        quantumLabel.setFont(new Font("Arial", Font.BOLD, 15));
        quantumLabel.setForeground(navy);

        quantumPanel.add(quantumLabel);
        quantumPanel.add(quantumField);

        // Buttons
        JButton add = createButton("+ Add Process", blue);
        JButton remove = createButton("Remove Selected", purple);
        JButton run = createButton("Run Round Robin",
                new Color(45, 155, 125));
        JButton reset = createButton("Reset",
                new Color(220, 145, 75));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.setBackground(bg);
        buttons.add(add);
        buttons.add(remove);
        buttons.add(run);
        buttons.add(reset);

        // Results
        resultModel = new DefaultTableModel(
                new String[]{"Process", "AT", "BT", "CT", "TAT", "WT"}, 0
        );

        resultTable = new JTable(resultModel);
        styleTable(resultTable);

        JPanel resultCard = createCard("Scheduling Results", purple);
        resultCard.add(new JScrollPane(resultTable), BorderLayout.CENTER);

        // Average cards
        avgWT = new JLabel("Average Waiting Time: --");
        avgTAT = new JLabel("Average Turnaround Time: --");

        JPanel stats = new JPanel(new GridLayout(1, 2, 15, 10));
        stats.setBackground(bg);
        stats.add(createStatCard(avgWT,
                new Color(225, 232, 255)));
        stats.add(createStatCard(avgTAT,
                new Color(235, 225, 250)));

        // Gantt chart
        chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setPreferredSize(new Dimension(900, 160));

        chartPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(
                        new Color(200, 210, 235)),
                "Gantt Chart"
        ));

        chartPanel.add(new JLabel(
                "Click Run Round Robin to generate the Gantt Chart",
                SwingConstants.CENTER
        ), BorderLayout.CENTER);

        JPanel center = new JPanel();
        center.setBackground(bg);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        center.add(inputCard);
        center.add(quantumPanel);
        center.add(buttons);
        center.add(resultCard);
        center.add(stats);
        center.add(chartPanel);

        JPanel main = new JPanel(new BorderLayout(15, 15));
        main.setBackground(bg);
        main.setBorder(BorderFactory.createEmptyBorder(
                20, 25, 20, 25));

        main.add(heading, BorderLayout.NORTH);
        main.add(center, BorderLayout.CENTER);

        add(main);

        add.addActionListener(e ->
                inputModel.addRow(new Object[]{
                        "P" + (inputModel.getRowCount() + 1), 0, 1
                })
        );

        remove.addActionListener(e -> {
            int row = inputTable.getSelectedRow();

            if (row >= 0)
                inputModel.removeRow(row);
            else
                JOptionPane.showMessageDialog(this,
                        "Select a process first.");
        });

        run.addActionListener(e -> calculate());
        reset.addActionListener(e -> resetData());
    }

    private JPanel createCard(String name, Color color) {

        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(Color.WHITE);

        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 2),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));

        JLabel label = new JLabel(name);
        label.setFont(new Font("Arial", Font.BOLD, 17));
        label.setForeground(navy);

        panel.add(label, BorderLayout.NORTH);
        panel.setPreferredSize(new Dimension(900, 170));

        return panel;
    }

    private JPanel createStatCard(JLabel label, Color color) {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(color);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 10, 12, 10));

        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(navy);

        panel.add(label);
        return panel;
    }

    private JButton createButton(String name, Color color) {

        JButton button = new JButton(name);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(
                10, 16, 10, 16));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    private void styleTable(JTable table) {

        table.setRowHeight(28);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setGridColor(new Color(225, 228, 240));
        table.setSelectionBackground(new Color(210, 220, 250));
        table.setSelectionForeground(navy);

        table.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setBackground(
                new Color(225, 231, 248));
        table.getTableHeader().setForeground(navy);
    }

    private void calculate() {

        try {

            if (inputTable.isEditing())
                inputTable.getCellEditor().stopCellEditing();

            int quantum = Integer.parseInt(
                    quantumField.getText().trim());

            if (quantum <= 0)
                throw new Exception("Time quantum must be positive.");

            ArrayList<Job> jobs = new ArrayList<>();

            for (int i = 0; i < inputModel.getRowCount(); i++) {

                String id = inputModel.getValueAt(i, 0)
                        .toString().trim();

                int at = Integer.parseInt(
                        inputModel.getValueAt(i, 1).toString());

                int bt = Integer.parseInt(
                        inputModel.getValueAt(i, 2).toString());

                if (id.isEmpty() || at < 0 || bt <= 0)
                    throw new Exception("Enter valid process details.");

                for (Job j : jobs) {
                    if (j.id.equalsIgnoreCase(id))
                        throw new Exception("Duplicate Process ID: " + id);
                }

                jobs.add(new Job(id, at, bt));
            }

            if (jobs.isEmpty())
                throw new Exception("Add at least one process.");

            jobs.sort((a, b) -> Integer.compare(a.at, b.at));

            Queue<Job> queue = new LinkedList<>();

            ArrayList<String> names = new ArrayList<>();
            ArrayList<Integer> starts = new ArrayList<>();
            ArrayList<Integer> ends = new ArrayList<>();

            resultModel.setRowCount(0);

            int time = 0;
            int next = 0;
            int completed = 0;

            double totalWT = 0;
            double totalTAT = 0;

            while (completed < jobs.size()) {

                if (queue.isEmpty()) {

                    if (next < jobs.size() &&
                            time < jobs.get(next).at) {

                        names.add("IDLE");
                        starts.add(time);
                        ends.add(jobs.get(next).at);

                        time = jobs.get(next).at;
                    }

                    while (next < jobs.size() &&
                            jobs.get(next).at <= time) {
                        queue.add(jobs.get(next));
                        next++;
                    }
                }

                Job current = queue.remove();

                int start = time;
                int runTime = Math.min(
                        quantum, current.remaining);

                time += runTime;
                current.remaining -= runTime;

                names.add(current.id);
                starts.add(start);
                ends.add(time);

                // Add newly arrived processes to the queue
                while (next < jobs.size() &&
                        jobs.get(next).at <= time) {
                    queue.add(jobs.get(next));
                    next++;
                }

                if (current.remaining > 0) {

                    queue.add(current);

                } else {

                    current.ct = time;
                    current.tat = current.ct - current.at;
                    current.wt = current.tat - current.bt;

                    completed++;

                    totalWT += current.wt;
                    totalTAT += current.tat;

                    resultModel.addRow(new Object[]{
                            current.id,
                            current.at,
                            current.bt,
                            current.ct,
                            current.tat,
                            current.wt
                    });
                }
            }

            avgWT.setText(String.format(
                    "Average Waiting Time: %.2f",
                    totalWT / jobs.size()));

            avgTAT.setText(String.format(
                    "Average Turnaround Time: %.2f",
                    totalTAT / jobs.size()));

            chartPanel.removeAll();

            chartPanel.add(new GanttChartPanel(
                    names, starts, ends), BorderLayout.CENTER);

            chartPanel.revalidate();
            chartPanel.repaint();

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(this,
                    "Please check inputs: " + ex.getMessage());
        }
    }

    private void resetData() {

        inputModel.setRowCount(0);

        inputModel.addRow(new Object[]{"P1", 0, 5});
        inputModel.addRow(new Object[]{"P2", 1, 3});
        inputModel.addRow(new Object[]{"P3", 2, 2});
        inputModel.addRow(new Object[]{"P4", 3, 1});

        quantumField.setText("2");

        resultModel.setRowCount(0);

        avgWT.setText("Average Waiting Time: --");
        avgTAT.setText("Average Turnaround Time: --");

        chartPanel.removeAll();

        chartPanel.add(new JLabel(
                "Click Run Round Robin to generate the Gantt Chart",
                SwingConstants.CENTER
        ), BorderLayout.CENTER);

        chartPanel.revalidate();
        chartPanel.repaint();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() ->
                new RoundRobin().setVisible(true));
    }
}
