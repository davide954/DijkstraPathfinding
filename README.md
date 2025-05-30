# Dijkstra Pathfinding Visualizer

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Swing](https://img.shields.io/badge/Swing-GUI-blue?style=for-the-badge)

A Java Swing application that demonstrates **Dijkstra's shortest path algorithm** with real-time visualization. Watch the algorithm explore the grid step by step and find the optimal path between two points.

## 🎯 Features

- **Interactive Grid**: Click and drag to create walls and obstacles
- **Real-time Visualization**: See the algorithm in action with color-coded cells
- **Drag & Drop**: Move start and end points anywhere on the grid
- **Step-by-step Animation**: Watch how Dijkstra explores the grid
- **Clean UI**: Intuitive interface with clear instructions and controls

## 🎨 Color Legend

- 🟢 **Green**: Start point
- 🔴 **Red**: End point  
- ⬛ **Black**: Walls/Obstacles
- 🔵 **Light Blue**: Visited cells
- 🟡 **Yellow**: Optimal path
- 🟠 **Orange**: Current cell being processed

## 🚀 How to Run

### Prerequisites
- Java 8 or higher
- Any Java IDE (IntelliJ IDEA, Eclipse, etc.) or command line

### Running the Application

#### Option 1: Command Line
```bash
# Clone the repository
git clone https://github.com/bugubster13/DijkstraPathfinding.git

# Navigate to project directory
cd DijkstraPathfinding

# Compile the Java file
javac DijkstraPathfinding.java

# Run the application
java DijkstraPathfinding.DijkstraPathfinding
```

#### Option 2: IDE
1. Open the project in your favorite Java IDE
2. Navigate to `DijkstraPathfinding/DijkstraPathfinding.java`
3. Run the main method

## 🎮 How to Use

1. **Creating Walls**: Click on empty cells to create black walls
2. **Removing Walls**: Click on existing walls to remove them
3. **Moving Start/End Points**: Drag the green (start) or red (end) points to new positions
4. **Run Algorithm**: Click "Start Dijkstra" to begin the pathfinding visualization
5. **Reset Grid**: Clear all walls and reset the grid
6. **Clear Path**: Remove only the path and visited cells, keeping walls intact

## 📖 About Dijkstra's Algorithm

Dijkstra's algorithm is a graph search algorithm that finds the shortest path between nodes in a graph. In this visualization:

- Each cell is a node
- Adjacent cells (up, down, left, right) are connected
- All edges have equal weight (distance = 1)
- The algorithm guarantees finding the shortest path if one exists

## 🛠️ Technical Details

- **Language**: Java
- **GUI Framework**: Swing
- **Algorithm**: Dijkstra's shortest path
- **Data Structures**: Priority Queue, HashSet
- **Concurrency**: SwingWorker for non-blocking UI updates

## 📁 Project Structure

```
DijkstraPathfinding/
├── DijkstraPathfinding.java       # Main application file
├── README.md                      # Project documentation
├── .gitignore                     # Git ignore rules
└── LICENSE                        # MIT License
```

## 🤝 Contributing

Contributions are welcome! Feel free to:

- Report bugs
- Suggest new features
- Submit pull requests
- Improve documentation

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**bugubster13**

## 🌟 Acknowledgments

- Inspired by classic pathfinding visualizers
- Built for educational purposes to demonstrate algorithm concepts
- Thanks to the Java Swing community for GUI best practices

---

⭐ **Star this repository if you found it helpful!**
