package org.bot.functional;

import javassist.bytecode.ByteArray;
import org.bot.database.ConstantDB;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ExpenseChart {

    public byte[] createChart(ArrayList<Float> all_amounts) {

        BufferedImage bufferedImage = new BufferedImage(400, 400, BufferedImage.TYPE_INT_RGB);
        Graphics g = bufferedImage.getGraphics();

        drawChart(g, all_amounts);

        try {
            var baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", baos);
            byte[] data = baos.toByteArray();
            return data;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void drawChart(Graphics g, ArrayList<Float> all_amounts) {

        int total = 0;
        for (float expense : all_amounts) {
            total += expense;
        }

        int startAngle = 0;

        for (int i = 0; i < all_amounts.size(); i++) {
            int angle = (int) Math.round(360.0 * all_amounts.get(i) / total);
            g.setColor(getColor(i));
            g.fillArc(50, 50, 300, 300, startAngle, angle);
            startAngle += angle;
        }

        drawLegend(g, all_amounts);
    }

    private Color getColor(int index) {
        Color[] colors = {
                Color.RED,
                Color.BLUE,
                Color.GREEN,
                Color.YELLOW,
                Color.ORANGE,
                Color.MAGENTA,
                Color.CYAN,
                new Color(128, 0, 128), // Пурпурный
                new Color(255, 165, 0), // Оранжевый
                new Color(0, 128, 128), // Темно-бирюзовый
                new Color(255, 20, 147) // Глубокий розовый
        };

        return colors[index % colors.length];
    }

    private void drawLegend(Graphics g, ArrayList<Float> all_amounts) {
        int x = 370;
        int y = 50;
        for (int i = 0; i < ConstantDB.list_type_amounts.length; i++) {
            g.setColor(getColor(i));
            g.fillRect(x, y + (i * 20), 15, 15);
            g.setColor(Color.BLACK);
            g.drawString(ConstantDB.list_type_amounts[i] + ": " +
                    all_amounts.get(i), x + 20, y + (i * 20) + 15);
        }
    }
}
