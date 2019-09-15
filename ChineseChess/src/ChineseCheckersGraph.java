
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.*;

public final class ChineseCheckersGraph {

	private JFrame frame;
	private JButton cpu, human, diff;
	public final int H = 17, W = 25;
	public boolean run = false, aiSearch = false;
	private final int[][] win = {
		{0, 12},
		{1, 11},
		{1, 13},
		{2, 10},
		{2, 12},
		{2, 14},
		{3, 9},
		{3, 11},
		{3, 13},
		{3, 15}
	};
	private int tx = 0, ty = 0, player = 2, status;
	public static GraphFacilities graph;
	private int[][] logicMat = {
		{9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 1, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9},
		{9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 1, 9, 1, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9},
		{9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 1, 9, 1, 9, 1, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9},
		{9, 9, 9, 9, 9, 9, 9, 9, 9, 1, 9, 1, 9, 1, 9, 1, 9, 9, 9, 9, 9, 9, 9, 9, 9},
		{0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0},
		{9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9},
		{9, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 9},
		{9, 9, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 9, 9},
		{9, 9, 9, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 9, 9, 9},
		{9, 9, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 9, 9},
		{9, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 9},
		{9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9},
		{0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0},
		{9, 9, 9, 9, 9, 9, 9, 9, 9, 2, 9, 2, 9, 2, 9, 2, 9, 9, 9, 9, 9, 9, 9, 9, 9},
		{9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 2, 9, 2, 9, 2, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9},
		{9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 2, 9, 2, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9},
		{9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 2, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9}
	};
	private Point[][] coordMat;
	private Point[] cpuSoldiers = new Point[]{
		new Point(12, 0),
		new Point(11, 1),
		new Point(13, 1),
		new Point(10, 2),
		new Point(12, 2),
		new Point(14, 2),
		new Point(9, 3),
		new Point(11, 3),
		new Point(13, 3),
		new Point(15, 3)
	};
	private int coordIndex;

	public ChineseCheckersGraph() {
		coordMat = new Point[579][];
		coordIndex = 0;

		createCoordMat();
		for (int i = 0; i < coordMat.length; i++) {
			for (int j = 0; j < coordMat[i].length; j++) {
				if (GraphFacilities.vertexMat[coordMat[i][j].y][coordMat[i][j].x]
						== null) {
					if (coordMat[i][j].y < 4) {
						GraphFacilities.vertexMat[coordMat[i][j].y][coordMat[i][j].x]
								= new CellVertex(coordMat[i][j],
										PlayerAffiliation.CPU_PLAYER);
					} else if (coordMat[i][j].y > 12) {
						GraphFacilities.vertexMat[coordMat[i][j].y][coordMat[i][j].x]
								= new CellVertex(coordMat[i][j],
										PlayerAffiliation.HUMAN_PLAYER);
					} else {
						GraphFacilities.vertexMat[coordMat[i][j].y][coordMat[i][j].x]
								= new CellVertex(coordMat[i][j]);
					}
				}
			}
		}
		GraphFacilities.CreateGraph(coordMat);

		tx = ty = status = 0;
		player = PlayerAffiliation.CPU_PLAYER;
	}

