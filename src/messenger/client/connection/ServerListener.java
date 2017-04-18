package messenger.client.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.net.SocketException;
import java.util.BitSet;
import java.util.HashMap;

import javax.swing.JOptionPane;

import messenger.client.Session;


public class ServerListener implements Runnable{
	private ObjectInputStream ois;
	private boolean active;
	private KeyAccept ka;
	private HashMap<String, Object> answers;
	public ServerListener(HashMap<String, Object> answers, InputStream is) throws IOException {
		this.answers = answers;
		this.ois = new ObjectInputStream(is);

		System.err.println("Sono entrato nel listener");
			
		active = true;
		Thread refresh = new Thread(new RefreshFrequency());
		refresh.start();
		
	}
	
	@Override
	public void run() {
		while(active){
			try {
				listen();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	private void listen() throws IOException {
		System.err.println("esecuzione di listen");
		int hb=0;
		String message = "";
		String code = "";
		String text = "";
		try {
			Object object = ois.readObject();
			System.err.println("qui2");
			if(object instanceof String)
				message = (String)(object);
			else if(object instanceof HashMap){
				answers.put("chatlist", object);
			}
			else if(object instanceof BitSet){
				answers.put("refreshOnline", object);
			}
			else if(object instanceof byte[]){
				answers.put("image", object);
			}
			else{
				return;
			}
		System.out.println("Analisi di "+message);
		if(message.equalsIgnoreCase("hb") || message.equalsIgnoreCase("")){
			hb++;
			if(hb==50){
				//Session.getConnection().refreshOnline();
				hb=0;
			}
		}
		else if(message.startsWith("$$noSession")){
			String un = message.split(" ")[1];
			Session.destroyRemoteDES(un);
		}
		else if(message.startsWith("$$receiveCriptedMessage")){
			String to = null;
			String toArray[] = message.split(" ");
			text = message.substring(toArray[0].length() + toArray[1].length() + 2);
			to = toArray[1];
			if(Session.getKeyList().containsKey(to))
				System.out.println("Messaggio criptato correttamente");
			else{
				JOptionPane.showMessageDialog(null, "Hai ricevuto un messaggio criptato, ma non esiste nessuna connessione con "+to);
				System.exit(-1);
			}if(Session.getChatList().exist(to))
				Session.getChatList().getChatByName(to).addMessageToMe(Session.getDES(to).decrypt(text));
			else 
				Session.getChatList().addChat(to).addMessageToMe(Session.getDES(to).decrypt(text));
		}
		else if(message.startsWith("$$receiveCriptedKey")){
			ka.decodeKey(Long.decode(message.split(" ")[1]));
		}
		else if(message.startsWith("$$receiveKeyRequest")){
			ka = new KeyAccept(message);
		}
		else if(message.startsWith("$$receiveKey")){
			String[] keys = message.split(" ")[1].split(",");
			String username = message.split(" ")[2];
			long pk = Long.decode(keys[0]);
			long mod = Long.decode(keys[1]);
			Session.createDES(username, mod);
			long key = Session.getKey(username);
			BigInteger bi = BigInteger.valueOf(key);
			bi = bi.modPow(BigInteger.valueOf(pk), BigInteger.valueOf(mod));
			System.out.println("pk: "+pk+" mod: "+mod+" key: "+key);
			String answer = Session.getConnection().sendRequest("$$sendCriptedKey "+username+" "+bi);
			System.out.println("Ho inviato un sendCriptedKey ed ho ricevuto: "+answer);
		}
		
		else if(message.startsWith("$$receiveMessage")){
			String to = null;
			String toArray[] = message.split(" ");
			text = message.substring(toArray[0].length() + toArray[1].length() + 2);
			to = toArray[1];
			
			System.err.println("Sto ricevendo il seguente messaggio da "+to);
			System.err.println(text);
			if(Session.getChatList().exist(to))
				Session.getChatList().getChatByName(to).addMessageToMe(text);
			else 
				Session.getChatList().addChat(to).addMessageToMe(text);;
		}
		else if(message.startsWith("$$")){
			code = message.split(" ")[0];
			text = message.substring(code.length());
			text = text.trim();
			code = code.replaceAll("\\$", "");
			System.err.println( "code:"+code+" text:"+text);
			answers.put(code, text);
			System.err.println(message+code+text);
			
			//answers.notify();
		}else{
			try {
				throw new Exception();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch(SocketException e){
			System.out.println(e.getMessage());
			if(e.getMessage().equals("socket closed"))
				System.exit(-1);
			else
				e.printStackTrace();
		}
	}
	public void close() {
		active = false;
	}
	

}
