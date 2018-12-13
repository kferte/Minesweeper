import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Random;

public class GUI extends JFrame {

    int spacing = 5;
    int neighs = 0;
    public int mx = -1;
    public int my = -1;
    Random rand = new Random();
    int[][] mines = new int[16][9];
    int[][] neighbours = new int[16][9];
    boolean[][] revealed = new boolean[16][9];
    boolean[][] flagged = new boolean[16][9];

    public GUI(){
        this.setTitle("Minesweeper");
        this.setSize(1286, 829);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);
        for(int i = 0; i < 16; i++){
            for(int j = 0; j < 9; j++){
                if(rand.nextInt(100) < 20){
                    mines[i][j] = 1;
                } else {
                    mines[i][j] = 0;
                }
                revealed[i][j] = false;
            }
        }
        for(int i = 0; i < 16; i++){
            for(int j = 0; j < 9; j++){
                neighs = 0;
                for(int m = 0; m < 16; m++){
                    for(int n = 0; n < 9; n++){
                        if(!(m == i && n == j)) {
                            if (isN(i, j, m, n) == true)
                                neighs++;
                        }
                    }
                }
                neighbours[i][j] = neighs;
            }
        }
        Board board = new Board();
        this.setContentPane(board);
        Move move = new Move();
        this.addMouseMotionListener(move);
        Click click = new Click();
        this.addMouseListener(click);
    }

    public class Board extends JPanel {
        public void paintComponent(Graphics g){
            g.setColor(Color.DARK_GRAY);
            g.fillRect(0, 0, 1280, 860);
            for(int i = 0; i < 16; i++){
                for(int j = 0; j < 9; j++){
                    g.setColor(Color.GRAY);
                    if(mines[i][j] == 1){
                        g.setColor(Color.yellow);
                    }
                    if(revealed[i][j] == true){
                        g.setColor(Color.WHITE);
                        if(mines[i][j] == 1){
                            g.setColor(Color.RED);
                        }
                    }
                    if(mx >= spacing + i * 80 && mx < i * 80 + 80 - spacing && my >= spacing + j * 80 + 80 + 26 && my < j * 80 + 26 + 80 + 80 - spacing){
                        g.setColor(Color.lightGray);
                    }
                    g.fillRect(spacing + i * 80, spacing + j * 80 + 80, 80 - 2 * spacing, 80 - 2 * spacing);
                    if(revealed[i][j] == true) {
                        g.setColor(Color.BLACK);
                        if (mines[i][j] == 0 && neighbours[i][j] != 0) {
                            if(neighbours[i][j] == 1){
                                g.setColor(Color.BLUE);
                            } else if(neighbours[i][j] == 2) {
                                g.setColor(Color.GREEN);
                            } else if(neighbours[i][j] == 3) {
                                g.setColor(Color.RED);
                            } else if(neighbours[i][j] == 4) {
                                g.setColor(new Color(0, 0, 128));
                            } else if(neighbours[i][j] == 5) {
                                g.setColor(new Color(178, 34, 34));
                            } else if(neighbours[i][j] == 6) {
                                g.setColor(new Color(72, 209, 204));
                            } else if(neighbours[i][j] == 7) {
                                g.setColor(Color.DARK_GRAY);
                            }
                            g.setFont(new Font("Tahoma", Font.BOLD, 40));
                            g.drawString(Integer.toString(neighbours[i][j]), i * 80 + 27, j * 80 + 80 + 55);
                        } else if(mines[i][j] == 1){
                            g.fillRect(i * 80 + 30, j * 80 + 100, 20, 40);
                            g.fillRect(i * 80 + 20, j * 80 + 110, 40, 20);
                            g.fillRect(i * 80 + 25, j * 80 + 105, 30, 30);
                        }
                    }
                }
            }
        }
    }

    public class Move implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent e) {

        }

        @Override
        public void mouseMoved(MouseEvent e) {
            //System.out.println("The mouse was moved");
            mx = e.getX();
            my = e.getY();
            //System.out.println(mx + " " + my);
        }
    }

    public class Click implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {

            if(inBoxX() != -1 && inBoxY() != -1){
                revealed[inBoxX()][inBoxY()] = true;
            }

            if(inBoxX() != -1 && inBoxY() != -1) {
                System.out.println("The mouse is in the [" + inBoxX() + ", " + inBoxY() + "], Number of mine neighs: " + neighbours[inBoxX()][inBoxY()]);
            } else {
                System.out.println("The pointer is not inside of any box");
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    public int inBoxX(){
        for(int i = 0; i < 16; i++){
            for(int j = 0; j < 9; j++){
                if(mx >= spacing + i * 80 && mx < i * 80 + 80 - spacing && my >= spacing + j * 80 + 80 + 26 && my < j * 80 + 26 + 80 + 80 - spacing){
                    return i;
                }
            }
        }
        return -1;
    }

    public int inBoxY(){
        for(int i = 0; i < 16; i++){
            for(int j = 0; j < 9; j++){
                if(mx >= spacing + i * 80 && mx < i * 80 + 80 - spacing && my >= spacing + j * 80 + 80 + 26 && my < j * 80 + 26 + 80 + 80 - spacing){
                    return j;
                }
            }
        }
        return -1;
    }

    public boolean isN(int mX, int mY, int cX, int cY){
        if(mX - cX < 2 && mX - cX > -2 && mY - cY < 2 && mY - cY > -2 && mines[cX][cY] == 1){
            return true;
        }
        return false;
    }
}
