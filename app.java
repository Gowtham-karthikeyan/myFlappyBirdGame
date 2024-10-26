import javax.swing.*;

public class app{

    public static void main(String[] args) throws Exception {
       
        int boardWidth = 360;
        int boardHeight = 640;

        JFrame frame = new JFrame("flappy bird");

        //frame.setVisible(true);
        frame.setSize(boardWidth,boardHeight);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        flappybird flappybird = new flappybird();
        frame.add(flappybird);
        frame.pack();
        flappybird.requestFocus();
        frame.setVisible(true);
    }
}