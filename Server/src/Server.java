

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.channels.IllegalBlockingModeException;
import java.nio.file.*;
import java.util.LinkedList;
import java.util.Scanner;


/** Class Server: <br> 
 * This Class is responsible to interpret the command and parameters send by
 * The Client.
 * Afterwards it sends the results back to the Client.
 * 
 *  @author Pascal S. SENSITIVE INFORMATION REDACTED
 * Created on 10 May 2019
 * @version 2.0
 */

public class Server{
	
	/* ==================================================================== */
	
	
	/**This it a Default Constructor*/
	
	public Server() {
		// TODO Auto-generated constructor stub
	}
	
	/* ==================================================================== */
	
	/**This Method is listing all the content within a give Folder
	 * 
	 * @param nameOfDir The name of the directory 
	 * @return A Linked List with Names and Type of all content within the Folder.
	 * @throws NotDirectoryException If matching Object with nameOfDir is not
	 * a Directory an exception will be passed on to the {@link #command} Method.
	 * @throws FileNotFoundException If Matching Object does not exist an 
	 * exception will be passed on to the {@link #command} Method.
	 */

	private static String directoryListing(String nameOfDir) 
		throws NotDirectoryException,FileNotFoundException {
		
		//System.out.println( "DEBUG: in dirList Method" );
		
		LinkedList <String> dirView = new LinkedList <String>();
		File dir = new File(nameOfDir);
		
		//System.out.println(dir.getAbsoluteFile());
		
		if(dir.isDirectory()) {
			//For Each loop iterates over every object within the folder
			File [] children = dir.listFiles();
			if (children.length == 0) {
				return "Directory is empty";
			}
			for (File iter : children) {
				//Add "Type" before name of Object
				if (iter.isDirectory()) {
					dirView.add("<Dir> "+ iter.getName());
					}
				else if (iter.isFile()){
					dirView.add("<File> "+iter.getName());
				}
				else {
					dirView.add("<Unknown> "+ iter.getName());
				}
				
			}
		}
		else if (!dir.exists()) {
			throw new FileNotFoundException();
		}
		else if (dir.isFile()){
			throw new NotDirectoryException(nameOfDir);
		}
		//System.out.println( "DEBUG: Proccessed DirList Returning dirView: " 
			//+ dirView.toString() );
		
		return dirView.toString();
	}
	
	/*-  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -*/
	
	/**This Method is renaming a given folder
	 * 
	 * @param nameOfDir The name of the directory
	 * @param newNameOfDir The new name of the directory 
	 * @throws NotDirectoryException If matching Object with nameOfDir is not
	 * a Directory an exception will be passed on to the {@link #command} Method.
	 * @throws FileNotFoundException If Matching Object does not exist an 
	 * exception will be passed on to the {@link #command} Method.
	 * @return Success message
	 */
	
	private static String renameDirectory(String nameOfDir, String newNameOfDir) 
		throws NotDirectoryException, FileNotFoundException, IOException {
		
		File nowDir = new File(nameOfDir);
		File renameDir = new File(newNameOfDir);
		
		//Checks if Object with nameOfDir is actually a directory only than renames it
		if (nowDir.isDirectory()) {
			nowDir.renameTo( renameDir );
			
			if(renameDir.exists()) {
				return "Rename Succsessful";
			}
			else {
				throw new IOException("Rename unsuccessfull");
			}
		}
		else if (nowDir.isFile()) {
			throw new NotDirectoryException(nameOfDir);
		}
		else if (!nowDir.exists()) {
			throw new FileNotFoundException();
		}
		return "If you see me something has to be gone very wrong in the\n"
		+ "renameDirectory Method because I should never been seen. Somehow I\n"
		+ "bypassed all execption checks and ended up here";
	}

	/*-  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -*/
	
	/**This Method is renaming a file
	 * 
	 * @param nameOfFile The name of the file
	 * @param newNameOfFile The new name of the file 
	 * @throws NoSuchFileException If matching Object with nameOfDir is not
	 * a File an exception will be passed on to the {@link #command} Method.
	 * @throws FileNotFoundException If Matching Object does not exist an 
	 * exception will be passed on to the {@link #command} Method.
	 *  @return Success messag
	 */
	
