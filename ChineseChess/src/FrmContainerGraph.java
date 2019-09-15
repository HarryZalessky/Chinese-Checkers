
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class FrmContainerGraph extends JFrame implements Runnable {

	private final double DIF_H = 50, DIF_W = 29.5, OFFSET_Y = 0, OFFSET_X = 0;
	private ImageIcon icon;
	private Image img, redMarble, blueMarble, empty, optinal;
	private BackPanelGraph backGround;
	private BackPanelGraph[][] graphicMat;
	private ChineseCheckersGraph cc;
	private Thread thread;

	public FrmContainerGraph(String s) {
		super(s);
		super.setSize(787, 890);
		super.setDefaultCloseOperation(EXIT_ON_CLOSE);
		cc = new ChineseCheckersGraph();
		icon = new ImageIcon(this.getClass().getClassLoader().getResource("Resource/board.png"));
		img = icon.getImage();
		backGround = new BackPanelGraph(img, null);
		backGround.setLayout(null);
		backGround.setBounds(0, 0, 770, 850);
		backGround.addMouseListener(new M_over());
		icon = new ImageIcon(this.getClass().getClassLoader().getResource("Resource/RedMarble.png"));
		redMarble = icon.getImage();
		icon = new ImageIcon(this.getClass().getClassLoader().getResource("Resource/BlueMarble.png"));
		blueMarble = icon.getImage();
		empty = null;
		icon = new ImageIcon(this.getClass().getClassLoader().getResource("Resource/optinal.png"));
		optinal = icon.getImage();
		graphicMat = new BackPanelGraph[cc.H][cc.W];
		for (int i = 0; i < cc.H; i++) {
			for (int j = 0; j < cc.W; j++) {
				if (GraphFacilities.g.containsVertex(new CellVertex(new Point(j, i)))) {
					switch (GraphFacilities.vertexMat[i][j].getContent()) {
						
						case PlayerAffiliation.CPU_PLAYER:
							graphicMat[i][j] = new BackPanelGraph(redMarble, new Point(j, i));
							break;
						case PlayerAffiliation.HUMAN_PLAYER:
							graphicMat[i][j] = new BackPanelGraph(blueMarble, new Point(j, i));
							break;
						default:
							graphicMat[i][j] = new BackPanelGraph(empty, new Point(j, i));
							break;
					}

					graphicMat[i][j].setBounds((int) Math.round(OFFSET_X + (j * DIF_W)), (int) Math.round(OFFSET_Y + (i * DIF_H)), 59, 50);
					graphicMat[i][j].setOpaque(false);
					graphicMat[i][j].addMouseListener(new M_over(i, j));
					backGround.add(graphicMat[i][j]);
				}
			}
		}
		//CcMenuBar cMenuBar=new CcMenuBar(cc,this);
		//setJMenuBar(cMenuBar);
		super.add(backGround);
		cc.NewGame();
		thread = new Thread(this);
		thread.start();
		super.setLayout(null);
		super.setVisible(true);
	}

	public class M_over implements MouseListener {

		private int row;
		private int col;

		public M_over(int row, int col) {
			this.row = row;
			this.col = col;
		}

		public M_over() {
			super();
		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
			if (!cc.run) {
				JOptionPane.showConfirmDialog(backGround, "Please Choose Game Setting", "Start Error", JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);
			} else {
				if (cc.getPlayer() == 2 || cc.getStatus() != 1) {
					if (GraphFacilities.vertexMat[row][col].content == cc.getPlayer()) {
						System.out.println(col+", "+row);
						cc.setTx(col);
						cc.setTy(row);
						cc.resetBoard();
						cc.optinalPlays(cc.getTx(), cc.getTy());
					} else if (GraphFacilities.vertexMat[row][col].content > 9) {
						cc.Move(row, col);
						cc.endTurn();
					}
					reImage();
				}
			}
		}

		
		@Override
		public void mouseEntered(MouseEvent arg0) {

		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
		}
	}

	public void reImage() {
		for (int i = 0; i < cc.H; i++) {
			for (int j = 0; j < cc.W; j++) {
				if (GraphFacilities.g.containsVertex(new CellVertex(new Point(j, i)))) {
					switch (GraphFacilities.vertexMat[i][j].getContent()) {
						case PlayerAffiliation.NONE:
							graphicMat[i][j].setImg(empty);
							break;
						case PlayerAffiliation.CPU_PLAYER:
							graphicMat[i][j].setImg(redMarble);
							break;
						case PlayerAffiliation.HUMAN_PLAYER:
							graphicMat[i][j].setImg(blueMarble);
							break;
						case PlayerAffiliation.POSSIBLE_CPU_TARGET:
						case PlayerAffiliation.POSSIBLE_HUMAN_TARGET:
							graphicMat[i][j].setImg(optinal);
							break;
					}
				}
			}
		}
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
	}

	public static void main(String... args) {
		new FrmContainerGraph("Chinese Checkers");
	}

	@Override
	public void run() {
		while (cc.getPlayer() != PlayerAffiliation.NONE) {
			if (cc.getPlayer() == 1 && cc.getStatus() == 1 ) {
				//JOptionPane.showConfirmDialog(null, "Computer Thinking.....Done!", "CPU", JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);
				cc.AI();
				cc.endTurn();
				reImage();
			}

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
