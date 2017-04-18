package messenger.client.window;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import messenger.client.connection.ConnectionData;


public class SettingDialog extends JDialog{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SettingDialog(Frame parent){
		super(parent);
		intUI();
	}

	private void intUI() {
		setModalityType(ModalityType.APPLICATION_MODAL);
		setSize(250,200);
		setResizable(false);
        setTitle("Impostazioni");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        Container p = this.getContentPane();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(new JLabel("Inserisci l'indirizzo dell'host"));
        JTextField host = new JTextField();
        host.setText(ConnectionData.host);
        p.add(host);
        p.add(Box.createGlue());
        p.add(Box.createRigidArea(new Dimension(20,20)));
        p.add(new JLabel("Inserisci la porta remota"));
        JTextField port = new JTextField();
        port.setText(""+ConnectionData.port);
        p.add(port);
        p.add(Box.createGlue());
        p.add(Box.createRigidArea(new Dimension(20,20)));
        JButton submit = new JButton("Invia");
        SettingDialog thisWindow = this;
        submit.addActionListener(new ActionListener(){
        	
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ConnectionData.host = host.getText();
				ConnectionData.port = Integer.decode(port.getText());
				thisWindow.dispose();
			}
        	
        });
        p.add(submit);
        setLocationRelativeTo(getParent());
		
	}
}
