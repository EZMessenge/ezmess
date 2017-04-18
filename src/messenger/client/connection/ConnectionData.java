package messenger.client.connection;

public class ConnectionData {
	public static int port = 3334;
	public static String host = "localhost";
	public int getPort(){
		return port;
	}
	public String getHost(){
		return host;
	}
	@SuppressWarnings("static-access")
	public void changePort(int port){
		this.port = port;
	}
	@SuppressWarnings("static-access")
	public void changeHost(String host){
		this.host = host;
	}
}
