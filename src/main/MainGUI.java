package main;

/*
 * Name: Josue N Rivera
 * Date: December 27, 2019
 * 
 * GUI to interact with minimun pumping length and other features of regular language
 * */

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.SystemColor;
import javax.swing.JFormattedTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MainGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane, contentPanePump, contentPaneReg, contentPaneGen, contentPaneHelp;
	private JTextField pump_str_input, gen_str_input, textRegExpToNFA, textInputString;
	private JTextArea txtrHello, txtrHelloGen;
	private MinPumpingLength pumping;
	private RegExpToNFA regto = new RegExpToNFA();
	private NFA nfa;
	private LanguageStrGenerator lan;
	boolean outcome;
	String temp;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainGUI frame = new MainGUI();	
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainGUI() {
		ImageIcon img = new ImageIcon("icon.png");
		setBackground(Color.WHITE);
		setResizable(false);
		setTitle("MIPU: [Mi]nimum [Pu]mping Length Educational Software");
		setIconImage(img.getImage());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 705, 415);
		
		/***************************************************************** Main Window ***************************************************************/
		contentPane = new JPanel();
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JFormattedTextField frmtdtxtfldHello = new JFormattedTextField();
		frmtdtxtfldHello.setFont(new Font("Segoe UI", Font.BOLD, 30));
		frmtdtxtfldHello.setBackground(Color.DARK_GRAY);
		frmtdtxtfldHello.setForeground(Color.WHITE);
		frmtdtxtfldHello.setHorizontalAlignment(SwingConstants.CENTER);
		frmtdtxtfldHello.setEditable(false);
		frmtdtxtfldHello.setText("MIPU");
		frmtdtxtfldHello.setBounds(205, 95, 298, 47);
		contentPane.add(frmtdtxtfldHello);
		
		JButton btnRegularExpTo = new JButton("Membership Testing");
		btnRegularExpTo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setContentPane(contentPaneReg);
				validate();
				repaint();
			}
		});
		btnRegularExpTo.setForeground(Color.WHITE);
		btnRegularExpTo.setFont(new Font("Consolas", Font.BOLD, 14));
		btnRegularExpTo.setBorder(null);
		btnRegularExpTo.setBackground(SystemColor.activeCaption);
		btnRegularExpTo.setBounds(260, 199, 183, 26);
		contentPane.add(btnRegularExpTo);
		
		JButton btnLanguageGenerator = new JButton("String Generation");
		btnLanguageGenerator.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setContentPane(contentPaneGen);
				validate();
				repaint();
			}
		});
		btnLanguageGenerator.setForeground(Color.WHITE);
		btnLanguageGenerator.setFont(new Font("Consolas", Font.BOLD, 14));
		btnLanguageGenerator.setBorder(null);
		btnLanguageGenerator.setBackground(SystemColor.activeCaption);
		btnLanguageGenerator.setBounds(260, 248, 183, 26);
		contentPane.add(btnLanguageGenerator);
		
		JButton button_1 = new JButton("Minimun Pumping Length");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setContentPane(contentPanePump);
				validate();
				repaint();
			}
		});
		button_1.setForeground(Color.WHITE);
		button_1.setFont(new Font("Consolas", Font.BOLD, 14));
		button_1.setBorder(null);
		button_1.setBackground(SystemColor.activeCaption);
		button_1.setBounds(260, 295, 183, 26);
		contentPane.add(button_1);
		
		JTextArea txtrCreatedByJosue = new JTextArea();
		txtrCreatedByJosue.setEditable(false);
		txtrCreatedByJosue.setFont(new Font("Courier New", Font.PLAIN, 10));
		txtrCreatedByJosue.setBackground(Color.DARK_GRAY);
		txtrCreatedByJosue.setForeground(Color.WHITE);
		txtrCreatedByJosue.setText("Created by Josue N Rivera (19', 20')");
		txtrCreatedByJosue.setBounds(472, 363, 221, 16);
		contentPane.add(txtrCreatedByJosue);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBorderPainted(false);
		menuBar.setForeground(Color.LIGHT_GRAY);
		menuBar.setBackground(Color.WHITE);
		menuBar.setBounds(0, 0, 703, 22);
		contentPane.add(menuBar);
		
		JMenuItem mntmHelp = new JMenuItem("Help");
		mntmHelp.setForeground(Color.DARK_GRAY);
		mntmHelp.setBackground(Color.WHITE);
		mntmHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setContentPane(contentPaneHelp);
				validate();
				repaint();
			}
		});
		menuBar.add(mntmHelp);
		
		/*************************************************************** Regular Expression *************************************************************/
		contentPaneReg = new JPanel();
		contentPaneReg.setBackground(Color.WHITE);
		contentPaneReg.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPaneReg.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.DARK_GRAY);
		panel.setBounds(0, 0, 168, 390);
		contentPaneReg.add(panel);
		panel.setLayout(null);
		
		JButton btnMinimunPumpingLength = new JButton("Back");
		btnMinimunPumpingLength.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setContentPane(contentPane);
				validate();
				repaint();
			}
		});
		btnMinimunPumpingLength.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnMinimunPumpingLength.setForeground(Color.WHITE);
		btnMinimunPumpingLength.setBackground(SystemColor.activeCaption);
		btnMinimunPumpingLength.setBounds(10, 33, 148, 29);
		panel.add(btnMinimunPumpingLength);
		
		txtrHello = new JTextArea();
		txtrHello.setRows(4);
		txtrHello.setText("Reg Exp:");
		txtrHello.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		txtrHello.setTabSize(2);
		txtrHello.setForeground(Color.DARK_GRAY);
		txtrHello.setBackground(Color.WHITE);
		txtrHello.setEditable(false);
		txtrHello.setLineWrap(true);
		txtrHello.setBounds(276, 48, 78, 36);
		contentPaneReg.add(txtrHello);
		
		textRegExpToNFA = new JFormattedTextField();
		textRegExpToNFA.setToolTipText("Enter regular expression here");
		textRegExpToNFA.setHorizontalAlignment(SwingConstants.CENTER);
		textRegExpToNFA.setFont(new Font("Consolas", Font.BOLD, 18));
		textRegExpToNFA.setBounds(364, 49, 248, 36);
		contentPaneReg.add(textRegExpToNFA);
		textRegExpToNFA.setColumns(30);
		
		textInputString = new JFormattedTextField();
		textInputString.setToolTipText("Enter input string here");
		textInputString.setHorizontalAlignment(SwingConstants.CENTER);
		textInputString.setFont(new Font("Consolas", Font.BOLD, 18));
		textInputString.setColumns(30);
		textInputString.setBounds(364, 141, 248, 36);
		contentPaneReg.add(textInputString);
		
		JTextArea txtrRegStr = new JTextArea();
		txtrRegStr.setText("Input Str:");
		txtrRegStr.setTabSize(2);
		txtrRegStr.setRows(4);
		txtrRegStr.setLineWrap(true);
		txtrRegStr.setForeground(Color.DARK_GRAY);
		txtrRegStr.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		txtrRegStr.setEditable(false);
		txtrRegStr.setBackground(Color.WHITE);
		txtrRegStr.setBounds(270, 140, 84, 36);
		contentPaneReg.add(txtrRegStr);
		
		JFormattedTextField frmtdtxtfldFalse = new JFormattedTextField();
		frmtdtxtfldFalse.setHorizontalAlignment(SwingConstants.CENTER);
		frmtdtxtfldFalse.setFont(new Font("Consolas", Font.BOLD, 20));
		frmtdtxtfldFalse.setBackground(Color.BLACK);
		frmtdtxtfldFalse.setEditable(false);
		frmtdtxtfldFalse.setText("");
		frmtdtxtfldFalse.setBounds(395, 295, 94, 36);
		contentPaneReg.add(frmtdtxtfldFalse);

		JButton btnGetMinPump = new JButton("Does it belong to the language?");
		btnGetMinPump.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nfa = regto.getNFA(textRegExpToNFA.getText());
				
				if(nfa.compute(textInputString.getText())) {
					frmtdtxtfldFalse.setForeground(Color.GREEN);
					frmtdtxtfldFalse.setText("True");
				}
				else {
					frmtdtxtfldFalse.setForeground(Color.RED);
					frmtdtxtfldFalse.setText("False");
				}
			}
		});
		btnGetMinPump.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnGetMinPump.setForeground(Color.WHITE);
		btnGetMinPump.setBackground(Color.DARK_GRAY);
		btnGetMinPump.setBounds(325, 221, 236, 36);
		btnGetMinPump.setBorder(null);
		contentPaneReg.add(btnGetMinPump);
		
		/*************************************************************** Language Generator *************************************************************/
		contentPaneGen = new JPanel();
		contentPaneGen.setBackground(Color.WHITE);
		contentPaneGen.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPaneGen.setLayout(null);
		
		JPanel panelGen = new JPanel();
		panelGen.setBackground(Color.DARK_GRAY);
		panelGen.setBounds(0, 0, 168, 390);
		contentPaneGen.add(panelGen);
		panelGen.setLayout(null);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setContentPane(contentPane);
				validate();
				repaint();
			}
		});
		btnBack.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnBack.setForeground(Color.WHITE);
		btnBack.setBackground(SystemColor.activeCaption);
		btnBack.setBounds(10, 33, 148, 29);
		panelGen.add(btnBack);
		
		txtrHelloGen = new JTextArea();
		txtrHelloGen.setRows(4);
		txtrHelloGen.setText("  Insert regular expression below\n  for the first 10 strings that\n  are part of the language");
		txtrHelloGen.setFont(new Font("Consolas", Font.PLAIN, 18));
		txtrHelloGen.setTabSize(2);
		txtrHelloGen.setForeground(Color.DARK_GRAY);
		txtrHelloGen.setBackground(SystemColor.control);
		txtrHelloGen.setEditable(false);
		txtrHelloGen.setLineWrap(true);
		txtrHelloGen.setBounds(270, 21, 341, 256);
		contentPaneGen.add(txtrHelloGen);
		
		gen_str_input = new JTextField();
		gen_str_input.setToolTipText("Enter regular expression here");
		gen_str_input.setHorizontalAlignment(SwingConstants.CENTER);
		gen_str_input.setFont(new Font("Consolas", Font.BOLD, 18));
		gen_str_input.setBounds(319, 291, 248, 36);
		contentPaneGen.add(gen_str_input);
		gen_str_input.setColumns(30);
		
		JButton btnGetLanGen = new JButton("Get strings");
		btnGetLanGen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nfa = regto.getNFA(gen_str_input.getText()); 
				lan = new LanguageStrGenerator(nfa);
				temp = "\n";
				
				for(int i = 0; i < 10; i++) {
					temp += "  \"" + lan.next() + "\"\n";
		        }
				
				txtrHelloGen.setText(temp);
				
			}
		});
		btnGetLanGen.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnGetLanGen.setForeground(Color.WHITE);
		btnGetLanGen.setBackground(Color.DARK_GRAY);
		btnGetLanGen.setBounds(377, 338, 145, 30);
		btnGetLanGen.setBorder(null);
		contentPaneGen.add(btnGetLanGen);
		
		/******************************************************* Minimum Pumping Length Window *****************************************************/
		contentPanePump = new JPanel();
		contentPanePump.setBackground(Color.WHITE);
		contentPanePump.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanePump.setLayout(null);
		
		panel = new JPanel();
		panel.setBackground(Color.DARK_GRAY);
		panel.setBounds(0, 0, 168, 390);
		contentPanePump.add(panel);
		panel.setLayout(null);
		
		btnMinimunPumpingLength = new JButton("Back");
		btnMinimunPumpingLength.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setContentPane(contentPane);
				validate();
				repaint();
			}
		});
		btnMinimunPumpingLength.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnMinimunPumpingLength.setForeground(Color.WHITE);
		btnMinimunPumpingLength.setBackground(SystemColor.activeCaption);
		btnMinimunPumpingLength.setBounds(10, 33, 148, 29);
		panel.add(btnMinimunPumpingLength);
		
		txtrHello = new JTextArea();
		txtrHello.setRows(4);
		txtrHello.setText("  Insert regular expression below");
		txtrHello.setFont(new Font("Consolas", Font.PLAIN, 18));
		txtrHello.setTabSize(2);
		txtrHello.setForeground(Color.DARK_GRAY);
		txtrHello.setBackground(SystemColor.control);
		txtrHello.setEditable(false);
		txtrHello.setLineWrap(true);
		txtrHello.setBounds(270, 21, 341, 256);
		contentPanePump.add(txtrHello);
		
		pump_str_input = new JTextField();
		pump_str_input.setToolTipText("Enter regular expression here");
		pump_str_input.setHorizontalAlignment(SwingConstants.CENTER);
		pump_str_input.setFont(new Font("Consolas", Font.BOLD, 18));
		pump_str_input.setBounds(319, 291, 248, 36);
		contentPanePump.add(pump_str_input);
		pump_str_input.setColumns(30);
		
		btnGetMinPump = new JButton("Get Min Pump");
		btnGetMinPump.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pumping = new MinPumpingLength(pump_str_input.getText());
				
				int minPump = pumping.minimunP();	
				txtrHello.setText(
				  "\n\n\n\n  Reg Exp: \"" + pump_str_input.getText() + "\"\n"
				+ "  Minimun pumping length: " + minPump + "\n"
				+ "  Minimun string: \"" + pumping.getMinString()+ "\"\n  "
				+ pumping.getXYZ());
			}
		});
		btnGetMinPump.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnGetMinPump.setForeground(Color.WHITE);
		btnGetMinPump.setBackground(Color.DARK_GRAY);
		btnGetMinPump.setBounds(375, 338, 145, 30);
		btnGetMinPump.setBorder(null);
		contentPanePump.add(btnGetMinPump);
		
		/****************************************************************** Help ****************************************************************/
		
		contentPaneHelp = new JPanel();
		contentPaneHelp.setBackground(Color.WHITE);
		contentPaneHelp.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPaneHelp.setLayout(null);
		
		JPanel panelHelp = new JPanel();
		panelHelp.setBackground(Color.DARK_GRAY);
		panelHelp.setBounds(0, 0, 168, 390);
		contentPaneHelp.add(panelHelp);
		panelHelp.setLayout(null);
		
		JButton btnHelp = new JButton("Back");
		btnHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setContentPane(contentPane);
				validate();
				repaint();
			}
		});
		btnHelp.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnHelp.setForeground(Color.WHITE);
		btnHelp.setBackground(SystemColor.activeCaption);
		btnHelp.setBounds(10, 33, 148, 29);
		panelHelp.add(btnHelp);
		
		JTextPane textPane = new JTextPane();
		textPane.setEditable(false);
		textPane.setContentType("text/html");
		textPane.setText("<style type='text/css'>\r\n" + 
				"		body{\r\n" + 
				"			font-family: 'Segoe UI', arial;	\r\n" + 
				"		}\r\n" + 
				"		h3{\r\n" + 
				"			padding-bottom: -2px;\r\n" + 
				"			margin-bottom: -2px;\r\n" + 
				"		}\r\n" + 
				"\r\n" + 
				"		img{\r\n" + 
				"			width: 80px;\r\n" + 
				"		}\r\n" + 
				"\r\n" + 
				"		.footer{\r\n" + 
				"			font-size: 10px;\r\n" + 
				"		}\r\n" + 
				"\r\n" + 
				"		.sub_sec{\r\n" + 
				"			margin-left: 24;\r\n" + 
				"		}\r\n" + 
				"\r\n" + 
				"		.title{\r\n" + 
				"			font-size: 18;\r\n" + 
				"		}\r\n" + 
				"\r\n" + 
				"		b{\r\n" + 
				"			text-decoration: underline;\r\n" + 
				"		}\r\n" + 
				"\r\n" + 
				"	</style>\r\n" + 
				"<body>\r\n" + 
				"	<h1>Help Section</h1>\r\n" + 
				"	<hr>\r\n" + 
				"\r\n" + 
				"	<div class=\"overview\">\r\n" + 
				"		<h2>Overview</h2>\r\n" + 
				"		<div class=\"sub_sec\">\r\n" + 
				"			<p>This is a program designed to explore the pumping lemma property of regular expressions. Three main functionalities are offered</p>\r\n" + 
				"		</div>\r\n" + 
				"	</div>\r\n" + 
				"\r\n" + 
				"	<div class='menu'>\r\n" + 
				"		<h2>Menu</h2>\r\n" + 
				"\r\n" + 
				"		<div class=\"sub_sec\">\r\n" + 
				"\r\n" + 
				"			<h2 class=\"title\">\r\n" + 
				"				1. Regular Expression (Accept/Reject)\r\n" + 
				"			</h2>\r\n" + 
				"\r\n" + 
				"			<div class='sub_sec'>\r\n" + 
				"				<p>This option will allow you to insert an regular expression and an input string with it. It will then tell you if the input string belong to the language of the regular expression.</p>\r\n" + 
				"				<h3>How To use: </h3>\r\n" + 
				"				<p><b>Input:</b></p>\r\n" + 
				"				<p><i>\"Reg Exp:\"</i> Here is where you insert the regular expression</p>\r\n" + 
				"				<p><i>\"Inp Str:\"</i> Here is where you insert the input string</p>\r\n" + 
				"				<p><b>Output:</b></p>\r\n" + 
				"				<p>After pressing <i>\"Does it belong to the langauge?\"</i>, the state of the box will change to either \"True\" (Green) color or \"False\" (Red). </p>\r\n" + 
				"				<p><b>Note</b> that it will not fade or anything like it, so if the state of the previous state is still the same after you clicked the buttom, it will state the same without any animation to showcase it. For example, if the regular expression is \"10*\" and you previously tested for input string \"10\"; resulting in a state of being true. When you test for \"100\", the answer is still  betrue so the box will stay in true and it will not have an animation to showcase that a new input string is being tested.</p>\r\n" + 
				"				<br>\r\n" + 
				"				<h3>Example: </h3>\r\n" + 
				"				<p><b>Input:</b></p>\r\n" + 
				"				<p><i>\"Reg Exp:\"</i> 10*</p>\r\n" + 
				"				<p><i>\"Inp Str:\"</i> 100</p>\r\n" + 
				"				<p><b>Output:</b></p>\r\n" + 
				"				<p>The box will light up green and contains the word \"True\"</p>\r\n" + 
				"				<br>\r\n" + 
				"			</div>\r\n" + 
				"\r\n" + 
				"			<h2 class=\"title\">\r\n" + 
				"				2. Language for a given Regular Expression\r\n" + 
				"			</h2>\r\n" + 
				"\r\n" + 
				"			<div class='sub_sec'>\r\n" + 
				"				<h3>How To use: </h3>\r\n" + 
				"				<p><b>Input:</b></p>\r\n" + 
				"				<p>A regular expression must be inserted in the text field below</p>\r\n" + 
				"				<p><b>Output:</b></p>\r\n" + 
				"				<p>After pressing <i>\"Get Strings\"</i>, the 10 smallest string the belong to the language of the regular expression will be displayed in the text box</p>\r\n" + 
				"				<br>\r\n" + 
				"				<h3>Example: </h3>\r\n" + 
				"				<p><b>Input:</b></p>\r\n" + 
				"				<p>10*</p>\r\n" + 
				"				<p><b>Output:</b></p>\r\n" + 
				"				<p>\"1\"</p>\r\n" + 
				"				<p>\"10\"</p>\r\n" + 
				"				<p>\"100\"</p>\r\n" + 
				"				<p>\"1000\"</p>\r\n" + 
				"				<p>\"10000\"</p>\r\n" + 
				"				<p>\"100000\"</p>\r\n" + 
				"				<p>\"1000000\"</p>\r\n" + 
				"				<p>\"10000000\"</p>\r\n" + 
				"				<p>\"100000000\"</p>\r\n" + 
				"				<p>\"1000000000\"</p>\r\n" + 
				"				<br>\r\n" + 
				"			</div>\r\n" + 
				"\r\n" + 
				"			<h2 class=\"title\">\r\n" + 
				"				3. Minimun Pumping Length for a given Regular Expression\r\n" + 
				"			</h2>\r\n" + 
				"\r\n" + 
				"			<div class='sub_sec'>\r\n" + 
				"				<h3>How To use: </h3>\r\n" + 
				"				<p><b>Input:</b></p>\r\n" + 
				"				<p>A regular expression must be inserted in the text field below</p>\r\n" + 
				"				<p><b>Output:</b></p>\r\n" + 
				"				<p>After pressing <i>\"Get Min Pump\"</i>, information regarding the minimun pumping length will be outputed</p>\r\n" + 
				"				<br>\r\n" + 
				"				<h3>Example: </h3>\r\n" + 
				"				<p><b>Input:</b></p>\r\n" + 
				"				<p>10*</p>\r\n" + 
				"				<p><b>Output:</b></p>\r\n" + 
				"				<p>Input string: \"10*\"</p>\r\n" + 
				"				<p>Minimun pumping length: 2</p>\r\n" + 
				"				<p>Minimun string: \"10\"</p>\r\n" + 
				"				<p>X: 1, Y: 0, Z: e</p>\r\n" + 
				"				<br>\r\n" + 
				"			</div>\r\n" + 
				"		</div>\r\n" + 
				"	</div>\r\n" + 
				"\r\n" + 
				"	<div class=\"note\">\r\n" + 
				"		<h2>Notes</h2>\r\n" + 
				"		<div class=\"sub_sec\">\r\n" + 
				"			<h2 class=\"title\">\r\n" + 
				"				- Operand characters\r\n" + 
				"			</h2>\r\n" + 
				"			<div class='sub_sec'>\r\n" + 
				"				<p>The characters (\".\", \"U\", \"*\", \"(\", \")\") are not allowed to be used as elements in the input string or be use as something else other than operands in the regular expression. They represent operands in the regular expression so they can only be used for that purpose.</p>\r\n" + 
				"				<p><i>\".\":</i> Represents concatenation. There is not need to use it since the regular expression parser will assume a concatenation is being perform if their is not other operands between two elements in the regular expression</p>\r\n" + 
				"				<p><i>\"*\":</i> Represents star operation. The elemnts to the left will used for the star operation</p>\r\n" + 
				"				<p><i>\"U\":</i> Represents union. The elements from both sides of teh character will used for the union</p>\r\n" + 
				"				<p><i>\"(\" or \")\":</i> Represents parenthesis. As in other mathematical expression, parathesis represent sepration and they have the highest priority when performing operation</p>\r\n" + 
				"			</div>\r\n" + 
				"			<h2 class=\"title\">\r\n" + 
				"				- Tricky characters\r\n" + 
				"			</h2>\r\n" + 
				"			<div class='sub_sec'>\r\n" + 
				"				<p><i>\"e\":</i> Represents epsilon. \"e\" can be used in the regular expression to represent the empthy language or it can be used in an input string to also represent nothing</p>\r\n" + 
				"				<p><i>\" \":</i> Represents spaces. Spaces are also considered character in this program just like \"a\", \"b\", \"1\" or \"0\". Be careful with its use and the result may confused you when display</p>\r\n" + 
				"				<p><i>\"\":</i> Represents emthy string. To insert the empthy string or the empthy language, the field can be left blank where the regular expression will go</p>\r\n" + 
				"			</div>\r\n" + 
				"		</div>\r\n" + 
				"	</div>\r\n" + 
				"\r\n" + 
				"	<div class=\"warning\">\r\n" + 
				"		<h2>Warning</h2>\r\n" + 
				"		<div class=\"sub_sec\">\r\n" + 
				"			<p>DO NOT write regular expression like \"e*\", \"()*\" or \"(0Ue)*\" since this will result in the program crashing. It will try to determine the end of the episolon transition but because their is not one, it will go into an infinite loop. Similarly, for the expression \"()*\", it will try to determine the string the belongs to nothing star. These strings are valid in teh context of regular languages either way.</p>\r\n" + 
				"		</div>\r\n" + 
				"	</div>\r\n" + 
				"\r\n" + 
				"	<div class='footer'>\r\n" + 
				"		<p>Created by: Josue N Rivera 19' 20'</p>\r\n" + 
				"	</dir>\r\n" + 
				"	\r\n" + 
				"</body>");
		textPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		JScrollPane jsp = new JScrollPane(textPane);
		jsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.setBounds(178, 11, 505, 362);
		
		contentPaneHelp.add(jsp);

	}
	
	public class RoundedBorder implements Border {
	    private int radius;

	    RoundedBorder(int radius) {
	        this.radius = radius;
	    }

	    public Insets getBorderInsets(Component c) {
	        return new Insets(this.radius+1, this.radius+1, this.radius+2, this.radius);
	    }

	    public boolean isBorderOpaque() {
	        return true;
	    }

	    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
	        g.drawRoundRect(x, y, width-1, height-1, radius, radius);
	    }
	}
}
