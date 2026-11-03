
package osvision;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public class FCFS extends JFrame {

    DefaultTableModel inputModel, resultModel;
    JTable inputTable, resultTable;
    JPanel chartPanel, statsPanel;
    JLabel avgWT, avgTAT;

    Color bg = new Color(240, 243, 252);
    Color navy = new Color(45, 55, 90);
    Color blue = new Color(100, 130, 220);
    Color purple = new Color(145, 115, 205);

    class Job {
        String id;
        int at, bt, ct, tat, wt;

        Job(String id, int at, int bt) {
            this.id = id;
            this.at = at;
            this.bt = bt;
        }
    }

    public FCFS() {

        setTitle("OS Vision - FCFS Scheduling");
        setSize(1050, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(bg);

        JLabel title = new JLabel("FCFS CPU SCHEDULING");
        title.setFont(new Font("Arial", Font.BOLD, 27));
        title.setForeground(navy);

        JLabel subtitle = new JLabel(
                "First Come First Serve | Non-Preemptive Scheduling"
        );
        subtitle.setForeground(Color.GRAY);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));

        JPanel heading = new JPanel(new GridLayout(2, 1, 5, 5));
        heading.setBackground(bg);
        heading.add(title);
        heading.add(subtitle);

        // Input table
        inputModel = new DefaultTableModel(
                new String[]{"Process ID", "Arrival Time", "Burst Time"}, 0
        );

        inputModel.addRow(new Object[]{"P1", 0, 4});
        inputModel.addRow(new Object[]{"P2", 2, 3});
        inputModel.addRow(new Object[]{"P3", 4, 2});
        inputModel.addRow(new Object[]{"P4", 1, 5});

        inputTable = new JTable(inputModel);
        styleTable(inputTable);

        JPanel inputCard = createCard("Process Input", blue);
        inputCard.add(new JScrollPane(inputTable), BorderLayout.CENTER);

        JButton add = createButton(" + Add Process", blue);
        JButton remove = createButton("Remove Selected", purple);
        JButton run = createButton("Run FCFS", new Color(45, 155, 125));
        JButton reset = createButton("Reset", new Color(220, 145, 75));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.setBackground(bg);
        buttons.add(add);
        buttons.add(remove);
        buttons.add(run);
        buttons.add(reset);

        // Result table
        resultModel = new DefaultTableModel(
                new String[]{"Process", "AT", "BT", "CT", "TAT", "WT"}, 0
        );

        resultTable = new JTable(resultModel);
        styleTable(resultTable);

        JPanel resultCard = createCard("Scheduling Results", purple);
        resultCard.add(new JScrollPane(resultTable), BorderLayout.CENTER);

        // Statistics
        avgWT = new JLabel("Average Waiting Time: --");
        avgTAT = new JLabel("Average Turnaround Time: --");

        statsPanel = new JPanel(new GridLayout(1, 2, 15, 10));
        statsPanel.setBackground(bg);

        statsPanel.add(createStatCard(avgWT, new Color(225, 232, 255)));
        statsPanel.add(createStatCard(avgTAT, new Color(235, 225, 250)));

        // Gantt chart
        chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setPreferredSize(new Dimension(900, 150));
        chartPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 210, 235)),
                "Gantt Chart"
        ));

        JLabel message = new JLabel(
                "Click Run FCFS to generate the Gantt Chart",
                SwingConstants.CENTER
        );
        chartPanel.add(message, BorderLayout.CENTER);

        JPanel center = new JPanel();
        center.setBackground(bg);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        center.add(inputCard);
        center.add(Box.createVerticalStrut(8));
        center.add(buttons);
        center.add(Box.createVerticalStrut(8));
        center.add(resultCard);
        center.add(Box.createVerticalStrut(10));
        center.add(statsPanel);
        center.add(Box.createVerticalStrut(10));
        center.add(chartPanel);

        add(heading, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);

        JPanel main = new JPanel(new BorderLayout(15, 15));
        main.setBackground(bg);
        main.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
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

        reset.addActionListener(e -> resetData());

        run.addActionListener(e -> calculate());
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
        button.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
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
                new Font("Arial", Font.BOLD, 14)
        );
        table.getTableHeader().setBackground(
                new Color(225, 231, 248)
        );
        table.getTableHeader().setForeground(navy);
    }

    private void calculate() {

        try {

            if (inputTable.isEditing())
                inputTable.getCellEditor().stopCellEditing();

            ArrayList<Job> jobs = new ArrayList<>();

            for (int i = 0; i < inputModel.getRowCount(); i++) {

                String id = inputModel.getValueAt(i, 0).toString().trim();
                int at = Integer.parseInt(
                        inputModel.getValueAt(i, 1).toString()
                );
                int bt = Integer.parseInt(
                        inputModel.getValueAt(i, 2).toString()
                );

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

            jobs.sort(Comparator.comparingInt(j -> j.at));

            resultModel.setRowCount(0);

            ArrayList<String> names = new ArrayList<>();
            ArrayList<Integer> starts = new ArrayList<>();
            ArrayList<Integer> ends = new ArrayList<>();

            int time = 0;
            double totalWT = 0;
            double totalTAT = 0;

            for (Job j : jobs) {

                if (time < j.at) {

                    names.add("IDLE");
                    starts.add(time);
                    ends.add(j.at);
                    time = j.at;
                }

                int start = time;
                time += j.bt;

                j.ct = time;
                j.tat = j.ct - j.at;
                j.wt = j.tat - j.bt;

                names.add(j.id);
                starts.add(start);
                ends.add(time);

                totalWT += j.wt;
                totalTAT += j.tat;

                resultModel.addRow(new Object[]{
                        j.id, j.at, j.bt, j.ct, j.tat, j.wt
                });
            }

            avgWT.setText(String.format(
                    "Average Waiting Time: %.2f",
                    totalWT / jobs.size()
            ));

            avgTAT.setText(String.format(
                    "Average Turnaround Time: %.2f",
                    totalTAT / jobs.size()
            ));

            chartPanel.removeAll();
            chartPanel.add(new GanttChartPanel(names, starts, ends),
                    BorderLayout.CENTER);

            chartPanel.revalidate();
            chartPanel.repaint();

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(this,
                    "Please check inputs: " + ex.getMessage());
        }
    }

    private void resetData() {

        inputModel.setRowCount(0);
        inputModel.addRow(new Object[]{"P1", 0, 4});
        inputModel.addRow(new Object[]{"P2", 2, 3});
        inputModel.addRow(new Object[]{"P3", 4, 2});
        inputModel.addRow(new Object[]{"P4", 1, 5});

        resultModel.setRowCount(0);
        avgWT.setText("Average Waiting Time: --");
        avgTAT.setText("Average Turnaround Time: --");

        chartPanel.removeAll();
        chartPanel.add(new JLabel(
                "Click Run FCFS to generate the Gantt Chart",
                SwingConstants.CENTER
        ), BorderLayout.CENTER);

        chartPanel.revalidate();
        chartPanel.repaint();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() ->
                new FCFS().setVisible(true)
        );
    }
}
