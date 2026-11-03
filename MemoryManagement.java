
package osvision;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MemoryManagement extends JFrame {

    DefaultTableModel blocks, processes, results;
    JTable blockTable, processTable, resultTable;

    Color bg = new Color(240, 243, 252);
    Color navy = new Color(45, 55, 90);

    public MemoryManagement() {

        setTitle("OS Vision - Memory Management");
        setSize(950, 680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JLabel title = new JLabel("MEMORY MANAGEMENT");
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setForeground(navy);

        JLabel subtitle = new JLabel(
                "First Fit | Best Fit | Worst Fit"
        );
        subtitle.setForeground(Color.GRAY);

        JPanel heading = new JPanel(new GridLayout(2, 1, 5, 5));
        heading.setBackground(bg);
        heading.add(title);
        heading.add(subtitle);

        // Memory blocks
        blocks = new DefaultTableModel(
                new String[]{"Block", "Size"}, 0);

        int[] b = {100, 500, 200, 300, 600};

        for (int i = 0; i < b.length; i++)
            blocks.addRow(new Object[]{"B" + (i + 1), b[i]});

        blockTable = new JTable(blocks);

        // Processes
        processes = new DefaultTableModel(
                new String[]{"Process", "Memory Required"}, 0);

        int[] p = {212, 417, 112, 426};

        for (int i = 0; i < p.length; i++)
            processes.addRow(new Object[]{"P" + (i + 1), p[i]});

        processTable = new JTable(processes);

        styleTable(blockTable);
        styleTable(processTable);

        JPanel blockCard = card("Memory Blocks", blockTable);
        JPanel processCard = card("Processes", processTable);

        JPanel inputs = new JPanel(new GridLayout(1, 2, 15, 15));
        inputs.setBackground(bg);
        inputs.add(blockCard);
        inputs.add(processCard);

        // Buttons
        JButton addB = button("Add Block", new Color(100, 130, 220));
        JButton removeB = button("Remove Block", new Color(145, 115, 205));
        JButton addP = button("Add Process", new Color(55, 150, 140));
        JButton removeP = button("Remove Process", new Color(220, 145, 75));

        JButton first = button("First Fit", new Color(100, 130, 220));
        JButton best = button("Best Fit", new Color(145, 115, 205));
        JButton worst = button("Worst Fit", new Color(55, 150, 140));

        JPanel editButtons = new JPanel();
        editButtons.setBackground(bg);
        editButtons.add(addB);
        editButtons.add(removeB);
        editButtons.add(addP);
        editButtons.add(removeP);

        JPanel algorithmButtons = new JPanel();
        algorithmButtons.setBackground(bg);
        algorithmButtons.add(first);
        algorithmButtons.add(best);
        algorithmButtons.add(worst);

        // Result table
        results = new DefaultTableModel(
                new String[]{"Process", "Required", "Block",
                        "Remaining", "Status"}, 0);

        resultTable = new JTable(results);
        styleTable(resultTable);

        JPanel resultCard = card("Allocation Results", resultTable);

        JPanel center = new JPanel();
        center.setBackground(bg);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        center.add(inputs);
        center.add(editButtons);
        center.add(algorithmButtons);
        center.add(resultCard);

        JPanel main = new JPanel(new BorderLayout(15, 15));
        main.setBackground(bg);
        main.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        main.add(heading, BorderLayout.NORTH);
        main.add(center, BorderLayout.CENTER);

        add(main);

        addB.addActionListener(e ->
                blocks.addRow(new Object[]{
                        "B" + (blocks.getRowCount() + 1), 100
                }));

        addP.addActionListener(e ->
                processes.addRow(new Object[]{
                        "P" + (processes.getRowCount() + 1), 100
                }));

        removeB.addActionListener(e ->
                removeRow(blockTable, blocks));

        removeP.addActionListener(e ->
                removeRow(processTable, processes));

        first.addActionListener(e -> allocate(1));
        best.addActionListener(e -> allocate(2));
        worst.addActionListener(e -> allocate(3));
    }

    JPanel card(String name, JTable table) {

        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(190, 205, 235), 2),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));

        JLabel label = new JLabel(name);
        label.setFont(new Font("Arial", Font.BOLD, 17));
        label.setForeground(navy);

        panel.add(label, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        return panel;
    }

    JButton button(String name, Color color) {

        JButton btn = new JButton(name);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(9, 13, 9, 13));

        return btn;
    }

    void styleTable(JTable table) {

        table.setRowHeight(28);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setGridColor(new Color(225, 228, 240));
        table.setSelectionBackground(new Color(210, 220, 250));

        table.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setBackground(
                new Color(225, 231, 248));
        table.getTableHeader().setForeground(navy);
    }

    void removeRow(JTable table, DefaultTableModel model) {

        int row = table.getSelectedRow();

        if (row >= 0)
            model.removeRow(row);
        else
            JOptionPane.showMessageDialog(this,
                    "Select a row first.");
    }

    void allocate(int choice) {

        try {

            if (blockTable.isEditing())
                blockTable.getCellEditor().stopCellEditing();

            if (processTable.isEditing())
                processTable.getCellEditor().stopCellEditing();

            int n = blocks.getRowCount();
            int m = processes.getRowCount();

            if (n == 0 || m == 0)
                throw new Exception("Add blocks and processes first.");

            int[] remaining = new int[n];

            for (int i = 0; i < n; i++) {
                remaining[i] = Integer.parseInt(
                        blocks.getValueAt(i, 1).toString());

                if (remaining[i] <= 0)
                    throw new Exception("Invalid block size.");
            }

            results.setRowCount(0);

            for (int i = 0; i < m; i++) {

                String id = processes.getValueAt(i, 0).toString();
                int size = Integer.parseInt(
                        processes.getValueAt(i, 1).toString());

                if (id.trim().isEmpty() || size <= 0)
                    throw new Exception("Invalid process details.");

                int selected = -1;

                for (int j = 0; j < n; j++) {

                    if (remaining[j] >= size) {

                        if (choice == 1) {
                            selected = j;
                            break;
                        }

                        if (selected == -1 ||
                            (choice == 2 &&
                             remaining[j] < remaining[selected]) ||
                            (choice == 3 &&
                             remaining[j] > remaining[selected])) {

                            selected = j;
                        }
                    }
                }

                if (selected == -1) {

                    results.addRow(new Object[]{
                            id, size, "-", "-", "Not Allocated"
                    });

                } else {

                    remaining[selected] -= size;

                    results.addRow(new Object[]{
                            id, size,
                            blocks.getValueAt(selected, 0),
                            remaining[selected],
                            "Allocated"
                    });
                }
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(this,
                    "Check inputs: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new MemoryManagement().setVisible(true));
    }
}
