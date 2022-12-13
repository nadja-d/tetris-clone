/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tetris2;
import javax.swing.JFrame;

/**
 *
 * @author User
 */
public class WindowGame {
    // how to deploy a new frame (without jframe function)
    public static final int WIDTH = 445, HEIGHT = 629;
    
    private Board board;
    private JFrame window;
    
    public WindowGame() {
        window = new JFrame("TETRIS JAVA");
        window.setSize(WIDTH, HEIGHT);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setLocationRelativeTo(null); // No need to drag to different positions
        
        board = new Board();
        window.add(board);
        window.addKeyListener(board);
        window.setVisible(true);
    }
    
    public static void main(String[] args) {
        new WindowGame();
    }
}
