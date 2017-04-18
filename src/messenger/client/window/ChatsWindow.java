package messenger.client.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.BitSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.SortedSet;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import messenger.client.Session;
import messenger.client.Theme;
import messenger.client.Updatable;
import messenger.client.chat.ChatList;


@SuppressWarnings("serial")
public class ChatsWindow  extends ConnectedFrame implements Updatable{
	

	private RoundedPanel right;
	private RoundedPanel left;
	private ChatList cl;
	private MessagesPanel currentMp;
	private JPanel chatListPanel;
	private LinkedList<String> openChat;
	private JLabel currentChatLabel;
	private HashMap<String, JLabel> onlineList;
	private RoundedPanel chatListPanelTopBar;
	private RoundedPanel currentChatTopBar;
	private BufferedImage biLock;
	private BufferedImage biUnlock;
	private JLabel lock;
	public ChatsWindow(){
		super("Conversazioni di "+Session.getUsername());


		openChat = new LinkedList<String>();
		currentChatLabel = new JLabel();
		//COLOR DEFINITION
		Color background = Theme.background;
		Color firstColor = Theme.windowColor;
		
		onlineList = new HashMap<String, JLabel>();
		cl = Session.getChatList();
		if(cl.getChatsNum()!=0)
			currentMp = new MessagesPanel(cl.getChatById(0));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		
		JScrollPane contentScrollable = new JScrollPane();
		JPanel content = new JPanel();
		content.setLayout(new FlowLayout());
		
		contentScrollable.getViewport().add(content);
		
		
		
		//sinistra: lista chat
		left = new RoundedPanel();
		left.setLayout(new BorderLayout());

		left.setBackground(background);
		left.setPreferredSize(new Dimension(350,500));
		
		
		//TOP BAR
		chatListPanelTopBar = new RoundedPanel();
		chatListPanelTopBar.setPreferredSize(new Dimension(0, 35));
		chatListPanelTopBar.setBackground(firstColor);
		chatListPanelTopBar.setLayout(new BoxLayout(chatListPanelTopBar, BoxLayout.X_AXIS));
		chatListPanelTopBar.add(Box.createRigidArea(new Dimension(7,7)));
		chatListPanelTopBar.add(new JLabel("<html><font color='white' face='Comic Sans MS' size='5'><b>Lista conversazioni</b></font></html>"));
		chatListPanelTopBar.add(Box.createGlue());
		InputStream isAdd = null;
		BufferedImage biAdd = null;
		InputStream isGear = null;
		BufferedImage biGear = null;
		InputStream isLock = null;
		biLock = null;
		InputStream isUnlock = null;
		 biUnlock = null;
		
		try {

			isAdd = this.getClass().getClassLoader().getResourceAsStream("add.png");
			biAdd = ImageIO.read(isAdd);
			
			isGear = this.getClass().getClassLoader().getResourceAsStream("gear.png");
			biGear = ImageIO.read(isGear);
			
			isLock = this.getClass().getClassLoader().getResourceAsStream("closed_lock.png");
			biLock = ImageIO.read(isLock);
			
			isUnlock = this.getClass().getClassLoader().getResourceAsStream("open_lock.png");
			biUnlock = ImageIO.read(isUnlock);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//bi = ImageHandler.resizeImageWithHint(bi, 1, 20, 20);
		
		
		
		JLabel gearLabel = new JLabel(new ImageIcon(biGear));
		gearLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		ChatsWindow thisWindow = this; 
		gearLabel.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e){
				SettingDialog2 sd = new SettingDialog2(thisWindow);
				sd.setVisible(true);
				
			}
		});
		
		JLabel addChatLabel = new JLabel(new ImageIcon(biAdd));
		//JLabel addChatLabel = new JLabel("Inserisci utente");
		addChatLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		addChatLabel.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e){
				String nameNewChat = JOptionPane.showInputDialog(null, "Inserisci il nome dell'amico che vuoi aggiungere");
				if(nameNewChat==null || nameNewChat.length()<=0 || nameNewChat.equals(""))
					System.err.println("Nessuna chat aggiunta");
				else{
					nameNewChat = nameNewChat.trim();
					Session.getChatList().addChat(nameNewChat);
				}
			}
		});
		//addChatLabel.setPreferredSize(new Dimension(25, 25));
		chatListPanelTopBar.add(gearLabel);
		chatListPanelTopBar.add(Box.createRigidArea(new Dimension(5,5)));
		chatListPanelTopBar.add(addChatLabel);
		chatListPanelTopBar.add(Box.createRigidArea(new Dimension(7,7)));
		chatListPanelTopBar.setBorder(new EmptyBorder(7,7,12,7));
		
		left.add(chatListPanelTopBar, BorderLayout.NORTH);
		
		//CHAT LSIT
		chatListPanel = new JPanel();
		JScrollPane chatListPanelScroll = new JScrollPane();
		chatListPanelScroll.getViewport().add(chatListPanel);
		chatListPanel.setLayout(new BoxLayout(chatListPanel, BoxLayout.Y_AXIS));
		chatListPanel.setOpaque(false);
		chatListPanelScroll.getViewport().setOpaque(false);
		chatListPanelScroll.setBorder(new EmptyBorder(10,10,10,10));
		chatListPanelScroll.setOpaque(false);
		chatListPanelScroll.getVerticalScrollBar().setUnitIncrement(15);
		chatListPanel.setBorder(new EmptyBorder(4,4,4,4));
		
		// print list
		for(int i=0; i<cl.getChatsNum(); i++){
			addChatBox(cl.getChatById(i).getName());
			
		}
		//chatListPanel.add(Box.createGlue());
		left.add(chatListPanelScroll, BorderLayout.CENTER);
		
		//destra: conversazioni
		right = new RoundedPanel(true);
		right.setBackground(background);
		right.setPreferredSize(new Dimension(350,500));
		right.setLayout(new BorderLayout());
		
		currentChatTopBar = new RoundedPanel();
		currentChatTopBar.setPreferredSize(new Dimension(0, 35));
		currentChatTopBar.setBackground(firstColor);
		currentChatTopBar.setLayout(new BoxLayout(currentChatTopBar, BoxLayout.X_AXIS));
		currentChatTopBar.add(Box.createRigidArea(new Dimension(7,7)));
		currentChatTopBar.add(currentChatLabel);
		currentChatTopBar.add(Box.createGlue());
		lock = new JLabel(new ImageIcon(biUnlock));
		lock.setCursor(new Cursor(Cursor.HAND_CURSOR));
		lock.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e){
				Session.getConnection().startPrivateSession(currentMp.getName());

				if(Session.getKeyList().containsKey(currentMp.getName()))
					lock.setIcon(new ImageIcon(biLock));
				else
					lock.setIcon(new ImageIcon(biUnlock));
				
				
				
			}
		});
		currentChatTopBar.add(lock);
		currentChatTopBar.setBorder(new EmptyBorder(7,7,12,7));
		
		right.add(currentChatTopBar, BorderLayout.NORTH);
		
		
		
		//right.add(rightCenter, BorderLayout.CENTER);

		JPanel messageForm = new JPanel();
		
		messageForm.setLayout(new BoxLayout(messageForm, BoxLayout.X_AXIS));
		messageForm.setOpaque(false);
		messageForm.setPreferredSize(new Dimension(0, 100));
		messageForm.setBorder(new EmptyBorder(20,20,20,20));
		JTextArea textArea = new JTextArea();
		textArea.setBorder(new TextBubbleBorder(Color.GRAY, 1, 8, 0));
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		messageForm.add(textArea);
		JButton invia = new JButton("Invia");
		invia.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				cl.getChatByName(currentMp.getName()).addMessageFromMe(textArea.getText());
				if(Session.getKeyList().containsKey(currentMp.getName())){	
					String criptedMessage = Session.getDES(currentMp.getName()).encrypt(textArea.getText());
					Session.getConnection().sendPrivateMessage(currentMp.getName(), criptedMessage);
				}
				else{
					Session.getConnection().sendMessage(currentMp.getName(), textArea.getText());
				}
				textArea.setText("");
				currentMp.goDown();
			}
			
		});
		
		messageForm.add(invia);
		right.add(messageForm, BorderLayout.SOUTH);

		
		content.add(left);
		content.add(right);
		add(contentScrollable);
		if(openChat != null && openChat.size()!=0)
			setChat(0);
		pack();
		setVisible(true);
	}
	
	
	public void setChat(MessagesPanel mp){
		
		right.remove(currentMp);
		mp.setPreferredSize(new Dimension(50,50));
		right.add(mp, BorderLayout.CENTER);
		currentChatLabel.setText("<html><font color='white' face='Comic Sans MS' size='5'><b>Conversazione con "+mp.getName()+"</b></font></html>");
		currentMp = mp;
		Session.setShowedChat(mp.getName());
		
		//JOptionPane.showMessageDialog(null,"settochat "+currentMp.getName());
		
		if(Session.getKeyList().containsKey(currentMp.getName()))
			lock.setIcon(new ImageIcon(biLock));
		else
			lock.setIcon(new ImageIcon(biUnlock));
		
		
		right.repaint();
		right.validate();
		right.revalidate();
	}
	
	public void setChat(String name){
		currentMp = new MessagesPanel(cl.getChatByName(name));
		setChat(currentMp);	
	}
	public void setChat(int id){
		currentMp = new MessagesPanel(cl.getChatById(id));
		setChat(currentMp);
	}
	
	private void addChatBox(String name){
		RoundedPanel jp = new RoundedPanel(false);
		jp.setLayout(new BorderLayout());
		onlineList.put(name, new JLabel("offline"));
		Dimension chatInfoSize = new Dimension(320,50);
		chatListPanel.add(Box.createRigidArea(new Dimension(10,10)));
		
		JLabel chatInfoText = new JLabel(name);
		jp.setCursor(new Cursor(Cursor.HAND_CURSOR));
		jp.setPreferredSize(chatInfoSize);
	//	jp.setSize(1,1);
		jp.setMaximumSize(chatInfoSize);
		jp.setMinimumSize(chatInfoSize);
		JPanel center = new JPanel();
		center.setOpaque(false);
		center.setLayout(new BoxLayout(center, BoxLayout.X_AXIS));
		center.add(Box.createGlue());
		center.add(chatInfoText);
		center.add(Box.createGlue());
		jp.add(center, BorderLayout.CENTER);
		//jp.add(Box.createHorizontalGlue());
		JPanel bottom = new JPanel();
		bottom.setOpaque(false);
		bottom.setLayout(new BoxLayout(bottom, BoxLayout.X_AXIS));
		bottom.add(Box.createGlue());
		bottom.add(onlineList.get(name));
		bottom.add(Box.createRigidArea(new Dimension(10,10)));
		jp.add(bottom, BorderLayout.SOUTH);
		jp.addMouseListener(new SelectChatListener(this));
		jp.setName(name);
		chatListPanel.add(jp);
		openChat.add(name);
	}
	
	public void update() {
		setChat(new MessagesPanel(cl.getChatByName(currentMp.getName())));	
		chatListPanelTopBar.setBackground(Theme.windowColor);
		currentChatTopBar.setBackground(Theme.windowColor);
		/*if(Session.getKeyList().containsKey(currentMp.getName()))
			lock.setIcon(new ImageIcon(biLock));
		else
			lock.setIcon(new ImageIcon(biUnlock));
			*/
		currentMp.goDown();
	}
	
	public boolean isChatOpen(String name){
		return openChat.contains(name);
	}
	
	public void addNewChatRealTime(String name){
		System.out.println("start adding "+name);
		addChatBox(name);
		chatListPanel.repaint();
		chatListPanel.validate();
		chatListPanel.revalidate();
		left.repaint();
		left.validate();
		left.revalidate();
		//setChat(name);
		System.out.println("end adding");

	}


	public void refreshOnline() {
		BitSet bs = Session.getOnline();
		SortedSet<String> ss = Session.getChatList().getUsernameList();
		LinkedList<String> ll = new LinkedList<String>(ss);
		/*if(ss.size()!=bs.length()){
			System.out.println(ss.size()+" "+bs.length());
			System.out.println(bs);
			System.out.println(ll);
			throw new NullPointerException();
		}*/
		System.out.println("Analisi: "+bs);
		for(int i=0; i<bs.length(); i++){
			
			onlineList.get(ll.get(i)).setText(bs.get(i)?"online":"offline");
		}
	}
	
	
	public void lock(){
		
	}
	
	
	
}