	private static String renameFile(String nameOfFile, String newNameOfFile) 
		throws NoSuchFileException, FileNotFoundException, IOException {
		
		File nowFile = new File(nameOfFile);
		File renameFile = new File(newNameOfFile);
		
		//Checks if Object with nameOfFile is actually a File only than renames it
		if (nowFile.isFile()) {
			nowFile.renameTo( renameFile );
			if(renameFile.exists()) {
				return "Rename Succsessful";
			}
			else {
				throw new IOException("Rename unsuccessfull");
			}
		}
		else if (nowFile.isDirectory()) {
			throw new NoSuchFileException(nameOfFile, null, "is Directory");
		}
		else if (!nowFile.exists()) {
			throw new FileNotFoundException();
		}
		return "If you see me something has to be gone very wrong in the\n"
		+ "renameFile Method because I should never been seen. Somehow I\n"
		+ "bypassed all execption checks and ended up here";
	}

	/*-  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -*/
	
	/**This Method is adding a directory
	 * 
	 * @param nameOfDir The name of the new directory
	 * @throws FileAlreadyExistsException If Matching Object does already exists an 
	 * @throws FileNotFoundException when file is not found
	 * exception will be passed on to the {@link #command} Method.
	 *  @return Success message
	 */
	
	private static String addDirectory(String nameOfDir)
		throws FileAlreadyExistsException, FileNotFoundException{
		
		File newDir = new File(nameOfDir);
		//System.out.println("DEBUG: In addDir" );
		//If a Directory with the exact name already exists it wont be created
		
		if (newDir.exists()) {
			throw new FileAlreadyExistsException(nameOfDir);
		}
		//If no directory with the name exists it will be created
		else {
			newDir.mkdir();
			if (newDir.exists()) {
				//System.out.println("Directory succsessfully created");
				return "Directory succsessfully created";
			}
			else {
				throw new FileNotFoundException();
			}
		}
	}

	/*-  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -*/
	/**This Method is appending text to the File
	 * 
	 * @param nameOfFile The name of the new directory
	 * @param text the text to append
	 * @throws IOException If no access rights to file is granted an exeption
	 * is passed on to {@link #command} Method.
	 * @return Static String Success
	 */
	
	private static String appendToFile(String nameOfFile, String text)
		throws IOException {
		//Writes text to file with the help of the buffered and file writer
		
		File file = new File(nameOfFile);
		
		FileWriter fw = new FileWriter(file, true);
		BufferedWriter bw = new BufferedWriter(fw);
		
		bw.newLine();
		bw.append( text );
		bw.close();
		fw.close();
		return "Succsess";
	}

	/*-  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -*/
	
	/**Gets the content of a file
	 * @param nameOfFile the name of the file which content shall be retrived
	 * @param print a PrintWriter to extract the content of the File
	 * @throws FileNotFoundException if the file was not found
	 * @return the Content of the file
	 * */
	
	@SuppressWarnings( "resource" )
	private static String getFile(String nameOfFile, PrintWriter print)
		throws FileNotFoundException {
		File file = new File(nameOfFile);
		String tmp = "";
		
		if(file.isFile()) {
			//Reads File content
			
			Scanner scanner = null;
			scanner = new Scanner(file);
			
			while (scanner.hasNext()) {
				tmp += scanner.nextLine();
				print.println(tmp + "\n");
				print.flush();
			}
			print.close();
		}
		return tmp;
	}
	
	/*-  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -*/
	
	/**This Method is extracting the Data of the string and calling the required 
	 * method based on the data of the string.
	 * @param recived the modulated string received from the client.
	 * @param server The server in use to send and receive data from/to
	 * the client.
	 * @throws IOException Signals that an I/O exception of some sort has 
	 * occurred. This class is the general class of exceptions produced by 
	 * failed or interrupted I/O operations.
	 * @throws FileNotFoundException Signals that an attempt to open the file
	 * denoted by a specified pathname has failed.
	 * @throws FileAlreadyExistsException Checked exception thrown when an 
	 * attempt is made to create a file or directory and a file of that 
	 * name already exists.
	 * @throws NoSuchFileException Checked exception thrown when an attempt 
	 * is made to access a file that does not exist.
	 * @throws NotDirectoryException Checked exception thrown when a file 
	 * system operation, intended for a directory, fails because the file is
	 * not a directory.
	 * @return A String with Data to be send back to the Client.
	 * */
	
