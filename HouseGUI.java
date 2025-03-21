import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.*;


abstract class HouseBase extends JPanel {
    protected BufferedImage canvas;  
    protected String description = "";  // inheritance string
    public HouseBase() {
        canvas = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
        loadImage();
    }

    protected abstract void loadImage();

    protected void drawLayer(String imagePath, int x, int y) {
        try {
            BufferedImage layer = ImageIO.read(new File(imagePath));
            Graphics g = canvas.getGraphics();
            g.drawImage(layer, x, y, null);
            g.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(canvas, 0, 0, this);
    }

    public String getDescription() {
        return description;
    }
}

// roof (random color between red and blue, but once I got 10 times red and 11th time I got blue, works but not smart)
class HouseRoof extends HouseBase {
    public HouseRoof() {
        super();
    }

    @Override
    protected void loadImage() {
        try {
            BufferedImage roof = ImageIO.read(new File("houseRoof.png"));
            Color color;
            if (new Random().nextBoolean()) {
                color = Color.RED;
                description = "This house has a red roof";
            } else {
                color = Color.BLUE;
                description = "This house has a blue roof";
            }

            for (int x = 0; x < roof.getWidth(); x++) {
                for (int y = 0; y < roof.getHeight(); y++) {
                    int pixel = roof.getRGB(x, y);
                    if ((pixel >> 24) != 0x00) {
                        roof.setRGB(x, y, color.getRGB());
                    }
                }
            }
            Graphics g = canvas.getGraphics();
            g.drawImage(roof, 100, 80, null);
            g.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

// wall
class HouseWall extends HouseRoof {
    public HouseWall() {
        super();
    }

    @Override
    protected void loadImage() {
        super.loadImage();
        drawLayer("houseWall.png", 100, 150);
        description += ", beige walls";  // combine string
    }
}

// window
class HouseWindow extends HouseWall {
    public HouseWindow() {
        super();
    }

    @Override
    protected void loadImage() {
        super.loadImage();
        drawLayer("houseWindow.png", 230, 195);
        description += ", a blue window";  // sombine string
    }
}

// door
class HouseDoor extends HouseWindow {
    public HouseDoor() {
        super();
    }

    @Override
    protected void loadImage() {
        super.loadImage();
        drawLayer("houseDoor.png", 150, 305);
        description += ", and a brown door.";  // combine string
    }
}

// GUI main
public class HouseGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("House GUI");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(520, 580);

            HouseDoor house = new HouseDoor(); 
            JLabel label = new JLabel(house.getDescription(), SwingConstants.CENTER);  // show word
            label.setFont(new Font("Arial", Font.BOLD, 14));
            
            frame.setLayout(new BorderLayout());
            frame.add(house, BorderLayout.CENTER);
            frame.add(label, BorderLayout.SOUTH);  //word at bottom
            
            frame.setVisible(true);
        });
    }
}
