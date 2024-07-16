package application;

import java.awt.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
//import java.net.URL;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//Instance of the game
@SuppressWarnings("serial")
public class GameProper extends JPanel{

	private MainMenu menu;
	private JFrame frame;
	private Font font;
	private Clip accelSFX, brakeSFX, turnSFX, crashSFX;

	private int crx, cry,crx2;	//location of the crossing
	private int car_x,car_y;    //x and y location of user's car
	private int speedX,speedY;	//the movement values of the user's car
	private int nOpponent;      //the number of opponent vehicles in the game
	private String imageLoc[]; //array used to store oponnent car images
	private int lx[],ly[];  //integer arrays used to store the x and y values of the oncoming vehicles
	private int score;      //intger variable used to store the current score of the player
	private int highScore;  //integer variable used to store the high score of the player
	private int speedOpponent[]; //integer array used to store the spped value of each opponent vehicle in the game
	private static boolean isFinished; //boolean that will be used the end the game when a colision occurs
	private boolean isLeft, isRight;  //boolean values that show when a user clicks the corresponding arrow key
	private int lives; //lives counter
	private static Crash crash;
	private boolean crashed;
	private boolean blink;

	private int currentLap;
    private long lapStartTime;
    private static final long LAP_DURATION = 30000; // 30 seconds for each lap
    private int baseOpponentSpeed; // Initial speed of spawning cars
    private int maxOpponentSpeed; // Maximum speed of spawning cars
    private int opponentSpeedIncrement; // Speed increment for each lap
    private boolean win; // win or lose

    public GameProper(MainMenu menu){
    	this.menu = menu;
    	this.frame = new JFrame("Car Racing Game");   //creating a new JFrame window to display the game


		try {
			AudioInputStream accelFile = AudioSystem.getAudioInputStream(new File("sounds/accel.wav"));
			this.accelSFX = AudioSystem.getClip();
			this.accelSFX.open(accelFile);

			AudioInputStream brakeFile = AudioSystem.getAudioInputStream(new File("sounds/turn.wav"));
			this.brakeSFX = AudioSystem.getClip();
			this.brakeSFX.open(brakeFile);

			AudioInputStream turnFile = AudioSystem.getAudioInputStream(new File("sounds/brake.wav"));
			this.turnSFX = AudioSystem.getClip();
			this.turnSFX.open(turnFile);

			AudioInputStream crashAudio = AudioSystem.getAudioInputStream(new File("sounds/crash.wav"));
			this.crashSFX = AudioSystem.getClip();
			this.crashSFX.open(crashAudio);
//			FloatControl gainControl = (FloatControl) this.crashSFX.getControl(FloatControl.Type.MASTER_GAIN);
//			gainControl.setValue(6.0f); // Reduce volume by 10 decibels.

		} catch (UnsupportedAudioFileException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (LineUnavailableException e1) {
			e1.printStackTrace();
		}

        try {
			this.font = Font.createFont(Font.TRUETYPE_FONT, new File("font/Minecraft.ttf"));
			this.font  = font.deriveFont(Font.BOLD, 30);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

        //Listener to get input from user when a key is pressed and released
        addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
            }
            public void keyReleased(KeyEvent e) { //when a key is released
                stopCar(e); //stop movement of car
            }
            public void keyPressed(KeyEvent e) { //when a key is pressed
                moveCar(e); //move the car in the direction given by the key
            }
        });

        currentLap = 1;
        lapStartTime = System.currentTimeMillis();
        baseOpponentSpeed = 5;
        maxOpponentSpeed = 15;
        opponentSpeedIncrement = 2;

