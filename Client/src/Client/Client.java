/**
 * 
 */
package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author Pascal S. SENSITIVE INFORMATION REDACTED
 * Created on 20 May 2019
 */
public class Client{
	
	/* ==================================================================== */

	/**The Default Constructor
	 */
	
	public Client() {
		// TODO Auto-generated constructor stub
	}
	
	/* ==================================================================== */
	
	/**The reader method reads the command via a 
	 * Buffered Input Stream entered by
	 * the user.
	 * @throws IOException If the stream is closed
	 * @return Received String
	 * */
	
	private static String reader() throws IOException {
		
		BufferedReader in = new BufferedReader(new InputStreamReader(
        System.in));
		
		String input = "";
		String received = new String();
		
		System.out.println("->");// Starting reading text\n Is Ready?: " +
		//	in.ready() + " received is empty?: " + received 
		//	+ " System.in is available?: " + System.in.available());
		
		while((input = in.readLine()) != null)
		{
			if (input.contains( "~~end~~" )) {
				input = input.replace( "~~end~~", "" );
				received = received + input;
				//in.close();
				
			//	System.out.println("DEBUG: Text read: " + received);
				break;
				
			}
			received = received + input + "\n";
		}
		return received;
	}
	
	/*-  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -*/
	
	/**The sender method is is sending the command via the socket to the server
	 * @param ipAddress self Explanatory
	 * @throws IOException pass through from reader
	 * @throws ConnectException When the Server is not Online
	 * @return a boolean to initialize the quitting process
	 * */
	
	private static boolean sender(String ipAddress) throws IOException, ConnectException{
		
		Socket soc = new Socket(ipAddress,65535);
		//while(!soc.isConnected()) {}
		
		//System.out.println("DEBUG: Sender Method reached, Socket created");
		String received = reader();
		
		//System.out.println("DEBUG: Exited Reader");			
		PrintWriter pw = new PrintWriter(soc.getOutputStream());
		pw.println( received );
		pw.flush();
		pw.close();
		soc.close();
		
		if (received.equals( "closeProgram" )) {
			return true;
		}
		//System.out.println("DEBUG: Data was send,\nSocket is clolsed?" 
		//+ soc.isClosed() + "\n Exiting sender Method");
		return false;
	}
	
	/*-  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -*/
	
	/**The receiver method receives the data from the server
	 * @param ipAddress self Explanatory
	 * @throws UnknownHostException Thrown to indicate that the IP address 
	 * of a host could not be determined.
	 * @throws IOException when the input Stream is closed
	 * @throws ConnectException When the server is not online
	 * @return the received String send by the Server
	 * */
	
	private static String receiver(String ipAddress) 
		throws UnknownHostException, IOException, ConnectException {
		
		Socket soc = new Socket(ipAddress,65535);
		//System.out.println("DEBUG: Reading Connection established?: "
		//+soc.isConnected());
		
		/*PrintWriter pw = new PrintWriter(soc.getOutputStream());
		pw.println( "ready" );*/
		
		//System.out.println("DEBUG: Attempting to read data from Server");
		BufferedReader read = new BufferedReader(new InputStreamReader(
		soc.getInputStream()));
		
		String received = new String();
		String input;
		
		while((input = read.readLine()) != null)
		{
			received = received + input + "\n";
			//System.out.println( input + " read" );
		}
		received.trim();
		read.close();
		soc.close();
		//System.out.println("DEBUG: Socket Closed: " + soc.isClosed() + " Retuning to main");
		
		return received;
	}

	/* ==================================================================== */
	
	/** @param args Allows the user to enter an IP Address (Not tested)
	 */
	
	public static void main( String [] args ) {
		
		//System.out.println("DEBUG: Client Started");
		
		String ipAddress = "127.0.0.1";
		
		if (args.length == 2) {
			if (args[1].matches( "\\d*\\." )){
				ipAddress = args[1];
			}
		}
		do{
			try {
				boolean close = sender(ipAddress);
				
				if (close) {
					System.exit( 0 );
				}
				//System.out.println("DEBUG: Back in Main method opening received method");
				String received = receiver(ipAddress);
				
				System.err.println(received);		
			}
			catch (SecurityException e) {
				System.err.println( "Security Manager Violation" );
			}
			catch (IllegalArgumentException e) {
				System.err.println( "Illigal Arguments" );
				
			}
			catch (UnknownHostException e) {
				System.err.println( "Unknown Host" );
			
			}
			catch(ConnectException e){
				System.err.println( "Server not online. Program quits" );
				//Yes I've could have handled that better and retry it.
				System.exit(0);
			}
			catch ( IOException e ) {
				//e.printStackTrace();
				//e.getCause();
				System.err.println( "Read/Write operation went wrong" );
			}
			//System.out.println("DEBUG: Loop traversed");
		}while(true);
	}	
}
