package messenger.client;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.BitSet;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import messenger.client.chat.ChatList;
import messenger.client.connection.Connection;
import messenger.client.connection.ConnectionData;
import messenger.client.cryptography.DES;
import messenger.client.cryptography.PrimeNumber;
import messenger.client.window.ChatsWindow;

public class Session {
	private static String username = null;
	private static Connection connection;
	private static ChatList chatList;
	private static JFrame jFrame;
	private static String current;
	private static HashMap<String, Long> privateSessions;
	private static HashMap<String, DES> desMap;
	private static BitSet online;
	private static BufferedImage image;
	
	public Session(String un, Connection c) {
		username = un.toLowerCase();
		connection = c;
		privateSessions = new HashMap<String ,Long>();
		desMap = new HashMap<String, DES>();
		online = new BitSet();
		URL url = null;
		try {
			url = new URL("http://"+ConnectionData.host+"/image/?u="+username);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		BufferedImage img = null;
		try {
			img = ImageIO.read(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		image = img;
	}
	
	public Session(String un, Connection c, BufferedImage image2) {
		username = un.toLowerCase();
		connection = c;
		privateSessions = new HashMap<String ,Long>();
		desMap = new HashMap<String, DES>();
		online = new BitSet();
		image = image2;
	}

	public static void createDESWithKey(String name, long key){
		JOptionPane.showMessageDialog(null, name+" ha avviato una sessione privata con te, clicca sul lucchetto per chiuderla");
		privateSessions.put(name, key);
		desMap.put(name, new DES(ByteBuffer.allocate(Long.BYTES).putLong(privateSessions.get(name)).array()));
		ChatsWindow cw = (ChatsWindow)jFrame;
		cw.update();
	}
	
	public static DES createDES(String name, long limit){
		JOptionPane.showMessageDialog(null, "Hai avviato una sessione privata con "+name);
		privateSessions.put(name, PrimeNumber.get8BytesNumber(limit));
//		JOptionPane.showMessageDialog(null, "Sono "+getUsername()+" e ho generato la chiave des: "+privateSessions.get(name));
		System.out.println("Sto encriptando con la chiave: "+privateSessions.get(name));
		DES des = new DES(ByteBuffer.allocate(Long.BYTES).putLong(privateSessions.get(name)).array());
		desMap.put(name, des);
		ChatsWindow cw = (ChatsWindow)jFrame;
		cw.update();
		return des;
	}
	
	public static DES getDES(String name){
		return desMap.get(name);
	}
	
	public static long getKey(String name){
		return privateSessions.get(name);
	}
	
	
	public static String getUsername(){
		if(username==null)
			return "Nessuna sessione inizializzata";
		return username;
	}
	
	public static Connection getConnection(){
		return connection;
	}

	public static ChatList getChatList(){
		return chatList;
	}
	public static void createChatListFromDb() {
		chatList = new ChatList();
		chatList.fillFromDB();
		
	}
	
	public static void setMainWindow(JFrame jf) {
		jFrame = jf;
		
	}
	public static JFrame getMainWindow(){
		return jFrame;
	}

	public static void setShowedChat(String c){
		current = c;
	}
	
	public static String getShowedChat() {
		
		return current;
	}

	public static void close() {
		connection.close();
		
	}

	public static void refreshOnline(BitSet new_online) {
		online=new_online;
		if(jFrame!=null){
			ChatsWindow cw = (ChatsWindow)jFrame;
			cw.refreshOnline();
		}
		
	}

	public static BitSet getOnline() {
		return online;
	}
	
	
	public static void destroyRemoteDES(String username2){
		JOptionPane.showMessageDialog(null, username2+" ha chiuso la sessione privata con te");
		privateSessions.remove(username2);
		//connection.destroySession(username2);
		ChatsWindow cw = (ChatsWindow)jFrame;
		cw.update();
	}
	
	public static void destroyDES(String username2) {
		JOptionPane.showMessageDialog(null, "Hai chiuso la sessione privata con "+username2);
		privateSessions.remove(username2);
		connection.destroySession(username2);
		ChatsWindow cw = (ChatsWindow)jFrame;
		cw.update();
	}

	public static HashMap<String, Long> getKeyList() {
		return privateSessions;
	}

	public static BufferedImage getImage() {
		return image;
		
	}
}
