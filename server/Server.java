package server;

import server.ClientHandler;

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
public class Server implements Runnable
{
    private int port;                                 //port the server uses
    private boolean is_stopped = false;
	
	private ServerSocket server_socket = null;        //socket for port
    private int client_number = 0;                    //identifier for client

	/* Constructor
	 * ===========
	 * Sets up the server with a given port.
	 */
        public Server( int port )
	{
        this.port = port;	
	}

    private void stop()
    {
        System.out.println( "INFO: Stopping server..." );
        this.is_stopped = true;
        
        try
        {
            this.server_socket.close();
        }
        catch ( IOException ioe )
        {
            throw new RuntimeException( "ERROR: Failed to close server!", ioe );
        }

        System.out.println( "INFO: Closed socket..." );
    }

    private boolean isStopped()
    {
        return this.is_stopped;
    }

	/* createSockets() : PRIVATE
	 * =========================
	 * Creates the server socket and the client socket. Throws IOException
	 * on error.
	 */
	private Socket createSockets() throws IOException
	{
		//create socket bound to port
        try
        {
            this.server_socket = new ServerSocket( this.port );
        }
        catch ( BindException be )
        {
            be.printStackTrace();
        }
		//block until connection is made with client
		Socket client_socket = this.server_socket.accept();
        this.client_number++;

		System.out.println( this.client_number + ":Accepted client " +
				            client_socket.getInetAddress() );

        return client_socket;
	}

	/* serveRequest() : PRIVATE
	 * ========================
	 * Serves a request from a client. Connects with client, waits for
	 * request from client.
	 */
	private void serveRequest() throws IOException
	{
        Socket client_socket = null;        
        while ( !isStopped() )
        {
            client_socket = createSockets();

		    //create new client handler to process requests
            ClientHandler handler = new ClientHandler( client_socket,
                                                       this.port,
                                                       this.client_number );
            //create new thread for client handler
            new Thread( handler ).start();
        }
	}

    @Override
    public void run()
    {
        try
        {
            serveRequest();
        }
        catch ( IOException ioe )
        {
            throw new RuntimeException( "ERROR: dealing with IO on socket", ioe );
        }
    }

	/* main() : PUBLIC
	 * ===============
	 * Program entrypoint for creating a server that listens to port 8000.
	 */
	public static void main( String[] args )
	{
        System.out.println( "Starting socket-based server..." );
		
		Server server = new Server( 9000 );       //uses port 9000
	    new Thread( server ).start();
	}
}
