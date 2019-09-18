# Chinese Checkers
## Background on the game and rules
### Background on the game
<p align="justify">Chinese checkers (US and Canadian spelling) or Chinese chequers (UK spelling) is a strategy board game of German origin (named "Sternhalma") which can be played by two, three, four, or six people, playing individually or with partners. The game is a modern and simplified variation of the game Halma.</p>

<p align="justify">The objective is to be first to race all of one's pieces across the hexagram-shaped board into "home"—the corner of the star opposite one's starting corner—using single-step moves or moves that jump over other pieces. The remaining players continue the game to establish second-, third-, fourth-, fifth-, and last-place finishers. The rules are simple, so even young children can play.</p>
<span align="center"><img src="/docs/fig1.png" alt="Chinese checkers board"><br>
<font size="1">Figure 1 - Chinese Checkers Board with 6 player sets</font></span>
<p align="justify">Despite its name, the game isn't a variation of checkers, nor did it originate in China or any part of Asia (whereas the game 象棋 xiangqi, or "Chinese chess", is from China). The game was invented in Germany in 1892 under the name "Stern-Halma" as a variation of the older American game Halma. The "Stern" (German for star) refers to the board's star shape (in contrast to the square board used in Halma).</p>

<p align="justify">The name "Chinese Checkers" originated in the United States as a marketing scheme by Bill and Jack Pressman in 1928. The Pressman company's game was originally called "Hop Ching Checkers".</p>

<p align="justify">The game was introduced to Chinese-speaking regions mostly by the Japanese.</p>

### Rules
#### The goal of the game
<p align="justify">To race all one's pieces into the star corner on the opposite side of the board before opponents do the same.</p>

#### Structure of the board
<p align="justify">The game goard is made of 121 cells ordered in the shape of a six pointed star (Star of David), while the small triangles that make its corners are made of 10 cells each.</p>
<span align="center"><img alt="Chinese checkers board" src="/docs/fig2.png"></span>

#### Starting layout
<p align="justify">Each player sets his 10 pieces in one of the board corners.
When two players play, like in our case, the pieces go in opposing corners of the board.</p>

#### Game progression
<p align="justify">Players take turns moving a single piece, either by moving one step in any direction to an adjacent empty space, or by jumping in one or any number of available consecutive hops over other single pieces. A player may not combine hopping with a single-step move – a move consists of one or the other. There is no capturing in Chinese Checkers, so hopped pieces remain active and in play.</p>
<span align="center"><img alt="Chinese checkers piece movement options" src="/docs/fig3.png"></span>

#### The winner
<p>The first to move all 10 of his game pieces to the opposing base.</p>

------------

## Data structure
<p align="justify">The game board is represented with an undirected and unweighted graph, consisting of 121 vertexes (one for each cell on the board). The maximum degree for each vertex is 6, the minimum degree is 2, and it is connected with an edge to each of its neighbours.
In addition, the vertexes are placed in a matrix using which we get the location of the vertex on the board and print the vertex on screen.</p>
<span align="center"><img alt="The empty matrix." src="/docs/fig4.png"></span>

<span align="center"><img alt="The matrix with the game pieces." src="/docs/fig5.png"></span>

```java
public class GraphFacilities {
	public static final int W=25, H=17;
	public static CellVertex[][] vertexMat = new CellVertex[H][W];
	public static Graph<CellVertex, CCEdge> g = new SimpleGraph<CellVertex, CCEdge>(CCEdge.class);
}
```
<p align="justify">Every vertex contains the following data:<ul><li>A field that contains info about the occupation of the cell:<ul><li>0 - Empty cell.</li><li>1 - The cell is occupied with a piece belonging to the AI player.</li><li>2 - The cell is occupied with a piece belonging to the human player.</li></ul><li>A field that contains the position of the vertex in the matrix.</li>
</ul></p>

```java
public class CellVertex {
	public int content; // The content of the vertex
	private Point l; // The location of the vertex in the graph
}
```
<p align="justify">At the start, the program creates an array of all possible edges and updates the graph accordingly.</p>

<p align="justify">On every turn, the player (human or AI) get all the places where they can move the chosen piece.</p>

<p align="justify">They choose the destination cell, and the program checks if the current player won.</p>

<p align="justify">If he didn't win the program updates the active edges and passes the turn to the opponent.</p>
The reasons to use a graph over other data structures are:<ul><li>It is possible to find all possible jumps from a given vertex at O(1) complexity.</li><li>It is possible to calculate a path at O(E log(V)) complexity.</li></ul></p>