	private static String command(String recived, ServerSocket server) 
		throws IOException, FileNotFoundException, 
			FileAlreadyExistsException, NoSuchFileException, 
				NotDirectoryException {
		
		String [] arr = recived.split( ";" );
			
		/*-  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -*/
		//System.out.println( "DEBUG: parsing" );	
		/*DONE*/
		if (arr[0].equals("directoryListing")) {
			
			String nameOfDir = arr[1];
			nameOfDir = nameOfDir.replaceAll( "\\n|\\r", "");
				
			String dirView = directoryListing(nameOfDir);
			
			return dirView;	
		}
		/*-  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -*/
			
		/*DONE*/
		
		else if(arr[0].equals("renameDirectory")) {
			
			String nameOfDir = arr[1];
			String newNameOfDir = arr[2];
			
			nameOfDir = nameOfDir.replaceAll( "\\n|\\r", "");
			newNameOfDir = newNameOfDir.replaceAll( "\\n|\\r", "");
			//System.out.println( "DEBUG: renameDir: "+ nameOfDir );
			String returnval = renameDirectory( nameOfDir, newNameOfDir);
			
			return returnval;
			
		}
		/*-  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -*/
			
		/*DONE*/
		else if(arr[0].equals("renameFile")) {
			String nameOfFile = arr[1];
			String newNameOfFile = arr[2];
			
			nameOfFile = nameOfFile.replaceAll( "\\n|\\r", "");
			newNameOfFile = newNameOfFile.replaceAll( "\\n|\\r", "");
				
			String returnval = renameFile(nameOfFile, newNameOfFile);
			
			return returnval; //Yes I could write this in a single line but I wont
		}
		/*-  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -*/
			
		/*DONE*/
		else if(arr[0].equals("addDirectory")) {
				
			String nameOfDir = arr[1];
			
			nameOfDir = nameOfDir.replaceAll( "\\n|\\r", "");
			
			String returnval = addDirectory(nameOfDir);	
			
			return returnval;
		}
			
		/*-  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -*/
			
		else if(arr[0].equals("appendToFile")) {
			
			String nameOfFile = arr[1];
			String text = arr[2];
			
			nameOfFile = nameOfFile.replaceAll( "\\n|\\r", "");
			
			String returnval = appendToFile(nameOfFile, text);
			
			return returnval;
		}
			
		/*-  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -*/
			
		else if(arr[0].equals("getFile")) {
			
			Socket soc = server.accept();
			
			PrintWriter print = new PrintWriter(soc.getOutputStream());
			String nameOfFile = arr[1];
			
			nameOfFile = nameOfFile.replaceAll( "\\n|\\r", "");
			
			String content = getFile( nameOfFile, print);
			
			return content;
		}
		
		/*-  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -*/
		
		/*TODO NOT DONE*/
		else if(arr[0].equals("closeProgram\n")) {
			return "close";
		}
		return "Command Unknown";
	}		
	
	/*-  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -*/
	
	/**This Method is responsible to receiving data from the Client
	 * @param soc The socket of the Server to receive the Data
	 * @throws IOException  if an I/O error occurs when creating the input 
	 * stream, the socket is closed, the socket is not connected, or the socket 
	 * input has been shutdown using shutdownInput() 
	 * @throws SecurityException Thrown by the security manager to indicate 
	 * a security violation.
	 * @throws IllegalArgumentException Thrown to indicate that a method has
	 * been passed an illegal or inappropriate argument.
	 * @return The string received
	 * */
	
