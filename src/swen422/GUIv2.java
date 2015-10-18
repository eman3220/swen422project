
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.imageio.ImageIO;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;


public class GUIv2 {

	JFrame frame = new JFrame("NZSL Virtual Classroom");
	
	JPanel panelCont = new JPanel();
	JPanel baseMenu = new JPanel();
	JPanel numeracyMenu = new JPanel();
	JPanel searchMenu = new JPanel();
	
	JButton buttonNumeracy = makeButton("Numeracy");
	JButton buttonSearch = makeButton("Search by Sign");
	JButton buttonExit = makeButton("Exit");
	JButton buttonHomeNumeracy = makeBack();
	JButton buttonHomeSearch = makeBack();
	JButton buttonHelpSearch = makeHelp();

	JLabel titleIcon;
	JLabel numeracyTitleIcon;
	JLabel searchTitleIcon;
	JLabel resultsTitleIcon;
	JLabel helpDialogue;
	
	JComboBox refine = makeComboBox();
	int refineValue;
	
	BufferedImage titleImage;
	BufferedImage background;
	
	Dimension screen;
	
	boolean helpVisible = false;

	Color offWhite = new Color(252, 243, 255, 255);
	Color lightBlue = new Color(219, 229, 229, 255);
	Color greyBlue = new Color(108, 122, 170, 255);
	Color midBlue = new Color(51, 34, 153, 255);
	Color darkBlue = new Color(23, 18, 33, 255);
	
	CardLayout cl = new CardLayout();
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUIv2 window = new GUIv2();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUIv2() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		panelCont.setLayout(cl);
		screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize(screen);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		createBaseMenu();
		createNumeracyMenu();
		createSearchMenu();

		panelCont.add(baseMenu, "baseMenu");
		panelCont.add(numeracyMenu, "numeracyMenu");
		panelCont.add(searchMenu, "searchMenu");
		cl.show(panelCont, "baseMenu");
				
		frame.add(panelCont);
	}

	//Makes a blank button for navigating the menu
	public JButton makeButton(String name){
		JButton jb = new JButton(name);	
		jb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Assets/button - blank.png")));
		jb.setHorizontalTextPosition(JButton.CENTER);
		jb.setVerticalTextPosition(JButton.CENTER);
		jb.setBorderPainted(false);
		jb.setFocusPainted(false);
		jb.setContentAreaFilled(false);	
		jb.setForeground(new Color(219, 229, 229, 255)); //for whatever reason referencing "lightBlue" was turning it grey
		jb.setFont(new Font("EBRIMA", Font.BOLD, 28));
		return jb;
	}

	//Makes a button for returning to the prior screen
	public JButton makeBack(){
		JButton jb = new JButton();	
		jb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Assets/button - backV2.png")));
		jb.setBorderPainted(false);
		jb.setFocusPainted(false);
		jb.setContentAreaFilled(false);	
		return jb;
	}
	
	//Makes a button for help
	public JButton makeHelp(){
		JButton jb = new JButton();	
		jb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Assets/button - help.png")));
		jb.setBorderPainted(false);
		jb.setFocusPainted(false);
		jb.setContentAreaFilled(false);
		return jb;
	}
	
	//Makes icon combobox
    public JComboBox makeComboBox(){
    	JComboBox cb;
    	Object[] items =
    		{
    			new ImageIcon(getClass().getResource("/Assets/location_anywhere.png")),
    			new ImageIcon(getClass().getResource("/Assets/location_ears.png")),
    			new ImageIcon(getClass().getResource("/Assets/location_in_front_of_body.png")),
    			new ImageIcon(getClass().getResource("/Assets/location_top_of_head.png"))
            };
        cb = new JComboBox( items );
        return cb;
    }

	//Makes the main menu panel
	private void createBaseMenu(){
		//Layout
		baseMenu.setLayout(null);
		//Title
		titleIcon = new JLabel(new javax.swing.ImageIcon(getClass().getResource("/Assets/Title.png")));
		baseMenu.add(titleIcon);
		titleIcon.setBounds(frame.getWidth()/2 - 512, frame.getHeight()/30, 1024, 256);
		//Buttons
		baseMenu.add(buttonNumeracy);
		buttonNumeracy.setBounds(frame.getWidth()/2 - 241, frame.getHeight()/10*4, 482, 89);
		baseMenu.add(buttonSearch);
		buttonSearch.setBounds(frame.getWidth()/2 - 241, frame.getHeight()/10*4+100, 482, 89);
		baseMenu.add(buttonExit);
		buttonExit.setBounds(frame.getWidth()/2 - 241, frame.getHeight()/10*4+200, 482, 89);
		//Background
		baseMenu.setBackground(Color.WHITE);
		//Button functionality
		buttonNumeracy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0){
				cl.show(panelCont, "numeracyMenu");
			}
		});		
		buttonSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0){
				cl.show(panelCont, "searchMenu");
			}
		});		
		buttonExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0){
				frame.dispose();
			}
		});	
	}

	//Makes the Numeracy menu panel
	private void createNumeracyMenu(){
		//Layout
		numeracyMenu.setLayout(null);
		//Title
		numeracyTitleIcon = new JLabel(new javax.swing.ImageIcon(getClass().getResource("/Assets/Title - Numeracy.png")));
		numeracyMenu.add(numeracyTitleIcon);
		numeracyTitleIcon.setBounds(frame.getWidth()/2-290, frame.getHeight()/30, 580, 90);
		//Buttons
		numeracyMenu.add(buttonHomeNumeracy);
		buttonHomeNumeracy.setBounds(frame.getHeight()/30, frame.getHeight()/30, 69, 68);
		//Background
		numeracyMenu.setBackground(Color.WHITE);
		//Button functionality
		buttonHomeNumeracy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0){
				cl.show(panelCont, "baseMenu");
			}
		});	
	}
	
	//Makes the Search by Sign menu panel
	private void createSearchMenu(){
		//Layout
		searchMenu.setLayout(null);
		//Title
		searchTitleIcon = new JLabel(new javax.swing.ImageIcon(getClass().getResource("/Assets/Title - Search by Sign.png")));
		searchMenu.add(searchTitleIcon);
		searchTitleIcon.setBounds(frame.getWidth()/2-290, frame.getHeight()/30, 580, 90);
		//Buttons
		searchMenu.add(buttonHomeSearch);
		buttonHomeSearch.setBounds(frame.getHeight()/30, frame.getHeight()/30, 69, 68);
		searchMenu.add(buttonHelpSearch);
		buttonHelpSearch.setBounds(frame.getWidth()-(frame.getWidth()/30)-84, frame.getHeight()/30, 84, 68);
		//Help dialogue
		helpDialogue = new JLabel(new javax.swing.ImageIcon(getClass().getResource("/Assets/SbS explanation.png")));
		searchMenu.add(helpDialogue);
		helpDialogue.setBounds(frame.getWidth()/2-430, frame.getHeight()/2-187, 860, 374);
		helpDialogue.setVisible(helpVisible);
		//Combobox
		searchMenu.add(refine);
		refine.setBounds(30, frame.getHeight()/2-100, 200, 200);
		refineValue = refine.getSelectedIndex();
		//Background
		searchMenu.setBackground(Color.WHITE);
		//Button functionality
		buttonHomeSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0){
				cl.show(panelCont, "baseMenu");
			}
		});
		buttonHelpSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0){
				if(!helpVisible){
					helpDialogue.setVisible(true);
					helpVisible = true;
				}
				else{
					helpDialogue.setVisible(false);
					helpVisible = false;
				}
			}
		});
	}
}