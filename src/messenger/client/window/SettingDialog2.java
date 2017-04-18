package messenger.client.window;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import messenger.client.Session;
import messenger.client.Theme;

public class SettingDialog2 extends JDialog{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SettingDialog2(Frame parent){
		super(parent);
		intUI();
	}

	private void intUI() {
		SettingDialog2 thisWindow = this;
		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);
        setTitle("Impostazioni");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        Container p = this.getContentPane();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        ////
        Socket s = Session.getConnection().getSocket();
        p.add(new JLabel("Porta: "+s.getLocalPort()+" Indirizzo: "+s.getLocalAddress()));
        p.add(new JLabel("Colore messaggi ricevuti: "));
        JPanel tp1 = new JPanel(); 
        tp1.setLayout(new BoxLayout(tp1, BoxLayout.X_AXIS));
        JPanel c1 = new JPanel();
        c1.setPreferredSize(new Dimension(50,50));
        c1.setBackground(Theme.messageReceived);
        tp1.add(c1);
        tp1.add(Box.createGlue());
        JButton jb1 = new JButton("Cambia");
        jb1.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Color color = JColorChooser.showDialog(thisWindow, 
                        "Scegli colore", Theme.messageReceived);
               c1.setBackground(color);
				
			}
        	
        });
        tp1.add(jb1);
        p.add(tp1);
        p.add(Box.createGlue());
        p.add(Box.createRigidArea(new Dimension(20,20)));
        
        ////
        p.add(new JLabel("Colore messaggi inviati: "));
        JPanel tp2 = new JPanel(); 
        tp2.setLayout(new BoxLayout(tp2, BoxLayout.X_AXIS));
        JPanel c2 = new JPanel();
        c2.setPreferredSize(new Dimension(50,50));
        c2.setBackground(Theme.messageSent);
        tp2.add(c2);
        tp2.add(Box.createGlue());
        JButton jb2 = new JButton("Cambia");
        jb2.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Color color = JColorChooser.showDialog(thisWindow, 
                        "Scegli colore", Theme.messageSent);
               c2.setBackground(color);
				
			}
        	
        });
        tp2.add(jb2);
        p.add(tp2);
        p.add(Box.createGlue());
        p.add(Box.createRigidArea(new Dimension(20,20)));
        //
        
        p.add(new JLabel("Colore finestra: "));
        JPanel tp3 = new JPanel(); 
        tp3.setLayout(new BoxLayout(tp3, BoxLayout.X_AXIS));
        JPanel c3 = new JPanel();
        c3.setPreferredSize(new Dimension(50,50));
        c3.setBackground(Theme.windowColor);
        tp3.add(c3);
        tp3.add(Box.createGlue());
        JButton jb3 = new JButton("Cambia");
        jb3.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Color color = JColorChooser.showDialog(thisWindow, 
                        "Scegli colore", Theme.windowColor);
               c3.setBackground(color);
				
			}
        	
        });
        tp3.add(jb3);
        p.add(tp3);
        p.add(Box.createGlue());
        p.add(Box.createRigidArea(new Dimension(20,20)));
        
        //
        
        JButton submit = new JButton("Invia");
        
        submit.addActionListener(new ActionListener(){
        	
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					Theme.messageReceived = c1.getBackground();
					Theme.messageSent = c2.getBackground();
					Theme.windowColor = c3.getBackground();
					ChatsWindow cw = (ChatsWindow)Session.getMainWindow();
					//cw.setChat(Session.getShowedChat());
					cw.update();
				}catch(NullPointerException e){
					e.printStackTrace();
				}
				thisWindow.dispose();
			}
        	
        });
        p.add(submit);
        setLocationRelativeTo(getParent());
		pack();
	}
}
