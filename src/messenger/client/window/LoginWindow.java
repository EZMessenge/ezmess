package messenger.client.window;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.awt.event.MouseAdapter;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class LoginWindow extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel container;
	private Dimension maxField;
	private Dimension minField;
	private Dimension preferred;
	private JPanel signUp;
	private JPanel login;
	private JPanel topbar;
	
	public LoginWindow(){
		super("Portable Instant Messenger");
		setSize(450, 250);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		// bounds
		maxField = new Dimension(500, 30);
		minField = new Dimension(200, 20);
		preferred = new Dimension(200, 30);
		container = new JPanel();
		container.setLayout(new BorderLayout());
		container.setMinimumSize(minField);
		
		//
		getLeft();
		add(signUp, BorderLayout.WEST);
		getRight();
		add(login, BorderLayout.EAST);
		getTop();
		add(topbar, BorderLayout.NORTH);
		/*
		loginForm = getRight();
		JPanel right = new JPanel();
		right.setLayout(new BorderLayout());
		right.setBorder(new EmptyBorder(5,5,5,5));
		right.add(loginForm, BorderLayout.CENTER);
		
		container.add(left, BorderLayout.WEST);
		container.add(right, BorderLayout.EAST);
		
		add(container);
		*/
		pack();
		setResizable(false);
		setVisible(true);
		
	}
	
	private void getLeft(){
		signUp = new JPanel();
		signUp.setLayout(new BorderLayout());
		Border titled = BorderFactory.createTitledBorder("Registrati");
		Border padding = new EmptyBorder(5,5,5,5);
		signUp.setBorder(BorderFactory.createCompoundBorder(titled,padding));
		JButton signUpButton = new JButton("Registrati");
		final LoginWindow thisWindow = this;
		signUpButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				thisWindow.setVisible(false);
				new SignUpWindow();
			}
			
		});
		
		signUp.add(signUpButton, BorderLayout.CENTER);
		signUp.add(Box.createRigidArea(new Dimension(20,20)), BorderLayout.NORTH);
		signUp.add(Box.createRigidArea(new Dimension(20,20)), BorderLayout.SOUTH);
	}
	
	private void getRight(){
		login = new JPanel();
		login.setLayout(new BoxLayout(login, BoxLayout.Y_AXIS));
		Border titled = BorderFactory.createTitledBorder("Login");
		Border padding = new EmptyBorder(5,5,5,5);
		login.setBorder(BorderFactory.createCompoundBorder(titled,padding));
		
		login.add(Box.createRigidArea(new Dimension(10,10)));
		login.add(new JLabel("Username"));
		JTextField usernameField = new JTextField(20);
		usernameField.setMaximumSize(maxField);
		usernameField.setMinimumSize(minField);
		usernameField.setPreferredSize(preferred);
		login.add(usernameField);
		
		login.add(Box.createVerticalGlue());
		login.add(Box.createRigidArea(new Dimension(10,10)));
		

		login.add(new JLabel("Password"));
		JPasswordField passwordField = new JPasswordField(20);
		passwordField.setMaximumSize(maxField);
		passwordField.setMinimumSize(minField);
		passwordField.setPreferredSize(preferred);
		login.add(passwordField);
		
		login.add(Box.createVerticalGlue());
		login.add(Box.createRigidArea(new Dimension(10,10)));
		
		JButton invia = new JButton("Login");
		invia.addActionListener(new StartConnectionListener(this, usernameField, passwordField));
		invia.setMaximumSize(maxField);
		invia.setMinimumSize(minField);
		invia.setPreferredSize(preferred);
		login.add(invia);
	}
	
	private void getTop(){
		
		BufferedImage biGear = null;
		InputStream isGear = null;
		
		BufferedImage biLogo = null;
		InputStream isLogo = null;
		try {
			
			isGear = this.getClass().getClassLoader().getResourceAsStream("gear.png");
			biGear = ImageIO.read(isGear);
			
			isLogo = this.getClass().getClassLoader().getResourceAsStream("logo.png");
			biLogo = ImageIO.read(isLogo);
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		topbar = new JPanel();
		topbar.setLayout(new BoxLayout(topbar, BoxLayout.X_AXIS));
		topbar.add(Box.createRigidArea(new Dimension(80,35)));
		topbar.add(new JLabel(new ImageIcon(biLogo)));
		topbar.add(Box.createHorizontalGlue());
		JLabel gearLabel = new JLabel(new ImageIcon(biGear));
		JFrame thisWindow = this;
		gearLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		gearLabel.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e){
				SettingDialog sd = new SettingDialog(thisWindow);
				sd.setVisible(true);
			}
		});
		topbar.add(gearLabel);
		topbar.add(Box.createRigidArea(new Dimension(15,0)));
	}
}
