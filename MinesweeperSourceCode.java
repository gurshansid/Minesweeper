import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;
import tester.*;

// class for initialization of the game
class MineSweeperGame extends World {
  int columns; // number of columns in game
  int rows; // number of rows in game 
  int mines; // number of mines in game  
  ArrayList<ArrayList<Cell>> board; // list of cells on the board 
  Random random; // random object for placing mines randomly
  int width; // width of game
  int height; // height of game
  boolean gameOver; // indicates if game is over or not
  boolean gameWon; // indicates if the game was won or not
  int gameScore; // keeps the score of the game

  // constructor for MineSweeperGame
  MineSweeperGame(int columns, int rows, int mines) {
    if (rows > 20) {
      throw new IllegalArgumentException("Too many rows, please have less than 21!");
    }
    else {
      this.rows = rows;
    }
    if (columns > 35) {
      throw new IllegalArgumentException("Too many columns, please have less than 36!");
    }
    else {
      this.columns = columns;
    }
    if (mines > rows * columns * 0.21) {
      throw new IllegalArgumentException("Too many mines!");
    }
    else {
      this.mines = mines;
    }

    board = new ArrayList<ArrayList<Cell>>();

    for (int r = 0; r < rows; r++) {
      board.add(new ArrayList<Cell>());
      for (int c = 0; c < columns; c++) {
        // default cell for board creation
        Cell defaultCell = new Cell();
        board.get(r).add(defaultCell); 
      }
    }


    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < columns; c++) {
        Cell currentCell = board.get(r).get(c);

        if (r == 0 && c == 0) { // top left
          currentCell.connect(board.get(r).get(c + 1));
          currentCell.connect(board.get(r + 1).get(c));
          currentCell.connect(board.get(r + 1).get(c + 1));
        }
        else if (r == rows - 1 && c == 0) { // bottom left
          currentCell.connect(board.get(r).get(c + 1));
          currentCell.connect(board.get(r - 1).get(c));
          currentCell.connect(board.get(r - 1).get(c + 1)); 
        }
        else if (r == 0 && c == columns - 1) { // top right
          currentCell.connect(board.get(r).get(c - 1));
          currentCell.connect(board.get(r + 1).get(c));
          currentCell.connect(board.get(r + 1).get(c - 1));
        }
        else if (r == rows - 1 && c == columns - 1) { //bottom right
          currentCell.connect(board.get(r).get(c - 1));
          currentCell.connect(board.get(r - 1).get(c));
          currentCell.connect(board.get(r - 1).get(c - 1));
        }
        else if (c == 0) { // middle left
          currentCell.connect(board.get(r - 1).get(c));
          currentCell.connect(board.get(r - 1).get(c + 1));
          currentCell.connect(board.get(r + 1).get(c));
          currentCell.connect(board.get(r + 1).get(c + 1));
          currentCell.connect(board.get(r).get(c + 1));
        }
        else if (c == columns - 1) { // middle right
          currentCell.connect(board.get(r + 1).get(c));
          currentCell.connect(board.get(r - 1).get(c));
          currentCell.connect(board.get(r + 1).get(c - 1));
          currentCell.connect(board.get(r - 1).get(c - 1));
          currentCell.connect(board.get(r).get(c - 1));
        }
        else if (r == 0) { //middle top
          currentCell.connect(board.get(r + 1).get(c - 1));
          currentCell.connect(board.get(r + 1).get(c));
          currentCell.connect(board.get(r).get(c - 1));
          currentCell.connect(board.get(r).get(c + 1));
          currentCell.connect(board.get(r + 1).get(c + 1));
        }
        else if (r == rows - 1) { //middle bottom
          currentCell.connect(board.get(r).get(c + 1));
          currentCell.connect(board.get(r).get(c - 1));
          currentCell.connect(board.get(r - 1).get(c));
          currentCell.connect(board.get(r - 1).get(c + 1));
          currentCell.connect(board.get(r - 1).get(c - 1));
        }
        else { //middle 
          currentCell.connect(board.get(r).get(c - 1));
          currentCell.connect(board.get(r + 1).get(c - 1));
          currentCell.connect(board.get(r + 1).get(c));
          currentCell.connect(board.get(r + 1).get(c + 1));
          currentCell.connect(board.get(r).get(c + 1));
          currentCell.connect(board.get(r - 1).get(c + 1));
          currentCell.connect(board.get(r - 1).get(c));
          currentCell.connect(board.get(r - 1).get(c - 1));
        }
      }
    }

    Random rand = new Random();

    // assigns random cells on the board as mines 
    while (this.mines > 0) {
      int r = rand.nextInt(rows);
      int c = rand.nextInt(columns);

      if (!board.get(r).get(c).hasMine) {
        board.get(r).get(c).makeMine();
        this.mines -= 1;
      }
    }
    width = 20 * columns; // width of game
    height = 20 * rows; // height of game
    gameOver = false; // initialize game as not over
    gameWon = false; // initialize game as not won
    gameScore = 0; // initialize game score as 0
  }

  //void method for testing random placement of mines 
  void mineTesting(int r, int c) {
    board.get(r).get(c).makeMine();
  }

  // draws scene
  public WorldScene makeScene() {
    WorldScene scene = new WorldScene(width, height);

    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < columns; c++) {      
        if (board.get(r).get(c).flagged && board.get(r).get(c).clicked) {
          scene.placeImageXY(new OverlayImage(
              new OverlayOffsetImage(new RotateImage(new EquilateralTriangleImage(8, 
                  OutlineMode.SOLID, Color.RED), 270), 3, 4, 
                  new RectangleImage(1, 15, "outline", Color.black)),
              new RectangleImage(20, 20, "outline", Color.black) ),((c * 20) + 10), 
              ((r * 20) + 10));
        }
        else if (board.get(r).get(c).howManyNeighborMines() == 0 && board.get(r).get(c).clicked 
            && !board.get(r).get(c).hasMine) {
          scene.placeImageXY(
              new RectangleImage(20, 20, "solid", Color.GRAY),((c * 20) + 10), ((r * 20) + 10));
        }
        else if (board.get(r).get(c).hasMine && board.get(r).get(c).clicked) {
          scene.placeImageXY(new OverlayImage(
              new OverlayImage(new OverlayImage(new StarImage(6, 7, 2, OutlineMode.SOLID, 
                  Color.ORANGE), 
                  new StarImage(11, 5, 3, OutlineMode.SOLID, Color.RED) ), new CircleImage(6, 
                      "solid", Color.BLACK)),
              new RectangleImage(20, 20, "outline", Color.RED)),((c * 20) + 10), 
              ((r * 20) + 10));

        }

        else if (board.get(r).get(c).howManyNeighborMines() > 0 && board.get(r).get(c).clicked 
            && !board.get(r).get(c).hasMine) {
          scene.placeImageXY(new OverlayImage(
              new TextImage(Integer.toString(board.get(r).get(c).howManyNeighborMines()), 
                  10, Color.BLACK),
              new RectangleImage(20, 20, "outline", Color.black)),((c * 20) + 10), 
              ((r * 20) + 10));
        }
        else {
          scene.placeImageXY(new RectangleImage(20, 20, "outline", Color.GRAY), 
              ((c * 20) + 10), ((r * 20) + 10));
        }
      }
    }

    // returns end screen(loss) if a mine is clicked
    if (this.gameOver) {
      this.endOfWorld("You Lost!");
    }
    // returns end screen(win) if a player beats game
    else if (this.gameWon) {
      this.endOfWorld("You Won!");
    }
    return scene;
  }

  // method to end world 
  public WorldScene lastScene(String msg) {
    WorldScene scene = new WorldScene(width, height);

    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < columns; c++) {      
        if (board.get(r).get(c).flagged && board.get(r).get(c).clicked) {
          scene.placeImageXY(new OverlayImage(
              new OverlayOffsetImage(new RotateImage(new EquilateralTriangleImage(8, 
                  OutlineMode.SOLID, Color.RED), 270), 3, 4, 
                  new RectangleImage(1, 15, "outline", Color.black)),
              new RectangleImage(20, 20, "outline", Color.black) ),((c * 20) + 10), 
              ((r * 20) + 10));
        }
        else if (board.get(r).get(c).howManyNeighborMines() == 0 && board.get(r).get(c).clicked 
            && !board.get(r).get(c).hasMine) {
          scene.placeImageXY(
              new RectangleImage(20, 20, "solid", Color.GRAY),((c * 20) + 10), ((r * 20) + 10));
        }
        else if (board.get(r).get(c).hasMine && board.get(r).get(c).clicked) {
          scene.placeImageXY(new OverlayImage(
              new OverlayImage(new OverlayImage(new StarImage(6, 7, 2, OutlineMode.SOLID, 
                  Color.ORANGE), 
                  new StarImage(11, 5, 3, OutlineMode.SOLID, Color.RED) ), new CircleImage(6, 
                      "solid", Color.BLACK)),
              new RectangleImage(20, 20, "outline", Color.RED)),((c * 20) + 10), 
              ((r * 20) + 10));
        }
        else if (board.get(r).get(c).hasMine) {
          scene.placeImageXY(new OverlayImage(new CircleImage(6, 
              "solid", Color.BLACK),
              new RectangleImage(20, 20, "outline", Color.RED)),((c * 20) + 10), 
              ((r * 20) + 10));
        }

        else if (board.get(r).get(c).howManyNeighborMines() > 0 && board.get(r).get(c).clicked 
            && !board.get(r).get(c).hasMine) {
          scene.placeImageXY(new OverlayImage(
              new TextImage(Integer.toString(board.get(r).get(c).howManyNeighborMines()), 
                  10, Color.BLACK),
              new RectangleImage(20, 20, "outline", Color.black)),((c * 20) + 10), 
              ((r * 20) + 10));
        }
        else {
          scene.placeImageXY(new RectangleImage(20, 20, "outline", Color.GRAY), 
              ((c * 20) + 10), ((r * 20) + 10));
        }
      }
    }

    if (this.gameOver) {
      TextImage lossMessage = new TextImage(msg, 
          20, FontStyle.BOLD, Color.RED);
      scene.placeImageXY(lossMessage, columns * 10, rows * 9);
      TextImage scoreMessage = new TextImage("Your Score Was: " + gameScore, 
          20, FontStyle.BOLD, Color.RED);
      scene.placeImageXY(scoreMessage, columns * 10, rows * 11);
    }
    // returns end screen(win) if a player beats game
    else if (this.gameWon) {
      TextImage winMessage = new TextImage(msg, 
          20, FontStyle.BOLD, Color.GREEN);
      scene.placeImageXY(winMessage, columns * 10, rows * 9);
      TextImage scoreMessage = new TextImage("Your Score Was: " + gameScore, 
          20, FontStyle.BOLD, Color.GREEN);
      scene.placeImageXY(scoreMessage, columns * 10, rows * 11);
    }
    return scene;
  }

  // on mouse click function for flood effect
  public void onMouseClicked(Posn pos, String buttonName) {

    Cell coordinates = 
        board.get((int)Math.floor((pos.y) / 20)).get((int)Math.floor((pos.x / 20)));    

    if (buttonName.equals("LeftButton") && !this.gameOver && !coordinates.flagged 
        && !coordinates.hasMine) {
      gameScore = gameScore + 10;
      coordinates.reveal();
    }
    else if (buttonName.equals("LeftButton") && !this.gameOver && coordinates.hasMine 
        && !coordinates.flagged) {
      coordinates.clicked = true;
      this.gameOver = true;
    }
    else if (buttonName.equals("RightButton") && !this.gameOver && !coordinates.flagged) {
      coordinates.clicked = true;
      coordinates.flagged = true;
    }
    else if (buttonName.equals("RightButton") && !this.gameOver && coordinates.flagged) {
      coordinates.flagged = false;
      coordinates.clicked = false;
    }

    // checks if game has been won
    int progress = 0; // checks how many tiles have been correctly found
    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < columns; c++) {
        if ((!board.get(r).get(c).hasMine 
            && board.get(r).get(c).clicked && !board.get(r).get(c).flagged)
            || (board.get(r).get(c).hasMine && !board.get(r).get(c).clicked 
                && board.get(r).get(c).flagged)) {
          progress++;
        }
      }
    }

    // checks if win condition has been met
    if (progress == columns * rows) {
      this.gameWon = true;
    }
  }
}

