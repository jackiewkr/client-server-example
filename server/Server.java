package server;

//libraries for TCP/IP
import java.io.*;
import java.net.*;

/* class Server
 * ============
 * Barebones server to show a simple single-threaded client-server model
 *
 * By Jacqueline W.
 */
public class Server
{
        private int port;                                 //port the server uses
	private ServerSocket server_socket = null;        //socket for port
	private Socket client_socket = null;              //socket for client

	/* Constructor
	 * ===========
	 * Sets up the server to create a socket that is set up to listen to a
	 * given port and wait until a client connects.
	 */
        public Server( int port )
	{
                this.port = port;

		
	}

	/* serveRequest() : PRIVATE
	 * ========================
	 * Serves requests from client(s).
	 */
	private void serveRequest() throws IOException
	{
                System.out.println( "Waiting for request from client..." );

		//create socket bound to port
		this.server_socket = new ServerSocket( this.port );
		//block until connection is made with a client
		this.client_socket = server_socket.accept();

		System.out.println( "Accepted client " +
				    this.client_socket.getInetAddress() );

		//create IO streams for input and output
		InputStreamReader input_stream = new InputStreamReader( client_socket.getInputStream() );
		BufferedReader in = new BufferedReader( input_stream );
		PrintWriter out = new PrintWriter( client_socket.getOutputStream(), true );

		//get request from client
                String request = in.readLine();
		System.out.println( "Got request from client " +
			            request + " ." );

		//send reply to client
		String response = "You made a request to port " + port + " at "
			          + LocalTime.now() + ".";
                out.println( response );
		
		//close in and out
		in.close();
		out.close();
		
		//close socket
		this.server_socket.close();
		
	}

	/* main() : PUBLIC
	 * ===============
	 * Program entrypoint for creating a server that listens to port 8000.
	 */
	public static void main( String[] args )
	{
                System.out.println( "Starting socket-based server..." );
		
		Server server = new Server( 8000 );       //uses port 8000
                server.serveRequest();
	}
}
