
package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.bounding.BoundingVolume;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.shadow.PointLightShadowRenderer;
import com.jme3.system.AppSettings;
import java.util.Random;
import com.jme3.scene.shape.Box;
import com.jme3.renderer.queue.RenderQueue;


/**
 * The main class contain all the code and algorithm
 *
 * @extends SimpleApplication
 * @author Dursun Satiroglu 
 * 
 */
public class Game extends SimpleApplication {

    //initialize class variables
    private static Game app;
    public static AppSettings mySettings;

    int score, level;
    int rotationSpeed;

    Node table;
    Geometry ball, paddle, target;
    Geometry[] arr = new Geometry[5];
    Vector3f direction;

    float boundLeft = -4.4f;
    float boundRight = 4.4f;
    float boundUp = -5.7f;
    float boundDown = 5.9f;
   
    boolean isRunning = false;
    boolean start = false;
    
    Sound sound;
    Text scoreBoard;
    Text info;
    
    PointLight thisLight;
    DirectionalLight sun;
    
    
    @Override
    public void simpleInitApp() {
       
        //set up sound
        sound = new Sound(new AudioNode(assetManager, "Sounds/bump.wav", false),
                new AudioNode(assetManager, "Sounds/ding.wav", false),
                new AudioNode(assetManager, "Sounds/BGM.wav", false));
        sound.setBgm(false, true, 0.1f);
        sound.setCollide1(false, false, 0.5f);
        sound.setCollide2(false, false, 0.5f);
        
        //set up gui
        scoreBoard = new Text(new BitmapText(assetManager.loadFont("Interface/Fonts/Default.fnt")));
        scoreBoard.setTextSettings(60f, settings.getWidth()/50, scoreBoard.getText().getLineHeight() + 500, 0);
        
        info = new Text(new BitmapText(assetManager.loadFont("Interface/Fonts/Default.fnt")));
        info.setTextSettings(40f, settings.getWidth()/100, scoreBoard.getText().getLineHeight() + 650, 0);
        
        
        //set up camera
        this.flyCam.setEnabled(false);
        this.setDisplayFps(true);
        this.setDisplayStatView(false);
        
        
        
        //set gameball
        ball = new Geometry("ball", new Sphere(50, 50, 0.45f));
        ball.scale(0.9f);
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setTexture("DiffuseMap", assetManager.loadTexture("Textures/red.jpeg"));
        ball.setMaterial(mat);
        
        
        //set up the specimen target
        target = new Geometry("target", new Sphere(50, 50, 0.5f));
        Material materialTarget = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        materialTarget.setTexture("DiffuseMap", assetManager.loadTexture("Textures/green.jpeg"));
        target.setMaterial(materialTarget);
        Material materialSpecial = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        materialSpecial.setTexture("DiffuseMap", assetManager.loadTexture("Textures/specialGreen.jpeg"));
        
        for(int a = 0; a<5; a++){
            Geometry newTarget = target.clone();
            
            if (a==3 || a==4){
                
                newTarget.setMaterial(materialSpecial);
        
            }
            
            arr[a] = newTarget;
        }
        
        
        //set up the table
        table = (Node) assetManager.loadModel("Models/table.j3o");
        Material materialTable = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        materialTable.setTexture("DiffuseMap", assetManager.loadTexture("Textures/table.jpeg"));
        table.setMaterial(materialTable);
        
        
        //Set up the paddle
        paddle = new Geometry("paddle", new Box(0.5f, 0.2f,0.2f));
        Material materialPaddle = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        materialPaddle.setTexture("DiffuseMap", assetManager.loadTexture("Textures/paddle.png"));
        paddle.setMaterial(materialPaddle);//Set material.
        paddle.scale(1, 1, 0.7f);//Set scale.
        
        
        
        //set mapping
        inputManager.addMapping("Shoot",
                new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Right",
                new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Left",
                new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Restart",
                new KeyTrigger(KeyInput.KEY_X));
        inputManager.addMapping("Pause",
                new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(analogListener, "Shoot");
        inputManager.addListener(analogListener, "Right");
        inputManager.addListener(analogListener, "Left");
        inputManager.addListener(analogListener, "Restart");
        inputManager.addListener(analogListener, "Pause");

        //set lighting
        sun = new DirectionalLight();
        sun.setDirection(new Vector3f(1, -1, -1));
        rootNode.addLight(sun);
        //set up a point light
        thisLight = new PointLight();
        thisLight.setColor(ColorRGBA.White);
        thisLight.setPosition(new Vector3f(0, 1, 3));
        thisLight.setRadius(30);
        
        
        // setting up the renderers.
        PointLightShadowRenderer plsr = new PointLightShadowRenderer(assetManager, 512);
        plsr.setLight(thisLight);
        plsr.setFlushQueues(false);
        
        ball.setShadowMode(RenderQueue.ShadowMode.Cast);
        // The table can both cast and receive
        table.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        
        
        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(assetManager, 512, 2);
        dlsr.setLight(sun);
        viewPort.addProcessor(plsr);
        viewPort.addProcessor(dlsr);
        
        
      

        
        
        
        //set camera settings
        cam.setLocation(new Vector3f(0, 17, 6));
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
        cam.clearViewportChanged();
        
        
        //give direction and ball positioning
        direction = new Vector3f(0, 0, 0);
        ball.setLocalTranslation(0, 0, 3);
        
        
        //attach all to node
        attachToNode();
        

    }
    
    //attach all things set up that need to be attached to a node.
    private void attachToNode(){
        
        guiNode.attachChild(scoreBoard.getText());
        guiNode.attachChild(info.getText());
        
        rootNode.attachChild(ball);
        rootNode.attachChild(table);
        rootNode.attachChild(paddle);
        
        for (int a =0; a<arr.length; a++){
            rootNode.attachChild(arr[a]);
        }
        
        rootNode.addLight(thisLight);
        
    }
   

    //allows for key control
    private AnalogListener analogListener = new AnalogListener() {
        @Override
        public void onAnalog(String name, float value, float tpf) {

            if (isRunning && name.equals("Left")) {
                paddle.move(-9 * tpf, 0, 0);
            } else if (isRunning && name.equals("Right")) {
                paddle.move(9 * tpf, 0, 0);
            } else if (name.equals("Shoot")) {
                start = true;
            } else if (name.equals("Pause")) {
                isRunning = !isRunning;
            } else if (name.equals("Restart")) {
                
                //Restart the level 1
                
                for (int a = 0; a <5; a++){
                    arr[a].move(10,10,10);
                }
                
                isRunning = true;
                sound.getBgm().play();
                lv1();
            }

        }
    };


    //game loop: check collisions and keep track of level progression
    @Override
    public void simpleUpdate(float tpf) {

        scoreBoard.setText("Level " + level + "\n" + "Your Score: " + (score));
        info.setText("Press X to re(start) \n Use W to Shoot \n Use A & D to move");
        
        if (isRunning == true) {
            if (start) {
                ball.rotate(0, rotationSpeed * FastMath.PI * tpf, 0);
                //initial move!
                ball.move(direction.mult(tpf));
                
                
            }
            
            
            targetCollision();
            paddleTableCollision();
            boundaryCollision();
            paddleCollision();
            
             

        }
       
        if (score == 3 && level == 1) {
            lv2();
        }
        if (score == 5 && level == 2) {
            Win();
        }
    }
    
    
    
    
    
    //check paddle collision to ball
    private void paddleCollision() {
        
        CollisionResults resultBallPaddle = new CollisionResults();
        BoundingVolume bvBall = ball.getWorldBound();
        paddle.collideWith(bvBall, resultBallPaddle);
            
        if (resultBallPaddle.size() > 0) {

            sound.getCollide1().playInstance();
            
            //reverse z
            direction.setZ(-FastMath.abs(direction.getZ()));
            
            //put direction on x
            if (paddle.getLocalTranslation().getX() < 0) {
                direction.setX(direction.getX() + 0.5f);

            } else if (paddle.getLocalTranslation().getX() > 0) {
                direction.setX(direction.getX() - 0.5f);

            }
           
        }
    }

    //Check ball collison with table boundaries 
    private void boundaryCollision() {
        
        if (boundUp >= ball.getLocalTranslation().z) {

            direction.setZ(FastMath.abs(direction.getZ()));
            direction = direction.mult(1.005f);
            sound.getCollide1().playInstance();
            
        } else if (boundDown <= ball.getLocalTranslation().z) {//if ball goes past the paddle
            
            gameOver();
            
        } else if (boundLeft >= ball.getLocalTranslation().x) {
            direction.setX(FastMath.abs(direction.getX()));
            direction = direction.mult(1.005f);
            sound.getCollide1().playInstance();
            
        } else if (boundRight <= ball.getLocalTranslation().x) {
            
            direction.setX(-FastMath.abs(direction.getX()));
            direction = direction.mult(1.005f);
            sound.getCollide1().playInstance();
        }
       
    }

    //these variables keep track of the amount of times a special ball has been hit.
    int count3 = 0;
    int count4 = 0;
    
    //manage ball-target collisions
    private void targetCollision() {
        
        //loop through all targets in the geom array
        for (Geometry target : arr){
            
            //instanciate collision results for each and set collide
            CollisionResults targetCollision = new CollisionResults();
            ball.collideWith(target.getWorldBound(),targetCollision);
            
            
            if (targetCollision.size() > 0){
                
                //manage trajectories
                sound.getCollide2().playInstance();
                Vector3f norm = new Vector3f((target.getLocalTranslation().subtract(ball.getLocalTranslation())).normalize());
                float projVal = direction.dot(norm);
                Vector3f projection = norm.mult(projVal);
                Vector3f parall = direction.subtract(projection);
                direction = parall.subtract(projection);
                
                //manage when balls should dissapear
                if ((level == 2) && target == arr[3]){
                    
                    if (count3 == 1){
                        score += 1;
                        target.move(15,15,15);   
                    } else if (count3 == 0){
                         count3 = 1;   
                    }   
                } else if((level == 2) && target == arr[4]){
                    
                    
                      
                    if (count4 == 1){
                        score += 1;
                        target.move(15,15,15);                       
                    } else if (count4 ==0){
                        count4 =1;    
                    }
                    
                } else{
                    score += 1;
                    target.move(15,15,15); 
                }
                
            }
          
        }
    
    }
    
    //handle paddle colliding with the table
    private void paddleTableCollision(){
        
        //check bounds and if paddle has overstepped them
        //put the paddle back if so
        if (boundLeft >= paddle.getLocalTranslation().x) {
            paddle.setLocalTranslation((float) -3.5, 0, 6);
            
        } else if (boundRight <= paddle.getLocalTranslation().x) {

            paddle.setLocalTranslation((float) 3.5, 0, 6);
        }
       
    }
    
    
    
    
    
    
    
    

   

    //handle first level
    protected void lv1() {
        
        //set params
        level = 1; score = 0;
        
        //set up positions of targets
        for (int a=0; a < 3; a++){
            if (a == 0){
                arr[a].setLocalTranslation(1f,0,-2f);
            }else if (a ==1){
                arr[a].setLocalTranslation(-2f,0,-2f);
            }else if (a==2){
                arr[a].setLocalTranslation(-3f,0,-1f);
            }else{
                arr[a].setLocalTranslation(10, 10, 10);
            }
           
        }
        
        //position ball and paddle
        paddle.setLocalTranslation(0, 0, 6);
        ball.setLocalTranslation(0, 0, (float) (6 - 0.3 * 3));

        //set ball params
        rotationSpeed = 3;
        float num = new Random().nextFloat() * 2 - 1;
        direction = new Vector3f(num, 0, -1);
        direction = direction.mult(5f);
        start = false;
    }

    //handle level 2 
    protected void lv2() {

        level = 2; score = 0;
        
        //enter all positions of entities on the screen
        for (int a=0; a < 5; a++){
            if (a == 0){
                arr[a].setLocalTranslation(1f,0,-2f);
            }else if (a ==1){
                arr[a].setLocalTranslation(-2f,0,-2f);
            }else if (a==2){
                arr[a].setLocalTranslation(-3f,0,-1f);
            }else if (a ==3){
                arr[a].setLocalTranslation(4f, 0, -4f);
            }else if (a ==4){
                arr[a].setLocalTranslation(-4f, 0, -1f);
            }else{
                arr[a].setLocalTranslation(-2f,0,-1f);
            }
           
        }
      
        ball.setLocalTranslation(0, 0, (float) (6 - 0.3 * 3));
        paddle.setLocalTranslation(0, 0, 6);

        //set up ball
        rotationSpeed = 5;
        float num = new Random().nextFloat() * 2 - 1;
        direction = new Vector3f(num, 0, -1);
        direction = direction.mult(7f);
        start = false;
    }
    
    //handles a loss
    protected void gameOver() {
        
        //set ball to no movement
        rotationSpeed = 0;
        direction = new Vector3f(0, 0, 0);
        
        //notify user
        
        info.setTextSettings(40f, 50f, scoreBoard.getText().getLineHeight() + 500, 0);
        info.setText("YOU LOST!");

    }

   
   
    //handles win event
    protected void Win() {

       
        //notify user of a win
        info.setTextSettings(40f, 50f, scoreBoard.getText().getLineHeight() + 500, 0);
        info.setText("YOU WIN!");
                
        //stop ball        
        direction = direction.mult(0f);
    }
   
       
}
