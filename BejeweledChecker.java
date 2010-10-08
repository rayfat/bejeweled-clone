import java.util.ArrayList;
import java.util.List;

class Chain
{
	public int start;
	public int end;
	public int length;
}

public class BejeweledChecker 
{
	private static final int MIN_CHAIN_LENGTH = 3;
	private BejeweledBoard board;
	
	public BejeweledChecker(BejeweledBoard board) 
	{
		this.board = board;
	}
	
	public boolean canSwap(int sourceRow, int sourceColumn, 
			   int destinationRowX, int destinationColumn)
	{
		int deltaX = Math.abs(sourceRow - destinationRowX);
		int deltaY = Math.abs(sourceColumn - destinationColumn);
		
		return (deltaX == 0 && deltaY == 1) ||		
			   (deltaX == 1 && deltaY == 0);
	}
	
	public boolean processRow(int row)
	{
		List<Position> chain = findChainInRow(row);
		
		if(!chain.isEmpty())
		{
			board.removePieces(chain);
		}

		return !chain.isEmpty();
	}
	
	public boolean processColumn(int column)
	{
		List<Position> chain = findChainInColumn(column);

		if(!chain.isEmpty())
		{
			board.removePieces(chain);
		}
		
		return !chain.isEmpty();
	}
	
	private List<Position> findChainInRow(int row)
	{
		List<Piece> jewels = board.getRow(row);
		Chain chain = findChain(jewels);

		if(chain.length < MIN_CHAIN_LENGTH)
		{
			return new ArrayList<Position>();
		}

		List<Position> pieces = new ArrayList<Position>();
		
		for(int column = chain.start; column <= chain.end; column++)
		{
			pieces.add(new Position(row, column));
		}
		
		return pieces;
	}
	
	private List<Position> findChainInColumn(int column)
	{
		List<Piece> jewels = board.getColumn(column);
		Chain chain = findChain(jewels);
		
		if(chain.length < MIN_CHAIN_LENGTH)
		{
			return new ArrayList<Position>();
		}
		
		List<Position> pieces = new ArrayList<Position>();
		
		for(int row = chain.start; row <= chain.end; row++)
		{
			pieces.add(new Position(row, column));
		}
		
		return pieces;
	}

	private Chain findChain(List<Piece> pieces)
	{
		Chain chain = new Chain();
		chain.length = 1;
		
		for(int i = 0; i < pieces.size() - 1; i++)
		{
			Piece currentPiece = pieces.get(i);
			Piece nextPiece = pieces.get(i + 1);
			
			if(currentPiece.equals(nextPiece))
			{
				chain.length++;
				
				if(chain.length >= MIN_CHAIN_LENGTH)
				{
					chain.end = i + 1;
					break;
				}
			}
			else
			{
				chain.start = i + 1;
				chain.length = 1;
			}
		}
		
		return chain;		
	}
	
	public void processBoard()
	{
		boolean piecesRemoved = false;
		
		do
		{
			piecesRemoved = false;
			
			for(int row = 0; row < board.totalRows; row++)
			{
				piecesRemoved = piecesRemoved || processRow(row);
			}
	
			for(int column = 0; column < board.totalColumns; column++)
			{
				piecesRemoved = piecesRemoved || processColumn(column);
			}
		}
		while(piecesRemoved);
	}
}