// class for a cell within the game
class Cell {
  boolean hasMine; // indicates if this cell has a mine
  ArrayList<Cell> neighbors; // list of neighboring cells
  boolean flagged; // indicates if this cell has been flagged
  boolean clicked; // indicates if this cell has been clicked

  //constructor for a Cell thats empty
  Cell() {
    this.hasMine = false; // determines if this cell has a mine or not
    neighbors = new ArrayList<Cell>();
    this.clicked = false;
    this.flagged = false;

  }

  //constructor for a Cell with boolean
  Cell(boolean hasMine) {
    this.hasMine = hasMine; // determines if this cell has a mine or not
    neighbors = new ArrayList<Cell>();
    this.clicked = false;
    this.flagged = false;

  }

  //constructor for a Cell with ArrayList
  Cell(ArrayList<Cell> neighbors) {
    this.hasMine = false; 
    this.neighbors = neighbors;
    this.clicked = false;
    this.flagged = false;
  }

  // reveals this cell and its neighbors for flood effect
  public void reveal() {
    if (!this.clicked) {
      this.clicked = true;
      if (!this.hasMine && !this.flagged && this.howManyNeighborMines() == 0) {
        for (int i = 0; i < this.neighbors.size(); i++) {
          neighbors.get(i).reveal();
        }
      } 
    }
  }

