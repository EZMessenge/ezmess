package messenger.client.window;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import messenger.client.Session;

public abstract class ConnectedFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConnectedFrame(String title){
		super(title);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.addWindowListener(new WindowAdapter(){
			@Override
            public void windowClosing(WindowEvent we) {
                Session.close();
                System.exit(0);
            }
		});
	}
}
