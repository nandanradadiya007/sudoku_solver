import sys
from PyQt5.QtWidgets import (
    QApplication, QWidget, QGridLayout, QVBoxLayout,
    QPushButton, QLineEdit, QMessageBox
)
from PyQt5.QtGui import QFont
from PyQt5.QtCore import Qt

SIZE = 9

class SudokuGUI(QWidget):
    def __init__(self):
        super().__init__()
        self.setWindowTitle("Sudoku Solver")
        self.setStyleSheet("background-color: white;")
        self.grid = [[None for _ in range(SIZE)] for _ in range(SIZE)]
        self.is_user_input = [[False] * SIZE for _ in range(SIZE)]
        self.init_ui()

    def init_ui(self):
        main_layout = QVBoxLayout()
        main_layout.setAlignment(Qt.AlignCenter)

        grid_layout = QGridLayout()
        grid_layout.setSpacing(0)

        for row in range(SIZE):
            for col in range(SIZE):
                cell = QLineEdit()
                cell.setFixedSize(55, 55)
                cell.setFont(QFont("Arial", 20, QFont.Bold))
                cell.setAlignment(Qt.AlignCenter)

                # Light blue background for alternating 3x3 blocks
                bg = "#e6f2ff" if (row // 3 + col // 3) % 2 == 1 else "#ffffff"

                border_style = []
                if row % 3 == 0:
                    border_style.append("border-top: 2px solid #003366;")
                if col % 3 == 0:
                    border_style.append("border-left: 2px solid #003366;")
                if row == SIZE - 1:
                    border_style.append("border-bottom: 2px solid #003366;")
                if col == SIZE - 1:
                    border_style.append("border-right: 2px solid #003366;")

                cell.setStyleSheet(f"""
                    background-color: {bg};
                    border: 1px solid #a0a0a0;
                    {"".join(border_style)}
                    color: black;
                """)

                self.grid[row][col] = cell
                grid_layout.addWidget(cell, row, col)

        solve_button = QPushButton("Solve Sudoku")
        solve_button.setFixedHeight(45)
        solve_button.setFont(QFont("Arial", 14))
        solve_button.setStyleSheet("""
            QPushButton {
                background-color: #003366;
                color: white;
                border-radius: 8px;
                padding: 8px 16px;
            }
            QPushButton:hover {
                background-color: #0055aa;
            }
        """)
        solve_button.clicked.connect(self.solve_sudoku)

        main_layout.addLayout(grid_layout)
        main_layout.addSpacing(20)
        main_layout.addWidget(solve_button)

        self.setLayout(main_layout)
        self.setFixedSize(600, 700)
        self.show()

    def solve_sudoku(self):
        board = [[0]*SIZE for _ in range(SIZE)]
        self.is_user_input = [[False] * SIZE for _ in range(SIZE)]

        for row in range(SIZE):
            for col in range(SIZE):
                val = self.grid[row][col].text().strip()
                if val:
                    try:
                        num = int(val)
                        if not 1 <= num <= 9:
                            raise ValueError
                        board[row][col] = num
                        self.is_user_input[row][col] = True
                    except ValueError:
                        QMessageBox.warning(self, "Invalid Input", f"Invalid number at ({row+1}, {col+1})")
                        return

        if self.solve(board):
            for row in range(SIZE):
                for col in range(SIZE):
                    self.grid[row][col].setText(str(board[row][col]))

                    # Restore background color
                    bg = "#e6f2ff" if (row // 3 + col // 3) % 2 == 1 else "#ffffff"
                    color = "black" if self.is_user_input[row][col] else "#0066cc"

                    border_style = []
                    if row % 3 == 0:
                        border_style.append("border-top: 2px solid #003366;")
                    if col % 3 == 0:
                        border_style.append("border-left: 2px solid #003366;")
                    if row == SIZE - 1:
                        border_style.append("border-bottom: 2px solid #003366;")
                    if col == SIZE - 1:
                        border_style.append("border-right: 2px solid #003366;")

                    self.grid[row][col].setStyleSheet(f"""
                        background-color: {bg};
                        border: 1px solid #a0a0a0;
                        {"".join(border_style)}
                        color: {color};
                    """)
        else:
            QMessageBox.information(self, "Unsolvable", "No solution exists for this puzzle.")

    def solve(self, board):
        for row in range(SIZE):
            for col in range(SIZE):
                if board[row][col] == 0:
                    for num in range(1, 10):
                        if self.is_valid(board, row, col, num):
                            board[row][col] = num
                            if self.solve(board):
                                return True
                            board[row][col] = 0
                    return False
        return True

    def is_valid(self, board, row, col, num):
        for i in range(SIZE):
            if board[row][i] == num or board[i][col] == num:
                return False
        box_row, box_col = 3 * (row // 3), 3 * (col // 3)
        for i in range(box_row, box_row + 3):
            for j in range(box_col, box_col + 3):
                if board[i][j] == num:
                    return False
        return True

if __name__ == '__main__':
    app = QApplication(sys.argv)
    window = SudokuGUI()
    sys.exit(app.exec_())