  // connects this cell to a given neighboring cell
  void connect(Cell c) {
    this.neighbors.add(c);
  }

  // makes this cell contain a mine
  void makeMine() {
    this.hasMine = true;
  }

  // checks how many neighbpr cells of this cell have a mine
  int howManyNeighborMines() {
    int count = 0;
    for (Cell c : this.neighbors) {
      if (c.hasMine) {
        count = count + 1;
      }
    }
    return count;
  }
}

//  tests for MineSweeprGame
class ExamplesMineSweeper {

  Cell cell0;
  Cell cell1;
  Cell cell2;
  Cell cell3;
  Cell cell4;
  Cell cell5;
  Cell cell6;
  Cell cell7;
  Cell cell8;

  MineSweeperGame ms1;


  World world0;


  Random random1;
  int seed1;

  Random random2;
  int seed2;


  void initData() {
    this.cell0 = new Cell();
    this.cell1 = new Cell();
    this.cell2 = new Cell();
    this.cell3 = new Cell();
    this.cell4 = new Cell();
    this.cell5 = new Cell();
    this.cell6 = new Cell();
    this.cell7 = new Cell();
    this.cell8 = new Cell();


    this.cell5.makeMine();
    this.cell6.makeMine();
    this.cell7.makeMine();
    this.cell8.makeMine();



    this.cell0.connect(cell1);
    this.cell0.connect(cell2);
    this.cell0.connect(cell3);
    this.cell0.connect(cell4);
    this.cell0.connect(cell5);
    this.cell0.connect(cell6);
    this.cell0.connect(cell7);
    this.cell0.connect(cell8);

    this.world0 = new MineSweeperGame(4, 4, 1);

    random1 = new Random(1);
    seed1 = random1.nextInt(30);
    random2 = new Random(2);
    seed2 = random2.nextInt(16);

    this.ms1 = new MineSweeperGame(4, 4, 1);

  }