------------

## The main algorithm
### AI move strategy
<span align="justify">1. Calculate amount of pieces not in destination base ([Function 1](#function-1-countoutsidesoldiers)).</span><br>
<span align="justify">2. If only one piece isn't in the destination base:</span><br>
<span align="justify">2.1. For every piece belonging to the AI:</span><br>
<span align="justify">2.1.1. If the pieace isn't in the destination base:</span><br>
<span align="justify">2.1.1.1. For every cell in the destination base:</span><br>
<span align="justify">2.1.1.1.1. If the cell is empty:</span><br>
<span align="justify">2.1.1.1.1.1. Set the cell as path end.</span><br>
<span align="justify">2.1.1.2. Calculate the shortest path from piece location to path end location.</span><br>
<span align="justify">2.1.1.3. Set move start location as the first vertex in the path.</span><br>
<span align="justify">2.1.1.4. Set move end location as the second vertex in the path.</span><br>
<span align="justify">2.1.1.5. Do the move ([Function 9](#function-9-move)).</span><br>
<span align="justify">2.1.1.6. Update the piece location to the new location.</span><br>
<span align="justify">2.1.1.7. End turn ([Function 7](#function-7-endturn)).</span><br>
<span align="justify">3. If there is a piece able to perform an S[<sup>1</sup>](#f_n_1) move ([Function 2](#function-2-is-s-turnexist)):</span><br>
<span align="justify">3.1. Find the index of the piece in the AI pieces array ([Function 3](#function-3-findcpuplayerindex)).</span><br>
<span align="justify">3.2. Set the move start location to the piece location.</span><br>
<span align="justify">3.3. Set the move end location to the S move end location (4 rows forward, same column).</span><br>
<span align="justify">3.4. Do the move ([Function 9](#function-9-move)).</span><br>
<span align="justify">3.5. Update the piece location to the new location.</span><br>
<span align="justify">3.6. End turn ([Function 7](#function-7-endturn)).</span><br>
<span align="justify">4. Else if the left outermost piece[<sup>2</sup>](#f_n_2) is in its original place:</span><br>
<span align="justify">4.1. Set its location as the move start location.</span><br>
<span align="justify">4.2. Set the move end location (1 row down, 1 column right).</span><br>
<span align="justify">4.3. Do the move ([Function 9](#function-9-move)).</span><br>
<span align="justify">4.4. Update the piece location to the new location.</span><br>
<span align="justify">4.5. End turn ([Function 7](#function-7-endturn)).</span><br>
<span align="justify">5. Else if the right outermost piece[<sup>2</sup>](#f_n_2) is in its original place:</span><br>
<span align="justify">5.1. Set its location as the move start location.</span><br>
<span align="justify">5.2. Set the move end location (1 row down, 1 column left).</span><br>
<span align="justify">5.3. Do the move ([Function 9](#function-9-move)).</span><br>
<span align="justify">5.4. Update the piece location to the new location.</span><br>
<span align="justify">5.5. End turn ([Function 7](#function-7-endturn)).</span><br>
<span align="justify">6. Else:</span><br>
<span align="justify">6.1. Find farthest jump ([Function 4](#function-4-findfarthestjump)).</span><br>
<span align="justify">6.2. If jump exists:</span><br>
<span align="justify">6.2.1. Set move start location to jumping piece location.</span><br>
<span align="justify">6.2.2. Set move end location to jump end location.</span><br>
<span align="justify">6.2.3. Do the move ([Function 9](#function-9-move)).</span><br>
<span align="justify">6.2.4. Update the piece location to the new location.</span><br>
<span align="justify">6.2.5. End turn ([Function 7](#function-7-endturn)).</span><br>
<span align="justify">6.3. Else:</span><br>
<span align="justify">6.3.1. For every piece from the rearmost:</span><br>
<span align="justify">6.3.1.1. Set its location as the move start location.</span><br>
<span align="justify">6.3.1.2. Reset board ([Function 8](#function-8-resetboard)).</span><br>
<span align="justify">6.3.1.3. Find optional moves for the piece ([Function 5](#function-5-optionalplays)).</span><br>
<span align="justify">6.3.1.4. If it can move forward <b>and</b> centerward:</span><br>
<span align="justify">6.3.1.4.1. Set move end location to the said location.</span><br>
<span align="justify">6.3.1.4.2. Do the move ([Function 9](#function-9-move)).</span><br>
<span align="justify">6.3.1.4.3. Update the piece location to the new location.</span><br>
<span align="justify">6.3.1.4.4. End turn ([Function 7](#function-7-endturn)).</span><br>
<span align="justify">6.3.1.5. Else if it is in the center and can move forward:</span><br>
<span align="justify">6.3.1.5.1. Set move end location to the said location.</span><br>
<span align="justify">6.3.1.5.2. Do the move ([Function 9](#function-9-move)).</span><br>
<span align="justify">6.3.1.5.3. Update the piece location to the new location.</span><br>
<span align="justify">6.3.1.5.4. End turn ([Function 7](#function-7-endturn)).</span><br>
<span align="justify">6.3.1.6. Else if it move Centerward:</span><br>
<span align="justify">6.3.1.6.1. Set move end location to the said location.</span><br>
<span align="justify">6.3.1.6.2. Do the move ([Function 9](#function-9-move)).</span><br>
<span align="justify">6.3.1.6.3. Update the piece location to the new location.</span><br>
<span align="justify">6.3.1.6.4. End turn ([Function 7](#function-7-endturn)).</span><br>
<span align="justify">6.3.1.7. Else if it can move forward:</span><br>
<span align="justify">6.3.1.7.1. Set move end location to the said location.</span><br>
<span align="justify">6.3.1.7.2. Do the move ([Function 9](#function-9-move)).</span><br>
<span align="justify">6.3.1.7.3. Update the piece location to the new location.</span><br>
<span align="justify">6.3.1.7.4. End turn ([Function 7](#function-7-endturn)).</span><br>

<small id="f_n_1">1. An s move is a jump move involving 4 pieces belonging to the player. The rearmost piece jumps twice so its final position is 4 rows forward and in the same column.</small><br>
<small id="f_n_2">2. The outermost piece is the piece located in one of the base corners of the triangle (tangent to the neutral zone) <b>at game start</b>.</small><br>

------------

### Function 1: countOutsideSoldiers
<span align="justify">1. Initialize a counter to 10.</span><br>
<span align="justify">2. For every cell in the destination base:</span><br>
<span align="justify">2.1. If the cell has an AI piece:</span><br>
<span align="justify">2.1.1. Decrement counter.</span><br>
<span align="justify">3. Return counter.</span><br>
### Function 2: is_S_TurnExist
<span align="justify">1. For every cell on the board starting rearmost:</span><br>
<span align="justify">1.1. If the cell contains an AI piece:</span><br>
<span align="justify">1.2. Reset board ([Function 8](#function-8-resetboard)).</span><br>
<span align="justify">1.3. Find optional moves for the piece ([Function 5](#function-5-optionalplays)).</span><br>
<span align="justify">1.4. If an AI piece exists at (same column, 2 rows down) <b>and</b> a target exists at (same column, 4 rows down) <b><i>and either</i></b> an AI piece exists at (1 column left, 1 row down) <b>and</b> at (1 column left, 3 rows down) <b>and</b> a target exists at (2 columns left, 2 rows down) <b><i>or</i></b> an AI piece exists at (1 column right, 1 row down) <b>and</b> at (1 column right, 3 rows down) <b>and</b> a target exists at (2 columns right, 2 rows down):</span><br>
<span align="justify">1.4.1. Return soldier position.</span><br>
<span align="justify">2. Return null.</span><br>
### Function 3: findCpuPlayerIndex
<span align="justify">1. If passed location is a cell:</span><br>
<span align="justify">1.1. For every AI piece:</span><br>
<span align="justify">1.1.1. If the piece is in the passed location:</span><br>
<span align="justify">1.1.1.1. Return piece index.</span><br>
<span align="justify">2) Return -1.</span><br>
### Function 4: findFarthestJump
<span align="justify">1. Initalize "longest jump" variable to null.</span><br>
<span align="justify">2. For every AI piece from rearmost:</span><br>
<span align="justify">2.1. Reset board ([Function 8](#function-8-resetboard)).</span><br>
<span align="justify">2.2. Find its optional jumps ([Function 6](#function-6-optionalplayslen3)).</span><br>
<span align="justify">2.3. For every possible jump forwards <b>and/or</b> centerwards:</span><br>
<span align="justify">2.3.1. If "longest jump" is null:</span><br>
<span align="justify">2.3.1.1. If the jump advances the piece forwards <b>and/or</b> centerwards:</span><br>
<span align="justify">2.3.1.1.1. Set the jump as the longest jump.</span><br>
<span align="justify">2.3.2. Else if the jump advances the piece farther than the longest jump <b>or</b> to the same row but closer to the center than the longest jump:</span><br>
<span align="justify">2.3.2.1. Set the jump as the longest jump.</span><br>
<span align="justify">3. Return the longest jump.</span><br>
### Function 5: optionalPlays
<span align="justify">1. Create empty location set.</span><br>
<span align="justify">2. For every edge outgoing from the passed location:</span><br>
<span align="justify">2.1. If the other cell is empty:</span><br>
<span align="justify">2.1.1. Set the cell as possible target.</span><br>
<span align="justify">2.1.2. Add cell location to location set.</span><br>
<span align="justify">2.1.3 If the edge is a jump:</span><br>
<span align="justify">2.1.3.1. Find all optional jumps from the passed location ([Function 6](#function-6-optionalplayslen3)).</span><br>
<span align="justify">2.1.3.2. Add all jumps to location set.</span><br>
<span align="justify">3. Return location set.</span><br>
### Function 6: optionalPlaysLen3
<span align="justify">1. Create empty location set.</span><br>
<span align="justify">2. For every edge outgoing from the passed location:</span><br>
<span align="justify">2.1. If the other cell is empty <b>and</b> if the edge is a jump:</span><br>
<span align="justify">2.1.1. Set the cell as possible target.</span><br>
<span align="justify">2.1.2. Add cell location to location set.</span><br>
<span align="justify">2.1.3. Find all optional jumps from the passed location ([Function 6](#function-6-optionalplayslen3)).</span><br>
<span align="justify">2.1.4. Add all jumps to location set.</span><br>
<span align="justify">3. Return location set.</span><br>
### Function 7: endTurn
<span align="justify">1. Reset board ([Function 8](#function-8-resetboard)).</span><br>
<span align="justify">2. If no one won yet:</span><br>
<span align="justify">2.1. Switch active player.</span><br>
<span align="justify">2.2. Reset touch.</span><br>
### Function 8: resetBoard
<span align="justify">1. For every graph cell:</span><br>
<span align="justify">1.1. If it doesn't contain a piece but isn't empty (Past piece or a target):</span><br>
<span align="justify">1.1.1. Empty the cell.</span><br>
<span align="justify">2. Update graph edges ([Function 10](#function-10-updategraph)).</span><br>
### Function 9: Move
<span align="justify">1. Set move end location to current player's piece.</span><br>
<span align="justify">2. Set move start location to past piece.</span><br>
### Function 10: updateGraph
<span align="justify">1. For every edge in the edges array:</span><br>
<span align="justify">1.1. If the edge connects 2 adjacent cells <i>and</i> the edge doesn't exist:</span><br>
<span align="justify">1.1.1. Add the edge.</span><br>
<span align="justify">1.2. Else if the edge jumps over an occupied cell:</span><br>
<span align="justify">1.2.1. Add the edge.</span><br>
<span align="justify">1.3. Else if the edge exists:</span><br>
<span align="justify">1.3.1. Delete the edge.</span><br>
### Function 11: win
<span align="justify">1. Initialize a boolian variable for each player to <i>true</i>.</span><br>
<span align="justify">2. For each cell in the home bases of the players:</span><br>
<span align="justify">2.1. If the cell is occupied with an opponent piece:</span><br>
<span align="justify">2.1.1. Set the opponent's variable to itself.</span><br>
<span align="justify">2.2. Else:</span><br>
<span align="justify">2.2.1 Set the opponent's variable to <i>false</i></span>
<span align="justify">3. If the human player's variableis <i>true</i>:</span><br>
<span align="justify">3.1. Show appropriate message.</span><br>
<span align="justify">3.2. Return <i>true</i>.</span><br>
<span align="justify">4. If the AI player's variableis <i>true</i>:</span><br>
<span align="justify">4.1. Show appropriate message.</span><br>
<span align="justify">4.2. Return <i>true</i>.</span><br>
<span align="justify">5. Return <i>false</i></span><br>

------------

## The code
### AI:
```java
public void AI() {
	int i;
	// If only one player is outside
	if (countOutsideSoldiers() == 1) {
		CellVertex dest = null;
		for (i = 0; i < cpuSoldiers.length; i++) {
			if (cpuSoldiers[i].y < 13) {
				for (int h = 13; h < GraphFacilities.H; h++) {
					for (int j = 9 + (h - 13); j < 16 - (h - 13); j += 2) {
						if (GraphFacilities.vertexMat[h][j].content == PlayerAffiliation.NONE) {
							dest = GraphFacilities.vertexMat[h][j];
						}
					}
				}
				List<CellVertex> path = GraphFacilities.findShortestPathLength(
					GraphFacilities.vertexMat[cpuSoldiers[i].y][cpuSoldiers[i].x], dest).getVertexList();
				setTx(path.get(0).getLocation().x);
				setTy(path.get(0).getLocation().y);
				Move(path.get(1).getLocation().y, path.get(1).getLocation().x);
				cpuSoldiers[i].setLocation(path.get(1).getLocation());
				return;
			}
		}
	}
	// If there's an S turn available
	if ((i = findCpuPlayerIndex(is_S_TurnExist())) > -1) {
		setTx(cpuSoldiers[i].x);
		setTy(cpuSoldiers[i].y);
		Move(cpuSoldiers[i].y + 4, cpuSoldiers[i].x);
		cpuSoldiers[i].y += 4;
	} else {
		if (cpuSoldiers[6].getLocation().equals(new Point(9, 3))
				&& GraphFacilities.vertexMat[4][10].content != PlayerAffiliation.HUMAN_PLAYER
				&& GraphFacilities.vertexMat[4][10].content != PlayerAffiliation.CPU_PLAYER) {
			setTx(9);
			setTy(3);
			Move(4, 10);
			cpuSoldiers[6].setLocation(10, 4);
		} else if (cpuSoldiers[9].getLocation().equals(new Point(15, 3))
				&& GraphFacilities.vertexMat[4][14].content != PlayerAffiliation.HUMAN_PLAYER
				&& GraphFacilities.vertexMat[4][14].content != PlayerAffiliation.CPU_PLAYER) {
			setTx(15);
			setTy(3);
			Move(4, 14);
			cpuSoldiers[9].setLocation(14, 4);
		} else {
			CCEdge jump = findFarthestJump();
			if (jump != null) {
				setTx(jump.getSrcVertx().getLocation().x);
				setTy(jump.getSrcVertx().getLocation().y);
				Move(jump.getDestVertx().getLocation().y, jump.getDestVertx().getLocation().x);
				cpuSoldiers[findCpuPlayerIndex(jump.getSrcVertx().getLocation())].setLocation(jump.getDestVertx().getLocation());
			} else {
				for (i = 0; i < GraphFacilities.H; i++) {
					for (i = 0; i < GraphFacilities.H; i++) {
						for (int j = 0; j < GraphFacilities.W; j++) {
							if (GraphFacilities.vertexMat[i][j] != null
									&& GraphFacilities.vertexMat[i][j].content == PlayerAffiliation.CPU_PLAYER) {
								setTx(j);
								setTy(i);
								resetBoard();
								Set<Point> points = optinalPlays(tx, ty);
								Iterator<Point> it = points.iterator();
								while (it.hasNext()) {
									Point p = it.next();
									if ((p.y > ty
											&& ((Math.abs(tx - GraphFacilities.W / 2) > Math.abs(p.x - GraphFacilities.W / 2))
											|| (Math.abs(tx - GraphFacilities.W / 2) == 0)))) {
										Move(p.y, p.x);
										cpuSoldiers[findCpuPlayerIndex(new Point(tx, ty))].setLocation(p);
										return;
									}
								}
								it = points.iterator();
								while (it.hasNext()) {
									Point p = it.next();
									if ((p.y == ty && Math.abs(tx - GraphFacilities.W / 2) > Math.abs(p.x - GraphFacilities.W / 2))) {
										Move(p.y, p.x);
										cpuSoldiers[findCpuPlayerIndex(new Point(tx, ty))].setLocation(p);
										return;
									}
								}
								it = points.iterator();
								while (it.hasNext()) {
									Point p = it.next();
									if (p.y > i) {
										Move(p.y, p.x);
										cpuSoldiers[findCpuPlayerIndex(new Point(tx, ty))].setLocation(p);
										return;
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
```
### countOutsideSoldiers:
```java
private int countOutsideSoldiers() {
	int counter = 10;
	for (int i = 13; i < GraphFacilities.H; i++) {
		for (int j = 9 + (i - 13); j < 16 - (i - 13); j += 2) {
			if (GraphFacilities.vertexMat[i][j].content == PlayerAffiliation.CPU_PLAYER) {
				counter--;
			}
		}
	}
	return counter;
}
```
### Move:
```java
public void Move(int y, int x) {
	GraphFacilities.vertexMat[y][x].content = player;
	GraphFacilities.vertexMat[ty][tx].content = 99;
}
```
### is_S_TurnExist:
```java
private Point is_S_TurnExist() {
	for (int i = GraphFacilities.vertexMat.length - 1; i >= 0; i--) {
		for (int j = GraphFacilities.vertexMat[i].length - 1; j >= 0; j--) {
			if (GraphFacilities.vertexMat[i][j] != null && GraphFacilities.vertexMat[i][j].content == PlayerAffiliation.CPU_PLAYER) {
				resetBoard();
				optinalPlays(j, i);
				if (i + 4 < GraphFacilities.vertexMat.length
						&& GraphFacilities.vertexMat[i + 4][j] != null
						&& j - 1 > 0 && j + 1 < GraphFacilities.vertexMat[i].length
						&& GraphFacilities.vertexMat[i + 2][j].content == PlayerAffiliation.CPU_PLAYER
						&& GraphFacilities.vertexMat[i + 4][j].content == PlayerAffiliation.POSSIBLE_CPU_TARGET
						&& ((GraphFacilities.vertexMat[i + 1][j + 1].content == PlayerAffiliation.CPU_PLAYER
						&& GraphFacilities.vertexMat[i + 3][j + 1].content == PlayerAffiliation.CPU_PLAYER
						&& GraphFacilities.vertexMat[i + 2][j + 2].content == PlayerAffiliation.POSSIBLE_CPU_TARGET)
						|| (GraphFacilities.vertexMat[i + 1][j - 1].content == PlayerAffiliation.CPU_PLAYER
						&& GraphFacilities.vertexMat[i + 3][j - 1].content == PlayerAffiliation.CPU_PLAYER
						&& GraphFacilities.vertexMat[i + 2][j - 2].content == PlayerAffiliation.POSSIBLE_CPU_TARGET))) {
					return new Point(j, i);
				}
			}
		}
	}
	return null;
}
```
### findCpuPlayerIndex:
```java
private int findCpuPlayerIndex(Point p) {
	if (p != null) {
		for (int i = 0; i < cpuSoldiers.length; i++) {
			if (cpuSoldiers[i].equals(p)) {
				return i;
			}
		}
	}
	return -1;
}
```
### endTurn:
```java
public void endTurn() {
	resetBoard();
	if (!win()) {
		player = (player == PlayerAffiliation.CPU_PLAYER) ? PlayerAffiliation.HUMAN_PLAYER : PlayerAffiliation.CPU_PLAYER;
		ty = 0;
		tx = 0;
	}
}
```
### resetBoard:
```java
public void resetBoard() {
	for (int i = 0; i < H; i++) {
		for (int j = 0; j < W; j++) {
			if (GraphFacilities.vertexMat[i][j] != null && GraphFacilities.vertexMat[i][j].content > 9) {
				GraphFacilities.vertexMat[i][j].content = PlayerAffiliation.NONE;
			}
		}
	}
	GraphFacilities.updateGraph(coordMat);
}
```
### findFarthestJump:
```java
private CCEdge findFarthestJump() {
	CCEdge jump = null;
	for (int i = 0; i < GraphFacilities.vertexMat.length; i++) {
		for (int j = 0; j < GraphFacilities.vertexMat[i].length; j++) {
			if (GraphFacilities.vertexMat[i][j] != null && GraphFacilities.vertexMat[i][j].content == PlayerAffiliation.CPU_PLAYER) {
				resetBoard();
				optinalPlaysLen3(j, i);
				for (int h = i; h < GraphFacilities.vertexMat.length; h++) {
					for (int g = 0; g < GraphFacilities.vertexMat[h].length; g++) {
						if (GraphFacilities.vertexMat[h][g] != null
								&& GraphFacilities.vertexMat[h][g].content == PlayerAffiliation.POSSIBLE_CPU_TARGET) {
							if (jump != null) {
								if ((jump.getDestVertx().getLocation().y < h) 
										|| (jump.getDestVertx().getLocation().y == h
										&& Math.abs(jump.getDestVertx().getLocation().x - (GraphFacilities.W / 2)) > Math.abs(g - (GraphFacilities.W / 2)))) {
									if ((i < h)
											|| (i == h
											&& Math.abs(j - (GraphFacilities.W / 2)) > Math.abs(g - (GraphFacilities.W / 2)))) {
										jump.setSrcVertx(GraphFacilities.vertexMat[i][j]);
										jump.setDestVertx(GraphFacilities.vertexMat[h][g]);
									}
								}
							} else {
								if ((i < h)
										|| (i == h
										&& Math.abs(j - (GraphFacilities.W / 2)) > Math.abs(g - (GraphFacilities.W / 2)))) {
									jump = new CCEdge(GraphFacilities.vertexMat[i][j], GraphFacilities.vertexMat[h][g]);
								}
							}
						}
					}
				}
			}
		}
	}
	return jump;
}
```
### updateGraph:
```java
public static void updateGraph(Point[][] edgeArr) {
	for (int i = 0; i < edgeArr.length; i++) {
		if (edgeArr[i].length == 2) {
			addEdge(edgeArr[i][0].y, edgeArr[i][0].x, edgeArr[i][1].y, edgeArr[i][1].x);
		} else {
			if (vertexMat[edgeArr[i][1].y][edgeArr[i][1].x].getContent() == PlayerAffiliation.CPU_PLAYER
					|| vertexMat[edgeArr[i][1].y][edgeArr[i][1].x].getContent() == PlayerAffiliation.HUMAN_PLAYER) {
				addEdge(edgeArr[i][0].y, edgeArr[i][0].x, edgeArr[i][2].y, edgeArr[i][2].x);
			} else {
				g.removeEdge(vertexMat[edgeArr[i][0].y][edgeArr[i][0].x], vertexMat[edgeArr[i][2].y][edgeArr[i][2].x]);
			}
		}
	}
}
```
### optinalPlays:
```java
public Set<Point> optinalPlays(int x, int y) {
	Set<Point> endPoints = new HashSet<>();
	Set<CCEdge> edges = GraphFacilities.g.outgoingEdgesOf(GraphFacilities.vertexMat[y][x]);
	Iterator<CCEdge> it = edges.iterator();
	while (it.hasNext()) {
		CCEdge edge = it.next();
		// Source isn't the origin.
		if (!edge.getSrcVertx().getLocation().equals(new Point(x, y))) {
			// Cell empty
			if (edge.getSrcVertx().content != PlayerAffiliation.CPU_PLAYER
					&& edge.getSrcVertx().content != PlayerAffiliation.HUMAN_PLAYER) {
				if (player == PlayerAffiliation.HUMAN_PLAYER) {
					edge.getSrcVertx().content = PlayerAffiliation.POSSIBLE_HUMAN_TARGET;
				} else {
					edge.getSrcVertx().content = PlayerAffiliation.POSSIBLE_CPU_TARGET;
				}
				endPoints.add(edge.getSrcVertx().getLocation());
				if (edge.getSrcVertx().getLocation().y - 2 == edge.getDestVertx().getLocation().y
						|| edge.getSrcVertx().getLocation().y + 2 == edge.getDestVertx().getLocation().y
						|| edge.getSrcVertx().getLocation().x - 4 == edge.getDestVertx().getLocation().x
						|| edge.getSrcVertx().getLocation().x + 4 == edge.getDestVertx().getLocation().x) {
					endPoints.addAll(optinalPlaysLen3(edge.getSrcVertx().getLocation().x, edge.getSrcVertx().getLocation().y));
				}
			}
		} else {
			// Cell empty
			if (edge.getDestVertx().content != PlayerAffiliation.CPU_PLAYER
					&& edge.getDestVertx().content != PlayerAffiliation.HUMAN_PLAYER) {
				if (player == PlayerAffiliation.HUMAN_PLAYER) {
					edge.getDestVertx().content = PlayerAffiliation.POSSIBLE_HUMAN_TARGET;
				} else {
					edge.getDestVertx().content = PlayerAffiliation.POSSIBLE_CPU_TARGET;
				}
				endPoints.add(edge.getDestVertx().getLocation());
				if (edge.getSrcVertx().getLocation().y - 2 == edge.getDestVertx().getLocation().y
						|| edge.getSrcVertx().getLocation().y + 2 == edge.getDestVertx().getLocation().y
						|| edge.getSrcVertx().getLocation().x - 4 == edge.getDestVertx().getLocation().x
						|| edge.getSrcVertx().getLocation().x + 4 == edge.getDestVertx().getLocation().x) {
					endPoints.addAll(optinalPlaysLen3(edge.getDestVertx().getLocation().x, edge.getDestVertx().getLocation().y));
				}
			}
		}
	}
	return endPoints;
}
```
### optinalPlaysLen3
```java
public Set<Point> optinalPlaysLen3(int x, int y) {
	Set<Point> endPoints = new HashSet<>();
	Set<CCEdge> edges = GraphFacilities.g.outgoingEdgesOf(GraphFacilities.vertexMat[y][x]);
	Iterator<CCEdge> it = edges.iterator();
	while (it.hasNext()) {
		CCEdge edge = it.next();
		// Source isn't the origin.
		if (!edge.getSrcVertx().getLocation().equals(new Point(x, y))) {
			// Cell empty
			if (edge.getSrcVertx().content == PlayerAffiliation.NONE) {
				if (edge.getSrcVertx().getLocation().y - 2 == edge.getDestVertx().getLocation().y
						|| edge.getSrcVertx().getLocation().y + 2 == edge.getDestVertx().getLocation().y
						|| edge.getSrcVertx().getLocation().x - 4 == edge.getDestVertx().getLocation().x
						|| edge.getSrcVertx().getLocation().x + 4 == edge.getDestVertx().getLocation().x) {
					if (player == PlayerAffiliation.HUMAN_PLAYER) {
						edge.getSrcVertx().content = PlayerAffiliation.POSSIBLE_HUMAN_TARGET;
					} else {
						edge.getSrcVertx().content = PlayerAffiliation.POSSIBLE_CPU_TARGET;
					}
					endPoints.add(edge.getSrcVertx().getLocation());
					endPoints.addAll(optinalPlaysLen3(edge.getSrcVertx().getLocation().x, edge.getSrcVertx().getLocation().y));
				}
			}
		} else {
			// Cell empty
			if (edge.getDestVertx().content == PlayerAffiliation.NONE) {
				if (edge.getSrcVertx().getLocation().y - 2 == edge.getDestVertx().getLocation().y
						|| edge.getSrcVertx().getLocation().y + 2 == edge.getDestVertx().getLocation().y
						|| edge.getSrcVertx().getLocation().x - 4 == edge.getDestVertx().getLocation().x
						|| edge.getSrcVertx().getLocation().x + 4 == edge.getDestVertx().getLocation().x) {
					if (player == PlayerAffiliation.HUMAN_PLAYER) {
						edge.getDestVertx().content = PlayerAffiliation.POSSIBLE_HUMAN_TARGET;
					} else {
						edge.getDestVertx().content = PlayerAffiliation.POSSIBLE_CPU_TARGET;
					}
					endPoints.add(edge.getDestVertx().getLocation());
					endPoints.addAll(optinalPlaysLen3(edge.getDestVertx().getLocation().x, edge.getDestVertx().getLocation().y));
				}
			}
		}
	}
	return endPoints;
}
```
### Win:
```java
public boolean win() {
	boolean p1 = true, p2 = true;
	for (int i = 0; i < 10 && (p1 || p2); i++) {
		p1 = (GraphFacilities.vertexMat[win[i][0]][win[i][1]].content == PlayerAffiliation.HUMAN_PLAYER && p1);
		p2 = (GraphFacilities.vertexMat[(H - 1) - win[i][0]][win[i][1]].content == PlayerAffiliation.CPU_PLAYER && p2);
	}
	if (p1) {
		if (status == 2) {
			JOptionPane.showConfirmDialog(null, "Blue Player Won!", "Winner", JOptionPane.CLOSED_OPTION,
				JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showConfirmDialog(null, "You Won!", "Winner", JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);
		}
		player = PlayerAffiliation.NONE;
		return true;
	} else if (p2) {
		if (status == 2) {
			JOptionPane.showConfirmDialog(null, "Red Player Won!", "Winner", JOptionPane.CLOSED_OPTION,
				JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showConfirmDialog(null, "The Computer Won!", "Winner", JOptionPane.CLOSED_OPTION,
				JOptionPane.INFORMATION_MESSAGE);
		}
		player = PlayerAffiliation.NONE;
		return true;
	}
	return false;
}
```
