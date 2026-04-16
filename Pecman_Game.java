import java.util.*;

class Game_Board
{
    private int size;
    private char[][] board;

    public Game_Board(int size)
    {
        this.size = size;
        board = new char[size][size];

        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                board[i][j] = '.';
            }
        }
    }

    public boolean isValid(int x, int y)
    {
        return x >= 0 && x < size && y >= 0 && y < size;
    }

    public void place(int x, int y, char ch)
    {
        if (isValid(x, y))
        {
            board[x][y] = ch;
        }
    }

    public void clear(int x, int y)
    {
        if (isValid(x, y))
        {
            board[x][y] = '.';
        }
    }

    public char get(int x, int y)
    {
        if (isValid(x, y))
            return board[x][y];
        return '#'; // invalid
    }

    public void show()
    {
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }
}

class Pacman
{
    private int x, y;
    private int score;
    private Game_Board board;

    public Pacman(int x, int y, Game_Board board)
    {
        this.x = x;
        this.y = y;
        this.board = board;
        this.score = 0;
    }

    public int getX()
    {
        return x;
    }
    public int getY()
    {
        return y;
    }
    public int getScore()
    {
        return score;
    }

    public void move(char dir)
    {
        int newX = x;
        int newY = y;

        switch (dir)
        {
            case 'w':
            {
                newX--;
                break;
            }
            case 's':
            {
                newX++;
                break;
            }
            case 'a':
            {
                newY--;
                break;
            }
            case 'd':
            {
                newY++;
                break;
            }
            default:
            {
                System.out.println("Invalid key! Use w/a/s/d");
                return;
            }
        }

        if (!board.isValid(newX, newY))
        {
            System.out.println("Can't move outside!");
            return;
        }

        // Score if dot
        if (board.get(newX, newY) == '.')
        {
            score++;
        }

        board.clear(x, y);
        x = newX;
        y = newY;
        board.place(x, y, 'P');
    }
}

class Ghost
{
    private int x, y;
    private Game_Board board;
    private Random r = new Random();

    public Ghost(int x, int y, Game_Board board)
    {
        this.x = x;
        this.y = y;
        this.board = board;
    }

    public int getX()
    {
        return x;
    }
    public int getY()
    {
        return y;
    }

    public void move()
    {
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        for (int tries = 0; tries < 10; tries++)
        {
            int i = r.nextInt(4);

            int newX = x + dx[i];
            int newY = y + dy[i];

            if (board.isValid(newX, newY) && board.get(newX, newY) != 'G')
            {
                board.clear(x, y);
                x = newX;
                y = newY;
                board.place(x, y, 'G');
                break;
            }
        }
    }
}

public class Pecman_Game
{
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);

        System.out.println("Select Level:");
        System.out.println("1. Easy\n2. Medium\n3. Hard");

        int level = sc.nextInt();
        int ghostMoves = (level == 1) ? 1 : (level == 2) ? 2 : 3;

        Game_Board board = new Game_Board(10);

        Pacman p = new Pacman(0, 0, board);
        Ghost g = new Ghost(5, 5, board);

        board.place(0, 0, 'P');
        board.place(5, 5, 'G');

        while (true)
        {
            System.out.println("\nScore: " + p.getScore());
            board.show();

            System.out.print("Move (w/a/s/d or q to quit): ");
            char move = sc.next().toLowerCase().charAt(0);

            if (move == 'q')
            {
                System.out.println("Game exited.");
                break;
            }

            // Pacman move
            p.move(move);

            // Check collision AFTER Pacman move
            if (p.getX() == g.getX() && p.getY() == g.getY())
            {
                System.out.println("\n💀 Game Over!");
                break;
            }

            // Ghost moves
            for (int i = 0; i < ghostMoves; i++)
            {
                g.move();
                // Check collision AFTER each ghost move
                if (p.getX() == g.getX() && p.getY() == g.getY())
                {
                    System.out.println("\n💀 Game Over!");
                    board.show();
                    return;
                }
            }
        }
        sc.close();
    }
}