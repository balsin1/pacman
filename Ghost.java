/**
 * Ghost Class
 * 
 * Available functions (see Assignment document for explanations on what each function does):
 * treeFront, treeAbove, treeBelow, treeToLeft, treeToRight,
 * getDirection, setDirection,
 * move,
 * isScared,
 * animate, animateDead, animateScared,
 * getClara, getGhostHealer,
 * isAboveMe, isBelowMe, isToMyLeft, isToMyRight,
 * makeClaraDead,
 * playGhostEatenSound,
 * isPacmanIntroStillPlaying,
 * wrapAroundWorld
 */
class Ghost extends Character
{
    //Add and initialise Ghost variables here
    /**
     * Act method, runs on every frame
     */
    int counter;
    boolean turn;
    boolean intersection;
    boolean dead;
    
public void act()
{
    //Make the Ghost do things here
    if (!isPacmanIntroStillPlaying()) {
        // The ghost animations are called every time the act method is run
        if (counter == 0) {
            if (isScared() == false && dead == false) {
                animate();
                counter = 0;
            }
            if (isScared() == true && dead == false) {
                animateScared();
                counter = 0;
            }
            if (dead == true) {
                animateDead();
                counter = 0;
            }
        } else {
            counter++;
        }
        ghostInput();
    }

}


//Give the Ghost functions here
void ghostInput()
{
    if (intersects(getClara())) {
        // If a ghost encounters Clara while not scared, Clara will die
        if (isScared() == false && dead == false) {
            makeClaraDead();
        }
        // If a ghost encounters Clara while scared, the ghost will die
        if (isScared() == true && dead == false) {
            animateDead();
            playGhostEatenSound();
            dead = true;
        }
    }

    if (intersects(getGhostHealer())) {
        // If a ghost encounts a ghost healer, it will revive and exit the base
        dead = false;
        setDirection("up");
    }
    // Ghost enters scared mode when Clara has eaten a mushroom
    if (isScared() == true && dead == false) {
        animateScared();
    }
    if (!treeAbove() && !treeBelow() && !treeToLeft() && !treeToRight()) {
    // Prevents ghosts from exiting the world
        wrapAroundWorld();
        if (!treeAbove() && !treeBelow() && !treeToLeft() && !treeToRight() && dead == false) {
            intersection = true;
            // When at intersections:
            if (intersection = true) {
                /* Travelling upwards ghosts will scan to the left and right for Clara,
                    if found ghosts will follow her direction or avoid her direction 
                    if in scared mode, if not found ghosts will continue moving upwards */
                if (getDirection() == "up") {
                    scanClaraFromSouth();
                    intersection = false;
                } else {
                    // Travelling downwards ghosts scan left and right for Clara
                    if (getDirection() == "down") {
                        scanClaraFromNorth();
                        intersection = false;
                    } else {
                        // Travelling downwards ghosts scan up and and for Clara
                        if (getDirection() == "left") {
                            scanClaraFromEast();
                            intersection = false;
                        } else {
                            // Travelling downwards ghosts scan up and down for Clara
                            if (getDirection() == "right") {
                                scanClaraFromWest();
                                intersection = false;
                            }
                        }
                    }
                }
            }
        }
    }
    // Ghosts encountering trees when dead, see void
    if (treeFront() && dead == false) {
        if (getDirection() == "up" || getDirection() == "down") {
            treeFrontUpOrDown();
        } else {
            if (getDirection() == "left" || getDirection() == "right") {
                treeFrontLeftOrRight();
            }
        }
    }
    // Ghosts encountering trees when dead, see void
    if (treeFront() && dead == true) {
        if (getDirection() == "up" || getDirection() == "down") {
            deadTreeFrontUpOrDown();
        } else {
            if (getDirection() == "left" || getDirection() == "right") {
                deadTreeFrontLeftOrRight();
            }
        }
    }
    if (dead == true && isBelowMe(getGhostHealer()) == true) {
        /* Upon dying, ghosts will make more frequent turns based on their direction and 
            the position of the ghost healer */
        if (getDirection() == "left" && !treeBelow() && treeAbove()) {
            setDirection("down");
        } else {
            if (getDirection() == "right" && !treeBelow() && treeAbove()) {
                setDirection("down");
            } else {
                if (getDirection() == "up") {
                    if (!treeToRight() && treeToLeft()) {
                        setDirection("right");
                    } else {
                        if (!treeToLeft() && treeToRight()) {
                            setDirection("left");
                        }
                    }
                }
            }
        }

    }
    // See void
    returnToBase();
    safeMove();
}
void safeMove()
{
    if (!treeFront() && isScared() == false) {
        move(3);
    } else {
        // Ghosts move slightly faster when in scared mode
        if (!treeFront() && isScared() == true) {
            move(4);
        }
    }
}
void treeFrontUpOrDown()
{
    // When encountering trees going upwards or downwards:
    if (treeToLeft() && treeToRight()) {
        // If there are trees on both sides, ghosts will turn backwards
        if (getDirection() == "up") {
            setDirection("down");
        }
        if (getDirection() == "down") {
            setDirection("up");
        }
    } else {
        if (!treeToLeft() && !treeToRight()) {
            /* If there are no trees on both sides, based on the random number generated, 
            if even - ghosts will turn left, if odd - ghosts will turn right */
            int i = Clara.getRandomNumber(10);
            if (i % 2 == 0) {
                setDirection("left");
            }
            if (i % 2 != 0) {
                setDirection("right");
            }
        } else {
            if (!treeToLeft()) {
                // If there is a tree on the right only ghosts will turn left
                setDirection("left");
            } else {
                if (!treeToRight()) {
                    // If there is a tree on the right only ghosts will turn left
                    setDirection("right");
                }
            }
        }
    }
}
void treeFrontLeftOrRight()
{
    // When encountering trees going left or right:
    if (treeAbove() && treeBelow()) {
        // If there are trees on both sides, ghosts will turn backwards
        if (getDirection() == "left") {
            setDirection("right");
        }
        if (getDirection() == "right") {
            setDirection("left");
        }
    } else {
        if (!treeAbove() && !treeBelow()) {
            /* If there are no trees above or below, based on the random number generated, 
            if even - ghosts will turn upwards, if odd - ghosts will turn downwards */
            int j = Clara.getRandomNumber(10);
            if (j % 2 == 0) {
                setDirection("up");
            }
            if (j % 2 != 0) {
                setDirection("down");
            }
        } else {
            if (!treeAbove()) {
                // If there is only a tree below ghosts will turn up
                setDirection("up");
            } else {
                if (!treeBelow()) {
                    // If there is only a tree above ghosts will down
                    setDirection("down");
                }
            }
        }
    }
}
void scanClaraFromSouth()
{
    // As noted above x4
    if (isToMyLeft(getClara()) == true && isAboveMe(getClara()) == false) {
        if (isScared() == false) {
            setDirection("left");
        }
        if (isScared() == true) {
            setDirection("right");
        }
    } else {
        if (isToMyRight(getClara()) == true && isAboveMe(getClara()) == false) {
            if (isScared() == false) {
                setDirection("right");
            }
            if (isScared() == true) {
                setDirection("left");
            }
        } else {
            setDirection("up");
        }

    }
}
void scanClaraFromNorth()
{
    if (isToMyLeft(getClara()) == true && isBelowMe(getClara()) == false) {
        if (isScared() == false) {
            setDirection("left");
        }
        if (isScared() == true) {
            setDirection("right");
        }
    } else {
        if (isToMyRight(getClara()) == true && isBelowMe(getClara()) == false) {
            if (isScared() == false) {
                setDirection("right");
            }
            if (isScared() == true) {
                setDirection("left");
            }
        } else {
            setDirection("down");

        }
    }
}
void scanClaraFromEast()
{
    if (isAboveMe(getClara()) == true && isToMyLeft(getClara()) == false) {
        if (isScared() == false) {
            setDirection("up");
        }
        if (isScared() == true) {
            setDirection("down");
        }
    } else {
        if (isBelowMe(getClara()) == true && isToMyLeft(getClara()) == false) {
            if (isScared() == false) {
                setDirection("down");
            }
            if (isScared() == true) {
                setDirection("up");
            }
        } else {
            setDirection("left");
        }
    }
}
void scanClaraFromWest()
{
    if (isAboveMe(getClara()) == true && isToMyRight(getClara()) == false) {
        if (isScared() == false) {
            setDirection("up");
        }
        if (isScared() == true) {
            setDirection("down");
        }
    } else {
        if (isBelowMe(getClara()) == true && isToMyRight(getClara()) == false) {
            if (isScared() == false) {
                setDirection("down");
            }
            if (isScared() == true) {
                setDirection("up");
            }
        } else {
            setDirection("right");
        }
    }
}
void scanGhostHealerFromSouth()
{
    // As noted below x4
    if (isToMyLeft(getGhostHealer()) == true && isAboveMe(getGhostHealer()) == false) {
        setDirection("left");
    } else {
        if (isToMyRight(getGhostHealer()) == true && isAboveMe(getGhostHealer()) == false) {
            setDirection("right");
        } else {
            setDirection("up");
        }

    }
}
void scanGhostHealerFromNorth()
{
    if (isToMyLeft(getGhostHealer()) == true && isBelowMe(getGhostHealer()) == false) {
        setDirection("left");
    } else {
        if (isToMyRight(getGhostHealer()) == true && isBelowMe(getGhostHealer()) == false) {
            setDirection("right");
        } else {
            setDirection("down");

        }
    }
}
void scanGhostHealerFromEast()
{
    if (isAboveMe(getGhostHealer()) == true && isToMyLeft(getGhostHealer()) == false) {
        setDirection("up");
    } else {
        if (isBelowMe(getGhostHealer()) == true && isToMyLeft(getGhostHealer()) == false) {
            setDirection("down");
        } else {
            setDirection("left");
        }
    }
}
void scanGhostHealerFromWest()
{
    if (isAboveMe(getGhostHealer()) == true && isToMyRight(getGhostHealer()) == false) {
        setDirection("up");
    } else {
        if (isBelowMe(getGhostHealer()) == true && isToMyRight(getGhostHealer()) == false) {
            setDirection("down");
        } else {
            setDirection("right");

        }
    }
}
void returnToBase()
{
    // Intersections upon dying:
    /* Ghosts now scan their sides based on direction of travel for ghost healer, 
    if present, they will change directions, else they will continue moving forward */
    if (!treeAbove() && !treeBelow() && !treeToLeft() && !treeToRight() && dead == true) {
        intersection = true;
        if (intersection = true) {
            if (getDirection() == "up") {
                scanGhostHealerFromSouth();
                intersection = false;
            } else {
                if (getDirection() == "down") {
                    scanGhostHealerFromNorth();
                    intersection = false;
                } else {
                    if (getDirection() == "left") {
                        scanGhostHealerFromEast();
                        intersection = false;
                    } else {
                        if (getDirection() == "right") {
                            scanGhostHealerFromWest();
                            intersection = false;
                        }
                    }
                }
            }
        }
    }
}
void deadTreeFrontUpOrDown()
{
    // If trees on both sides, ghosts turn backwards
    if (treeToLeft() && treeToRight()) {
        if (getDirection() == "up") {
            setDirection("down");
        }
        if (getDirection() == "down") {
            setDirection("up");
        }
    } else {
        /* If no trees on either side, ghosts scan both sides for ghost healer and turn if
        applicable, else a random number is generated to make a random choice for turning */
        if (!treeToLeft() && !treeToRight()) {
            if (isToMyLeft(getGhostHealer()) == true) {
                setDirection("left");
            } else {
                if (isToMyRight(getGhostHealer()) == true) {
                    setDirection("right");
                } else {
                    int k = Clara.getRandomNumber(10);
                    if (k % 2 == 0) {
                        setDirection("left");
                    }
                    if (k % 2 != 0) {
                        setDirection("right");
                    }
                }
            }
        } else {
            // If only tree to right, ghosts turn left
            if (!treeToLeft()) {
                setDirection("left");
            } else {
                // If only tree to left, ghosts turn right
                if (!treeToRight()) {
                    setDirection("right");
                }
            }
        }
    }
}
void deadTreeFrontLeftOrRight()
{
    // If trees above and below, ghosts turn backwards
    if (treeAbove() && treeBelow()) {
        if (getDirection() == "left") {
            setDirection("right");
        }
        if (getDirection() == "right") {
            setDirection("left");
        }
    } else {
        /* If no trees above or below, ghosts scan both for ghost healer and turn if
        applicable, else a random number is generated to make a random choice for turning */
        if (!treeAbove() && !treeBelow()) {
            if (isAboveMe(getGhostHealer()) == true) {
                setDirection("up");
            } else {
                if (isBelowMe(getGhostHealer()) == true) {
                    setDirection("down");
                } else {
                    int l = Clara.getRandomNumber(10);
                    if (l % 2 == 0) {
                        setDirection("up");
                    }
                    if (l % 2 != 0) {
                        setDirection("down");
                    }
                }
            }
        } else {
            // If only tree below, ghosts turn up
            if (!treeAbove()) {
                setDirection("up");
            } else {
                // If only tree above, ghosts turn down
                if (!treeBelow()) {
                    setDirection("down");
                }
            }
        }
    }
}

}
