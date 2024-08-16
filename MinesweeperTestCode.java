import tester.*;

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
