package messenger.client.window;


import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import messenger.client.Session;

public class SelectChatListener implements MouseListener {

	//private ChatsWindow cw;

	private JPanel chatList;
	public SelectChatListener(ChatsWindow cw){
		//this.cw = cw;
	}
	
	@Override
	public void mouseClicked(MouseEvent event) {
		chatList = (JPanel) event.getSource();
		String name =  chatList.getName();
		ChatsWindow cw = (ChatsWindow)Session.getMainWindow();
		
		//JOptionPane.showMessageDialog(null, "qua ci sono "+name+" "+Session.getChatList().getChatByName(name));
		
		cw.setChat(new MessagesPanel(Session.getChatList().getChatByName(name)));
		//JOptionPane.showMessageDialog(null, "qua ci sono "+name);
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
	
}