	public void createCoordMat() {
		int i, j, xOffset, yOffset;
		for (i = 0; i < (H) * (W - 2); i++) {
			yOffset = i / (W - 2);
			xOffset = i % (W - 2);
			if (logicMat[yOffset][xOffset] != 9 && logicMat[yOffset][xOffset + 2] != 9) {
				coordMat[coordIndex] = new Point[2];
				for (j = 0; j < 3; j += 2) {
					coordMat[coordIndex][j / 2] = new Point(xOffset + j, yOffset);
				}
				coordIndex++;
			}
		}

		for (i = 0; i < (H - 1) * (W); i++) {
			yOffset = i / (W);
			xOffset = i % (W);

			if (logicMat[yOffset][xOffset] != 9
					&& (xOffset > 0 && logicMat[yOffset + 1][xOffset - 1] != 9)) {
				coordMat[coordIndex] = new Point[2];
				for (j = 0; j < 2; j++) {
					coordMat[coordIndex][j] = new Point(xOffset - j, yOffset + j);
				}
				coordIndex++;
			}
			if (logicMat[yOffset][xOffset] != 9
					&& (xOffset + 1 < W && logicMat[yOffset + 1][xOffset + 1] != 9)) {
				coordMat[coordIndex] = new Point[2];
				for (j = 0; j < 2; j++) {

					coordMat[coordIndex][j] = new Point(xOffset + j, yOffset + j);
				}
				coordIndex++;
			}
		}
//////////////////////////////////////////////////////////////////////////////////////////

		for (i = 0; i < (H) * (W - 4); i++) {
			yOffset = i / (W - 4);
			xOffset = i % (W - 4);
			if (logicMat[yOffset][xOffset] != 9 && logicMat[yOffset][xOffset + 2] != 9
					&& logicMat[yOffset][xOffset + 4] != 9) {
				coordMat[coordIndex] = new Point[3];
				for (j = 0; j < 5; j += 2) {
					coordMat[coordIndex][j / 2] = new Point(xOffset + j, yOffset);
				}
				coordIndex++;
			}
		}

		for (i = 0; i < (H - 2) * (W); i++) {
			yOffset = i / (W);
			xOffset = i % (W);

			if (logicMat[yOffset][xOffset] != 9 && (xOffset > 1
					&& logicMat[yOffset + 2][xOffset - 2] != 9)) {
				coordMat[coordIndex] = new Point[3];
				for (j = 0; j < 3; j++) {
					coordMat[coordIndex][j] = new Point(xOffset - j, yOffset + j);
				}
				coordIndex++;
			}
			if (logicMat[yOffset][xOffset] != 9 && (xOffset + 2 < W
					&& logicMat[yOffset + 2][xOffset + 2] != 9)) {
				coordMat[coordIndex] = new Point[3];
				for (j = 0; j < 3; j++) {

					coordMat[coordIndex][j] = new Point(xOffset + j, yOffset + j);
				}
				coordIndex++;
			}
		}
	}

