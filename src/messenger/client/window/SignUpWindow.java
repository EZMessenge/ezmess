package messenger.client.window;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SignUpWindow extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel left;
	private JPanel right;
	private JPanel bottom;
	private EmptyBorder padding;
	private Dimension verticalSpacing;
	private JTextField usernameField;
	private JTextField emailField;
	private JPasswordField passwordField;
	private JPasswordField passwordField2;
	private BufferedImage propic;
	
	public SignUpWindow(){
		super("Registrati");
		initUI();
		this.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				new LoginWindow();
			}
		});
	}
	public void initUI(){
		setSize(650, 350);
		setLayout(new BorderLayout());
		
		right = new JPanel();
		bottom = new JPanel();
		left = new JPanel();
		
		
		
		padding = new EmptyBorder(20,20,20,20);
		verticalSpacing = new Dimension(20,20);
		
		
		right.setBorder(padding);
		bottom.setBorder(padding);
		
		getLeft();
		getRight();
		getBottom();
		
		add(left, BorderLayout.WEST);
		add(right, BorderLayout.EAST);
		add(bottom, BorderLayout.SOUTH);
		
		pack();
		setResizable(false);
		setVisible(true);
		
	}
	
	private void getBottom() {
		Dimension d = new Dimension(250, 50);
		bottom.setLayout(new BoxLayout(bottom, BoxLayout.X_AXIS));
		bottom.add(Box.createHorizontalGlue());
		JButton submit = new JButton("invia");
		/*submit.setPreferredSize(d);
		submit.setSize(d);
		submit.setMinimumSize(d);*/
		final SignUpWindow thisWindow = this;
		submit.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Color c1 = usernameField.getBackground();
				Color c2 = emailField.getBackground();
				Color c3 = passwordField.getBackground();
				Color c4 = passwordField2.getBackground();
				if(c1.equals(c2)&&c2.equals(c3)&&c3.equals(c4)&&c4.equals(Color.decode("#99FFCC"))){
					//JOptionPane.showMessageDialog(null, new JLabel("lol"+new ImageIcon(propic)));
					StartConnectionListener scl = new StartConnectionListener(thisWindow, usernameField,  passwordField, emailField, propic);
					scl.execute("Registrati");
				}
				else{
					JOptionPane.showMessageDialog(thisWindow, "Correggi i campi colorati di rosso");
				}
			}
			
		});
		JPanel submitPanel = new JPanel();
		submitPanel.setLayout(new BorderLayout());
		submitPanel.setSize(d);
		submitPanel.setPreferredSize(d);
		submitPanel.setMaximumSize(d);
		submitPanel.add(submit, BorderLayout.CENTER);
		
		bottom.add(submitPanel);
		bottom.add(Box.createHorizontalGlue());
		
	}

	private void getRight() {
		// 270x270
		Dimension d = new Dimension(200, 200);
		right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
		/*BufferedImage noImage = null;
		try {
			noImage = ImageIO.read(getClass().getResource("images/noImage.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		InputStream noImage = null;
		BufferedImage bini = null;
		try {
			noImage = this.getClass().getClassLoader().getResourceAsStream("noImage.png");
			bini = ImageIO.read(noImage);
			propic = bini;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("/images/noImage.png"), "No image");
		ImageIcon icon = new ImageIcon(bini);
		JLabel image = new JLabel(icon);
		image.setSize(d);
		Dimension buttonSize = new Dimension((int)d.getWidth(), 30);
		JButton loadImageButton = new JButton("Carica un'immagine");
		loadImageButton.setPreferredSize(buttonSize);
		loadImageButton.setSize(buttonSize);
		loadImageButton.setMinimumSize(buttonSize);
		loadImageButton.setMaximumSize(buttonSize);
		loadImageButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				 FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "png", "gif", "jpeg");;
		            fc.addChoosableFileFilter(filter);

		            int ret = fc.showDialog(null, "Open file");

		            if (ret == JFileChooser.APPROVE_OPTION) {
		                File file = fc.getSelectedFile();
		                try {
							BufferedImage bi = ImageIO.read(file);
							propic = bi;
							bi = ImageHandler.resizeImageWithHint(bi, 1, 200, 200);
							ImageIcon ii = new ImageIcon(bi);
							image.setIcon(ii);
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		            }
		        }
		    
			
		});
		right.add(image);
		right.add(Box.createRigidArea(new Dimension(20,20)));
		right.add(loadImageButton);
		right.add(Box.createGlue());
	}

	private void getLeft(){
		
		left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
		left.setBorder(padding);
		left.add(new JLabel("Username"));
		usernameField = new JTextField(20);
		usernameField.addKeyListener(new CheckUsername());
		left.add(usernameField);
		left.add(Box.createVerticalGlue());
		left.add(Box.createRigidArea(verticalSpacing));
		
		left.add(new JLabel("Indirizzo e-mail"));
		emailField = new JTextField(); 
		emailField.addKeyListener(new CheckEmail());
		left.add(emailField);
		left.add(Box.createVerticalGlue());
		left.add(Box.createRigidArea(verticalSpacing));
		
		left.add(new JLabel("Password"));
		passwordField = new JPasswordField(); 
		passwordField.addKeyListener(new CheckPassword(null));
		left.add(passwordField);
		left.add(Box.createVerticalGlue());
		left.add(Box.createRigidArea(verticalSpacing));
		
		left.add(new JLabel("Ripeti Password"));
		passwordField2 = new JPasswordField(); 
		passwordField2.addKeyListener(new CheckPassword(passwordField));
		left.add(passwordField2);
		left.add(Box.createVerticalGlue());
		left.add(Box.createRigidArea(verticalSpacing));
	}

	   
}

class CheckUsername extends KeyAdapter {
	
	@Override
	public void keyPressed(KeyEvent e) {
        JTextField component = (JTextField)e.getComponent();
        String text = component.getText();
        if(e.getKeyCode()!=8)
        	text += e.getKeyChar();
        else if(text.length()>=1)
        	text = text.substring(0, text.length()-1);
        
        
        System.out.println("testo username: "+text);
        
        if(text.matches("^[a-z0-9_-]{3,15}$"))
        	component.setBackground(Color.decode("#99FFCC"));
        else
        	component.setBackground(Color.decode("#FFCCCC"));
    }

	
}


class CheckEmail extends KeyAdapter {
	
	@Override
	public void keyPressed(KeyEvent e) {
        JTextField component = (JTextField)e.getComponent();
        String text = component.getText();
        if(e.getKeyCode()!=8)
        	text += e.getKeyChar();
        else if(text.length()>=1)
        	text = text.substring(0, text.length()-1);
        
        System.out.println("testo email: "+text);
        
        if(text.matches("^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$"))
        	component.setBackground(Color.decode("#99FFCC"));
        else
        	component.setBackground(Color.decode("#FFCCCC"));
    }

	
}

class CheckPassword extends KeyAdapter {
	private JPasswordField pf;
	public CheckPassword(JPasswordField passwordField) {
		pf = passwordField;
	}

	/*
	 * 
	 * 


(					# Start of group
  (?=.*\d)			#   must contains one digit from 0-9
  (?=.*[a-z])		#   must contains one lowercase characters
  (?=.*[A-Z])		#   must contains one uppercase characters
  (?=.*[@#$%])		#   must contains one special symbols in the list "@#$%"
              .		#     match anything with previous condition checking
           {6,20}	#        length at least 6 characters and maximum of 20	
)					# End of group

	 */
	
	@Override
	public void keyPressed(KeyEvent e) {
        JPasswordField component = (JPasswordField)e.getComponent();
        char[] password = component.getPassword();
        String text = new String(password);
        if(e.getKeyCode()!=8)
        	text += e.getKeyChar();
        else if(text.length()>=1)
        	text = text.substring(0, text.length()-1);
        
        System.out.println("testo password: "+text);
        if(pf!=null && !(new String(pf.getPassword()).equals(text)))
        	component.setBackground(Color.decode("#FFCCCC"));
        else if(text.matches("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%!?§]).{6,20})"))
	        	component.setBackground(Color.decode("#99FFCC"));
        else
        	component.setBackground(Color.decode("#FFCCCC"));
        
	
    }

	
}