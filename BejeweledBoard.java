import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Random;

enum Piece
{
	BLANK, BLUE, RED, ORANGE, 
	WHITE, GREEN, PURPLE, YELLOW
}

enum EventType
{
	PIECE_MOVED, PIECE_DELETED
}

class BejeweledEvent
{
	public EventType type;
	public Position piece;
	
	public BejeweledEvent(EventType type, Position piece) 
	{
		this.type = type;
		this.piece = piece;
	}
}

class Position
{
	public int row;
	public int column;

	public Position(int row, int column) 
	{
		this.row = row;
		this.column = column;
	}
	
	public String toString() 
	{
		return row + ":" + column;
	}
}

public class BejeweledBoard extends Observable
{	
	private Piece[][] board;
	public int totalRows;
	public int totalColumns;
	
	public BejeweledBoard(int rows, int columns) 
	{
		this.totalRows = rows;
		this.totalColumns = columns;
		this.board = createBoard();
	}
	
	private Piece[][] createBoard()
	{
		Piece[][] board = new Piece[totalRows][totalColumns];

		for(int row = 0; row < totalRows; row++)
		{
			for(int column = 0; column < totalColumns; column++)
			{
				board[row][column] = getRandomPiece();
			}
		}
		
		return board;
	}
	
	private Piece getRandomPiece()
	{
		Random numberGenerator = new Random();
		int totalJewels = Piece.values().length - 1;
		int jewelIndex = numberGenerator.nextInt(totalJewels) + 1;

		return Piece.values()[jewelIndex];
	}
	
	public Piece getPieceAt(int row, int column)
	{
		return board[row][column];
	}
	
	public void setPieceAt(int row, int column, Piece piece)
	{
		board[row][column] = piece;
		updatePieceAt(EventType.PIECE_MOVED, row, column);
	}

	public void swapPieces(int srcX, int srcY, int dstX, int dstY)
	{
		Piece firstPiece = getPieceAt(srcX, srcY);
		Piece secondPiece = getPieceAt(dstX, dstY);
		
		setPieceAt(srcX, srcY, secondPiece);
		setPieceAt(dstX, dstY, firstPiece);
	}
	
	public void removePieces(List<Position> chain)
	{
		for(Position p : chain)
		{
			setPieceAt(p.row, p.column, Piece.BLANK);
			updatePieceAt(EventType.PIECE_DELETED, p.row, p.column);
		}
		
		for(Position p : chain)
		{
			fillBlankAt(p.row, p.column);
		}		
	}
	
	public void removePieceAt(int row, int column)
	{
		setPieceAt(row, column, Piece.BLANK);
		fillBlankAt(row, column);
	}
	
	public void fillBlankAt(int row, int column)
	{
		if(row == 0)
		{
			Piece newPiece = getRandomPiece();
			setPieceAt(row, column, newPiece); 
		}
		else
		{
			Piece topPiece = getPieceAt(row - 1, column);
			setPieceAt(row - 1, column, Piece.BLANK);
			setPieceAt(row, column, topPiece); 
			removePieceAt(row - 1, column);
		}
	}
	
	public List<Piece> getRow(int row)
	{
		ArrayList<Piece> jewels = new ArrayList<Piece>();
		
		for(int column = 0; column < totalColumns; column++)
		{
			jewels.add(board[row][column]);
		}
		
		return jewels;
	}
	
	public List<Piece> getColumn(int column)
	{
		ArrayList<Piece> jewels = new ArrayList<Piece>();
		
		for(int row = 0; row < totalRows; row++)
		{
			jewels.add(board[row][column]);
		}
		
		return jewels;
	}
	
	private void updatePieceAt(EventType type, int row, int column)
	{
		Position piece = new Position(row, column);
		BejeweledEvent event = new BejeweledEvent(type, piece);
		
		setChanged();
		notifyObservers(event);
	}
}