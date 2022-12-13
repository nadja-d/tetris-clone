/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tetris2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author User
 */
public class Board extends JPanel implements KeyListener{
    public static int STATE_GAME_PLAY = 0;
    public static int STATE_GAME_PAUSE = 1;
    public static int STATE_GAME_OVER = 2;
    
    private int state = STATE_GAME_PLAY;
    
    private static int FPS = 60;
    private static int delay = FPS / 1000;
    
    public static final int BOARD_WIDTH = 10;
    public static final int BOARD_HEIGHT = 20;
    public static final int BLOCK_SIZE = 30;
    private Timer looper;
    private Color[][] board = new Color[BOARD_HEIGHT][BOARD_WIDTH];
    
    private Random random;
    
    private Color[] colors = {Color.decode("#3cba54"), Color.decode("#f4c20d"), Color.decode("#db3236"),
            Color.decode("#4885ed"), Color.decode("#00a2e8"), Color.decode("#a349a4"), Color.decode("#3f48cc")};
    
    private Shape[] shapes = new Shape[7];
    private Shape currentShape;
        
    public Board() {
        random = new Random();
        
        shapes[0] = new Shape(new int[][]{
            {1, 1, 1, 1} // I shape;
        }, this, colors[0]);
        
        shapes[1] = new Shape(new int[][]{
            {1,1,1},
            {0,1,0}, // T shape;
        }, this, colors[1]);
        
        shapes[2] = new Shape(new int[][]{
            {1,1,1},
            {1,0,0}, // L shape;
        }, this, colors[2]);
        
        shapes[3] = new Shape(new int[][]{
            {1,1,1},
            {0,0,1}, // J shape;
        }, this, colors[3]);
        
        shapes[4] = new Shape(new int[][]{
            {0,1,1},
            {1,1,0}, // S shape;
        }, this, colors[4]);
        
        shapes[5] = new Shape(new int[][]{
            {1,1,0},
            {0,1,1}, // Z shape;
        }, this, colors[5]);
        
        shapes[6] = new Shape(new int[][]{
            {1,1},
            {1,1}, // O shape;
        }, this, colors[6]);
        
        currentShape = shapes[0];
        
        // insert timer as loop, every 200 seconds
        looper = new Timer(delay, new ActionListener(){
            int n=0;
            // for every 500seconds, public void actionperformed
            @Override
            public void actionPerformed(ActionEvent e) {
                update();
                repaint();
            }
            
        });
        looper.start();
    }
    
    private void update() {
        if(state == STATE_GAME_PLAY) {
            currentShape.update();
        }
    }
    
    public void setCurrentShape() {
        currentShape = shapes[random.nextInt(shapes.length)];
        currentShape.reset();
        checkOverGame();
    }
    
    private void checkOverGame() {
        int[][] coords = currentShape.getCoords();
         for(int row = 0; row < coords.length; row++) {
             for(int col = 0; row < coords.length; row++) {
                 if(coords[row][col] != 0) {
                     if(board[row + currentShape.getY()][col + currentShape.getX()] != null) {
                         System.out.println("Nice try!");
                         state = STATE_GAME_OVER;
                     }
                 }
             }
         }        
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        g.setColor(Color.black);
        g.fillRect(0,0, getWidth(), getHeight());
        
        currentShape.render(g);
        
        for(int row = 0; row < board.length; row++) {
            for(int col = 0; col < board[row].length; col++) {
                if (board[row][col] != null) {
                    g.setColor(board[row][col]);
                    g.fillRect(col * BLOCK_SIZE, row * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                }
            }
        }
        
        // draw the white rows of the board
        g.setColor(Color.decode("#FAF9F6"));
        for(int row = 0; row<BOARD_HEIGHT; row++) {
            g.drawLine(0, BLOCK_SIZE * row, BLOCK_SIZE * BOARD_WIDTH, BLOCK_SIZE * row);
        }
        
        // draw the white columns of the board
        g.setColor(Color.decode("#FAF9F6"));
        for(int col = 0; col<BOARD_WIDTH; col++) {
            g.drawLine(col * BLOCK_SIZE, 0, col * BLOCK_SIZE, BLOCK_SIZE * BOARD_HEIGHT);
        }
        
        if(state == STATE_GAME_OVER) {
            g.setColor(Color.white);
            g.drawString("[ GAME OVER ]", 317, 100);
        }
        
        if(state == STATE_GAME_PAUSE) {
            g.setColor(Color.white);
            g.drawString("[ GAME PAUSE ]", 319, 100);
        }
        
        g.fillRect(325,205,80,2);
        g.drawString("TETRIS JAVA", 325, 200);
        g.setColor(Color.white);
        g.drawString("SCOREBOARD:", 320, 230);
        g.drawString("  NJD 2m25s", 327, 245);
//        g.drawString("  XXX xmxxs", 327, 260);
    }

    
    public Color[][] getBoard() {
        return board;
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            currentShape.speedUp();
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            currentShape.moveRight();
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            currentShape.moveLeft();
        }
        else if (e.getKeyCode() == KeyEvent.VK_UP) {
            currentShape.rotateShape();
        }
        
        // clean the board
        if(state == STATE_GAME_OVER) {
            if(e.getKeyCode() == KeyEvent.VK_SPACE) {
                for(int row = 0; row < board.length; row++) {
                    for(int col = 0; col < board[row].length; col++) {
                        board[row][col] = null;
                    }
                }
                setCurrentShape();
                state = STATE_GAME_PLAY;
            }
        }

        // pause 
        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            if(state == STATE_GAME_PLAY) {
                state = STATE_GAME_PAUSE;
            }
            else if(state == STATE_GAME_PAUSE) {
                state = STATE_GAME_PLAY;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            currentShape.speedDown();
        }
    }
}
