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

	private final int REQUESTS_AMT = 3;               //no. of requests
	                                                  //to send

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

	/* sendRequests() : PRIVATE
	 * ========================
	 * Sends a number of requests to the server defined by REQUESTS_AMT.
	 * Prints response to stdout.
	 * Throws IOException on error.
	 */
	private void sendRequests() throws IOException
	{
		createIOStreams();

		//send GET request to server
                String request = "GET";
		String response;
		for ( int i = 0; i < REQUESTS_AMT; i++ )
		{
			out.println( request );
			System.out.println( "Sent " + request + " to server." );

			System.out.println( "Response: " );
			response = in.readLine();
			System.out.println( response );
                }
		in.close();
		out.close();
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

		//create socket to server
	        createSocket();

		//send multiple requests to server
		try
		{
		        sendRequests();
		}
		catch ( IOException ioe )
		{
		        throw new RuntimeException( "Failed to send request(s)!"
				                    , ioe );
		}

		//clean up opened resources
		try
		{
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
                        Client client = new Client( "localhost", 9000 );
			client.makeRequest();
		}
		catch ( RuntimeException re )
		{
                        System.out.println( "Couldnt make request!" + re );
		}
	}
}
