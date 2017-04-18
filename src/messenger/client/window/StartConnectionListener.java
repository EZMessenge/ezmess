package messenger.client.window;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import messenger.client.Session;
import messenger.client.connection.Connection;

public class StartConnectionListener implements ActionListener{
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JTextField emailField;
	private BufferedImage image;
	private JFrame window;
	
	public StartConnectionListener(JFrame window, JTextField usernameField, JPasswordField passwordField){
		this.usernameField = usernameField;
		this.passwordField = passwordField;
		this.window = window;
	}
	
	public StartConnectionListener(JFrame window, JTextField usernameField, JPasswordField passwordField, JTextField emailField, BufferedImage image){
		this.usernameField = usernameField;
		this.passwordField = passwordField;
		this.emailField = emailField;
		this.image = image;
		this.window = window;
	}
	
	public void execute(String command){
		
		String un = usernameField.getText();
		String pw = new String(passwordField.getPassword());
		boolean check = un.contains(" ") || pw.contains(" ") || un.contains("$$") || pw.contains("$$");
		if(check)
			JOptionPane.showMessageDialog(null, "Hai inserito caratteri non ammessi in un campo","Errore", JOptionPane.WARNING_MESSAGE);
		else {
			String answer = "err_null";
			Connection c = new Connection();
			if(command.equals("Registrati")){
				answer = c.signIn(un, pw, emailField.getText(), image);
				new Session(un, c, image);
			}
			else if(command.equals("Login")){
				answer = c.login(un, pw);
				new Session(un, c);
			}
			else
				throw new NullPointerException();			
			switch(answer){
				case "ok":
					window.dispose();
					
					
					SwingUtilities.invokeLater(new Runnable(){
						@Override
						public void run() {
							Session.createChatListFromDb();
							ChatsWindow cw = new ChatsWindow();
							Session.setMainWindow(cw);
						}
					});
					break;
				case "psnull":
					JOptionPane.showMessageDialog(null, "Errore nello stream di ricezione","Errore", JOptionPane.ERROR_MESSAGE);
					throw new NullPointerException();
				case "exist":
					JOptionPane.showMessageDialog(null, "Il nome utente che hai scelto esiste già","Errore", JOptionPane.WARNING_MESSAGE);
					break;
				case "no_l":
					JOptionPane.showMessageDialog(null, "Username o password errata", "Errore", JOptionPane.WARNING_MESSAGE);
					break;
				default:
					JOptionPane.showMessageDialog(null,  "Errore imprevisto, ritenta", "Errore", JOptionPane.ERROR_MESSAGE);
					break;
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		execute(ae.getActionCommand());
	}
	

}
