import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SudokuGUI extends JFrame {
    private static final int SIZE = 9;
    private JTextField[][] cells = new JTextField[SIZE][SIZE];
    private boolean[][] isUserInput = new boolean[SIZE][SIZE];

    public SudokuGUI() {
        setTitle("Sudoku Solver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel gridPanel = new JPanel(new GridLayout(SIZE, SIZE));
        gridPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20),
                BorderFactory.createLineBorder(new Color(0, 51, 102), 4)));
        gridPanel.setBackground(new Color(240, 248, 255));

        Color lightBlue = new Color(220, 235, 250);
        Color white = Color.WHITE;

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                JTextField field = new JTextField();
                field.setHorizontalAlignment(JTextField.CENTER);
                field.setFont(new Font("SansSerif", Font.BOLD, 26));
                field.setBorder(BorderFactory.createMatteBorder(
                        (row % 3 == 0 ? 3 : 1),
                        (col % 3 == 0 ? 3 : 1),
                        (row == SIZE - 1 ? 3 : (row % 3 == 2 ? 3 : 1)),
                        (col == SIZE - 1 ? 3 : (col % 3 == 2 ? 3 : 1)),
                        new Color(0, 51, 102)));

                boolean isShadedBlock = (row / 3 == 0 && col / 3 == 1) ||
                                        (row / 3 == 1 && col / 3 == 0) ||
                                        (row / 3 == 1 && col / 3 == 2) ||
                                        (row / 3 == 2 && col / 3 == 1);

                field.setBackground(isShadedBlock ? lightBlue : white);
                field.setForeground(Color.BLACK);
                field.setCaretColor(Color.DARK_GRAY);

                cells[row][col] = field;
                gridPanel.add(field);
            }
        }

        JButton solveButton = new JButton("Solve");
        solveButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        solveButton.setBackground(new Color(255, 255, 255));
        solveButton.setForeground(new Color(0, 102, 204));
        solveButton.setFocusPainted(false);
        solveButton.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 2));
        solveButton.addActionListener(e -> solveSudoku());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(new Color(240, 248, 255));
        buttonPanel.add(solveButton);

        add(gridPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setSize(700, 800);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void solveSudoku() {
        int[][] board = new int[SIZE][SIZE];
        isUserInput = new boolean[SIZE][SIZE];

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                String text = cells[row][col].getText().trim();
                if (!text.isEmpty()) {
                    try {
                        int num = Integer.parseInt(text);
                        if (num < 1 || num > 9) throw new NumberFormatException();
                        board[row][col] = num;
                        isUserInput[row][col] = true;
                        cells[row][col].setForeground(Color.BLACK);
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "Invalid number at (" + (row+1) + ", " + (col+1) + "). Enter numbers 1-9 only.");
                        return;
                    }
                }
            }
        }

        if (solve(board)) {
            for (int row = 0; row < SIZE; row++) {
                for (int col = 0; col < SIZE; col++) {
                    cells[row][col].setText(String.valueOf(board[row][col]));
                    if (!isUserInput[row][col]) {
                        cells[row][col].setForeground(new Color(0, 102, 204));
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "No solution exists.");
        }
    }

    private boolean solve(int[][] board) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (isValid(board, row, col, num)) {
                            board[row][col] = num;
                            if (solve(board)) return true;
                            board[row][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isValid(int[][] board, int row, int col, int num) {
        for (int i = 0; i < SIZE; i++) {
            if (board[row][i] == num || board[i][col] == num) return false;
        }

        int startRow = row - row % 3;
        int startCol = col - col % 3;
        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                if (board[i][j] == num) return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SudokuGUI::new);
    }
}
