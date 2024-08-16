import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

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