        setFocusable(true); //indicates the JPanel can be focused
        crx = -1950; //initializing the location of the 1st road to -1950
        cry = 0;
        crx2 = 0; //initializing  the location of the 1st road to 0
        car_x = 100; //initializing setting the user's car location to (100,700)
        car_y = 700;
//        forward = true; //initial arrow key values set to false, meaning car is just moving forward
        isLeft = isRight = false;
        speedX = speedY = 0;    //movement of the car in the x and y direction initially set to 0 (starting position)
        nOpponent = 0;  //set the number of opponent cars initially to zero
        lx = new int[20]; //array to be used to store the x position of all enemy cars
        ly = new int[20]; //array to be used to store the y position of all enemy cars
        imageLoc = new String[20];
        speedOpponent = new int[20]; //integer array used to store the speed value of each opponent vehicle in the game
        isFinished = false; //when false, game is running, when true, game has ended
        score = highScore = 0;  //initializing setting the current score and the high-score to zero
        lives = 3; //initializing lives to 3
        win = false; // di pa panalo ofc by default
    }

    //function that paints all graphic images to the screen at specified locations
    //scene is repainted every time the scene settings change
    public void paint(Graphics g){

        super.paint(g);
        Graphics2D obj = (Graphics2D) g;
        obj.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        try{
        	obj.drawImage(getToolkit().getImage(new File("src/images/game_bg.png").getAbsolutePath()), 0,0 ,this); //draw road on window

//            if(crx >= -1949) //if a road crossing has passed the window view
        	obj.drawImage(getToolkit().getImage(new File("src/images/1.png").getAbsolutePath()),crx,0,this); //draw another road crossing on window

        	obj.drawImage(getToolkit().getImage(new File("src/images/1.png").getAbsolutePath()),crx2,0,this); //draw another road crossing on window

            for(int i = 0; i < lives; i++)
        		obj.drawImage(getToolkit().getImage(new File("src/images/steering wheel.png").getAbsolutePath()), 1570 + (i*100), 50 ,this); //draw lives on top

            if(!blink)
            {
            	if(isLeft)
            		obj.drawImage(getToolkit().getImage(new File("src/images/mycar_left.png").getAbsolutePath()),car_x,car_y,this);   //draw car on window
            	else if(isRight)
            		obj.drawImage(getToolkit().getImage(new File("src/images/mycar_right.png").getAbsolutePath()),car_x,car_y,this);   //draw car on window
            	else
            		obj.drawImage(getToolkit().getImage(new File("src/images/mycar.png").getAbsolutePath()),car_x,car_y,this);   //draw car on window
            }
            else
            	obj.drawImage(getToolkit().getImage(new File("src/images/mycar_blink.png").getAbsolutePath()),car_x,car_y,this);   //draw car on window

            if(crashed){ //if collision occurs
                obj.drawImage(getToolkit().getImage(new File("src/images/smoke.png").getAbsolutePath()),car_x+10,car_y-30,this); //draw explosion image on window at collision to indicate the collision has occured
            }

            if(this.nOpponent > 0){ //if there is more than one opponent car in the game
                for(int i=0;i<this.nOpponent;i++){ //for every opponent car
                    obj.drawImage(getToolkit().getImage(this.imageLoc[i]),this.lx[i],this.ly[i],this); //draw onto window
                }
            }
        }
        catch(Exception e){
            System.out.println(e);
        }

        obj.setColor(Color.GRAY);

        obj.setFont(this.font);

        obj.drawString("Score: " + (score * 100), 20, 50);
        obj.drawString("Lap: " + currentLap, 20, 90); // Modify the coordinates as needed

        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - lapStartTime;
        long remainingSeconds = (LAP_DURATION - elapsedTime) / 1000;

        obj.drawString("Time Remaining: " + remainingSeconds + " seconds", 20, 130);
    }

    //function that moves the road scene across the window to make it seem like the car is driving
    void moveRoad(int count, GameProper game){
        if(crx <= -1950){ //if the road crossing has passed by
                crx = 1935;      //send crossing location back at the beginning
//                cry = 0;
        }
        else{   //otherwise
            crx-=15; //keep moving the crossing across the window
        }

        if(crx2 <= -1950){ //if the road crossing has passed by
                crx2 = 1935;      //send crossing location back at the beginning
//                cry = 0;
        }
        else{   //otherwise
            crx2-=15; //keep moving the crossing across the window
        }

        car_x += speedX; //update car x position
        car_y += speedY; //update car y position

        //case analysis to restrict car from going outside the left side of the screen
        if(car_x < 0)   //if the car has reached or gone under its min x axis value
            car_x = 0;  //keep it at its min x axis value

        //case analysis to restrict car from going outside the right side of the screen
        if(car_x+280 >= 1950) //if the car has reached or gone over its max x axis value
            car_x = 1950-280; //keep it at its max x axis value

        //case analysis to restrict car from going outside the right side of the road
        if(car_y <= 420)    //if the car has reached the the right side of the road or is trying to go further
            car_y = 420;    //keep the car where it is

        //case analysis to restrict car from going outside the left side of the road
        if(car_y >= 870-75) //if the car has reached the the left side of the road or is trying to go further
            car_y = 870-75; //keep the car where it is


        for(int i=0;i<this.nOpponent;i++){ //for all opponent cars
            this.lx[i] -= speedOpponent[i]; //move across the screen based on already calculated speed values
        }

        //next 16 lines unknown
        int index[] = new int[nOpponent];
        for(int i=0;i<nOpponent;i++){
            if(lx[i] >= -300){ //if the opponent car passes the user without colliding
                index[i] = 1;
            }
        }
        //reset opponent car position to beginning to start over
        int c = 0;
        for(int i=0;i<nOpponent;i++){
            if(index[i] == 1){
                imageLoc[c] = imageLoc[i];
                lx[c] = lx[i];
                ly[c] = ly[i];
                speedOpponent[c] = speedOpponent[i];
                c++;
            }
        }

        score += nOpponent - c; //score is incremented everytime an opponent car passes

        if(score > highScore)   //if the current score is higher than the high score
            highScore = score;  //update high score to the current score

        nOpponent = c;

       //Check for collision
       //int diff = 0; //difference between users car and opponents car initially set to zero
        for(int i=0;i<nOpponent;i++){ //for all opponent cars
           // diff = car_y - ly[i]; //diff is the distance between the user's car and the opponent car
            if((ly[i] >= car_y && ly[i] <= car_y+75) || (ly[i]+75 >= car_y && ly[i]+75 <= car_y+75)){   //if the cars collide vertically
                if(car_x+280 >= lx[i] && !(car_x >= lx[i]+280)){  //and if the cars collide horizontally
//                    System.out.println("My car : "+car_x+", "+car_y);
//                    System.out.println("Colliding car : "+lx[i]+", "+ly[i]);

                    if(!crash.isAlive())
                    	crash.start();
                }
            }
        }
    }

    void crashed() {
        System.out.println("My car : " + car_x + ", " + car_y);
        System.out.println("Lives : " + lives);

        this.crashSFX.loop(Clip.LOOP_CONTINUOUSLY);
        this.crashSFX.start();

        crashed = true;

        lives--;

        if (lives == 0) {
            this.finish();
            this.win = false;
        }
    }

    void uncrashed()
    {
    	crashed = false;
    }

    void stopCrashSFX()
    {
    	this.crashSFX.stop();
    }

    void blink()
    {
    	blink = true;
    }

    void unblink()
    {
    	blink = false;
    }

    void reset(GameProper game)
    {
    	crash = new Crash(game);
    }

	//function that will display message after user has lost the game
    private void finish(){
        String str = "";    //create empty string that will be used for a congratulations method
        isFinished = true;  //indicates that game has finished to the rest of the program
        this.repaint();     //tells the window manager that the component has to be redrawn
//        if(score == highScore && score != 0) //if the user scores a new high score, or the same high score
//            str = "\nCongratulations!!! Its a high score";  //create a congratulations message

    }


    //function that handles input by user to move the user's car up, left, down and right
    private void moveCar(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_RIGHT){   //if user clicks on the right arrow key
            speedX = 5;     //moves car forward
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT){ //if user clicks on the left arrow key
        	if(this.accelSFX.isRunning())
        	{
        		this.accelSFX.stop();
        		this.brakeSFX.loop(Clip.LOOP_CONTINUOUSLY);
            	this.brakeSFX.start();
        	}
            speedX = -3;    //moves car backwards
        }
        if(e.getKeyCode() == KeyEvent.VK_DOWN){ //if user clicks on the down arrow key
        	if(this.accelSFX.isRunning())
        	{
        		this.accelSFX.stop();
        		this.turnSFX.loop(Clip.LOOP_CONTINUOUSLY);
            	this.turnSFX.start();
        	}
        	isRight = true;
        	speedY = 4;     //moves car to the right
        }
        if(e.getKeyCode() == KeyEvent.VK_UP){ //if user clicks on the up arrow key
        	if(this.accelSFX.isRunning())
        	{
        		this.accelSFX.stop();
        		this.turnSFX.loop(Clip.LOOP_CONTINUOUSLY);
            	this.turnSFX.start();
        	}
        	isLeft = true;
            speedY = -4;    //moves car to the left
        }
    }

    //function that handles user input when the car is supposed to be stopped
    private void stopCar(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_RIGHT){   //if user clicks on the right arrow key
            speedX = 0; //set speed of car to zero
        }
        else if(e.getKeyCode() == KeyEvent.VK_LEFT){    //if user clicks on the left arrow key
        	this.brakeSFX.stop();
        	this.accelSFX.loop(Clip.LOOP_CONTINUOUSLY);
        	this.accelSFX.start();
            speedX = 0; //set speed of car to zero
        }
        else if(e.getKeyCode() == KeyEvent.VK_DOWN){    //if user clicks on the down arrow key
        	this.turnSFX.stop();
        	this.accelSFX.loop(Clip.LOOP_CONTINUOUSLY);
        	this.accelSFX.start();
        	isRight = false;
            speedY = 0; //set speed of car to zero
        }
        else if(e.getKeyCode() == KeyEvent.VK_UP){   //if user clicks on the up arrow key
        	this.turnSFX.stop();
        	this.accelSFX.loop(Clip.LOOP_CONTINUOUSLY);
        	this.accelSFX.start();
        	isLeft = false;
        	speedY = 0; //set speed of car to zero
        }
    }

    private void updateLap() {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - lapStartTime;

        if (elapsedTime >= LAP_DURATION) {
            if (currentLap < 3) { // Assuming max lap is 3
                currentLap++;
                System.out.println("Lap: " + currentLap);
                lapStartTime = currentTime;
                increaseOpponentSpeed(); // Add this method to adjust opponent speed
            } else {
                // The player has completed all laps, panalo
                this.finish();
                this.win = true;
            }
        }
    }

    private int increaseOpponentSpeed() {
        if (currentLap == 1) {
            return baseOpponentSpeed;  // Use baseOpponentSpeed for lap 1
        } else {
            // Add the rest of the code for lap > 1
        	System.out.println("baseOpponentSpeed: " + baseOpponentSpeed);
            if (baseOpponentSpeed + opponentSpeedIncrement <= maxOpponentSpeed) {
                baseOpponentSpeed += opponentSpeedIncrement;
            }
            System.out.println("new baseOpponentSpeed: " + baseOpponentSpeed);
            return baseOpponentSpeed;  // Return the updated speed value
        }
    }

    private void spawnOpponent() {
        String path = "src/images/car" + ((int) ((Math.random() * 100) % 8) + 1) + ".png";
        this.imageLoc[this.nOpponent] = new File(path).getAbsolutePath();

        this.lx[this.nOpponent] = 1949;

        int p = (int) (Math.random() * 100) % 4;
        if (p == 0) {
            p = (int) (Math.random() * (465 - 430 + 1)) + 430;
        } else if (p == 1) {
            p = (int) (Math.random() * (570 - 530 + 1)) + 530;
        } else if (p == 2) {
            p = (int) (Math.random() * (690 - 650 + 1)) + 650;
        } else {
            p = (int) (Math.random() * (800 - 760 + 1)) + 760;
        }

        boolean collision = false;
        for (int i = 0; i < this.nOpponent; i++) {
            if (Math.abs(p - this.ly[i]) < 115) { // Adjust the value based on the height of opponent cars
                collision = true;
                break;
            }
        }

        if (!collision) {
            this.ly[this.nOpponent] = p;
            int updatedSpeed = increaseOpponentSpeed();
            this.speedOpponent[this.nOpponent] = updatedSpeed + (int) (Math.random() * 2);
            this.nOpponent++;
        }
    }

    //method where the java application begins processing
    void startGame(){
        frame.add(this);		//Graphics2D components are added to JFrame Window
        frame.setSize(1950,990); //setting size of screen to 1950x990
        frame.setVisible(true); //allows the JFrame and its children to displayed on the screen
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        int count = 1, c = 1;

        crash = new Crash(this);

        this.accelSFX.loop(Clip.LOOP_CONTINUOUSLY); // start default sfx
        this.accelSFX.start();

        while(!isFinished){
            moveRoad(count, this);   //move the road
            updateLap();
            repaint();
            //game.repaint();
            while(c <= 1){
            	this.repaint();     //redraw road to match new locations

                try{
                    Thread.sleep(10);    //wait so that the road appears to be moving continously
                }
                catch(Exception e){
                    System.out.println(e);
                }
                c++;
            }
            c = 1;
            count++; //increment count value

            if (currentLap == 1 && this.nOpponent < 4 && count % 50 == 0) {
                spawnOpponent();
            } else if (currentLap == 2 && this.nOpponent < 6 && count % 35 == 0) {
                spawnOpponent();
            } else if (currentLap == 3 && this.nOpponent < 8 && count % 25 == 0) {
                spawnOpponent();
            }

        }
        if(this.win)
        	this.menu.launchWinningScene();
        else
        	this.menu.launchLosingScene();

    	this.accelSFX.stop();
    	this.brakeSFX.stop();
    	this.turnSFX.stop();
    	this.crashSFX.stop();
        frame.dispose();
    }
}
