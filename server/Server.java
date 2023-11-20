package server;

//libraries for sockets
import java.io.*;
import java.net.*;

//library for time
import java.time.LocalTime;

/* class Server
 * ============
 * Simple server to show a simple single-threaded client-server model
 *
 * By Jacqueline W.
 */
public class Server
{
        private int port;                                 //port the server uses
	
	private ServerSocket server_socket = null;        //socket for port
	private Socket client_socket = null;              //socket for client
	
	private BufferedReader in = null;                 //in stream
	private PrintWriter out = null;                   //out stream

	/* Constructor
	 * ===========
	 * Sets up the server with a given port.
	 */
        public Server( int port )
	{
                this.port = port;

		
	}

	/* createSockets() : PRIVATE
	 * =========================
	 * Creates the server socket and the client socket. Throws IOException
	 * on error.
	 */
	private void createSockets() throws IOException
	{
		//create socket bound to port
                this.server_socket = new ServerSocket( this.port );
		//block until connection is made with client
		this.client_socket = this.server_socket.accept();

		System.out.println( "Accepted client " +
				    this.client_socket.getInetAddress() );
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
		this.out = new PrintWriter( client_socket.getOutputStream(),
					    true ); //flushing not buffered
	}

	/* serveRequest() : PRIVATE
	 * ========================
	 * Serves a request from a client. Connects with client, waits for
	 * request from client.
	 */
	private void serveRequest() throws RuntimeException
	{
                
                try                                       //create sockets
		{
			createSockets();
		}
		catch ( IOException ioe )
		{
                        throw new RuntimeException( "Failed to create socket!",
				                    ioe );
		}

		try                                       //create io streams
		{
		        createIOStreams();
		}
		catch( IOException ioe )
		{
                        throw new RuntimeException( "Failed to create streams!",
				                    ioe );
		}

		try
		{
			//get request from client
			String request = this.in.readLine();
			System.out.println( "Got request from client " +
					    request + " ." );
                }
		catch ( IOException ioe )
		{
                        throw new RuntimeException( "Failed to read request",
				                    ioe );
		}

		//send reply to client
		String response = "You made a request to port " + port + " at "
			+ LocalTime.now();
                this.out.println( response );

		try
		{
			//close in and out
			this.in.close();
			this.out.close();

			//close socket
			this.server_socket.close();
		}
		catch ( IOException ioe )
		{
                        throw new RuntimeException( "Failed to close clean!",
				                    ioe );
		}
	}

	/* main() : PUBLIC
	 * ===============
	 * Program entrypoint for creating a server that listens to port 8000.
	 */
	public static void main( String[] args )
	{
                System.out.println( "Starting socket-based server..." );
		
		Server server = new Server( 8000 );       //uses port 8000
		try
		{
                        server.serveRequest();
		}
		catch ( RuntimeException re )
		{
                        System.out.println( "Could not serve request!\n" + re );
		}
	}
}
