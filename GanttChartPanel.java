
package osvision;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GanttChartPanel extends JPanel {

    private final List<String> processNames;
    private final List<Integer> startTimes;
    private final List<Integer> endTimes;

    private final Color[] colors = {
        new Color(52, 120, 190),
        new Color(35, 145, 110),
        new Color(205, 135, 45),
        new Color(145, 90, 180),
        new Color(210, 90, 100)
    };

    public GanttChartPanel(
            List<String> processNames,
            List<Integer> startTimes,
            List<Integer> endTimes) {

        this.processNames = processNames;
        this.startTimes = startTimes;
        this.endTimes = endTimes;

        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(700, 150));
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        if (processNames.isEmpty()) {
            g.drawString("Run an algorithm to view the Gantt chart.",
                    20, 40);
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();

        int left = 30;
        int top = 35;
        int chartWidth = getWidth() - 60;
        int blockHeight = 45;

        int firstTime = startTimes.get(0);
        int lastTime = endTimes.get(endTimes.size() - 1);
        int totalTime = lastTime - firstTime;

        if (totalTime <= 0) {
            g2.dispose();
            return;
        }

        g2.setFont(new Font("Arial", Font.BOLD, 13));
        g2.setColor(Color.DARK_GRAY);
        g2.drawString("Gantt Chart", left, 20);

        for (int i = 0; i < processNames.size(); i++) {

            int start = startTimes.get(i);
            int end = endTimes.get(i);

            int x = left + (int) (
                    (start - firstTime) * chartWidth / (double) totalTime);

            int width = (int) (
                    (end - start) * chartWidth / (double) totalTime);

            width = Math.max(width, 2);

            if (processNames.get(i).equals("IDLE")) {
                g2.setColor(new Color(225, 225, 225));
            } else {
                g2.setColor(colors[i % colors.length]);
            }

            g2.fillRoundRect(x, top, width, blockHeight, 8, 8);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 12));

            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(processNames.get(i));

            if (width > textWidth + 8) {
                g2.drawString(
                        processNames.get(i),
                        x + (width - textWidth) / 2,
                        top + 27
                );
            }

            g2.setColor(Color.DARK_GRAY);
            g2.drawString(String.valueOf(start), x, top + blockHeight + 20);
        }

        int finalX = left + chartWidth;
        g2.drawString(String.valueOf(lastTime),
                finalX - 5, top + blockHeight + 20);

        g2.dispose();
    }
}