	private static String receiver(Socket soc) 
		throws IOException, SecurityException, IllegalArgumentException{
		
		BufferedReader read = new BufferedReader(new InputStreamReader(
		soc.getInputStream()));
		
		String received = new String();
    	
		String input;
		
		while((input = read.readLine()) != null)
		{
			received = received + input + "\n";
		}
		received.trim();
		read.close();
		//soc.close();
		
		//System.out.println( "DEBUG: DATA received: " + received 
		//+"\nSocket is closed? " + soc.isClosed());
		
		return received;
	}
	
	/*-  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -*/
	
	/**This Method is responsible to sending the data to the Client
	 * @param server The Server to allow the opening of a new socket
	 * @param returnparam The String of Data to be send
	 * @throws IOException  if an I/O error occurs when creating the input 
	 * stream, the socket is closed, the socket is not connected, or the socket 
	 * input has been shutdown using shutdownInput() 
	 * @throws SecurityException Thrown by the security manager to indicate 
	 * a security violation.
	 * @throws SocketTimeoutException Signals that a timeout has occurred on
	 * a socket read or accept.
	 * @throws IllegalBlockingModeException Unchecked exception thrown when 
	 * a blocking-mode-specific operation is invoked upon a channel in the 
	 * incorrect blocking mode.
	 * */
	
	private static void sender(ServerSocket server, String returnparam) 
		throws IOException, SecurityException, SocketTimeoutException, 
			java.nio.channels.IllegalBlockingModeException{
		
		Socket soc = server.accept();
		
		BufferedReader read = new BufferedReader(new InputStreamReader(
		soc.getInputStream()));
		
		String received = new String();
    	
		String input;
		//System.out.println( "DEBUG: Waiting to send\nSoclet is Closed?: "
		//	+soc.isClosed());
		while(!soc.isConnected()) {System.out.println( "Waiting" );}
		
		PrintWriter pw = new PrintWriter(soc.getOutputStream());
		pw.println( returnparam );
		pw.flush();
		pw.close();
		soc.close();
		//System.out.println( "DEBUG: Send attempt made" );
	}
	
	/* ==================================================================== */
	
	
	/** The main method of the server. 
	 * It handles all exceptions as well as calling sending an receiving 
	 * Data methods.
	 * @param args The program arguments not used in this program
	 */
	
	public static void main( String [] args ){
		//System.out.println( "DEBUG: I Started" );
		ServerSocket server;
		try {
			server = new ServerSocket(65535);
    		do {
    			try {
    				Socket soc = server.accept();
    				//System.out.println( "DEBUG: Socket is Closed?: " + soc.isClosed() );
    			
    				String received = receiver(soc);
    			
    				String returnparam = command(received, server);
    				
    				if(returnparam.equals( "close" )) {
    					soc.close();
    					server.close();
    					System.exit( 0 );
    				}
    			
    				sender(server, returnparam);
    				
    				soc.close();
    				
    				//System.out.println( "DEBUG: I looped around Socket is Closed?: " + soc.isClosed() );
    				}
        		catch(FileNotFoundException e) {
        			e.printStackTrace();
        			e.getCause();
        			e.getMessage();			
        		}
        		catch(FileAlreadyExistsException e) {
        			e.printStackTrace();
        			e.getCause();
        			e.getMessage();			
        		}
        		catch(NoSuchFileException e) {
        			e.printStackTrace();
        			e.getCause();
        			e.getMessage();			
        		}
        		catch (NotDirectoryException e) {
        			e.printStackTrace();
        			e.getCause();
        			e.getMessage();		
        		}
        		catch (SecurityException e) {
        			e.printStackTrace();
        			e.getCause();
        			e.getMessage();
        		}
        		catch (SocketTimeoutException e) {
        			e.printStackTrace();
        			e.getCause();
        			e.getMessage();
        		}
        		catch (IllegalBlockingModeException e) {
        			e.printStackTrace();
        			e.getCause();
        			e.getMessage();
        		}
        		catch (IllegalArgumentException e) {
        			e.printStackTrace();
        			e.getCause();
        			e.getMessage();
        		}
    		}while(true);
		}
    	catch (IOException e) {
    		e.printStackTrace();
    		e.getCause();
    		e.getMessage();	
    	}
	}	
}