	public boolean win() {
		boolean p1 = true, p2 = true;
		for (int i = 0; i < 10 && (p1 || p2); i++) {
			p1 = (GraphFacilities.vertexMat[win[i][0]][win[i][1]].content
					== PlayerAffiliation.HUMAN_PLAYER && p1);
			p2 = (GraphFacilities.vertexMat[(H - 1) - win[i][0]][win[i][1]].content
					== PlayerAffiliation.CPU_PLAYER && p2);
		}
		if (p1) {
			if (status == 2) {
				JOptionPane.showConfirmDialog(null,
						"Blue Player Won!",
						"Winner",
						JOptionPane.CLOSED_OPTION,
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showConfirmDialog(null,
						"You Won!",
						"Winner",
						JOptionPane.CLOSED_OPTION,
						JOptionPane.INFORMATION_MESSAGE);
			}
			player = PlayerAffiliation.NONE;
			return true;
		} else if (p2) {
			if (status == 2) {
				JOptionPane.showConfirmDialog(null,
						"Red Player Won!",
						"Winner",
						JOptionPane.CLOSED_OPTION,
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showConfirmDialog(null,
						"The Computer Won!",
						"Winner",
						JOptionPane.CLOSED_OPTION,
						JOptionPane.INFORMATION_MESSAGE);
			}
			player = PlayerAffiliation.NONE;
			return true;
		}
		return false;
	}

	public void Move(int y, int x) {
		System.out.println("Player "+this.getPlayer()+": ("+this.getTx()+", "+this.getTy()+") To ("+x+", "+y+")");
		GraphFacilities.vertexMat[y][x].content = player;
		GraphFacilities.vertexMat[ty][tx].content = 99;
	}

	public void resetBoard() {
		for (int i = 0; i < H; i++) {
			for (int j = 0; j < W; j++) {
				if (GraphFacilities.vertexMat[i][j] != null
						&& GraphFacilities.vertexMat[i][j].content > 9) {
					GraphFacilities.vertexMat[i][j].content = PlayerAffiliation.NONE;
				}
			}
		}
		GraphFacilities.updateGraph(coordMat);
	}

	public void endTurn() {
		resetBoard();
		if (!win()) {
			player = (player == PlayerAffiliation.CPU_PLAYER)
					? PlayerAffiliation.HUMAN_PLAYER
					: PlayerAffiliation.CPU_PLAYER;
			ty = 0;
			tx = 0;
		}
	}

	// searches for all possible moves
	public Set<Point> optinalPlays(int x, int y) {
		Set<Point> endPoints = new HashSet<>();
		Set<CCEdge> edges
				= GraphFacilities.g.outgoingEdgesOf(GraphFacilities.vertexMat[y][x]);
		Iterator<CCEdge> it = edges.iterator();
		while (it.hasNext()) {
			CCEdge edge = it.next();
			// Source isn't the origin.
			if (!edge.getSrcVertx().getLocation().equals(new Point(x, y))) {
				// Cell empty
				if (edge.getSrcVertx().content != PlayerAffiliation.CPU_PLAYER
						&& edge.getSrcVertx().content != PlayerAffiliation.HUMAN_PLAYER) {
					if (player == PlayerAffiliation.HUMAN_PLAYER) {
						edge.getSrcVertx().content
								= PlayerAffiliation.POSSIBLE_HUMAN_TARGET;
					} else {
						edge.getSrcVertx().content
								= PlayerAffiliation.POSSIBLE_CPU_TARGET;
					}
					endPoints.add(edge.getSrcVertx().getLocation());
					if (edge.getSrcVertx().getLocation().y - 2
							== edge.getDestVertx().getLocation().y
							|| edge.getSrcVertx().getLocation().y + 2
							== edge.getDestVertx().getLocation().y
							|| edge.getSrcVertx().getLocation().x - 4
							== edge.getDestVertx().getLocation().x
							|| edge.getSrcVertx().getLocation().x + 4
							== edge.getDestVertx().getLocation().x) {
						endPoints.addAll(
								optinalPlaysLen3(edge.getSrcVertx().getLocation().x,
										edge.getSrcVertx().getLocation().y));
					}
				}
			} else {
				// Cell empty
				if (edge.getDestVertx().content
						!= PlayerAffiliation.CPU_PLAYER
						&& edge.getDestVertx().content
						!= PlayerAffiliation.HUMAN_PLAYER) {
					if (player == PlayerAffiliation.HUMAN_PLAYER) {
						edge.getDestVertx().content
								= PlayerAffiliation.POSSIBLE_HUMAN_TARGET;
					} else {
						edge.getDestVertx().content
								= PlayerAffiliation.POSSIBLE_CPU_TARGET;
					}
					endPoints.add(edge.getDestVertx().getLocation());
					if (edge.getSrcVertx().getLocation().y - 2
							== edge.getDestVertx().getLocation().y
							|| edge.getSrcVertx().getLocation().y + 2
							== edge.getDestVertx().getLocation().y
							|| edge.getSrcVertx().getLocation().x - 4
							== edge.getDestVertx().getLocation().x
							|| edge.getSrcVertx().getLocation().x + 4
							== edge.getDestVertx().getLocation().x) {
						endPoints.addAll(
								optinalPlaysLen3(edge.getDestVertx().getLocation().x,
										edge.getDestVertx().getLocation().y));
					}
				}
			}
		}
		return endPoints;
	}

	// only search for jumps
	public Set<Point> optinalPlaysLen3(int x, int y) {
		Set<Point> endPoints = new HashSet<>();
		Set<CCEdge> edges
				= GraphFacilities.g.outgoingEdgesOf(GraphFacilities.vertexMat[y][x]);
		Iterator<CCEdge> it = edges.iterator();
		while (it.hasNext()) {
			CCEdge edge = it.next();
			// Source isn't the origin.
			if (!edge.getSrcVertx().getLocation().equals(new Point(x, y))) {
				// Cell empty
				if (edge.getSrcVertx().content == PlayerAffiliation.NONE) {
					if (edge.getSrcVertx().getLocation().y - 2
							== edge.getDestVertx().getLocation().y
							|| edge.getSrcVertx().getLocation().y + 2
							== edge.getDestVertx().getLocation().y
							|| edge.getSrcVertx().getLocation().x - 4
							== edge.getDestVertx().getLocation().x
							|| edge.getSrcVertx().getLocation().x + 4
							== edge.getDestVertx().getLocation().x) {
						if (player == PlayerAffiliation.HUMAN_PLAYER) {
							edge.getSrcVertx().content
									= PlayerAffiliation.POSSIBLE_HUMAN_TARGET;
						} else {
							edge.getSrcVertx().content
									= PlayerAffiliation.POSSIBLE_CPU_TARGET;
						}
						endPoints.add(edge.getSrcVertx().getLocation());
						endPoints.addAll(
								optinalPlaysLen3(edge.getSrcVertx().getLocation().x,
										edge.getSrcVertx().getLocation().y));
					}
				}
			} else {
				// Cell empty
				if (edge.getDestVertx().content == PlayerAffiliation.NONE) {
					if (edge.getSrcVertx().getLocation().y - 2
							== edge.getDestVertx().getLocation().y
							|| edge.getSrcVertx().getLocation().y + 2
							== edge.getDestVertx().getLocation().y
							|| edge.getSrcVertx().getLocation().x - 4
							== edge.getDestVertx().getLocation().x
							|| edge.getSrcVertx().getLocation().x + 4
							== edge.getDestVertx().getLocation().x) {
						if (player == PlayerAffiliation.HUMAN_PLAYER) {
							edge.getDestVertx().content
									= PlayerAffiliation.POSSIBLE_HUMAN_TARGET;
						} else {
							edge.getDestVertx().content
									= PlayerAffiliation.POSSIBLE_CPU_TARGET;
						}
						endPoints.add(edge.getDestVertx().getLocation());
						endPoints.addAll(
								optinalPlaysLen3(edge.getDestVertx().getLocation().x,
										edge.getDestVertx().getLocation().y));
					}
				}
			}
		}
		return endPoints;
	}

	public void NewGame() {
		gameStart();
	}

	public class OptionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("CPU")) {
				status = 1;
				run = true;
				frame.dispose();
				JOptionPane.showConfirmDialog(null,
						"Enjoy!!",
						"New Game",
						JOptionPane.CLOSED_OPTION);

			} else {
				status = 2;
				run = true;
				frame.dispose();
				JOptionPane.showConfirmDialog(null,
						"Enjoy!!",
						"New Game",
						JOptionPane.CLOSED_OPTION);
			}
		}
	}

	private void gameStart() {
		BackPanelGraph menuPanel = new BackPanelGraph(null, null);
		cpu = new JButton("Player\n" + "		Versus\n" + "			CPU");
		cpu.setActionCommand("CPU");
		cpu.addActionListener(new OptionListener());
		human = new JButton("Player \n" + "		Versus \n" + "			Player");
		human.setActionCommand("Human");
		human.addActionListener(new OptionListener());
		diff = new JButton("Easy");
		diff.addActionListener(new OptionListener());
		diff.setVisible(false);
		frame = new JFrame("Option Menu");
		menuPanel.setLayout(null);
		menuPanel.setBounds(0, 0, 250, 300);
		frame.setDefaultCloseOperation(0);
		frame.setLocationRelativeTo(null);
		frame.setSize(250, 300);
		frame.setVisible(true);
		frame.add(menuPanel);
		Container c = frame.getContentPane();
		c.setLayout(null);
		menuPanel.add(cpu);
		menuPanel.add(human);
		menuPanel.add(diff);

		cpu.setBounds(25, 20, 180, 95);
		human.setBounds(25, 130, 180, 95);
	}

	public int getPlayer() {
		return player;
	}

	public int getTy() {
		return ty;
	}

	public void setTy(int y) {
		ty = y;
	}

	public int getTx() {
		return tx;
	}

	public void setTx(int x) {
		tx = x;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int value) {
		if (status == 0) {
			if (value == 1 || value == 2) {
				status = value;
			}
		}
	}

	public void AI() {
		int i;
		// If only one player is outside
		if (countOutsideSoldiers() == 1) {
			CellVertex dest = null;
			for (i = 0; i < cpuSoldiers.length; i++) {
				if (cpuSoldiers[i].y < 13) {
					for (int h = 13; h < GraphFacilities.H; h++) {
						for (int j = 9 + (h - 13); j < 16 - (h - 13); j += 2) {
							if (GraphFacilities.vertexMat[h][j].content
									== PlayerAffiliation.NONE) {
								dest = GraphFacilities.vertexMat[h][j];
							}
						}
					}
					List<CellVertex> path = GraphFacilities.findShortestPathLength(
							GraphFacilities.vertexMat[cpuSoldiers[i].y][cpuSoldiers[i].x],
							dest).getVertexList();
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
					&& GraphFacilities.vertexMat[4][10].content
					!= PlayerAffiliation.HUMAN_PLAYER
					&& GraphFacilities.vertexMat[4][10].content
					!= PlayerAffiliation.CPU_PLAYER) {
				setTx(9);
				setTy(3);
				Move(4, 10);
				cpuSoldiers[6].setLocation(10, 4);
			} else if (cpuSoldiers[9].getLocation().equals(new Point(15, 3))
					&& GraphFacilities.vertexMat[4][14].content
					!= PlayerAffiliation.HUMAN_PLAYER
					&& GraphFacilities.vertexMat[4][14].content
					!= PlayerAffiliation.CPU_PLAYER) {
				setTx(15);
				setTy(3);
				Move(4, 14);
				cpuSoldiers[9].setLocation(14, 4);
			} else {
				CCEdge jump = findFarthestJump();
				if (jump != null) {
					setTx(jump.getSrcVertx().getLocation().x);
					setTy(jump.getSrcVertx().getLocation().y);
					Move(jump.getDestVertx().getLocation().y,
							jump.getDestVertx().getLocation().x);
					cpuSoldiers[findCpuPlayerIndex(
							jump.getSrcVertx().getLocation())].setLocation(
							jump.getDestVertx().getLocation());
				} else {
					for (i = 0; i < GraphFacilities.H; i++) {
						for (int j = 0; j < GraphFacilities.W; j++) {
							if (GraphFacilities.vertexMat[i][j] != null
									&& GraphFacilities.vertexMat[i][j].content
									== PlayerAffiliation.CPU_PLAYER) {
								setTx(j);
								setTy(i);
								resetBoard();
								Set<Point> points = optinalPlays(tx, ty);
								Iterator<Point> it = points.iterator();
								while (it.hasNext()) {
									Point p = it.next();
									if ((p.y > ty
											&& ((Math.abs(tx - GraphFacilities.W / 2)
											> Math.abs(p.x - GraphFacilities.W / 2))
											|| (Math.abs(tx - GraphFacilities.W / 2)
											== 0)))) {
										Move(p.y, p.x);
										cpuSoldiers[findCpuPlayerIndex(
												new Point(tx, ty))].setLocation(p);
										return;
									}
								}
								it = points.iterator();
								while (it.hasNext()) {
									Point p = it.next();
									if ((p.y == ty
											&& Math.abs(tx - GraphFacilities.W / 2)
											> Math.abs(p.x - GraphFacilities.W / 2))) {
										Move(p.y, p.x);
										cpuSoldiers[findCpuPlayerIndex(
												new Point(tx, ty))].setLocation(p);
										return;
									}
								}
								it = points.iterator();
								while (it.hasNext()) {
									Point p = it.next();
									if (p.y > ty) {
										Move(p.y, p.x);
										cpuSoldiers[findCpuPlayerIndex(
												new Point(tx, ty))].setLocation(p);
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

	private int countOutsideSoldiers() {
		int counter = 10;
		for (int i = 13; i < GraphFacilities.H; i++) {
			for (int j = 9 + (i - 13); j < 16 - (i - 13); j += 2) {
				if (GraphFacilities.vertexMat[i][j].content
						== PlayerAffiliation.CPU_PLAYER) {
					counter--;
				}
			}
		}
		return counter;
	}

	private CCEdge findFarthestJump() {
		CCEdge jump = null;
		for (int i = 0; i < GraphFacilities.vertexMat.length; i++) {
			for (int j = 0; j < GraphFacilities.vertexMat[i].length; j++) {
				if (GraphFacilities.vertexMat[i][j] != null
						&& GraphFacilities.vertexMat[i][j].content
						== PlayerAffiliation.CPU_PLAYER) {
					resetBoard();
					optinalPlaysLen3(j, i);
					for (int h = i; h < GraphFacilities.vertexMat.length; h++) {
						for (int g = 0; g < GraphFacilities.vertexMat[h].length; g++) {
							if (GraphFacilities.vertexMat[h][g] != null
									&& GraphFacilities.vertexMat[h][g].content
									== PlayerAffiliation.POSSIBLE_CPU_TARGET) {
								if (jump != null) {
									if ((jump.getDestVertx().getLocation().y < h)
											|| (jump.getDestVertx().getLocation().y == h
											&& Math.abs(
													jump.getDestVertx().getLocation().x
													- (GraphFacilities.W / 2))
											> Math.abs(g - (GraphFacilities.W / 2)))) {
										if ((i < h)
												|| (i == h
												&& Math.abs(j - (GraphFacilities.W / 2))
												> Math.abs(
														g - (GraphFacilities.W / 2)))) {
											jump.setSrcVertx(
													GraphFacilities.vertexMat[i][j]);
											jump.setDestVertx(
													GraphFacilities.vertexMat[h][g]);
										}
									}
								} else {
									if ((i < h)
											|| (i == h
											&& Math.abs(j - (GraphFacilities.W / 2))
											> Math.abs(g - (GraphFacilities.W / 2)))) {
										jump = new CCEdge(GraphFacilities.vertexMat[i][j],
												GraphFacilities.vertexMat[h][g]);
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

	private Point is_S_TurnExist() {
		for (int i = GraphFacilities.vertexMat.length - 1; i >= 0; i--) {
			for (int j = GraphFacilities.vertexMat[i].length - 1; j >= 0; j--) {
				if (GraphFacilities.vertexMat[i][j] != null
						&& GraphFacilities.vertexMat[i][j].content
						== PlayerAffiliation.CPU_PLAYER) {
					resetBoard();
					optinalPlays(j, i);
					if (i + 4 < GraphFacilities.vertexMat.length
							&& GraphFacilities.vertexMat[i + 4][j] != null
							&& j - 1 > 0 && j + 1 < GraphFacilities.vertexMat[i].length
							&& GraphFacilities.vertexMat[i + 2][j].content
							== PlayerAffiliation.CPU_PLAYER
							&& GraphFacilities.vertexMat[i + 4][j].content
							== PlayerAffiliation.POSSIBLE_CPU_TARGET
							&& ((GraphFacilities.vertexMat[i + 1][j + 1].content
							== PlayerAffiliation.CPU_PLAYER
							&& GraphFacilities.vertexMat[i + 3][j + 1].content
							== PlayerAffiliation.CPU_PLAYER
							&& GraphFacilities.vertexMat[i + 2][j + 2].content
							== PlayerAffiliation.POSSIBLE_CPU_TARGET)
							|| (GraphFacilities.vertexMat[i + 1][j - 1].content
							== PlayerAffiliation.CPU_PLAYER
							&& GraphFacilities.vertexMat[i + 3][j - 1].content
							== PlayerAffiliation.CPU_PLAYER
							&& GraphFacilities.vertexMat[i + 2][j - 2].content
							== PlayerAffiliation.POSSIBLE_CPU_TARGET))) {
						return new Point(j, i);
					}
				}
			}
		}
		return null;
	}

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
}
