package server;

/* class Server
 * ============
 * Barebones server to show a simple single-threaded client-server model
 *
 * By Jacqueline W.
 */
public class Server
{
        private int port;                                 //port the server uses

	/* Constructor
	 * ===========
	 * Sets up the server to use given port.
	 */
        public Server( int port )
	{
                this.port = port;
	}

	/* serveRequest() : PRIVATE
	 * ========================
	 * Serves requests from client(s).
	 */
	private void serveRequest()
	{
                System.out.println( "Waiting for request from client..." );
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
