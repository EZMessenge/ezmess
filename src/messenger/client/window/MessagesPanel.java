package messenger.client.window;



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import messenger.client.Theme;
import messenger.client.chat.Chat;
import messenger.client.chat.Message;

public class MessagesPanel extends JPanel {

	private String name;
	JScrollPane messagesBoardScrollable;
	public MessagesPanel(Chat chat) {
		this.name = chat.getName();
		//JOptionPane.showMessageDialog(null, "nuovo messages panel con "+name);
		setOpaque(false);
		Color bg = Theme.background;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		//Dimension container_d = new Dimension(310,350);
		JPanel container = new JPanel();
		
		JPanel messagesBoard = new JPanel();
		//messagesBoard.setOpaque(false);
		messagesBoard.setLayout(new BoxLayout(messagesBoard, BoxLayout.Y_AXIS));

		
		
		messagesBoardScrollable = new JScrollPane();
		messagesBoardScrollable.setOpaque(false);
		//messagesBoardScrollable.getViewport().setOpaque(false);
		messagesBoardScrollable.setBorder(new EmptyBorder(10,10,10,10));
		messagesBoard.setBackground(bg);
		 Runnable doScroll = new Runnable() {
             public void run() {
            	messagesBoardScrollable.getVerticalScrollBar().setValue(messagesBoardScrollable.getVerticalScrollBar().getMaximum());
            	messagesBoardScrollable.validate();
            	messagesBoardScrollable.revalidate();
            	messagesBoardScrollable.repaint();
            	messagesBoard.validate();
            	messagesBoard.revalidate();
            	messagesBoard.repaint();
            	messagesBoardScrollable.getVerticalScrollBar().setUnitIncrement(30);
            	
            	messagesBoardScrollable.getViewport().setViewPosition(new Point(0,0));
             }
         };


			SwingUtilities.invokeLater(doScroll);

		
         
		/*messagesBoardScrollable.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
		    public void adjustmentValueChanged(AdjustmentEvent e) { 
		    	if(!scrollable){
		    		e.getAdjustable().setValue(e.getAdjustable().getMaximum());
		    		//scrollable = true;
		    	}
		    }
		});
		messagesBoard.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				messagesBoardScrollable.getVerticalScrollBar().getModel().setValue(messagesBoardScrollable.getVerticalScrollBar().getModel().getMaximum());

			}
		});
		messagesBoardScrollable.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				messagesBoardScrollable.getVerticalScrollBar().getModel().setValue(messagesBoardScrollable.getVerticalScrollBar().getModel().getMaximum());

			}
		});*/
		for(int i=0; i<chat.getMessagesNum(); i++){
			JPanel cont = new JPanel();
			cont.setLayout(new BoxLayout(cont, BoxLayout.X_AXIS));
			cont.setOpaque(false);
			Message m = chat.getMessageById(i);
			Color c;
			boolean sender = m.amISender();
			if(sender)
				c=Theme.messageSent;
			else
				c=Theme.messageReceived;
			int width = 250;
			JPanel jp = new BubbleText(c, sender);
			jp.setOpaque(false);
			jp.setLayout(new BorderLayout());
			//jp.setPreferredSize(new Dimension(width,50));
			jp.setMaximumSize(new Dimension(width, 500));
			jp.setMinimumSize(new Dimension(width,30));
			//Dimension d = new Dimension(300, 90);
			
			/*jp.setPreferredSize(d);
			jp.setMinimumSize(d);
			jp.setMaximumSize(d);
			/*if(m.amISender())
				jp.setBorder(new TextBubbleBorder(Color.GRAY, 1, 10, 10));
			else
				jp.setBorder(new TextBubbleBorder(Color.ORANGE, 1, 10, 10));*/
			//JLabel text = new JLabel(String.format("<html><div style=\"width:%dpx;\">%s</div><html>", width, m.getText()));
			JTextArea text = new JTextArea();
			text.setEditable(false);
			//text.setPreferredSize(new Dimension(width, 50));
			text.setSize(new Dimension(width, 50));
			text.setMaximumSize(new Dimension(width, 750));
			text.setMinimumSize(new Dimension(width, 50));
			text.setText(m.getText());
			text.setLineWrap(true);
			text.setWrapStyleWord(true);
			text.setOpaque(false);
			if(!sender)
				text.setBorder(new EmptyBorder(2,12,2,8));
			else
				text.setBorder(new EmptyBorder(2,8,2,8));
			jp.add(text, BorderLayout.CENTER);
			JPanel image = new JPanel();
			int taHeight = text.getPreferredSize().getHeight()>50?(int)text.getPreferredSize().getHeight():50;
			image.setPreferredSize(new Dimension(50, taHeight));
			image.setMaximumSize(new Dimension(50, taHeight));
			image.setMinimumSize(new Dimension(50, taHeight));
			//image.setBackground(Color.yellow);
			image.setOpaque(false);
			image.setLayout(new BoxLayout(image, BoxLayout.Y_AXIS));
			JPanel ii = new JPanel();
			ii.setPreferredSize(new Dimension(50,50));
			ii.setMaximumSize(new Dimension(50,50));
			ii.setLayout(new BorderLayout());
			if(!m.amISender()) ii.add(new JLabel(new ImageIcon(chat.getImage())), BorderLayout.CENTER);
			else ii.add(new JLabel(new ImageIcon(messenger.client.Session.getImage())), BorderLayout.CENTER);
			image.add(ii);
			image.add(Box.createVerticalGlue());
			if(sender){
				cont.add(jp);
				cont.add(image);
				
				
			}
			else{
				cont.add(image);
				cont.add(jp);
			}
			messagesBoard.add(cont);
			if(i!=(chat.getMessagesNum()-1))
				messagesBoard.add(Box.createRigidArea(new Dimension(0, 20)));
		}
		messagesBoard.add(Box.createGlue());
		messagesBoard.add(Box.createRigidArea(new Dimension(20,20)));

		messagesBoardScrollable.getViewport().add(messagesBoard);
		container.add(messagesBoardScrollable);
		container.add(Box.createGlue());
		add(Box.createGlue());
		add(messagesBoardScrollable);
		
		/*JPanel messageForm = new JPanel();
		messageForm.setPreferredSize(new Dimension(100,100));
		messageForm.setBackground(Color.RED);
		add(messageForm);*/
		
	}
	
	public String getName(){
		return name;
	}
	
	public void goDown(){
		messagesBoardScrollable.getVerticalScrollBar().getModel().setValue(messagesBoardScrollable.getVerticalScrollBar().getModel().getMaximum());
		messagesBoardScrollable.getVerticalScrollBar().setValue(messagesBoardScrollable.getVerticalScrollBar().getMaximum());
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				messagesBoardScrollable.getVerticalScrollBar().setValue(messagesBoardScrollable.getVerticalScrollBar().getMaximum());
				
				
			}
			
		});
		
	}
	
	public void addMessage(Message message){
		
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
