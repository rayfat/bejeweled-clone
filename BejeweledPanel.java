import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class BejeweledPanel extends JPanel implements Observer
{
	private static final long serialVersionUID = 5791603653812621875L;
	
	private BejeweledBoard board;
	private BejeweledChecker checker;
	private Position firstClick;
	private JButton[][] buttons;
	
	public BejeweledPanel(int rows, int columns) 
	{
		createNewBoard(rows, columns);
		createUI();
		refreshUI();		
	}
	
	private void createUI()
	{
		JPanel boardPanel = createBoardPanel();
		
		BorderLayout mainLayout = new BorderLayout();
		setLayout(mainLayout);
		add(boardPanel, BorderLayout.CENTER);
	}
	
	public void createNewBoard(int rows, int columns)
	{
		this.board = new BejeweledBoard(rows, columns);
		this.checker = new BejeweledChecker(board);
		board.addObserver(this);
	}
	
	private void refreshUI()
	{
		for(int row = 0; row < board.totalRows; row++)
		{
			for(int column = 0; column < board.totalColumns; column++)
			{
				updateButtonAt(row, column);
			}
		}
	}
	
	private JPanel createBoardPanel()
	{
		int rows = board.totalRows;
		int columns = board.totalColumns;
		
		GridLayout layout = new GridLayout(rows, columns);
		JPanel panel = new JPanel(layout);
		
		this.buttons = new JButton[rows][columns];
		
		for(int row = 0; row < rows; row++)
		{
			for(int column = 0; column < columns; column++)
			{
				buttons[row][column] = createButton(row, column);
				panel.add(buttons[row][column]);
			}
		}
		
		return panel;
	}
	
	private JButton createButton(final int row, final int column)
	{
		ActionListener buttonListener = new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				clickedButtonAt(row, column);	
			}
		};
		
		JButton button = new JButton();
		button.addActionListener(buttonListener);		
		
		return button;
	}
	
	private Color getPieceColor(Piece piece)
	{
		final Color[] colors = new Color[]
	    {
			Color.gray, Color.blue, Color.red, 
			Color.orange, Color.white, Color.green, 
			Color.pink, Color.yellow
		};

		return colors[piece.ordinal()];
	}
	
	private void clickedButtonAt(int row, int column)
	{
		if(firstClick == null)
		{
			firstClick = new Position(row, column);
			return;
		}
		
		if(checker.canSwap(firstClick.row, firstClick.column, row, column))
		{
			board.swapPieces(firstClick.row, firstClick.column, row, column);
			checker.processRow(firstClick.row);
			checker.processRow(row);
			
			checker.processColumn(firstClick.column);
			checker.processColumn(column);
		}
		else
		{
			showMessage("Bad Move!");
		}
		
		firstClick = null;		
	}
	
	private void showMessage(String message)
	{
		System.out.println(message);
	}
	
	private void updateButtonAt(int row, int column)
	{
		Piece piece = board.getPieceAt(row, column);
		Color buttonColor = getPieceColor(piece);
		buttons[row][column].setBackground(buttonColor);
	}
	
	public void update(Observable arg0, Object position) 
	{
		Position piece = (Position) position;	
		updateButtonAt(piece.row, piece.column);
	}
	
	public static void main(String[] args) 
	{
		BejeweledPanel panel = new BejeweledPanel(6, 6);
		JFrame frame = new JFrame();
		frame.setContentPane(panel);
		frame.setSize(600, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}