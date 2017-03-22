package convolutional.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

public class GridPanel extends JPanel implements MouseListener, MouseMotionListener{
	
	private boolean[][] grid;
	
	public boolean[][] getGrid() {
		return this.grid;
	}

	public GridPanel() {
		this.addMouseMotionListener(this);
		clearGrid();
		
	}

	private void clearGrid() {
		grid = new boolean[29][];
		for (int i=0; i<29; i++) {
			grid[i] = new boolean[29];
			for (int j=0; j<29; j++) {
				grid[i][j] = false;
			}
		}
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		grawGrid(g);
	}

	private void grawGrid(Graphics g) {
		g.setColor(Color.black);
		for (int row=0; row<30; row++) {
			for (int col=0; col<30; col++) {
				g.drawLine(5, col * 8 + 5, 29*8 + 5, col*8 + 5);
				g.drawLine(row*8 + 5 , 5, row*8 + 5, 29*8 + 5);
			}
		}
		for (int row=0; row<29; row++) {
			for (int col=0; col<29; col++) {
				if (grid[row][col] == true) {
					for (int i=0; i<8; i++) {
						g.drawLine(5 + row * 8, 5 + col * 8 + i, 13 + row * 8, 5 + col * 8 + i);
					}
				}
			}
		}
	}

	public void clear() {
		clearGrid();
		this.repaint();
	}
	
	public void mouseClicked(MouseEvent e) {
		
	}

	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {
		
	}

	public void mousePressed(MouseEvent e) {
		
	}

	public void mouseReleased(MouseEvent e) {
		
	}

	public void mouseDragged(MouseEvent evt) {
		int x = evt.getX();
		int y = evt.getY();
		int dx = (x - 5) / 8;
		int dy = (y - 5) / 8;
		if (dx<0 || dx>=29 || dy<0 || dy>=29) {
			return;
		}
		// System.out.println(x + " " + y + " " + dx + ":" + dy);
		
		safeSet(dx, dy);
		safeSet(dx - 1 , dy - 1);
		safeSet(dx , dy - 1);
		safeSet(dx + 1 , dy - 1);
		safeSet(dx - 1 , dy);
		safeSet(dx  , dy);
		safeSet(dx + 1 , dy);
		safeSet(dx - 1 , dy + 1);
		safeSet(dx  , dy + 1);
		safeSet(dx + 1 , dy + 1);

		this.repaint();
	}

	private void safeSet(int dx, int dy) {
		if (dx<0 || dx>=29 || dy<0 || dy>=29) {
			return;
		}
		grid[dx][dy] = true;
	}

	public void mouseMoved(MouseEvent arg0) {

	}

	public void draw(boolean[][] g) {
		this.clearGrid();
		this.grid = g;
		this.repaint();
	}
}
