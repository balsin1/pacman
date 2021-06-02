/**
 * MyClara
 * 
 * Available functions (see Assignment document for explanations on what each function does):
 * treeFront, ghostWallFront,
 * getDirection, setDirection,
 * move,
 * makeScared, isScared,
 * animate, animateDead, 
 * onLeaf, removeLeaf, 
 * onMushroom, removeMushroom,
 * allLeavesEaten, 
 * isClaraDead,
 * playClaraDieSound, isClaraDieSoundStillPlaying,
 * playLeafEatenSound,
 * playPacmanIntro, isPacmanIntroStillPlaying,
 * wrapAroundWorld,
 * getCurrentLevelNumber, advanceToLevel
 */
class MyClara extends Clara
{
    // Please leave this first level here,
    // until after you've completed \"Part 12 -
    // Making and Adding Levels\"
    public final char[][] LEVEL_1 = {
            {'#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#'},
            {'#','$','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','$','#'},
            {'#','.','#','#','.','#','.','#','#','#','#','#','.','#','.','#','#','.','#'},
            {'#','.','.','.','.','#','.','.','.','.','.','.','.','#','.','.','.','.','#'},
            {'#','#','#','#','.','#',' ','#','#','|','#','#',' ','#','.','#','#','#','#'},
            {' ',' ',' ',' ','.',' ',' ','#','%','?','%','#',' ',' ','.',' ',' ',' ',' '},
            {'#','#','#','#','.','#',' ','#','#','#','#','#',' ','#','.','#','#','#','#'},
            {'#','.','.','.','.','.','.','.','.','#','.','.','.','.','.','.','.','.','#'},
            {'#','.','#','#','.','#','#','#','.','#','.','#','#','#','.','#','#','.','#'},
            {'#','$','.','#','.','.','.','.','.','@','.','.','.','.','.','.','.','$','#'},
            {'#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#'}
        };
    public final char[][] LEVEL_2 = {
            {'#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#'},
            {'#','$','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','$','#'},
            {'#','.','#','#','.','#','.','#','#','#','#','#','.','#','.','#','#','.','#'},
            {'#','.','.','.','.','#','.','.','.','.','.','.','.','#','.','.','.','.','#'},
            {'#','#','#','#','.','#',' ','#','#','|','#','#',' ','#','.','#','#','#','#'},
            {' ',' ',' ',' ','.',' ',' ','#','%','?','%','#',' ',' ','.',' ',' ',' ',' '},
            {'#','#','#','#','.','#',' ','#','#','#','#','#',' ','#','.','#','#','#','#'},
            {'#','.','.','.','.','#','.','.','.','.','.','.','.','#','.','.','.','.','#'},
            {'#','.','#','#','.','#','.','#','#','#','#','#','.','#','.','#','#','.','#'},
            {'#','$','.','.','.','.','.','.','.','@','.','.','.','.','.','.','.','$','#'},
            {'#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#'}
        };
        
    // Movement constants
    public final String  UP = "up";    
    public final String  DOWN = "down";    
    public final String  LEFT = "left";    
    public final String  RIGHT = "right";        
    
    // Add and initialise Clara's variables here
    
     int counter;
     boolean intro = true;
     boolean lose = true;

    /**
     * Act method
     * 
     * Runs of every frame
     */

    public void act()
    {
        //Make Clara do things here

        // Pacman intro is played every time user starts a level
        if (intro == true) {
            playPacmanIntro();
            intro = false;
        }
        if (!isPacmanIntroStillPlaying()) {
            // Clara's animations are called every time the act method is run
            if (counter == 0) {
                if (isClaraDead() == false) {
                    animate();
                    counter = 0;
                }
                if (isClaraDead() == true) {
                    animateDead();
                    counter = 0;
                }
            } else {
                counter++;
            }
            keyboardInput();
        }
    }
    //Give Clara functions here

    void keyboardInput()
    {
        // Clara begins movement upon user command using arrow keys
        if (allLeavesEaten() == false && isClaraDead() == false) {
            if (Keyboard.isKeyDown("up")) {
                setDirection(UP);
            }
            if (Keyboard.isKeyDown("down")) {
                setDirection(DOWN);
            }
            if (Keyboard.isKeyDown("left")) {
                setDirection(LEFT);
            }
            if (Keyboard.isKeyDown("right")) {
                setDirection(RIGHT);
            }
            claraMove();
        } else {
            // If Clara has died, user will no longer be able to control Clara
            if (isClaraDead() == true && lose == true) {
                playClaraDieSound();
                animateDead();
                lose = false;
                System.out.println("Defeat");
            }
            // After Clara dies, user is reset back to Level 1
            if (!isClaraDieSoundStillPlaying()) {
                advanceToLevel(LEVEL_1);
                intro = true;
            } else {
                // If user has completed the level, they will not be able to control Clara until the next level begins
                if (allLeavesEaten() == true) {
                    System.out.println("Victory");
                    getCurrentLevelNumber();
                    // If user has completed the first level, they will proceed to Level 2
                    if (getCurrentLevelNumber() == 1) {
                        advanceToLevel(LEVEL_2);
                        intro = true;
                    } 
                    // If user has compelted the second level, they will return back to Level 1
                    if (getCurrentLevelNumber() != 1) {
                        advanceToLevel(LEVEL_1);
                        intro = true;
                    }
                }

            }
        }
    }
    void safeMove()
    {
        if (!treeFront() && !ghostWallFront()) {
            // Clara's speed is above that of the Ghosts due to turns being more difficult with user input
            move(5);
        }
    }
    void claraMove()
    {
        // Prevents Clara from exiting the world
        if (!(treeAbove() && treeBelow() && treeToLeft() && treeToRight())) {
            wrapAroundWorld();
        }
        // Clara gains the ability to eat ghosts briefly when collecting mushrooms
        if (onMushroom()) {
            removeMushroom();
            makeScared();
        }
        if (onLeaf()) {
            removeLeaf();
            playLeafEatenSound();
        }
        safeMove();
    }
}