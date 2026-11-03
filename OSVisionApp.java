
package osvision;

import javax.swing.*;
import java.awt.*;

public class OSVisionApp extends JFrame {

    Color bg = new Color(239, 243, 252);
    Color navy = new Color(45, 55, 90);
    Color blue = new Color(100, 130, 220);
    Color purple = new Color(145, 115, 205);

    public OSVisionApp() {

        setTitle("OS Vision - Operating System Simulator");
        setSize(950, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel main = new JPanel(new BorderLayout(20, 20));
        main.setBackground(bg);
        main.setBorder(BorderFactory.createEmptyBorder(25, 30, 20, 30));

        // Heading
        JLabel title = new JLabel("OS VISION");
        title.setFont(new Font("Arial", Font.BOLD, 34));
        title.setForeground(navy);

        JLabel subtitle = new JLabel(
                "Operating System Simulator | Learn by Simulation"
        );
        subtitle.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitle.setForeground(new Color(100, 105, 125));

        JPanel heading = new JPanel(new GridLayout(2, 1, 5, 5));
        heading.setBackground(bg);
        heading.add(title);
        heading.add(subtitle);

        // Welcome panel
        JPanel welcome = new JPanel(new BorderLayout());
        welcome.setBackground(new Color(222, 230, 252));
        welcome.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(200, 210, 240)),
                        BorderFactory.createEmptyBorder(18, 20, 18, 20)
                )
        );

        JLabel welcomeText = new JLabel(
                "<html><b>Welcome to OS Vision!</b><br>" +
                "Explore and understand important Operating System concepts.</html>"
        );
        welcomeText.setFont(new Font("Arial", Font.PLAIN, 17));
        welcomeText.setForeground(navy);
        welcome.add(welcomeText);

        // Module cards
        JPanel cards = new JPanel(new GridLayout(2, 2, 20, 20));
        cards.setBackground(bg);

        addCard(cards, "CPU Scheduling",
                "⚙", "FCFS, SJF and Round Robin",
                new Color(225, 232, 255),
                blue);

        addCard(cards, "Memory Management",
                "▦", "First Fit, Best Fit and Worst Fit",
                new Color(231, 224, 250),
                purple);

        addCard(cards, "Disk Scheduling",
                "◉", "FCFS, SSTF and SCAN",
                new Color(220, 240, 239),
                new Color(55, 150, 140));

        addCard(cards, "Process Management",
                "▤", "Process States and Transitions",
                new Color(255, 237, 220),
                new Color(215, 145, 75));

        // Footer
        JLabel footer = new JLabel(
                "Made with Java Swing | OS Vision Project",
                SwingConstants.CENTER
        );
        footer.setFont(new Font("Arial", Font.PLAIN, 13));
        footer.setForeground(Color.GRAY);

        JPanel top = new JPanel(new BorderLayout(15, 15));
        top.setBackground(bg);
        top.add(heading, BorderLayout.NORTH);
        top.add(welcome, BorderLayout.CENTER);

        main.add(top, BorderLayout.NORTH);
        main.add(cards, BorderLayout.CENTER);
        main.add(footer, BorderLayout.SOUTH);

        add(main);
    }

    private void addCard(
            JPanel panel,
            String name,
            String symbol,
            String description,
            Color cardColor,
            Color buttonColor) {

        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(215, 220, 235), 1),
                        BorderFactory.createEmptyBorder(15, 18, 15, 18)
                )
        );

        JLabel icon = new JLabel(symbol, SwingConstants.CENTER);
        icon.setFont(new Font("Arial", Font.BOLD, 30));
        icon.setForeground(buttonColor);
        icon.setOpaque(true);
        icon.setBackground(cardColor);
        icon.setPreferredSize(new Dimension(65, 55));

        JLabel heading = new JLabel(name);
        heading.setFont(new Font("Arial", Font.BOLD, 19));
        heading.setForeground(navy);

        JLabel detail = new JLabel(description);
        detail.setFont(new Font("Arial", Font.PLAIN, 13));
        detail.setForeground(Color.GRAY);

        JButton open = new JButton("Open Module");
        open.setFont(new Font("Arial", Font.BOLD, 14));
        open.setForeground(Color.WHITE);
        open.setBackground(buttonColor);
        open.setFocusPainted(false);
        open.setBorder(BorderFactory.createEmptyBorder(9, 15, 9, 15));
        open.setCursor(new Cursor(Cursor.HAND_CURSOR));

        open.addActionListener(e -> openModule(name));

        JPanel info = new JPanel(new GridLayout(2, 1, 4, 4));
        info.setBackground(Color.WHITE);
        info.add(heading);
        info.add(detail);

        JPanel upper = new JPanel(new BorderLayout(12, 5));
        upper.setBackground(Color.WHITE);
        upper.add(icon, BorderLayout.WEST);
        upper.add(info, BorderLayout.CENTER);

        card.add(upper, BorderLayout.CENTER);
        card.add(open, BorderLayout.SOUTH);

        panel.add(card);
    }

    private void openModule(String name) {

        if (name.equals("CPU Scheduling")) {

            String[] options = {
                    "FCFS", "SJF", "Round Robin", "Cancel"
            };

            int choice = JOptionPane.showOptionDialog(
                    this,
                    "Choose CPU Scheduling Algorithm",
                    "CPU Scheduling",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if (choice == 0) new FCFS().setVisible(true);
            else if (choice == 1) new SJF().setVisible(true);
            else if (choice == 2) new RoundRobin().setVisible(true);

        } else if (name.equals("Memory Management")) {

            new MemoryManagement().setVisible(true);

        } else if (name.equals("Disk Scheduling")) {

            new DiskScheduling().setVisible(true);

        } else if (name.equals("Process Management")) {

            new ProcessManagement().setVisible(true);
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() ->
                new OSVisionApp().setVisible(true)
        );
    }
}
