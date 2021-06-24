package GOL;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class start {
    private final JFrame frame;
    private static Cell[][] cellArr;
    private static boolean isSpherical = false; // weź to pls zmień jak pamiętasz na tą jego angielską nazwę o tych krawędziach
    public static final int SIZE = 100;
    int control =0;
    int r=0,gr=0,b=0;
    Color color;
    Color inverseColor;

    public static Cell[][] getCellArr() {
        return cellArr;
    }
    public static boolean isNiePrzechodzi() {return isSpherical;}

    public start() {
        frame = new JFrame();
        frame.setSize(915, 937);
        cellArr = new Cell[SIZE][SIZE];
        String option = JOptionPane.showInputDialog("1-gen,2-load, 3-sym");
        createCanvas();
        if( option.equals("1")){
            new BoardGenerator();
        }else if (option.equals("2")||option.equals("3")){
            if (option.equals("2")){
                String filename = JOptionPane.showInputDialog("Type filename of saved game");
                if (!loadFromFile(filename)) {
                    JOptionPane.showMessageDialog(
                            null, "No games with that path/name",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            frame.add(new grid(), BorderLayout.CENTER);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setTitle("Game of life");
            frame.setVisible(true);

        }else{JOptionPane.showMessageDialog(
                null, "Type only 1 or 2 or 3",
                "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);

        }
    }

    public static void saveToFile(String fileName) {
        try {
            FileOutputStream fileOut =
                    new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(getCellArr());
            out.close();
            fileOut.close();
            System.out.print("Game saved as: " + fileName);
        }
        catch (IOException | NullPointerException ignored) {
        }
    }

    public static boolean loadFromFile(String fileName) {
        try {
            FileInputStream fileIn = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            cellArr = (Cell[][]) in.readObject();
            in.close();
            fileIn.close();
            start.isSpherical = true;
            return true;
        } catch (FileNotFoundException e){
            System.out.println("File not found");
            return false;
        } catch (IOException | NullPointerException i) {
            return false;
        } catch (ClassNotFoundException c) {
            System.out.println("Box class not found");
            return false;
        }
    }

    class grid extends JPanel {
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            int value = 990 / SIZE;

            for (int y = 0; y < SIZE; y++) {
                for (int x = 0; x < SIZE; x++) {

                    if (cellArr[y][x].getNextState()== State.ALIVE) {
                        g.setColor(inverseColor);
                    } else {
                        g.setColor(color);
                    }
                    g.fillRect(value * cellArr[y][x].getCol(), value * cellArr[y][x].getRow(), value, value);
                    cellArr[y][x].setPreviousState(cellArr[y][x].getNextState());
                }
            }
            try {
                Thread.sleep(15);
            } catch (InterruptedException ignored) {}
            color = new Color(r,gr,b);
            inverseColor = new Color(255-r,255-gr, 255-b);
            control++;
            if (control>=0 && control<=255) r++;
            else if (control>255 && control<=510) gr++;
            else if (control>510 && control<=765) b++;
            else if (control>765 && control<=1020) r--;
            else if (control>1020 && control<=1275) gr--;
            else if (control>1275 && control<=1530) b--;
            else{
                control=0;
                r=0;
                b=0;
                gr=0;
            }

            nextState();
        }
    }

    public void nextState(){
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                int aliveNeighbours = cellArr[y][x].checkNeighbors();
                    if (cellArr[y][x].getPreviousState()==State.DEAD &&
                            aliveNeighbours == 3) cellArr[y][x].setNextState(State.ALIVE);

                    else if (cellArr[y][x].getPreviousState()==State.ALIVE &&
                            aliveNeighbours < 2 || aliveNeighbours > 3) cellArr[y][x].setNextState(State.DEAD);

                    else cellArr[y][x].setNextState(cellArr[y][x].getPreviousState());
            }
        }
        SwingUtilities.updateComponentTreeUI(frame);
    }

    public void createCanvas() {
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                cellArr[y][x] = new Cell(y, x);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(
                start::new
        );
    }
}