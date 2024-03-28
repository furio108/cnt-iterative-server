import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.io.*;
public class Server {
	public static void main(String[] args) {
		try {
			Scanner inputScanner = new Scanner(System.in);
			System.out.println("Please enter a port number:");

			int portNumber = inputScanner.nextInt();
			ServerSocket serverSocket = new ServerSocket(portNumber);

			System.out.println("Server is listening on port number " + portNumber);

			String commandOutput = "";
			Process p;
			BufferedReader stdInput;

			while(true) {
				Socket socket = serverSocket.accept();
				InputStream input = socket.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(input));
				String command = reader.readLine();
				OutputStream output = socket.getOutputStream();
				PrintWriter writer = new PrintWriter(output, true);

				switch(command) {
					case "1":
						System.out.println("New client connected");
						p = Runtime.getRuntime().exec("date");
						stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
						while((commandOutput = stdInput.readLine()) != null)
						{
							writer.println(commandOutput);
						}
						stdInput.close();
						System.out.println("Completed 'date' command for client");
						break;

					case "2":
						System.out.println("New client connected");
						p = Runtime.getRuntime().exec("uptime");
						stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
						while((commandOutput = stdInput.readLine()) != null)
						{
							writer.println(commandOutput);
						}
						stdInput.close();
						System.out.println("Completed 'uptime' command for client");
						break;

					case "3":
						System.out.println("New client connected");
						p = Runtime.getRuntime().exec("free");
						stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
						while((commandOutput = stdInput.readLine()) != null)
						{
							//System.out.println(commandOutput);
							writer.println(commandOutput);
						}
						stdInput.close();
						System.out.println("Completed 'free' command for client");
						break;

					case "4":
						System.out.println("New client connected");
						p = Runtime.getRuntime().exec("netstat");
						stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
						while((commandOutput = stdInput.readLine()) != null)
						{
							writer.println(commandOutput);
						}
						stdInput.close();
						System.out.println("Completed 'netstat' command for client");
						break;

					case "5":
						System.out.println("New client connected");
						p = Runtime.getRuntime().exec("w");
						stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
						while((commandOutput = stdInput.readLine()) != null)
						{
							writer.println(commandOutput);
						}
						stdInput.close();
						System.out.println("Completed 'w' command for client");
						break;

					case "6":
						System.out.println("New client connected");
						p = Runtime.getRuntime().exec("ps");
						stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
						while((commandOutput = stdInput.readLine()) != null)
						{
							writer.println(commandOutput);
						}
						stdInput.close();
						System.out.println("Completed 'ps' command for client");
						break;

					default:
						break;
				}

				input.close();
				reader.close();
				output.close();
				writer.close();
				socket.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
