
package osvision;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ProcessManagement extends JFrame {

    DefaultTableModel model;
    JTable table;
    JTextArea log;

    Color bg = new Color(240, 243, 252);
    Color navy = new Color(45, 55, 90);

    public ProcessManagement() {

        setTitle("OS Vision - Process Management");
        setSize(850, 580);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JLabel title = new JLabel("PROCESS MANAGEMENT");
        title.setFont(new Font("Arial", Font.BOLD, 25));
        title.setForeground(navy);

        model = new DefaultTableModel(
                new String[]{"Process ID", "Process State"}, 0);

        model.addRow(new Object[]{"P1", "New"});
        model.addRow(new Object[]{"P2", "Ready"});
        model.addRow(new Object[]{"P3", "Running"});

        table = new JTable(model);
        table.setRowHeight(32);
        table.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton add = button("Add Process");
        JButton ready = button("Move to Ready");
        JButton start = button("Start");
        JButton block = button("Block");
        JButton wake = button("Wake Up");
        JButton terminate = button("Terminate");

        JPanel buttons = new JPanel(new GridLayout(2, 3, 10, 10));
        buttons.setBackground(bg);
        buttons.add(add);
        buttons.add(ready);
        buttons.add(start);
        buttons.add(block);
        buttons.add(wake);
        buttons.add(terminate);

        log = new JTextArea(8, 35);
        log.setEditable(false);
        log.setFont(new Font("Arial", Font.PLAIN, 14));
        log.setBackground(Color.WHITE);
        log.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel main = new JPanel(new BorderLayout(15, 15));
        main.setBackground(bg);
        main.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        main.add(title, BorderLayout.NORTH);
        main.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout(10, 10));
        bottom.setBackground(bg);
        bottom.add(buttons, BorderLayout.NORTH);
        bottom.add(new JScrollPane(log), BorderLayout.CENTER);

        main.add(bottom, BorderLayout.SOUTH);
        add(main);

        add.addActionListener(e ->
                model.addRow(new Object[]{
                        "P" + (model.getRowCount() + 1), "New"
                }));

        ready.addActionListener(e ->
                changeState("New", "Ready"));

        start.addActionListener(e ->
                changeState("Ready", "Running"));

        block.addActionListener(e ->
                changeState("Running", "Waiting"));

        wake.addActionListener(e ->
                changeState("Waiting", "Ready"));

        terminate.addActionListener(e ->
                changeState("Running", "Terminated"));
    }

    JButton button(String name) {
        JButton b = new JButton(name);
        b.setBackground(new Color(100, 130, 220));
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Arial", Font.BOLD, 13));
        b.setFocusPainted(false);
        return b;
    }

    void changeState(String oldState, String newState) {

        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a process first.");
            return;
        }

        String current = model.getValueAt(row, 1).toString();

        if (current.equals(oldState)) {

            String id = model.getValueAt(row, 0).toString();

            model.setValueAt(newState, row, 1);

            log.append(id + ": " + oldState + " -> " +
                    newState + "\n");

        } else {

            JOptionPane.showMessageDialog(this,
                    "Process must be in " + oldState + " state.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new ProcessManagement().setVisible(true));
    }
}
