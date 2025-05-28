/**
 * Dijkstra Pathfinding Visualizer
 * <p>
 * A Java Swing application that demonstrates the Dijkstra shortest path algorithm
 * with real-time visualization. Users can create walls, move start/end points,
 * and watch the algorithm find the optimal path step by step.
 * <p>
 * Features:
 * - Interactive grid with drag-and-drop functionality
 * - Real-time algorithm visualization
 * - Color-coded cells for different states
 * - Step-by-step pathfinding animation
 * <p>
 * Controls:
 * - Click to create/remove walls
 * - Drag green (start) or red (end) points to reposition
 * - Use control buttons to run algorithm, reset grid, or clear path
 *
 * @author bugubster13
 * @version 1.0
 * @since 2025
 */

package DijkstraPathfinding;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

public class DijkstraPathfinding extends JFrame {
    private static final int ROWS = 20;
    private static final int COLS = 30;
    private static final int CELL_SIZE = 30;
    private static final int DELAY = 50; // Milliseconds delay to visualize steps

    // Colors for visualization
    private static final Color COLOR_START = new Color(0, 153, 0);
    private static final Color COLOR_END = new Color(153, 0, 0);
    private static final Color COLOR_WALL = new Color(0, 0, 0);
    private static final Color COLOR_EMPTY = new Color(255, 255, 255);
    private static final Color COLOR_VISITED = new Color(173, 216, 230);
    private static final Color COLOR_PATH = new Color(255, 255, 0);
    private static final Color COLOR_CURRENT = new Color(255, 165, 0);

    private Cell[][] grid;
    private Cell startCell;
    private Cell endCell;
    private GridPanel gridPanel;

    private boolean isRunning = false;
    private JButton startButton;
    private JLabel statusLabel;

    /**
     * Class representing a cell in the grid
     */
    private static class Cell implements Comparable<Cell> {
        int row, col;
        boolean isStart;
        boolean isEnd;
        boolean isWall;
        boolean isVisited;
        boolean isPath;
        boolean isCurrent;
        int distance;
        Cell previous;

        public Cell(int row, int col) {
            this.row = row;
            this.col = col;
            this.isStart = false;
            this.isEnd = false;
            this.isWall = false;
            this.isVisited = false;
            this.isPath = false;
            this.isCurrent = false;
            this.distance = Integer.MAX_VALUE;
            this.previous = null;
        }

        public void reset() {
            this.isVisited = false;
            this.isPath = false;
            this.isCurrent = false;
            this.distance = Integer.MAX_VALUE;
            this.previous = null;
        }

        @Override
        public int compareTo(Cell other) {
            return Integer.compare(this.distance, other.distance);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Cell cell = (Cell) obj;
            return row == cell.row && col == cell.col;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }
    }

    /**
     * Custom panel for displaying the grid
     */
    private class GridPanel extends JPanel {
        private boolean mousePressed = false;
        private boolean movingStart = false;
        private boolean movingEnd = false;

