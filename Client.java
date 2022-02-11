/*
Name: Rishika Dwarak
Roll Number: 1810110187
SOCKET PROGRAMMING ASSIGNMENT
CLIENT-SIDE
TCP CONNECTION
*/

// Importing necessary Java packages
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Client extends JFrame {
    BufferedImage img;
    static Image globalImage;

    // Overriding the paint function
    public void paint(Graphics g) {

        System.out.println("Painting the graph from client side..");
        super.paint(g);
        Image img = globalImage;
        g.drawImage(img, 50, 50, this);
    }

    // Function to convert node number to node alphabet
    public static void nodeConvert(int i) {
        switch (i) {
            case 0:
                System.out.print("A ");
                break;
            case 1:
                System.out.print("B ");
                break;
            case 2:
                System.out.print("C ");
                break;
            case 3:
                System.out.print("D ");
                break;
            case 4:
                System.out.print("E ");
                break;
        }
    }

    public static void main(String[] args) {
        int adj_matrix[][] = new int[5][5];
        int pathLength, startNode, endNode;
        char start_node, end_node;
        char serverResponse;
        Scanner input = new Scanner(System.in);

        // TCP Connection

        try {
            // Creating client socket and input and output data streams
            Socket clients = new Socket("127.0.0.1", 6789);
            DataInputStream ip = new DataInputStream(clients.getInputStream());
            DataOutputStream op = new DataOutputStream(clients.getOutputStream());
            System.out.println("Client side running..\n");

            // Taking adjacency matrix as input from the user
            System.out.println("Enter the adjacency matrix:\n");
            for (int i = 0; i < 5; i++)
                for (int j = 0; j < 5; j++) {
                    adj_matrix[i][j] = input.nextInt();
                    if (adj_matrix[i][j] != 0)
                        adj_matrix[i][j] = 1;
                }

            // Printing the entered matrix
            System.out.println("The entered adjacency matrix:\n");
            for (int i = 0; i < 5; i++) {
                nodeConvert(i);
                for (int j = 0; j < 5; j++) {
                    System.out.print(adj_matrix[i][j] + " ");
                }
                System.out.println("\n");
            }

            // Taking required path length as input from the user
            System.out.print("Enter path length: ");
            pathLength = input.nextInt();

            // Taking starting node as input from the user
            System.out.print("Enter start node: ");
            start_node = Character.toUpperCase(input.next().charAt(0));
            startNode = (int) start_node - (int) 'A';

            // Taking ending node as input from the user
            System.out.print("Enter end node: ");
            end_node = Character.toUpperCase(input.next().charAt(0));
            endNode = (int) end_node - (int) 'A';

            // Sending all the inputs we have taken to the server
            for (int i = 0; i < 5; i++)
                for (int j = 0; j < 5; j++) {
                    op.writeInt(adj_matrix[i][j]);
                }
            op.flush();

            op.writeInt(pathLength);
            op.flush();

            op.writeInt(startNode);
            op.flush();

            op.writeInt(endNode);
            op.flush();

            // Reading respone from the server
            serverResponse = ip.readChar();

            // Printing the required output based on the server reponse
            if (serverResponse == 'Y') {
                System.out.print("Yes there exists a path between ");
                nodeConvert(startNode);
                System.out.print("and ");
                nodeConvert(endNode);
                System.out.print("of length " + pathLength + "\n");
            } else {
                System.out.print("No there does not exist a path between ");
                nodeConvert(startNode);
                System.out.print("and ");
                nodeConvert(endNode);
                System.out.print("of path length " + pathLength + "\n");
            }

            // Getting image from the server
            byte[] arr_size = new byte[4];
            ip.read(arr_size);

            int Size = ByteBuffer.wrap(arr_size).asIntBuffer().get();
            byte[] image_arr = new byte[Size];
            ip.read(image_arr);
            System.out.println("Received Image Bytes from Server");
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(image_arr));
            globalImage = image;

            JFrame f = new Client();
            f.setTitle("Graph Image from Client Side");
            f.setSize(500, 500);
            f.setVisible(true);

            // Closing the connection
            op.close();
            clients.close();

        } catch (IOException ex) {
            System.out.println(ex);
        }

    }

}