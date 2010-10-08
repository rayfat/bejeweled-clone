import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class BejeweledPanel extends JPanel implements Observer
{
	private static final long serialVersionUID = 5791603653812621875L;
	
	private BejeweledBoard board;
	private BejeweledChecker checker;
	private Position firstClick;
	private JButton[][] buttons;
	private JLabel pointsLabel;
	private int points;
	
	public BejeweledPanel(int rows, int columns) 
	{
		createNewBoard(rows, columns);
		createUI();
		refreshUI();
	}
	
	private void createUI()
	{
		JPanel boardPanel = createBoardPanel();
		JPanel bottomPanel = createBottomPanel();
		
		BorderLayout mainLayout = new BorderLayout();
		setLayout(mainLayout);
		add(boardPanel, BorderLayout.CENTER);
		add(bottomPanel,BorderLayout.SOUTH);
	}
	
	private JPanel createBottomPanel()
	{
		this.pointsLabel = new JLabel("Points:");
		
		JButton newGame = new JButton("New Game");
		newGame.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				createNewBoard(board.totalRows, board.totalColumns);
				refreshUI();
			}
		});
		
		BorderLayout layout = new BorderLayout();
		JPanel bottomPanel = new JPanel(layout);
		bottomPanel.add(pointsLabel, BorderLayout.WEST);
		bottomPanel.add(newGame, BorderLayout.EAST);
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		return bottomPanel;
	}
	
	public void createNewBoard(int rows, int columns)
	{
		this.board = new BejeweledBoard(rows, columns);
		this.checker = new BejeweledChecker(board);
		checker.processBoard();
		board.addObserver(this);
		this.points = 0;
	}
	
	private void updatePoints()
	{
		pointsLabel.setText("Points: " + points);
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

		updatePoints();
	}
	
	private JPanel createBoardPanel()
	{
		int rows = board.totalRows;
		int columns = board.totalColumns;
		
		GridLayout layout = new GridLayout(rows, columns, 5, 5);
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
			Color.decode("#800080"), Color.yellow
		};

		return colors[piece.ordinal()];
	}
	
	private void clickedButtonAt(final int row, final int column)
	{
		if(firstClick == null)
		{
			firstClick = new Position(row, column);
		}
		else
		{
			//TODO: Remove this hack
			new Thread()
			{
				public void run() 
				{
					processMove(row, column);
					firstClick = null;
				}
			}.start();
		}
	}
	
	private void processMove(int row, int column)
	{
		if(checker.canSwap(firstClick.row, firstClick.column, row, column))
		{
			board.swapPieces(firstClick.row, firstClick.column, row, column);
			checker.processBoard();
		}
		else
		{
			showMessage("Bad Move!");
		}
	}
	
	private void showMessage(String message)
	{
		System.out.println(message);
	}
	
	private void updateButtonAt(int row, int column)
	{
		Piece piece = board.getPieceAt(row, column);
		Color buttonColor = getPieceColor(piece);

		buttons[row][column].setVisible(!piece.equals(Piece.BLANK));
		buttons[row][column].setBackground(buttonColor);		
	}
	
	public void update(Observable arg0, Object position) 
	{
		BejeweledEvent event = (BejeweledEvent) position;
		Position piece = event.piece;
		
		switch(event.type)
		{
			case PIECE_MOVED:
				updateButtonAt(piece.row, piece.column);
			break;
			
			case PIECE_DELETED:
				points += 10;
				updatePoints();
			break;
		}
		
		//Pseudo Animation
		try 
		{
			Thread.sleep(50);
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}	
}