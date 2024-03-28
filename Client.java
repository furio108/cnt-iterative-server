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
            String line = reader.readLine();
            System.out.println("Client " + threadNum + " received: " + line);

            //calculate thread time
            long endTime = System.currentTimeMillis();
            this.intList.set(threadNum, new Integer((int)(endTime - startTime)));

            //close buffers
            writer.close();
            reader.close();
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
            int totalTime = 0;
            double averageTime = 0;

            Scanner scan = new Scanner(System.in);

            System.out.println("Please provide server IP");
            String serverIP = scan.next();
            System.out.println("Please provide server port");
            int serverPort = scan.nextInt();

            Socket server = new Socket(serverIP, serverPort);

            int command = -1;
            while(true) {
                //print menu
                System.out.println("---Type one of the following commands---");
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

                //get number of iterations
                System.out.println("---Please provide the number of iterations you would like to run---");
                int iterations = scan.nextInt();

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

                //calculate total time
                for (int i = 0; i < iterations; i++) {
                    System.out.println("Turn-around Time for Client " + i + ": " + intList.get(i).intValue() + "ms");
                    totalTime += intList.get(i).intValue();
                }

                //calculate average thread time
                System.out.println("Average Turn-around Time: " + totalTime / iterations + "ms");
                

            } 
        }
        catch(Exception e) {
            System.out.println("WOOPS: Client");
        }
    }
}