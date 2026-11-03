
package osvision;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public class SJF extends JFrame {

    DefaultTableModel inputModel, resultModel;
    JTable inputTable, resultTable;
    JPanel chartPanel;
    JLabel avgWT, avgTAT;

    Color bg = new Color(240, 243, 252);
    Color navy = new Color(45, 55, 90);
    Color blue = new Color(100, 130, 220);
    Color purple = new Color(145, 115, 205);

    class Job {
        String id;
        int at, bt, ct, tat, wt;
        boolean completed = false;

        Job(String id, int at, int bt) {
            this.id = id;
            this.at = at;
            this.bt = bt;
        }
    }

    public SJF() {

        setTitle("OS Vision - SJF Scheduling");
        setSize(1050, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JLabel title = new JLabel("SJF CPU SCHEDULING");
        title.setFont(new Font("Arial", Font.BOLD, 27));
        title.setForeground(navy);

        JLabel subtitle = new JLabel(
                "Shortest Job First | Non-Preemptive Scheduling"
        );
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);

        JPanel heading = new JPanel(new GridLayout(2, 1, 5, 5));
        heading.setBackground(bg);
        heading.add(title);
        heading.add(subtitle);

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

        JButton add = createButton("+ Add Process", blue);
        JButton remove = createButton("Remove Selected", purple);
        JButton run = createButton("Run SJF", new Color(45, 155, 125));
        JButton reset = createButton("Reset", new Color(220, 145, 75));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.setBackground(bg);
        buttons.add(add);
        buttons.add(remove);
        buttons.add(run);
        buttons.add(reset);

        resultModel = new DefaultTableModel(
                new String[]{"Process", "AT", "BT", "CT", "TAT", "WT"}, 0
        );

        resultTable = new JTable(resultModel);
        styleTable(resultTable);

        JPanel resultCard = createCard("Scheduling Results", purple);
        resultCard.add(new JScrollPane(resultTable), BorderLayout.CENTER);

        avgWT = new JLabel("Average Waiting Time: --");
        avgTAT = new JLabel("Average Turnaround Time: --");

        JPanel stats = new JPanel(new GridLayout(1, 2, 15, 10));
        stats.setBackground(bg);
        stats.add(createStatCard(avgWT,
                new Color(225, 232, 255)));
        stats.add(createStatCard(avgTAT,
                new Color(235, 225, 250)));

        chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setPreferredSize(new Dimension(900, 150));
        chartPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(
                        new Color(200, 210, 235)),
                "Gantt Chart"
        ));

        chartPanel.add(new JLabel(
                "Click Run SJF to generate the Gantt Chart",
                SwingConstants.CENTER
        ), BorderLayout.CENTER);

        JPanel center = new JPanel();
        center.setBackground(bg);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        center.add(inputCard);
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

            resultModel.setRowCount(0);

            ArrayList<String> names = new ArrayList<>();
            ArrayList<Integer> starts = new ArrayList<>();
            ArrayList<Integer> ends = new ArrayList<>();

            int time = 0;
            int completed = 0;

            double totalWT = 0;
            double totalTAT = 0;

            while (completed < jobs.size()) {

                Job shortest = null;

                for (Job j : jobs) {

                    if (!j.completed && j.at <= time) {

                        if (shortest == null || j.bt < shortest.bt) {
                            shortest = j;
                        }
                    }
                }

                if (shortest == null) {

                    int nextArrival = Integer.MAX_VALUE;

                    for (Job j : jobs) {
                        if (!j.completed && j.at < nextArrival)
                            nextArrival = j.at;
                    }

                    names.add("IDLE");
                    starts.add(time);
                    ends.add(nextArrival);

                    time = nextArrival;
                    continue;
                }

                int start = time;
                time += shortest.bt;

                shortest.ct = time;
                shortest.tat = shortest.ct - shortest.at;
                shortest.wt = shortest.tat - shortest.bt;
                shortest.completed = true;

                completed++;

                names.add(shortest.id);
                starts.add(start);
                ends.add(time);

                totalWT += shortest.wt;
                totalTAT += shortest.tat;

                resultModel.addRow(new Object[]{
                        shortest.id,
                        shortest.at,
                        shortest.bt,
                        shortest.ct,
                        shortest.tat,
                        shortest.wt
                });
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

        resultModel.setRowCount(0);

        avgWT.setText("Average Waiting Time: --");
        avgTAT.setText("Average Turnaround Time: --");

        chartPanel.removeAll();
        chartPanel.add(new JLabel(
                "Click Run SJF to generate the Gantt Chart",
                SwingConstants.CENTER
        ), BorderLayout.CENTER);

        chartPanel.revalidate();
        chartPanel.repaint();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() ->
                new SJF().setVisible(true));
    }
}
