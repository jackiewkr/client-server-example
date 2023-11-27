package server;

import java.net.*;
import java.io.*;

import java.time.LocalTime;

public class ClientHandler implements Runnable 
{
    private Socket client_socket = null;
    private int server_port = 0;
    private int client_number = 0;

    private BufferedReader in = null;
    private PrintWriter out = null;
        
    public ClientHandler( Socket clientSocket, int serverPort, 
                          int clientNumber )
        {
            this.client_socket = clientSocket;
            this.server_port = serverPort;
            this.client_number = clientNumber;
        }

    /* createIOStreams() : PRIVATE
	 * =========================
	 * Creates the IO streams for the server. Input is initialized as a
	 * BufferedReader, output is initialized as a PrintWriter.
	 * Throws IOException on error.
	 */
	private void createIOStreams() throws IOException
	{
        //create IO streams for in and out
		InputStreamReader in_stream = null;
		in_stream = new InputStreamReader( client_socket.getInputStream() );
		
		this.in = new BufferedReader( in_stream );
		this.out = new PrintWriter( client_socket.getOutputStream(), true );
	}

    /* processRequests()
	 * =================
	 * Creates IO streams and processes requests from client until the
	 * socket is no longer connected, then closes the IO streams.
	 */
	private void processRequests() throws IOException
	{
        System.out.println( this.client_number + ": Processing requests" );

		createIOStreams();

                
	    String response = "You made a request to port " + this.server_port +
                          " at " + LocalTime.now();
        String clientID = "";

		boolean keepRunning = true;
		while ( keepRunning )
		{
			System.out.println( this.client_number + 
            ": Waiting for request..." );
			
            //get request from client
			String request = this.in.readLine();
			
			//check whether socket is still connected
			if( request == null )
			{
			    keepRunning = false;
			}
            else
            {
			    System.out.println( this.client_number + 
                                    ": Got request from client :" + request +
                                    ":" );
                //clientID = request.substring( 0, 8 );
			        
                //send reply to client
			    this.out.println( response + clientID + "." );
            }
		}

		//close in and out
		this.in.close();
		this.out.close();
                
        System.out.println( this.client_number + 
                            ": Finished processing requests." );
        }

        @Override
        public void run()
        {
                try
                {
                        processRequests();
                }
                catch ( IOException ioe )
                {
                        ioe.printStackTrace();
                }
        }
}
