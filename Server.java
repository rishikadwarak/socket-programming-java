/*
Name: Rishika Dwarak
Roll Number: 1810110187
SOCKET PROGRAMMING ASSIGNMENT
SERVER-SIDE
TCP CONNECTION
*/

// Importing necessary Java packages
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class Server extends JFrame {

    // Setting the location of the nodes in the frame
    Point A = new Point(220, 80);
    Point B = new Point(100, 180);
    Point C = new Point(340, 180);
    Point D = new Point(140, 305);
    Point E = new Point(300, 305);

    int draw1[] = new int[25];
    int draw2[] = new int[25];

    int[][] global_adj_mat = new int[5][5];

    static Image globalImage;

    // Server constructor
    Server(int[][] adj_mat) {
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                global_adj_mat[i][j] = adj_mat[i][j];

    }

    // Overriding the paint function
    public void paint(Graphics g) {
        globalImage = createImageWithText();
        g.drawImage(globalImage, 20, 20, this);
    }

    // Function that converts Image to Buffered image
    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }

        // Creating a buffered image and drawing image on to it
        BufferedImage buff_image = new BufferedImage(image.getWidth(null), image.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D buffered_graphics = buff_image.createGraphics();
        buffered_graphics.drawImage(image, 0, 0, null);
        buffered_graphics.dispose();
        return buff_image;
    }

    // Function to create the graph nodes on to the frame
    private Image createImageWithText() {
        BufferedImage bufferedImage = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
        Graphics g = bufferedImage.getGraphics();
        g.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());

        // Visualising the nodes
        g.setColor(Color.yellow);
        g.fillOval(200, 50, 55, 55);
        g.fillOval(80, 150, 55, 55);
        g.fillOval(320, 150, 55, 55);
        g.fillOval(120, 275, 55, 55);
        g.fillOval(280, 275, 55, 55);
        g.setColor(Color.black);
        g.drawString("A", A.x, A.y);
        g.drawString("B", B.x, B.y);
        g.drawString("C", C.x, C.y);
        g.drawString("D", D.x, D.y);
        g.drawString("E", E.x, E.y);

        // Initializing the 2 arrays used to stores the edges between nodes
        for (int i = 0; i < 25; i++) {
            draw1[i] = -1;
            draw2[i] = -1;
        }

        // Storing the edges to be drawn in arrays draw1 and draw2
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (global_adj_mat[i][j] > 0) {
                    int n = 0;
                    int m = 0;
                    if (draw1[0] == -1)
                        draw1[0] = i;
                    else {
                        while (draw1[n] != -1)
                            n++;
                        draw1[n] = i;
                    }
                    if (draw2[0] == -1)
                        draw2[0] = j;
                    else {
                        while (draw2[m] != -1)
                            m++;
                        draw2[m] = j;
                    }
                }
            }
        }

        // Getting the coordinate values in order to draw an edge between the nodes
        for (int i = 0; i < 25; i++) {
            if (draw1[i] > -1 && draw2[i] > -1) {
                int a1 = 0, b1 = 0, a2 = 0, b2 = 0;
                switch (draw1[i]) {
                    case 0:
                        a1 = A.x;
                        b1 = A.y;
                        break;
                    case 1:
                        a1 = B.x;
                        b1 = B.y;
                        break;
                    case 2:
                        a1 = C.x;
                        b1 = C.y;
                        break;
                    case 3:
                        a1 = D.x;
                        b1 = D.y;
                        break;
                    case 4:
                        a1 = E.x;
                        b1 = E.y;
                        break;
                }
                switch (draw2[i]) {
                    case 0:
                        a2 = A.x;
                        b2 = A.y;
                        break;
                    case 1:
                        a2 = B.x;
                        b2 = B.y;
                        break;
                    case 2:
                        a2 = C.x;
                        b2 = C.y;
                        break;
                    case 3:
                        a2 = D.x;
                        b2 = D.y;
                        break;
                    case 4:
                        a2 = E.x;
                        b2 = E.y;
                        break;
                }
                drawArrow(g, a1, b1, a2, b2);
            }
        }

        return bufferedImage;
    }

    // Function to draw arrow (edge) between 2 nodes
    public void drawArrow(Graphics g, int x1, int y1, int x2, int y2) {
        int midx = (x1 + x2) / 2;
        int midy = (y1 + y2) / 2;
        x1 = (midx + x1) / 2;
        x2 = (midx + x2) / 2;
        y1 = (midy + y1) / 2;
        y2 = (midy + y2) / 2;
        g.drawLine(x1, y1, x2, y2);
        drawTip(g, x1, y1, x2, y2);

    }

    // Function to draw the tip of the arrow
    private void drawTip(Graphics g, int x1, int y1, int x2, int y2) {
        int x, y;
        double rads = 0.523599;
        double hyp_multiplier = 10;
        int diff_y = y2 - y1;
        int diff_x = x2 - x1;
        double t = Math.atan2(diff_y, diff_x);
        double r = rads + t;
        for (int j = 0; j < 2; j++) {
            x = (int) (x2 - hyp_multiplier * Math.cos(r));
            y = (int) (y2 - hyp_multiplier * Math.sin(r));
            g.drawLine(x2, y2, x, y);
            r = t - rads;
        }
    }

    // Function to convert adjacency matrix to adjacency list
    public static ArrayList<Integer>[] convertToList(int[][] a) {
        ArrayList<Integer>[] adj_list = new ArrayList[5];
        for (int i = 0; i < 5; i++) {
            adj_list[i] = (new ArrayList<Integer>());
        }
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (a[i][j] >= 1) {
                    adj_list[i].add(j);
                }
            }
        }

        return adj_list;

    }

    // Function to compare and check if the required path length is present in the
    // array of path lengths
    public static boolean checkPathLength(ArrayList<Integer>[] adj_list, int startNode, int endNode, int length) {
        ArrayList<Integer> track_path_list = new ArrayList<>();
        ArrayList<Integer> path_lengths_list = new ArrayList<>();
        boolean[] VistedNodes = new boolean[5];
        track_path_list.add(startNode);

        // Calling Depth First Traversing function to create path lengths array list
        checkAndAddPathDFS(adj_list, startNode, endNode, path_lengths_list, VistedNodes, track_path_list);

        // Return if the required path length is present or not
        return path_lengths_list.contains(length);
    }

    // Function to recursively check the path and then add to the list of path
    // lengths
    private static void checkAndAddPathDFS(ArrayList<Integer>[] adj_list, int startNode, int endNode,
            ArrayList<Integer> length, boolean[] visitedNodes, ArrayList<Integer> pathList) {

        if (startNode == endNode) {
            length.add(pathList.size() - 1);
            return;
        }
        visitedNodes[startNode] = true;

        // Recurring to every adjacent node for all every node
        for (Integer i : adj_list[startNode]) {
            if (!visitedNodes[i]) {
                // storing current node in the path so as to start the traversal
                pathList.add(i);
                checkAndAddPathDFS(adj_list, i, endNode, length, visitedNodes, pathList);
                // removing current node from the path
                pathList.remove(i);
            }
        }
        visitedNodes[endNode] = false;
    }

    public static void main(String[] args) throws Exception {

        try {

            // Creating a server socket and input and output data streams in order to
            // communicate with the client
            ServerSocket serversocket = new ServerSocket(6789);
            System.out.println("Server side running...\n");
            while (true) {
                Socket socket = serversocket.accept();
                DataInputStream ip = new DataInputStream(socket.getInputStream());
                DataOutputStream op = new DataOutputStream(socket.getOutputStream());
                int adj_matrix[][] = new int[5][5];
                int path_length, startNode, endNode;
                char serverResponse;
                for (int i = 0; i < 5; i++)
                    for (int j = 0; j < 5; j++)
                        adj_matrix[i][j] = ip.readInt();
                path_length = ip.readInt();
                startNode = ip.readInt();
                endNode = ip.readInt();

                // Converting adjacency matrix to list
                ArrayList<Integer>[] adjList = new ArrayList[5];
                adjList = convertToList(adj_matrix);

                // Checking if the required path length is present in the array of path lengths
                boolean pathExists = checkPathLength(adjList, startNode, endNode, path_length);

                if (pathExists)
                    serverResponse = 'Y';
                else
                    serverResponse = 'N';

                // Send server response to the Client
                op.writeChar(serverResponse);

                // Creatinf the graph GUI
                JFrame frame = new Server(adj_matrix);
                frame.setSize(0, 0);
                frame.setVisible(true);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                while (globalImage == null) {
                    System.out.println("Idle");
                }

                // Sending the image to the Client
                ByteArrayOutputStream arrOp = new ByteArrayOutputStream();
                BufferedImage temp_img = toBufferedImage(globalImage);
                ImageIO.write(temp_img, "jpg", arrOp);

                byte[] Size = ByteBuffer.allocate(4).putInt(arrOp.size()).array();
                op.write(Size);
                op.write(arrOp.toByteArray());
                op.flush();
                globalImage = null;

            }
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

}