        public GridPanel() {
            setPreferredSize(new Dimension(COLS * CELL_SIZE, ROWS * CELL_SIZE));
            setBorder(BorderFactory.createLineBorder(Color.BLACK));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (isRunning) return;
                    mousePressed = true;
                    handleMouseEvent(e);
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    mousePressed = false;
                    movingStart = false;
                    movingEnd = false;
                }
            });

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    if (isRunning || !mousePressed) return;
                    handleMouseEvent(e);
                }
            });
        }

        private void handleMouseEvent(MouseEvent e) {
            int col = e.getX() / CELL_SIZE;
            int row = e.getY() / CELL_SIZE;

            if (row < 0 || row >= ROWS || col < 0 || col >= COLS) {
                return;
            }

            Cell cell = grid[row][col];

            if (movingStart) {
                if (!cell.isEnd && !cell.isWall) {
                    startCell.isStart = false;
                    cell.isStart = true;
                    startCell = cell;
                    repaint();
                }
                return;
            }

            if (movingEnd) {
                if (!cell.isStart && !cell.isWall) {
                    endCell.isEnd = false;
                    cell.isEnd = true;
                    endCell = cell;
                    repaint();
                }
                return;
            }

            if (cell.isStart) {
                movingStart = true;
                return;
            }

            if (cell.isEnd) {
                movingEnd = true;
                return;
            }

            // Otherwise, toggle between wall and empty cell
            cell.isWall = !cell.isWall;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Draw the grid
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    Cell cell = grid[row][col];

                    if (cell.isStart) {
                        g.setColor(COLOR_START);
                    } else if (cell.isEnd) {
                        g.setColor(COLOR_END);
                    } else if (cell.isWall) {
                        g.setColor(COLOR_WALL);
                    } else if (cell.isCurrent) {
                        g.setColor(COLOR_CURRENT);
                    } else if (cell.isPath) {
                        g.setColor(COLOR_PATH);
                    } else if (cell.isVisited) {
                        g.setColor(COLOR_VISITED);
                    } else {
                        g.setColor(COLOR_EMPTY);
                    }

                    g.fillRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);

                    g.setColor(Color.GRAY);
                    g.drawRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }
        }
    }

    /**
     * Main constructor
     */
    public DijkstraPathfinding() {
        setTitle("Dijkstra Pathfinding Visualizer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        initializeGrid();
        setupUI();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Initialize the grid
     */
    private void initializeGrid() {
        grid = new Cell[ROWS][COLS];

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                grid[row][col] = new Cell(row, col);
            }
        }

        // Set default start and end points
        startCell = grid[ROWS/2][5];
        startCell.isStart = true;

        endCell = grid[ROWS/2][COLS-5];
        endCell.isEnd = true;
    }

    /**
     * Setup the user interface
     */
    private void setupUI() {
        setLayout(new BorderLayout());

        // Main panel
        gridPanel = new GridPanel();
        add(gridPanel, BorderLayout.CENTER);

        // Control panel
        JPanel controlPanel = new JPanel(new FlowLayout());

        startButton = new JButton("Start Dijkstra");
        startButton.addActionListener(_ -> runDijkstra());

        JButton resetButton = new JButton("Reset Grid");
        resetButton.addActionListener(_ -> resetGrid());

        JButton clearPathButton = new JButton("Clear Path");
        clearPathButton.addActionListener(_ -> clearPath());

        statusLabel = new JLabel("Ready. Click to create walls, drag start/end points.");

        controlPanel.add(startButton);
        controlPanel.add(resetButton);
        controlPanel.add(clearPathButton);
        controlPanel.add(statusLabel);

        add(controlPanel, BorderLayout.SOUTH);

        // Instructions panel
        JPanel instructionPanel = new JPanel();
        instructionPanel.setLayout(new BoxLayout(instructionPanel, BoxLayout.Y_AXIS));
        instructionPanel.setBorder(BorderFactory.createTitledBorder("Instructions"));

        instructionPanel.add(new JLabel("• Click to create/remove walls"));
        instructionPanel.add(new JLabel("• Drag green (start) or red (end) points"));
        instructionPanel.add(new JLabel("• Green: Start, Red: End"));
        instructionPanel.add(new JLabel("• Light Blue: Visited cells"));
        instructionPanel.add(new JLabel("• Yellow: Optimal path"));

        add(instructionPanel, BorderLayout.NORTH);
    }

    /**
     * Complete grid reset
     */
    private void resetGrid() {
        if (isRunning) return;

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Cell cell = grid[row][col];
                cell.isWall = false;
                cell.reset();
            }
        }

        statusLabel.setText("Grid reset.");
        gridPanel.repaint();
    }

    /**
     * Clear only path and visited cells
     */
    private void clearPath() {
        if (isRunning) return;

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                grid[row][col].reset();
            }
        }

        statusLabel.setText("Path cleared.");
        gridPanel.repaint();
    }

    /**
     * Execute Dijkstra's algorithm with visualization
     */
    private void runDijkstra() {
        if (isRunning) return;

        clearPath();
        isRunning = true;
        startButton.setEnabled(false);
        statusLabel.setText("Running Dijkstra's algorithm...");

        // Execute in separate thread to avoid blocking UI
        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return dijkstraSearch();
            }

            @Override
            protected void done() {
                try {
                    boolean found = get();
                    if (found) {
                        reconstructPath();
                        statusLabel.setText("Path found!");
                    } else {
                        statusLabel.setText("No path found.");
                    }
                } catch (Exception e) {
                    statusLabel.setText("Error during execution.");
                    e.printStackTrace();
                }

                isRunning = false;
                startButton.setEnabled(true);
            }
        };

        worker.execute();
    }

    /**
     * Implementation of Dijkstra's algorithm
     */
    private boolean dijkstraSearch() throws InterruptedException {
        PriorityQueue<Cell> openSet = new PriorityQueue<>();
        Set<Cell> closedSet = new HashSet<>();

        // Initialize start point distance
        startCell.distance = 0;
        openSet.add(startCell);

        while (!openSet.isEmpty()) {
            Cell current = openSet.poll();

            if (current.equals(endCell)) {
                return true; // Path found
            }

            if (closedSet.contains(current)) {
                continue;
            }

            closedSet.add(current);
            current.isVisited = true;
            current.isCurrent = true;

            // Visualize current step
            SwingUtilities.invokeLater(() -> gridPanel.repaint());
            Thread.sleep(DELAY);

            current.isCurrent = false;

            // Examine neighbors
            List<Cell> neighbors = getNeighbors(current);
            for (Cell neighbor : neighbors) {
                if (closedSet.contains(neighbor) || neighbor.isWall) {
                    continue;
                }

                int tentativeDistance = current.distance + 1;

                if (tentativeDistance < neighbor.distance) {
                    neighbor.distance = tentativeDistance;
                    neighbor.previous = current;

                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }
                }
            }
        }

        return false; // No path found
    }

    /**
     * Get neighbors of a cell (4 directions)
     */
    private List<Cell> getNeighbors(Cell cell) {
        List<Cell> neighbors = new ArrayList<>();
        int[] deltaRow = {-1, 1, 0, 0};
        int[] deltaCol = {0, 0, -1, 1};

        for (int i = 0; i < 4; i++) {
            int newRow = cell.row + deltaRow[i];
            int newCol = cell.col + deltaCol[i];

            if (newRow >= 0 && newRow < ROWS && newCol >= 0 && newCol < COLS) {
                neighbors.add(grid[newRow][newCol]);
            }
        }

        return neighbors;
    }

    /**
     * Reconstruct the optimal path
     */
    private void reconstructPath() {
        Cell current = endCell;

        while (current != null && !current.equals(startCell)) {
            current.isPath = true;
            current = current.previous;
        }

        SwingUtilities.invokeLater(() -> gridPanel.repaint());
    }

    /**
     * Main method to start the application
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Use default look and feel if system one fails
            }
            new DijkstraPathfinding();
        });
    }
}