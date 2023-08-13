package example;

import static example.Alignment.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JPanel;


public class Main extends JPanel {
	public static final int N = 8; // board size N x N cells
	public static final int S = 35; // cell size
	public static final int F = 30;

	public static final int NONE = 0;
	public static final int PLAYER1 = 1;//黒
	public static final int PLAYER2 = 2;//白
	public static final int NEXTSTONE = 3;//候補地のこと
	public static final int DROW = 4;
	public static final int [] DIRECTIONS= {-1,0,1};

	// modeling game board
	int [][] isBoard = new int[N][N];// one of 0,1,2,3
	int gameColor = PLAYER1; //1=黒からスタート
	int winner = NONE;  // one of NONE, PLAYER1, PLAYER2,DROW

	public Main() {
		setNewBoard();
		
		JButton button = new JButton("パス");

		JPanel canvas = new JPanel() {
			@Override
			public void paint(Graphics g) {
				Font font =new Font("MS Gothic",Font.PLAIN,F);
				g.setFont(font);
				for (int y = 0; y < N; y++) {
					for (int x = 0; x < N; x++) {
						g.drawRect(x * S, y * S, S, S);
						drawColor(x, y, g);
					}
				}
				drawString(g,"黒："+countStone(PLAYER1)+"枚",1);
				drawString(g,"白："+countStone(PLAYER2)+"枚",2);
				if (!Arrays.stream(isBoard).flatMapToInt(Arrays::stream).anyMatch(value -> value % 3 == 0)) {
					whoWinner(g);
				}
				else {
					drawString(g, "今は"+((gameColor==PLAYER1)?"黒":"白")+"の番です" ,3);
				}
				button.setVisible(winner==NONE&&countStone(NEXTSTONE)==0);
			}
		};
		
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				resetNext();
				nextStone();
				gameColor=3-gameColor;
				repaint();		
			}
		});
		
		canvas.setFocusable(true);
		canvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int x = e.getX() / S;
				int y = e.getY() / S;
				if (!isOnBoard(x, y))
					return;
				if (isBoard[y][x]==NEXTSTONE) {
					flipTiles(x,y);
					setBoard(x,y,gameColor);
					resetNext();
					nextStone();
					gameColor=3-gameColor;
				}
				repaint();
			}
		});
		
		canvas.setPreferredSize(new Dimension(S*N+5, S*N+150));
		add(canvas,BorderLayout.NORTH);
		add(button,BorderLayout.SOUTH);
	}

	//ゲームを始めるとき、最初の配置を置く
	void setNewBoard() {
		isBoard = new int[N][N];
		gameColor=PLAYER1;
		setBoard(2, 4, NEXTSTONE);
		setBoard(3, 3, PLAYER1);
		setBoard(3, 4, PLAYER2);
		setBoard(3, 5, NEXTSTONE);
		setBoard(4, 2, NEXTSTONE);
		setBoard(4, 3, PLAYER2);
		setBoard(4, 4, PLAYER1);
		setBoard(5, 3, NEXTSTONE);
	}

	// (x, y) is on board?
	boolean isOnBoard(int x, int y) {
		return (0 <= y && y < N && 0 <= x && x < N);
	}

	// isBoardの色を変える関数
	void setBoard(int x, int y, int isNum) {
		if (isBoard[y][x]%3==0)
			isBoard[y][x] = isNum;
	}
	
	//八方向確認してひっくり返す
	void flipTiles(int x, int y) {
		for (int dy : DIRECTIONS) {
			for (int dx : DIRECTIONS) {
				if(dy==0&&dx==0) {
					continue;
				}
				int j =0;
				for (int i=1;i<8;i++){
					int ny = y + dy*i;
					int nx = x + dx*i;
					if (isOnBoard(nx, ny)){
						if(isBoard[ny][nx]==3-gameColor) {
							j++;
						}
						else if (isBoard[ny][nx]==gameColor) {
							if(isBoard[ny][nx]%3==0) {
								break;
							}
							for (;j>0;j--) {
								isBoard[y+dy*j][x+dx*j]=gameColor;
							}
						}else if (isBoard[ny][nx]%3==0) {
							break;
						}
					}
					else {
						break;
						}
				}
			}
		}
	}
	
	void resetNext() {
		for (int i=0;i<8;i++) {
			for (int j=0;j<8;j++) {
				setBoard(j, i, isBoard[i][j]%3);
			}
		}
	}
	
	//候補地を探す
	void nextStone() {
		for (int i= 0;i<8;i++) {
			for (int j = 0;j<8;j++) {
				for (int dy:DIRECTIONS) {
					for (int dx:DIRECTIONS) {
						if(dy==0&&dx==0) {
							continue;
						}
						if(isOnBoard(j+dx, i+dy)&&isBoard[i+dy][j+dx]==gameColor&&isBoard[i][j]==NONE){
							for (int k=1;k<7;k++) {
								int nextY =i+dy*(k+1);
								int nextX =j+dx*(k+1);
								if(isOnBoard(nextX, nextY)) {
									if (isBoard[i + dy * k][j + dx * k] == gameColor &&
				                            isBoard[nextY][nextX] == 3 - gameColor) {
				                            setBoard(j, i, NEXTSTONE);
				                        }
								}
							}
						}
					}
				}
			}
		}
	}	
	
	int countStone(int color) {
		return Arrays.stream(isBoard)
        .flatMapToInt(Arrays::stream)
        .map(cell -> cell == color ? 1 : 0)
        .sum();
	}
	
	void whoWinner(Graphics g){
		if(countStone(PLAYER1)==countStone(PLAYER2)){
			winner=DROW;
			drawString(g,"引き分けです", 3);
		}else if(countStone(PLAYER1)>countStone(PLAYER2)){ 
			winner=PLAYER1;
			drawString(g,"黒の勝ちです",3);
		}else {
			winner=PLAYER2;
			drawString(g, "白の勝ちです",3);
		}
	}
	
	void drawColor(int x, int y, Graphics g) {
		String s = (isBoard[y][x]==PLAYER1) ? "●" : (isBoard[y][x]==PLAYER2) ? "○":(isBoard[y][x]==NEXTSTONE)?"*":"";
		g.drawString(s, center(s, x * S + S / 2, g),
				middle(y * S + S / 2, g));
	}
	
	void drawString(Graphics g, String msg,int num) {
		g.setColor(Color.black);
		g.drawString(msg, center(msg, N * S / 2, g),300+num*30);
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				MainPane.createFrame("Othello", new Main());
			}
		});
	}
}

