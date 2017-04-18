package messenger.client.chat;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.SortedSet;
import java.util.TreeSet;
import messenger.client.Session;
import messenger.client.window.ChatsWindow;

public class ChatList implements Serializable{
	private LinkedList<Chat> conversazioni;
	
	public ChatList(){
		conversazioni = new LinkedList<Chat>();
	}
	
	public void fillFromDB(){
		HashMap<String, String[]> hm = Session.getConnection().getChatList();
		int rows = hm.get("isMittente").length;
		for(int i=0; i<rows; i++){
			System.out.println("aggiungo "+hm.get("username")[i]);
			addChat(hm.get("username")[i], (hm.get("isMittente")[i].equals("1")), hm.get("testo")[i]);
			//final int k = i;
			
		}
			
	}
	
	public void askImage(String username) {
		BufferedImage image = Session.getConnection().askImage(username);
		getChatByName(username).setImage(image);
	}

	public boolean addChat(Chat c){
		if(conversazioni.size()>=100)
			return false;
		conversazioni.add(c);
		if(Session.getMainWindow()!= null){
			ChatsWindow cw = (ChatsWindow)Session.getMainWindow();
			String name = c.getName();
			if(!cw.isChatOpen(name))
				cw.addNewChatRealTime(name);
			
		}
		
		return true;
	}
	
	public Chat addChat(String name){
		Chat c = null;
		if(!exist(name))
			c=new Chat(name);
		addChat(c);
		return c;
	}
	
	public Chat addChat(String name, boolean isSender, String text){
		Chat c = null;
		boolean newChat = !exist(name);
		if(!newChat)
			c = getChatByName(name);
		else
			c = new Chat(name);
		
		if(isSender)
			c.addMessageFromMe(text);
		else
			c.addMessageToMe(text);
		if(newChat)
			addChat(c);
		return c;
	}
	
	public Chat getChatByName(String name){
		for(int i=0; i<conversazioni.size(); i++)
			if(conversazioni.get(i).getName().equalsIgnoreCase(name))
				return conversazioni.get(i);
		return null;
			
	}
	
	public Chat getChatById(int id){
		if(id<0 || id>conversazioni.size())
			return null;
		return conversazioni.get(id);
	}
	
	public void addMessage(String mittente, String destinatario, String testo){
		
	}
	
	public boolean exist(String name){
		return getChatByName(name)!=null?true:false;
	}
	
	public int getChatsNum(){
		return conversazioni.size();
	}
	
	public SortedSet<String> getUsernameList(){
		SortedSet<String> ss = new TreeSet<String>();
		for(int i=0; i<conversazioni.size(); i++)
			if(!conversazioni.get(i).isEmpty())
				ss.add(conversazioni.get(i).getName());
		
		return ss;
		
	}
	
	

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
