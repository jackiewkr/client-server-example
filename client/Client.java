package client;

//libraries for sockets
import java.net.*;
import java.io.*;

/* class Client
 * ============
 * Simple Client to show a simple single-threaded client-server model.
 *
 * By Jacqueline W.
 */
public class Client
{
        private String host;                              //hostname for server
	private int port;                                 //port for server

	private Socket socket = null;                     //socket for server
	
	private BufferedReader in = null;                 //in stream
	private PrintWriter out = null;                   //out stream

	/* Constructor
	 * ===========
	 * Sets up the client with a given hostname and port.
	 */
	public Client( String host, int port )
	{
                this.host = host;
		this.port = port;
	}

        /* createSocket() : PRIVATE
	 * =========================
	 * Creates the socket to the server. Throws RuntimeException
	 * on error.
	 */
	private void createSocket() throws RuntimeException
	{
		//create socket bound to port
                try
		{
                        this.socket = new Socket( this.host, this.port );
		}
		catch ( UnknownHostException uhe )
		{
			throw new RuntimeException( "Unknown host: " + host
						    + uhe );
		}
		catch ( IOException ioe )
		{
                        throw new RuntimeException( "Could not open port "
						    + port + " on host " + host
				                    , ioe );
		}

		System.out.println( "Connected to server " +
				    this.socket.getInetAddress() );
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
		in_stream = new InputStreamReader( socket.getInputStream() );
		
		this.in = new BufferedReader( in_stream );
		this.out = new PrintWriter( socket.getOutputStream(),
					    true ); //flushing not buffered
	}

	/* makeRequest()
	 * =============
	 * Makes a request to the server and prints the reply given.
	 * Creates socket to server and IO streams, then sends request to
	 * server and prints response. Closes streams and sockets.
	 * Throws a RuntimeException on error
	 */
	private void makeRequest() throws RuntimeException
	{
                System.out.println( "Trying to open socket to server..." );

	        createSocket();

		try
		{
                        createIOStreams();
		}
		catch ( IOException ioe )
		{
                        throw new RuntimeException( "Failed to create streams!",
				                    ioe );
		}

		//send GET request to server
                String request = "GET";
		out.println( request );
		System.out.println( "Sent " + request + " to server." );

		//print response from server
		try
		{
			System.out.println( "Response: " );
			String response = in.readLine();
			System.out.println( response );
                }
		catch ( IOException ioe )
		{
                        throw new RuntimeException( "Failed to read response!"
						    + ioe );
		}

		//clean up opened resources
		try
		{
		        in.close();
			out.close();
			
			this.socket.close();
		}
		catch ( IOException ioe )
		{
                        throw new RuntimeException( "Failed to close claen!",
						    ioe );
		}
	}

	/* main() : PUBLIC
	 * ===============
	 * Program entrypoint for creating a client that sends a request to
	 * localhost:8000 and prints the reply.
	 */
	public static void main( String[] args )
	{
                System.out.println( "Starting socket-based client..." );

		try
		{
                        Client client = new Client( "localhost", 8000 );
			client.makeRequest();
		}
		catch ( RuntimeException re )
		{
                        System.out.println( "Couldnt make request!" + re );
		}
	}
}
