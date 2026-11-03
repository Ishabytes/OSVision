
package osvision;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;

public class DiskScheduling extends JFrame {

    DefaultTableModel model;
    JTable table;
    JTextField headField, sizeField;
    JComboBox<String> direction;
    JTextArea result;

    Color bg = new Color(240, 243, 252);
    Color navy = new Color(45, 55, 90);

    public DiskScheduling() {

        setTitle("OS Vision - Disk Scheduling");
        setSize(850, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JLabel title = new JLabel("DISK SCHEDULING");
        title.setFont(new Font("Arial", Font.BOLD, 25));
        title.setForeground(navy);

        model = new DefaultTableModel(new String[]{"Request"}, 0);
        int[] values = {98, 183, 37, 122, 14, 124, 65, 67};

        for (int v : values)
            model.addRow(new Object[]{v});

        table = new JTable(model);
        table.setRowHeight(28);

        headField = new JTextField("53", 5);
        sizeField = new JTextField("200", 5);
        direction = new JComboBox<>(new String[]{"Right", "Left"});

        JPanel inputs = new JPanel();
        inputs.setBackground(bg);
        inputs.add(new JLabel("Initial Head:"));
        inputs.add(headField);
        inputs.add(new JLabel("Disk Size:"));
        inputs.add(sizeField);
        inputs.add(new JLabel("Direction:"));
        inputs.add(direction);

        JButton add = button("Add Request");
        JButton remove = button("Remove Request");
        JButton fcfs = button("FCFS");
        JButton sstf = button("SSTF");
        JButton scan = button("SCAN");

        JPanel controls = new JPanel();
        controls.setBackground(bg);
        controls.add(add);
        controls.add(remove);
        controls.add(fcfs);
        controls.add(sstf);
        controls.add(scan);

        result = new JTextArea(8, 40);
        result.setEditable(false);
        result.setFont(new Font("Monospaced", Font.BOLD, 14));
        result.setBackground(Color.WHITE);
        result.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel main = new JPanel(new BorderLayout(15, 15));
        main.setBackground(bg);
        main.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        main.add(title, BorderLayout.NORTH);
        main.add(new JScrollPane(table), BorderLayout.WEST);

        JPanel right = new JPanel(new BorderLayout(10, 10));
        right.setBackground(bg);
        right.add(inputs, BorderLayout.NORTH);
        right.add(new JScrollPane(result), BorderLayout.CENTER);
        right.add(controls, BorderLayout.SOUTH);

        main.add(right, BorderLayout.CENTER);
        add(main);

        add.addActionListener(e ->
                model.addRow(new Object[]{50}));

        remove.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) model.removeRow(row);
            else JOptionPane.showMessageDialog(this, "Select a request.");
        });

        fcfs.addActionListener(e -> run("FCFS"));
        sstf.addActionListener(e -> run("SSTF"));
        scan.addActionListener(e -> run("SCAN"));
    }

    JButton button(String name) {
        JButton b = new JButton(name);
        b.setBackground(new Color(100, 130, 220));
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Arial", Font.BOLD, 13));
        b.setFocusPainted(false);
        return b;
    }

    void run(String algorithm) {

        try {
            if (table.isEditing())
                table.getCellEditor().stopCellEditing();

            int head = Integer.parseInt(headField.getText());
            int diskSize = Integer.parseInt(sizeField.getText());

            if (diskSize <= 0 || head < 0 || head >= diskSize)
                throw new Exception("Invalid head position or disk size.");

            ArrayList<Integer> requests = new ArrayList<>();

            for (int i = 0; i < model.getRowCount(); i++) {
                int value = Integer.parseInt(
                        model.getValueAt(i, 0).toString());

                if (value < 0 || value >= diskSize)
                    throw new Exception("Request outside disk range.");

                requests.add(value);
            }

            if (requests.isEmpty())
                throw new Exception("Add at least one request.");

            ArrayList<Integer> order = new ArrayList<>(requests);
            int current = head;

            if (algorithm.equals("SSTF")) {

                order.clear();

                while (!requests.isEmpty()) {
                    int nearest = requests.get(0);

                    for (int r : requests)
                        if (Math.abs(current - r) <
                                Math.abs(current - nearest))
                            nearest = r;

                    order.add(nearest);
                    current = nearest;
                    requests.remove(Integer.valueOf(nearest));
                }

            } else if (algorithm.equals("SCAN")) {

                order.clear();
                Collections.sort(requests);

                ArrayList<Integer> left = new ArrayList<>();
                ArrayList<Integer> right = new ArrayList<>();

                for (int r : requests) {
                    if (r < head) left.add(r);
                    else right.add(r);
                }

                if (direction.getSelectedItem().equals("Right")) {
                    order.addAll(right);
                    order.add(diskSize - 1);
                    Collections.reverse(left);
                    order.addAll(left);
                } else {
                    Collections.reverse(left);
                    order.addAll(left);
                    order.add(0);
                    order.addAll(right);
                }
            }

            int total = 0;
            current = head;
            StringBuilder sequence = new StringBuilder(head + "");

            for (int r : order) {
                total += Math.abs(current - r);
                current = r;
                sequence.append(" -> ").append(r);
            }

            result.setText(
                    "Algorithm: " + algorithm +
                    "\n\nSeek Sequence:\n" + sequence +
                    "\n\nTotal Head Movement: " + total +
                    "\nAverage Movement: " +
                    String.format("%.2f", total / (double) order.size())
            );

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Check inputs: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new DiskScheduling().setVisible(true));
    }
}
