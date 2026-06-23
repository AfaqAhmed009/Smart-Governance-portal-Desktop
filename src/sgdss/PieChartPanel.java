package sgdss;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * PieChartPanel renders pie/donut charts for poll results and statistics.
 */
public class PieChartPanel extends JPanel {
    private final Map<String, Integer> values;
    private final Color[] colors = {
        new Color(33, 90, 160), new Color(40, 160, 80), new Color(240, 160, 40),
        new Color(220, 60, 60), new Color(160, 60, 200), new Color(60, 180, 180),
        new Color(200, 80, 160), new Color(100, 140, 220)
    };
    private final String title;

    public PieChartPanel(Map<String, Integer> values, String title) {
        this.values = values;
        this.title = title;
        setPreferredSize(new Dimension(350, 280));
        setBackground(Theme.chartBg());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (values.isEmpty()) return;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int total = values.values().stream().mapToInt(Integer::intValue).sum();
        if (total == 0) {
            g2.setColor(Theme.textSecondary());
            g2.setFont(new Font("SansSerif", Font.ITALIC, 14));
            g2.drawString("No data available", getWidth() / 2 - 50, getHeight() / 2);
            g2.dispose();
            return;
        }

        int cx = getWidth() / 2 - 60;
        int cy = getHeight() / 2;
        int radius = Math.min(cx, cy) - 20;

        double startAngle = 0;
        int colorIdx = 0;
        int legendY = 30;

        for (Map.Entry<String, Integer> e : values.entrySet()) {
            int val = e.getValue();
            double arc = 360.0 * val / total;
            Color c = colors[colorIdx % colors.length];
            g2.setColor(c);
            g2.fillArc(cx - radius, cy - radius, radius * 2, radius * 2, (int) startAngle, (int) arc);
            g2.setColor(Theme.chartBg());
            g2.drawArc(cx - radius, cy - radius, radius * 2, radius * 2, (int) startAngle, (int) arc);
            startAngle += arc;
            colorIdx++;
        }

        // Draw donut hole
        g2.setColor(Theme.chartBg());
        g2.fillOval(cx - radius / 2, cy - radius / 2, radius, radius);

        // Draw legend
        colorIdx = 0;
        int lx = cx + radius + 20;
        int ly = cy - (values.size() * 20) / 2;
        g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
        for (Map.Entry<String, Integer> e : values.entrySet()) {
            g2.setColor(colors[colorIdx % colors.length]);
            g2.fillRect(lx, ly, 12, 12);
            g2.setColor(Theme.text());
            String label = e.getKey() + " (" + e.getValue() + ")";
            g2.drawString(label, lx + 18, ly + 11);
            ly += 22;
            colorIdx++;
        }

        // Title
        g2.setColor(Theme.text());
        g2.setFont(new Font("SansSerif", Font.BOLD, 14));
        g2.drawString(title, 15, 22);

        g2.dispose();
    }
}
