package multiplayer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class ChatBox {
	
    private final static boolean CHAT_ON = true;
    
	private JFrame frame = new JFrame();
	private JTextField chatInput = new JTextField();
	private JTextArea chatFeed = new JTextArea();
	
	public ChatBox(GameMP client) {
		chatInput.setPreferredSize(new Dimension(300, 50));
		chatInput.setEditable(false);
		chatInput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				client.send_chat(event.getActionCommand());
				chatInput.setText("");
			}
		});
		chatFeed.setEditable(false);
		frame.add(new JScrollPane(chatFeed), BorderLayout.CENTER);
		frame.add(chatInput, BorderLayout.SOUTH);
		frame.setSize(300, 600);
		frame.setTitle("Chat");
		frame.setVisible(CHAT_ON);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
	
	
	void append_feed(String msg) {
		System.out.println(msg);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				chatFeed.append(msg + "\n");
			}
		});
	}
	
	void enable() {
		chatInput.setEditable(true);
	}
	
	void close() {
		frame.setVisible(false);
		frame.dispose();
	}
}
