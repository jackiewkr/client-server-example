package client;

import java.net.*;
import java.io.*;

public class Client
{
        private String host;
	private int port;

	private Socket socket = null;

	public Client( String host, int port )
	{
                this.host = host;
		this.port = port;
	}

	private void makeRequest() throws RuntimeException
	{
                System.out.println( "Trying to open socket to server..." );

	        try
		{
                        this.socket = new Socket( this.host, this.port );
		}
		catch ( UnknownHostException uhe )
		{
			throw new RuntimeException( "Unknown host: " + host + uhe );
		}
		catch ( IOException ioe )
		{
                        throw new RuntimeException( "Could not open port "
						    + port + " on host " + host
				                    , ioe );
		}

		try
		{
		        PrintWriter out = new PrintWriter( socket.getOutputStream(), true );
		        BufferedReader in  = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );

			String request = "GET";
			out.println( request );
			System.out.println( "Sent " + request + "to server." );

			System.out.println( "Response: " );
			String response = in.readLine();
			System.out.println( response );

                        in.close();
			out.close();
			
			this.socket.close();
                }
		catch ( IOException ioe )
		{
                        throw new RuntimeException( "Failed to send or get response!" + ioe );
		}

	        
		
	}

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
