package messenger.client.chat;

import java.util.Date;

import messenger.client.Session;

public class Message {
	//private int id;
	private boolean imsender;
	private Date orario;
	private String text;
	private boolean read;
	
	// da implementare: id
	public Message(String mittente, Date orario, String text){
		this.imsender = mittente.equals(Session.getUsername());
		this.orario = orario;
		this.text = text;
		read = false;
	}
	
	public void read(){
		read = true;
	}
	
	public boolean amISender(){
		return imsender;
	}
	
	public Date getOrario(){
		return orario;
	}
	
	public String getText(){
		return text;
	}
	
	public void changeText(String text){
		this.text = text;
	}
	
	public boolean getRead(){
		return read;
	}
}
