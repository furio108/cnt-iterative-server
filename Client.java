import java.io.*;
import java.util.*;
import java.lang.*;
import java.net.Socket;

class ThreadHandler implements Runnable {
    String serverIP;
    int serverPort;
    int command;
    int threadNum;
    ArrayList<Integer> intList;

    ThreadHandler(String serverIP, int serverPort, int command, int threadNum, ArrayList<Integer> intList) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.command = command;
        this.threadNum = threadNum;
        this.intList = intList;
    }

    public void run() {
        try {
            long startTime = System.currentTimeMillis();

            //open socket
            Socket socket = new Socket(serverIP, serverPort);

            //send command
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            writer.println(command);
            
            //collect result
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            //don't need this
            //System.out.println("Client " + (threadNum+1) + " received: "); 
            reader.lines().forEach(s -> System.out.println(s));

            //calculate thread time
            long endTime = System.currentTimeMillis();
            this.intList.set(threadNum, new Integer((int)(endTime - startTime)));

            //close buffers
            reader.close();
            writer.close();
            output.close();
            input.close();

            //close socket
            socket.close();
        }
        catch(Exception e) {
            System.out.println("WOOPS: Thread");
        }
    }
}



public class Client { 
    public static void main(String[] args) {
        try {
            String serverIP = "139.62.210.155";
            String temp;

            Scanner scan = new Scanner(System.in);

            System.out.println("Please Enter Network Address (or x for 139.62.210.155)");
            temp = scan.next();
            if (!temp.equals("x")) {
                serverIP = temp;
            }
            System.out.println("Please Enter Port");
            int serverPort = scan.nextInt();

            int command = -1;
            while(true) {
                int totalTime = 0;
                double averageTime = 0;

                //print menu
                System.out.println();
                System.out.println("---Please Enter Number Corresponding to Desired Command---");
                System.out.println("1. Date and Time - the date and time on the server");
                System.out.println("2. Uptime - how long the server has been running since last boot-up");
                System.out.println("3. Memory Use - the current memory usage on the server");
                System.out.println("4. Netstat - lists network connections on the server");
                System.out.println("5. Current Users - list of users currently connected to the server");
                System.out.println("6. Running Processes - list of programs currently running on the server");
                System.out.println("0. Quit");

                //get user command
                command = scan.nextInt();
                if (command == 0) {
                    break;
                }
                else if (command != 1 && command != 2 && command != 3 && command != 4 && command != 5 && command != 6) {
                    System.out.println("INVALID CHOICE");
                    continue;
                }

                //get number of iterations
                System.out.println();
                System.out.println("---Please Enter Number of Request (1, 5, 10, 15, 20, 25)---");
                int iterations = scan.nextInt();
                System.out.println();

                //create threads
                ArrayList<Thread> threadList = new ArrayList<>();
                ArrayList<Integer> intList = new ArrayList<>();
                for (int i = 0; i < iterations; i++) {
                    intList.add(new Integer(0));
                    threadList.add(new Thread(new ThreadHandler(serverIP, serverPort, command, i, intList)));
                }

                //for loop to start threads
                for (int i = 0; i < iterations; i++) {
                    threadList.get(i).start();
                }

                //for loop to join threads 
                for (int i = 0; i < iterations; i++) {
                    threadList.get(i).join();
                }

                System.out.println();

                //calculate total time
                for (int i = 0; i < iterations; i++) {
                    System.out.println("Turn-around Time for Client " + (i+1) + ": " + intList.get(i).intValue() + "ms");
                    totalTime += intList.get(i).intValue();
                }
                System.out.println("Total Turn-around Time: " + totalTime + "ms");

                //calculate average thread time
                System.out.println("Average Turn-around Time: " + ((double)totalTime )/ iterations + "ms");
                System.out.println();               

            } 
        }
        catch(Exception e) {
            System.out.println("WOOPS: Client");
        }
    }
}