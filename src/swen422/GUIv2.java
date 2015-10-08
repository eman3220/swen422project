
import java.awt.EventQueue;

import java.awt.event.*;
import java.awt.CardLayout;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


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
		frame.setSize(java.awt.Toolkit.getDefaultToolkit().getScreenSize());
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		createBaseMenu();
		createNumeracyMenu();
		createSearchMenu();

		panelCont.add(baseMenu, "1");
		panelCont.add(numeracyMenu, "2");
		panelCont.add(searchMenu, "3");
		cl.show(panelCont, "1");
				
		frame.add(panelCont);
	}

	//Makes a blank button for navigating the menu
	private JButton makeButton(String name){
		JButton jb = new JButton(name);	
		jb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Assets/button - blank.png")));
		jb.setHorizontalTextPosition(JButton.CENTER);
		jb.setVerticalTextPosition(JButton.CENTER);
		jb.setBorderPainted(false);
		jb.setFocusPainted(false);
		jb.setContentAreaFilled(false);		
		return jb;
	}
	
	//Makes a button that returns to the home screen
	private JButton makeBack(){
		JButton jb = new JButton();	
		jb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Assets/button - back.png")));
		jb.setBorderPainted(false);
		jb.setFocusPainted(false);
		jb.setContentAreaFilled(false);		
		return jb;
	}

	//Makes the main menu panel
	private void createBaseMenu(){
		baseMenu.add(buttonNumeracy);
		baseMenu.add(buttonSearch);
		baseMenu.add(buttonExit);
		baseMenu.setBackground(Color.WHITE);
		buttonNumeracy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0){
				cl.show(panelCont, "2");
			}
		});		
		buttonSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0){
				cl.show(panelCont, "3");
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
		numeracyMenu.add(buttonHomeNumeracy);
		numeracyMenu.setBackground(Color.GREEN);	
		buttonHomeNumeracy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0){
				cl.show(panelCont, "1");
			}
		});	
	}
	
	//Makes the Search by Sign menu panel
	private void createSearchMenu(){
		searchMenu.add(buttonHomeSearch);
		searchMenu.setBackground(Color.RED);
		buttonHomeSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0){
				cl.show(panelCont, "1");
			}
		});	
	}

}
