package sgdss;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * SimpleBarChartPanel renders horizontal bar charts for data visualization.
 * Supports both light and dark themes.
 */
public class SimpleBarChartPanel extends JPanel {
    private final Map<String, Integer> values = new LinkedHashMap<>();
    private final Color[] barColors = {
        new Color(33, 90, 160), new Color(40, 160, 80), new Color(240, 160, 40),
        new Color(220, 60, 60), new Color(160, 60, 200), new Color(60, 180, 180)
    };

    public SimpleBarChartPanel(Map<String, Integer> values) {
        this.values.putAll(values);
        setPreferredSize(new Dimension(400, 260));
        setBackground(Theme.chartBg());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (values.isEmpty()) return;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        int pad = 18;
        int left = 100;
        int bottom = 35;
        int top = 15;
        int w = getWidth() - left - pad;
        int h = getHeight() - top - bottom;
        int max = values.values().stream().mapToInt(Integer::intValue).max().orElse(1);
        int n = values.size();
        int barH = Math.max(20, h / Math.max(n, 1) - 10);

        g2.setColor(Theme.isDark() ? new Color(55, 55, 75) : new Color(235, 238, 244));
        g2.fillRect(left, top, w, h);

        int y = top + 8;
        int colorIdx = 0;
        for (Map.Entry<String, Integer> e : values.entrySet()) {
            String label = e.getKey();
            int val = e.getValue();
            int bw = (int) ((w - 20) * (val / (double) Math.max(max, 1)));
            Color barColor = barColors[colorIdx % barColors.length];

            g2.setColor(barColor);
            g2.fillRoundRect(left + 8, y, Math.max(bw, 4), barH, 10, 10);
            g2.setColor(Theme.text());
            g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
            g2.drawString(label, 10, y + barH - 6);
            if (bw > 30) {
                g2.setColor(Color.WHITE);
                g2.drawString(String.valueOf(val), left + 14, y + barH - 6);
            } else {
                g2.setColor(Theme.text());
                g2.drawString(String.valueOf(val), left + 14 + bw + 4, y + barH - 6);
            }
            y += barH + 10;
            colorIdx++;
        }
        g2.dispose();
    }
}
