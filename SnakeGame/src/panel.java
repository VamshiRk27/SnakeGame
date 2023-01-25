import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Random;

public class panel extends JPanel implements ActionListener {
    static int width = 1200, height = 700;//Dimensions of the window/Panel
    static int unit = 50;//size of single grid unit
    Timer timer;//timer to constantly check on the state of the food
    Random random;//Random generator for spawning snake food
    int foodX, foodY;//Coordinates of the food
    int foodEaten;//Score of the snake
    int snakeBody = 2;//Initial length of the snake body
    boolean flag = false;//flag to check the game running status
    char dir = 'R';//Initial Direction of the snake
    static int delay=150;//Delay between each move of the snake;measured in milliseconds
    static int gridSize=(width*height)/(unit*unit); //288 squares of 50 px each & total will be 1200*600
    int[] x_snake=new int[gridSize];
    int[] y_snake=new int[gridSize];
    panel(){
        this.setPreferredSize(new Dimension(width,height));//Set the size of the panel
        this.setBackground(Color.BLACK);//Sets the background color of the panel
        this.setFocusable(true);//To get the raw input from the KeyBoard
        this.addKeyListener(new MyKey());//KeyListener for passing the raw input to the snake
        random=new Random();
        Game_Start();//Function to start the game
    }
    public void Game_Start(){
        spawnFood();//Function to spawn the food
        flag=true;//Flag turns true to start the game
        timer=new Timer(delay,this);
        timer.start();
    }
    public void spawnFood(){
        foodX=random.nextInt((width/unit))*unit;// random integer between 0 - 1200 with multiple of 50
        foodY=random.nextInt((height/unit))*unit;
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        //If flag is true & Game is On/Started
        if(flag){
            //drawing food particle
            g.setColor(Color.ORANGE);
            g.fillOval(foodX,foodY,unit,unit);
            //drawing snake body
            for (int i = 0; i < snakeBody; i++) {
                if(i==0){
                    //For head of the snake
                    g.setColor(Color.RED);
                    g.fillOval(x_snake[0],y_snake[0],unit,unit);
                }
                else {
                    //For remaining body of the snake
                    g.setColor(Color.GREEN);
                    g.fillOval(x_snake[i],y_snake[i],unit,unit);
                }
            }
            //Score Display
            g.setColor(Color.CYAN);
            g.setFont(new Font("Times New Roman",Font.BOLD,40));
            FontMetrics fm=getFontMetrics(g.getFont());
            g.drawString("Score: "+foodEaten,(width-fm.stringWidth("Score: "+foodEaten))/2,g.getFont().getSize());
        }
        //If flag is false & Game is Finished/Stopped
        else {
            gameOver(g);
        }
    }
    public void gameOver(Graphics g) {
        //Score Display
        g.setColor(Color.RED);
        g.setFont(new Font("Times New Roman",Font.BOLD,40));
        FontMetrics fm=getFontMetrics(g.getFont());
        g.drawString("Score: "+foodEaten,(width-fm.stringWidth("Score: "+foodEaten))/2,g.getFont().getSize());

        //"Game Over" Display
        g.setColor(Color.RED);
        g.setFont(new Font("Times New Roman", Font.BOLD, 80));
        FontMetrics fm2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (width - fm2.stringWidth("Game Over")) / 2, height / 2);

        //"Press R to replay" Display
        g.setColor(Color.RED);
        g.setFont(new Font("Times New Roman", Font.BOLD, 40));
        FontMetrics fm3 = getFontMetrics(g.getFont());
        g.drawString("Press R to replay", (width - fm3.stringWidth("Press R to replay")) / 2, height/2-150);
    }
    public void moveSnake(){
        //Updating the body
        for(int i=snakeBody; i>0;i--){
            x_snake[i]=x_snake[i-1];
            y_snake[i]=y_snake[i-1];
        }
        //Updating the head
        switch (dir) {
            case 'R' -> x_snake[0] = x_snake[0] + unit;
            case 'L' -> x_snake[0] = x_snake[0] - unit;
            case 'U' -> y_snake[0] = y_snake[0] - unit;
            case 'D' -> y_snake[0] = y_snake[0] + unit;
        }
    }
    public void check() {
        //Check hit with its own snakeBody
        for (int i = snakeBody; i > 0; i--) {
            if (x_snake[0] == x_snake[i] && y_snake[0] == y_snake[i]) {
                flag = false;
                break;
            }
        }
        //Check hit with the walls
        if (x_snake[0] < 0) {
            flag = false;
        }
        else if (x_snake[0] > width) {
            flag = false;
        }
        else if (y_snake[0] < 0) {
            flag = false;
        }
        else if (y_snake[0] > height) {
            flag = false;
        }
        //To stop the game & go to game over screen
        if(!flag) {
            timer.stop();
        }
    }

    public void food(){
        if (x_snake[0] == foodX && y_snake[0] ==foodY) {
            snakeBody++;
            foodEaten++;
            spawnFood();
        }
    }
    public class MyKey extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP -> {
                    if (dir != 'D') {
                        dir = 'U';
                    }
                }
                case KeyEvent.VK_DOWN -> {
                    if (dir != 'U') {
                        dir = 'D';
                    }
                }
                case KeyEvent.VK_LEFT -> {
                    if (dir != 'R') {
                        dir = 'L';
                    }
                }
                case KeyEvent.VK_RIGHT -> {
                    if (dir != 'L') {
                        dir = 'R';
                    }
                }
                case KeyEvent.VK_R -> {
                    if (!flag) {
                        foodEaten = 0;
                        snakeBody = 2;
                        dir = 'R';
                        Arrays.fill(x_snake, 0);
                        Arrays.fill(y_snake, 0);
                        Game_Start();
                    }
                }
            }
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (flag){
            moveSnake();
            food();
            check();
        }
        repaint();
    }
}