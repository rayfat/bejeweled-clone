import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class OldBejeweled implements ActionListener
{
	private int coord1[] = new int[2];
	private int coord2[] = new int[2];

	private int turn=0;
	private int points=0;

	JLabel pt = new JLabel(""+points);
	JLabel lbpts = new JLabel("Points:");

    private final int ROW=8;
	private final int COL=8;

	private JButton jewel[][] = new JButton[ROW][COL];

	public OldBejeweled()
	{
		//Set up window
		JFrame frame = new JFrame("Bejeweled");
		frame.setLayout(new GridLayout(9,COL));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		int pic=0;

		//Create Buttons
		for(int i=0;i<ROW;i++)
		{
			for(int j=0;j<COL;j++)
			{
				pic = getPic(i,j);
				jewel[i][j].setActionCommand("("+ i + "," + j + "," + pic +")");
				jewel[i][j].addActionListener(this);
				frame.getContentPane().add(jewel[i][j]);
			}
		}

		//Check for 3 or more in a row
		boolean changev=false,changeh=false;

		do
		{
			changeh = compHorizontal(0);
			changev = compVertical(0);

			if(changev==true || changeh==true)
				fillHoles();
		}
		while(changev==true || changeh==true);

		JPanel status = new JPanel();

		status.add(lbpts);
		status.add(pt);
		//Add Status Bar
		frame.add(status);

		//Display Window
		frame.pack();
		frame.setSize(480,500);
		frame.setVisible(true);

		points=0;
		pt.setText(""+points);

	}

	public void eliminateRow(int x, int y,int num, int mode )
	{
		ImageIcon blank = new ImageIcon("icons/0.gif");

		points += ((num+1)*10);
		pt.setText(""+points);

		if (mode==1)
		{
			//Eliminate Horizontal Row
			for(int i=y;num>=0;i--,num--)
			{
				jewel[x][i].setActionCommand("("+ x + "," + i + "," + 0 +")");
				jewel[x][i].setIcon(blank);
			}
		}
		else
		{
			//Eliminate Vertical Row
			for(int i=x;num>=0;i--,num--)
			{
				jewel[i][y].setActionCommand("("+ i + "," + y + "," + 0 +")");
				jewel[i][y].setIcon(blank);
			}
		}
	}

	public  int getPic(int x, int y)
	{
		Random generator = new Random();
		int i =0;

		i = generator.nextInt(7)+1;
		ImageIcon pic = new ImageIcon("icons/" + i + ".gif");
		jewel[x][y] = new JButton(pic);

		return i;
	}

	public  void changeImage(int x1,int y1,int x2, int y2)
	{
		int pic1,pic2;

		if(isLegal(x1,y1,x2,y2))
		{
			pic1 = pType(jewel[x1][y1].getActionCommand());
			pic2 = pType(jewel[x2][y2].getActionCommand());

			jewel[x1][y1].setActionCommand("("+ x1 + "," + y1 + "," + pic2 +")");
			jewel[x2][y2].setActionCommand("("+ x2 + "," + y2 + "," + pic1 +")");

			Icon temp = jewel[x1][y1].getIcon() ;
			jewel[x1][y1].setIcon(jewel[x2][y2].getIcon());
			jewel[x2][y2].setIcon(temp);
		}
	}

	public static int[] getXY(String jewel)
	{
		int xy[] = new int[2];

		xy[0]= Integer.parseInt(jewel.substring(1,2));
		xy[1]= Integer.parseInt(jewel.substring(3,4));
		return xy;
	}

	public static boolean isLegal(int x1,int y1,int x2, int y2)
	{
		//Check Position
		// Left + Right
		if((x1==x2 && y1+1==y2) || (x1==x2 && y1-1==y2))
			return true;
		//Down + Up
		else if((x1-1==x2 && y1==y2) || (x1+1==x2 && y1==y2))
			return true;
		//Else move = illegal

		//Check for Matches
		return false;
	}

	public boolean compVertical(int mode)
	{
		int counter=0;
		boolean change=false;

		for(int j=0; j < 8;j++)
		{
			for(int i=0; i < 8;i++)
			{
				if(samePiece(i,j,i+1,j))
					counter++;
				else
				{
					if(counter>1)
					{
						if(mode==0)
							eliminateRow(i,j,counter,2);

						change=true;
					}
					counter=0;
				}
			}
		}
		return change;
	}

	public boolean compHorizontal(int mode)
	{
		int counter=0;
		boolean change=false;

		for(int i=0; i < 8;i++)
		{
			for(int j=0; j < 8;j++)
			{
				if(samePiece(i,j,i,j+1))
					counter++;
				else
				{
					if(counter>1)
					{
						if(mode==0)
							eliminateRow(i,j,counter,1);

						change=true;
					}
					counter=0;
				}
			}
		}

		return change;
	}

	public boolean isGameOver()
	{
		for(int i=0; i < 8;i++)
		{
			for(int j=0; j < 8;j++)
			{
				if(checkMove(i,j))
				{
					System.out.println("Check:("+i+","+j+")");
					return false;
				}
			}
		}

		return true;
	}

	public boolean checkMove(int x, int y)
	{
		boolean moveXh=false,moveXv=false;

		//move up
		if(x!=0)
		{
			changeImage(x,y,x-1,y);

			moveXh = compHorizontal(1);
			moveXv = compVertical(1);

			changeImage(x,y,x-1,y);
		}

		if(moveXh==true || moveXv==true)
			return true;

		//move down
		if(x!=7)
		{
			//Switch Images
			changeImage(x,y,x+1,y);

			moveXh = compHorizontal(1);
			moveXv = compVertical(1);

			//Switch back
			changeImage(x,y,x+1,y);
		}

		if(moveXh==true || moveXv==true)
			return true;

		//move left
		if(y!=0)
		{
			//Switch Images
			changeImage(x,y,x,y-1);

			moveXh = compHorizontal(1);
			moveXv = compVertical(1);

			//Switch back
			changeImage(x,y,x,y-1);
		}

		if(moveXh==true || moveXv==true)
			return true;

		//move right
		if(y!=7)
		{
			//Switch Images
			changeImage(x,y,x,y+1);

			moveXh = compHorizontal(1);
			moveXv = compVertical(1);

			//Switch back
			changeImage(x,y,x,y+1);
		}

		if(moveXh==true || moveXv==true)
			return true;

		return false;
	}

	/*Test isGameOver function
	public void endGame()
	{

		for(int j=0; j < 8;j++)
		{
			for(int i=0; i < 8;i++)
			{
				switch((i+j)%3)
				{
					case 0:
					setPic(j,i,1);
					break;
					case 1:
					setPic(j,i,2);
					break;
					case 2:
					setPic(j,i,3);
					break;
				}
			}
		}
	}*/

	public boolean samePiece(int x1,int y1,int x2, int y2)
	{
		if(x2 > 7 || y2 > 7 )
			return false;

		if(pType(jewel[x1][y1].getActionCommand())==pType(jewel[x2][y2].getActionCommand()))
		{
			return true;
		}
		else
			return false;
	}

	public int pType(String tag)
	{
		int num = Integer.parseInt(tag.substring(5,6));
		return num;
	}

	public void fillHoles()
	{
		int change=0;

		//Fill Holes with Random Pieces
		do
		{
			change=0;

			for(int i=0; i < 8;i++)
			{
				for(int j=0; j < 8;j++)
				{
					if(pType(jewel[i][j].getActionCommand())==0)
					{
						change++;

						if(i==0)
							setPic(i,j,0);
						else
							changeImage(i,j,i-1,j);
					}
				}
			}

		}while(change!=0);

	}

	public void setPic(int x,int y,int spic)
	{
		Random generator = new Random();
		int i =0;

		if(spic==0)
			i = generator.nextInt(7)+1;
		else
			i = spic;

		ImageIcon pic = new ImageIcon("icons/" + i + ".gif");
		jewel[x][y].setIcon(pic);
		jewel[x][y].setActionCommand("("+ x + "," + y + "," + i +")");
	}

	public void actionPerformed(ActionEvent e)
	{
		if(turn==0)
		{
			coord1 = getXY(e.getActionCommand());
			turn++;
		}
		else
		{
			coord2 = getXY(e.getActionCommand());
			changeImage(coord1[0],coord1[1],coord2[0],coord2[1]);

			boolean changev=false,changeh=false;
			int counter=0;

			do
			{
				changeh = compHorizontal(0);
				changev = compVertical(0);

				if(changev==true || changeh==true)
					fillHoles();
				else
				{//Move made no matches
					if(counter==0)
					{
						JOptionPane.showMessageDialog(null,"Bad Move.", "Bejeweled", JOptionPane.INFORMATION_MESSAGE);
						//Switchback
						changeImage(coord1[0],coord1[1],coord2[0],coord2[1]);
					}

				}
				counter++;
			}
			while(changev==true || changeh==true);


			//endGame();Test isGameOver function

			//Check for moves. if moves = 0 Game Over
			if(isGameOver())
				JOptionPane.showMessageDialog(null, "You Lose!!!", "Bejeweled", JOptionPane.INFORMATION_MESSAGE);

			turn=0;
		}
	}

	public static void main(String args[])
	{
		OldBejeweled app = new OldBejeweled();
	}
}