  void initData1() {
    this.cell0 = new Cell();
    this.cell1 = new Cell();
    this.cell2 = new Cell();
    this.cell3 = new Cell();
    this.cell4 = new Cell();
    this.cell5 = new Cell();
    this.cell6 = new Cell();
    this.cell7 = new Cell();
    this.cell8 = new Cell();

  }

  void testConnect(Tester t) {
    // initialize data
    this.initData1();

    // modify data
    this.cell0.connect(cell1);
    this.cell0.connect(cell2);
    this.cell1.connect(cell0);
    this.cell1.connect(cell4);

    // check if changes occurred 
    t.checkExpect(this.cell0, new Cell(new ArrayList<Cell>(Arrays.asList(cell1, cell2))));
    t.checkExpect(this.cell1, new Cell(new ArrayList<Cell>(Arrays.asList(cell0, cell4))));

  }

  void testMakeMine(Tester t) {
    // initialize data
    this.initData1();

    // modify data
    this.cell0.makeMine();
    this.cell1.makeMine();

    // check if changes occurred 
    t.checkExpect(this.cell0, new Cell(true));
    t.checkExpect(this.cell1, new Cell(true));


  }

  //testing total price method
  boolean testHowManyNeighborMines(Tester t) {
    // initialize data
    this.initData();

    return t.checkExpect(this.cell0.howManyNeighborMines(), 4);
  }

  //testing constructor for MineSweeperGame fields rows columns and mines
  boolean testMineSweeperGameConstructor(Tester t) {
    return t.checkConstructorException(new IllegalArgumentException(
        "Too many rows, please have less than 21!"),
        "MineSweeperGame", 22, 40, 9)
        && t.checkConstructorException(new IllegalArgumentException(
            "Too many columns, please have less than 36!"),
            "MineSweeperGame", 38, 20, 9)
        && t.checkConstructorException(new IllegalArgumentException(
            "Too many mines!"),
            "MineSweeperGame", 20, 20, 250);
  }

