package messenger.client;


import javax.swing.SwingUtilities;






import messenger.client.window.LoginWindow;
public class Main {
	public static void main(String args[]){
		System.out.println("Nuovo client avviato");
		SwingUtilities.invokeLater(new Runnable(){
		      public void run(){
		    	  new LoginWindow();
		      }
		    });
		
	}
}
