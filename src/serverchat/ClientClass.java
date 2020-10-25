package serverchat;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.awt.image.ImageObserver.WIDTH;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class ClientClass extends JFrame implements ActionListener {

    JLabel lblServer = new JLabel("Client");
    JTextField txfMessage = new JTextField(WIDTH * 15);
    JButton btnSend = new JButton("Send");
    JButton btnExit = new JButton("Exit");
    JTextArea ta = new JTextArea();

    JPanel p = new JPanel();
    JPanel panel = new JPanel();
    JPanel panel2 = new JPanel();
    JPanel panel3 = new JPanel();

    Socket soc;
    DataInputStream input;
    DataOutputStream out;

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();

    public ClientClass() {
        try {
            soc = new Socket("localhost", 9000);
            input = new DataInputStream(soc.getInputStream());
            out = new DataOutputStream(soc.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ClientClass.class.getName()).log(Level.SEVERE, null, ex);
        }

        setBounds(600, 400, 450, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setTitle("Client Chat");

        p.setLayout(new GridLayout(1, 2));
        panel.setLayout(new GridLayout(5, 1));
        panel2.setLayout(new GridLayout(1, 1));
        panel3.setLayout(new GridLayout(1,2));

        panel.add(lblServer);

        panel3.add(txfMessage);
        panel3.add(btnSend);

        panel.add(panel3);
        
        panel.add(new JLabel(""));
        
        panel.add(btnExit);

        panel2.add(ta);

        p.add(panel);
        p.add(panel2);

        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = txfMessage.getText();
                try {
                    out.writeUTF(" " + message);
                    ta.append(dtf.format(now) + " Client: " + message);
                    ta.append("\n");
                    txfMessage.setText("");
                } catch (IOException ex) {
                    Logger.getLogger(ClientClass.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });

        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    soc.close();
                } catch (IOException ex) {
                    Logger.getLogger(ClientClass.class.getName()).log(Level.SEVERE, null, ex);
                }
                    System.exit(0);
            }
        });

        this.getContentPane().add(p);
        pack();

        setVisible(true);

        try {
            input = new DataInputStream(soc.getInputStream());
            out = new DataOutputStream(soc.getOutputStream());

            while (true) {
                ta.append(dtf.format(now) + " Server: " + input.readUTF());
                ta.append("\n");
            }

        } catch (IOException ex) {
            Logger.getLogger(ServerChat.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void main(String[] args) {
        new ClientClass();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

}
