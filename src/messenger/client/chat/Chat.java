package messenger.client.chat;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.LinkedList;







import javax.imageio.ImageIO;



import messenger.client.Session;
import messenger.client.connection.ConnectionData;
import messenger.client.window.ChatsWindow;
import messenger.client.window.ImageHandler;
// un interlocutore è dato dalla stringa name, l'altro lo si ottiene da Session.getName();

public class Chat {
	private String name;
	private String name2;
	private LinkedList<Message> messages;
	private BufferedImage image;
	public Chat(String name){
		this.name = name.toLowerCase();
		messages = new LinkedList<Message>();
		name2 = Session.getUsername();
				URL url = null;
				try {
					url = new URL("http://"+ConnectionData.host+"/image/?u="+name);
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				}
				BufferedImage img = null;
				try {
					img = ImageIO.read(url);
				} catch (IOException e) {
					e.printStackTrace();
				}
				setImage(img);
	}
	
	public int getMessagesNum(){
		return messages.size();
	}
	
	public Message getMessageById(int id){
		return messages.get(id);
	}
	
	public String getTextById(int id){
		return messages.get(id).getText();
	}
	
	
	public String getName(){
		return name;
	}
	
	public void addMessageFromMe(String text){
		addMessage(new Message(name2, new Date(System.currentTimeMillis()), text));
	}
	
	public void addMessageToMe(String text){
		addMessage(new Message(name, new Date(System.currentTimeMillis()), text));
	}
	
	public void addMessage(Message m){
		messages.add(m);
		if(Session.getMainWindow() != null){
			ChatsWindow cw = (ChatsWindow)Session.getMainWindow();
			System.err.println("[[[ "+name+" "+Session.getShowedChat()+" "+cw.isChatOpen(name));
			if(Session.getShowedChat()!= null && Session.getShowedChat().equalsIgnoreCase(name))
				cw.update();
			
		}

	}
	
	public int getNum(){
		return messages.size();
	}
	
	public String getFullSimpleText(){
		String text = "";
		for(Message m : messages){
			String mittente = (m.amISender())?name2:name;
				
			text += mittente.toUpperCase()+": "+m.getText()+"\n";
		}
		return text;
	}
	
	@Override
	public String toString(){
		return "Conversazione tra "+name+" e "+name2;
	}

	public boolean isEmpty() {
		return messages.size()==0;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		if(image!=null){
			image = ImageHandler.resizeImageWithHint(image, 1, 50, 50);
			this.image = image;
		}
		
	}
	
}