  // tests for makeScene and lastScene
  void testMakeScene(Tester t) {
    //setting up the initial conditions
    initData();

    //places tiles on a world scene
    WorldScene scene = new WorldScene(80, 80);

    ArrayList<ArrayList<Cell>> board = new ArrayList<ArrayList<Cell>>();

    MineSweeperGame ms = new MineSweeperGame(4, 4, 2);

    for (int r = 0; r < 4; r++) {
      board.add(new ArrayList<Cell>());
      for (int c = 0; c < 4; c++) {
        // default cell for board creation
        Cell defaultCell = new Cell();
        board.get(r).add(defaultCell); 
      }
    }

    for (int r = 0; r < ms.rows; r++) {
      for (int c = 0; c < ms.columns; c++) {      
        if (board.get(r).get(c).flagged && board.get(r).get(c).clicked) {
          scene.placeImageXY(new OverlayImage(
              new OverlayOffsetImage(new RotateImage(new EquilateralTriangleImage(
                  8, OutlineMode.SOLID, Color.RED), 270), 3, 4, 
                  new RectangleImage(1, 15, "outline", Color.black)),
              new RectangleImage(20, 20, "outline", Color.black) ),
              ((c * 20) + 10), ((r * 20) + 10));
        }
        else if (board.get(r).get(c).howManyNeighborMines() == 0 
            && board.get(r).get(c).clicked && !board.get(r).get(c).hasMine) {
          scene.placeImageXY(
              new RectangleImage(20, 20, "solid", Color.GRAY),((c * 20) + 10), ((r * 20) + 10));
        }
        else if (board.get(r).get(c).hasMine && board.get(r).get(c).clicked) {
          scene.placeImageXY(new OverlayImage(
              new OverlayImage(new OverlayImage(new StarImage(6, 7, 2, 
                  OutlineMode.SOLID, Color.ORANGE), 
                  new StarImage(11, 5, 3, OutlineMode.SOLID, Color.RED) ), 
                  new CircleImage(6, "solid", Color.BLACK)),
              new RectangleImage(20, 20, "outline", Color.RED)),((c * 20) + 10), 
              ((r * 20) + 10));

        }

        else if (board.get(r).get(c).howManyNeighborMines() > 0 && board.get(r).get(c).clicked 
            && !board.get(r).get(c).hasMine) {
          scene.placeImageXY(new OverlayImage(
              new TextImage(Integer.toString(board.get(r).get(c).howManyNeighborMines()), 
                  10, Color.BLACK),
              new RectangleImage(20, 20, "outline", Color.black)),((c * 20) + 10), 
              ((r * 20) + 10));
        }
        else {
          scene.placeImageXY(new RectangleImage(20, 20, "outline", Color.GRAY), 
              ((c * 20) + 10), ((r * 20) + 10));
        }
      }
    }

    // returns end screen(loss) if a mine is clicked
    if (ms.gameOver) {
      ms.endOfWorld("You Lost!");
    }
    // returns end screen(win) if a player beats game
    else if (ms.gameWon) {
      ms.endOfWorld("You Won!");
    }

    //check that the makeScene method returns the correct scene
    t.checkExpect(ms.makeScene(), scene);

    // check to see if correct loss screen is displayed
    ms.gameOver = true;
    t.checkExpect(ms.makeScene(), scene);

    // check to see if correct win screen is displayed
    ms.gameOver = false;
    ms.gameWon = true;
    t.checkExpect(ms.makeScene(), scene);
  }

  void testRandomMinePlacement(Tester t) {
    //setting up the initial conditions
    this.initData();

    // checks to see if same random seed is produced every time
    t.checkExpect(seed1, 15);
    t.checkExpect(seed2, 11);

    // new world with a randomly placed mine
    MineSweeperGame world1 = new MineSweeperGame(30, 16, 1);

    // check to place mines in cell
    world1.mineTesting(seed1, seed2);

    // check to see if that cell was actually turned into a mine cell
    t.checkExpect(world1.board.get(seed1).get(seed2).hasMine, true);

    // check to see if another cell was not 
    t.checkExpect(world1.board.get(10).get(10).hasMine, false); 
  }

  //tests for the onMouseClicked method
  void testOnMouseClicked(Tester t) {

    // initialize data
    this.initData();


    // test for right click
    this.ms1.onMouseClicked(new Posn(40, 40), "RightButton"); 

    // check if changes occurred
    t.checkExpect(this.ms1.board.get(2).get(2).flagged, true);

    // test for left click
    this.ms1.onMouseClicked(new Posn(40, 40), "LeftButton");

    // check if changes occurred
    t.checkExpect(this.ms1.board.get(2).get(2).clicked, true);
  }

  //tests for the reveal method
  void testReveal(Tester t) {

    //  initialize data
    this.initData();

    // check to see that tile is not clicked
    t.checkExpect(this.cell0.clicked, false);

    //modify the cell to be clicked
    this.cell0.reveal();

    // check for changes
    t.checkExpect(this.cell0.clicked, true);
  }

  //renders the world
  void testBigBang(Tester t) {
    MineSweeperGame world = new MineSweeperGame(30, 16, 99);
    int worldWidth = world.width;
    int worldHeight = world.height;
    world.bigBang(worldWidth, worldHeight);
  }
}
