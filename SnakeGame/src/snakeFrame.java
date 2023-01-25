import javax.swing.*;
public class snakeFrame extends JFrame {
    snakeFrame() {
        //Initialize the Panel
        this.add(new panel());
        this.setTitle("Snake Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();//It will take preferred size of the window/panel; Here the dimensions are set in panel()
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
