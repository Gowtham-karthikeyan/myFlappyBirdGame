import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

import javax.management.StringValueExp;
import javax.swing.*;
import javax.swing.text.PlainView;


public class flappybird extends JPanel implements ActionListener, KeyListener{
    int boardWidth = 360;
    int boardHeight = 640;

     //image
     Image backroundImg;
     Image birdImg;
     Image topPipeImg;
     Image botttomPipeImg;

     //bird
     int birdX = boardWidth/8;
     int birdY = boardHeight/2;
     int birdwidth = 34;
     int birdheight =24;

    //pipes
    int pipeX = boardWidth;
    int pipeY = 0;
    int pipewidth = 64;
    int pipeheight = 512;

    class pipe {
        int x = pipeX;
        int y = pipeY;
        int width = pipewidth;
        int height = pipeheight;
        Image img;
        boolean passed = false;

        pipe(Image img){
            this.img = img;
        }
    }
     class Bird {

        int x = birdX;
        int y = birdY;
        int width = birdwidth;
        int height = birdheight;
        Image img;

        Bird(Image img) {
            this.img = img;
        }
     }
    //game logic
      Bird bird;
      int velocityX = -4; //move pipes to the left speed (simulates bird moving right)
      int velocityY = -9; //move bird up/down speed
      int gravity = 1;

      ArrayList<pipe> pipes;
      Random random = new Random();

      Timer gameloop;
      Timer placePipesTimer;
      boolean gameOver = false;
      double score = 0;

    flappybird () {
        setPreferredSize(new Dimension(boardWidth,boardHeight));
        //setBackground(Color.blue);
        setFocusable(true);
        addKeyListener(this);
        

        //loadimages
        backroundImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        botttomPipeImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();
       
        //bird
        bird = new Bird(birdImg);
        pipes = new ArrayList<pipe>();

        // place pipes timer
        placePipesTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                pipePlace();
            }
        });
        placePipesTimer.start();
        //gameloop
        gameloop = new Timer(1000/60,this); //1000/60 = 16.6
        gameloop.start();
    }

    public void pipePlace(){
        //(0-1) * pipeHeight/2 -> (0-256)
        //128
        //0 - 128 - (0-256) --> 1/4 pipeHeight -> 3/4 pipeHeight
        int randomPipeY = (int) (pipeY-pipeheight/4-Math.random()*(pipeheight/2));
        int openingSpace = boardHeight/4;

        pipe topPipe = new pipe(topPipeImg);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);

        pipe bottomPipe = new pipe(botttomPipeImg);
        bottomPipe.y = topPipe.y + pipeheight +openingSpace;
        pipes.add(bottomPipe);


    }

    public void paintComponent (Graphics g) {

        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        //System.out.println("draw");
        //background
        g.drawImage(backroundImg, 0, 0,boardWidth,boardHeight,null);

        //bird
        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);
        //pipes
        for(int i= 0; i < pipes.size(); i++){
            pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width,pipe.height, null);

        }
        //score
        g.setColor(Color.white);
        g.setFont(new Font("arial", Font.PLAIN, 32));

        if(gameOver){
            g.drawString("Game over: "+String.valueOf((int)score),10,35);

        }
        else{
            g.drawString(String.valueOf((int)score), 10, 35);
        }
    }

    public void move(){
        //bird
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0);
        //pipes
        for(int i =0; i<pipes.size();i++){
            pipe pipe = pipes.get(i);
            pipe.x += velocityX;

            if (!pipe.passed && bird.x > pipe.x + pipe.width){
                pipe.passed = true;
                score += 0.5; //0.5 because there are 2 pipes! so 0.5*2 =1, 1 for each set of pipes
            }

            if (collision(bird, pipe)){
                gameOver = true;
            }
        }
        if(bird.y > boardHeight){
            gameOver = true; 
        }
    }

        public boolean collision (Bird a, pipe b){
            return a.x < b.x + b.width &&   //a's top left corner doesn't reach b's top right corner
            a.x + a.width > b.x &&   //a's top right corner passes b's top left corner
            a.y < b.y + b.height &&  //a's top left corner doesn't reach b's bottom left corner
            a.y + a.height > b.y;    //a's bottom left corner passes b's top left corner


        }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver){
            placePipesTimer.stop();
            gameloop.stop();
        }
    }
   

    @Override
    public void keyPressed(KeyEvent e) {
    if(e.getKeyCode() == KeyEvent.VK_SPACE){
        velocityY = -9;
        if(gameOver){
            bird.y = birdY;
            velocityY = 0;
            pipes.clear();
            score = 0;
            gameOver = false;
            gameloop.start();
            placePipesTimer.start();

        }
    }        
    }

    public void keyTyped(KeyEvent e) {
     
    }
    @Override
    public void keyReleased(KeyEvent e) {
       
    }
    
}
