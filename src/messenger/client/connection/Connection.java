package messenger.client.connection;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.BitSet;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import messenger.client.Session;
import messenger.client.cryptography.Crittografia;

public class Connection {

	private Socket socket;
	private ObjectOutputStream oos;
	private ServerListener sl;
	private HashMap<String, Object> answers;
	
	public Connection(){
		ConnectionData d = new ConnectionData();
		answers = new HashMap<String, Object>();
		socket = null;
		oos = null;
		try {
			socket = new Socket(d.getHost(), d.getPort());
			oos = new ObjectOutputStream(socket.getOutputStream());
			Thread t = new Thread(new ServerListener(answers, socket.getInputStream()));
			t.start();
		} 
		catch(ConnectException errore2){
			JOptionPane.showMessageDialog(null, "Impossibile contattare il server", "Errore",
					JOptionPane.ERROR_MESSAGE);
		}
		catch (IOException errore) {
			errore.printStackTrace();
		} 



	}
	
	public Socket getSocket(){
		return socket;
	}
	public void close(){
		try {
			if(socket!=null)
				socket.close();
			if(oos != null)
				oos.close();
			//sl.close();
		} catch (IOException e) {
			System.err.println("Errore durante la chiusura");
		}
		
	}
	
	/*
	 * deprecato
	 * public String getAnswer() throws ClassNotFoundException, IOException{
		if(ois == null)
			return null;
		String answer = "";
		do{
			answer = (String) ois.readObject();
		}while(answer.equalsIgnoreCase("hb"));
		return answer;
	}*/
	
	public String sendRequest(String request) {
		System.out.println("Inizio invia richiesta: "+request);
		String code;
		code = request.split(" ")[0];
		code = code.replaceAll("\\$", "");
		
		
		String success = "bbb";
		if(oos==null)
			return "err_in";
		answers.put(code, null);
		try {
			oos.writeObject(request);
			if(!(request.startsWith("$$sendKey")||request.startsWith("$$sendCriptedKey")|| request.startsWith("$$sendPrivateMessage"))){
				do{
					success = (String)answers.get(code);
				}while(success == null);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Fine invio richiesta: "+request+" "+success);
		return success;
		
	}
	
	
	public String signIn(String username, String password, String email, BufferedImage image) {
		password = Crittografia.encrypt(username,password);
		username = username.toLowerCase();
		email = email.toLowerCase();
		String answer =  sendRequest("$$registra "+username+" "+password+" "+email+" "+0);
		//JOptionPane.showMessageDialog(null, answer);
		if(answer.equals("ok")){
			//JOptionPane.showMessageDialog(null, new JLabel(new ImageIcon(image)));
			byte[] imageByte;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try {
				ImageIO.write(image, "jpg", baos);
				baos.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			imageByte = baos.toByteArray();
	    	try {
				oos.writeObject(imageByte);
				oos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return answer;
	}
	
	public String sendMessage(String to, String message){
		return sendRequest("$$sendMessage "+to+" "+message);
		
	}
	public String sendPrivateMessage(String to, String message){
		String r = sendRequest("$$sendPrivateMessage "+to+" "+message);
		return r;
	}
	public boolean isOnline(String username){
		return sendRequest("$$isOnline "+username).equalsIgnoreCase("y")?true:false;
	}
	
	/*public String[] privateSession(){
		long key = 0;
		String answer = sendRequest("$$privateSession");
		String keys[] = answer.split(" ");
		if(keys.length==2){
			//key = new DES(
			sendRequest("$$sendKey ");
			return null;
		}
		else throw new NullPointerException();
	}*/
	
	
	public boolean startPrivateSession(String username){
		if(!Session.getKeyList().containsKey(username)){
			if(!isOnline(username)){
				return false;
			}
			sendRequest("$$privateSession "+username);		
			return true;
		}
		else{
			Session.destroyDES(username);
			return false;
		}
		
	}
	
	
	public String login(String username, String password){
		password = Crittografia.encrypt(username, password);
		username = username.toLowerCase();
		return sendRequest("$$login "+username+" "+password);
	}
	
	
	@SuppressWarnings("unchecked")
	public HashMap<String, String[]> getChatList(){
		if(oos==null)
			return null;
		HashMap<String, String[]> hm = null;
		try {
			oos.writeObject("$$chatlist "+Session.getUsername());
			oos.flush();
			answers.put("chatlist", null);
			do{
				hm = (HashMap<String, String[]>)answers.get("chatlist");
			}while(hm == null);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return hm;
		
	}


	public void refreshOnline() {
		BitSet online = null;
		String list = String.join(",", Session.getChatList().getUsernameList());
		System.err.println("####***##___@@@@!!!!!!!!! "+list);
		try {
			oos.writeObject("$$refreshOnline "+list);
			oos.flush();
			answers.put("refreshOnline", null);
			System.out.println("Ho inviato la richiesta");
			do{
				online = (BitSet)answers.get("refreshOnline");
			}while(online==null);
			System.out.println("Ho ricevuto risposta"+online);
			Session.refreshOnline(online);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	public BufferedImage askImage(String username) {
	   	BufferedImage bImageFromConvert  = null;
		byte[] byteImage = null; 
		String answer = sendRequest("$$askImage "+username);
		//JOptionPane.showMessageDialog(null, "risposta: "+answer);
		if(answer.equals("true")){
			answers.put("image", null);
			do{
				byteImage = (byte[])answers.get("image");
			}while(byteImage==null);
			InputStream in = new ByteArrayInputStream(byteImage);
	 

	    	try {
				bImageFromConvert = ImageIO.read(in);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if(answer.equals("false"))
			return null;
		else
			JOptionPane.showMessageDialog(null, "AAAAAAAAAA");
		return bImageFromConvert;
	}

	public void destroySession(String username2) {
		sendRequest("$$destroySession "+username2);
		
	}


	

}
