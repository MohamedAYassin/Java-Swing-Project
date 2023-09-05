// Default PATH: jdbc:sqlserver://localhost;databaseName=


package GUI;

import java.util.*;
import java.util.Random;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import GUI.MainFrame;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RealEstateGUI extends JFrame {
  private static String generateVerificationCode() {
    int length = 6;
    String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    Random rng = new Random();
    char[] code = new char[length];
    for (int i = 0; i < length; i++) {
      code[i] = characters.charAt(rng.nextInt(characters.length()));
    }
    return new String(code);
  }

  private JPanel contentPane;
  private Connection conn;
  private JTextField databasePathField;
  private JTextField usernameField;
  private JPasswordField passwordField;
  private JCheckBox showPasswordCheckBox;

  public RealEstateGUI() {

    setTitle("Real-estate Firm Database System");
    databasePathField = new JTextField(20);
    usernameField = new JTextField(20);
    passwordField = new JPasswordField(20);
    showPasswordCheckBox = new JCheckBox("Show password");
    JLabel titleLabel = new JLabel("Real-estate Firm Database System Entry");
    databasePathField = new JTextField("#####", 20);
    usernameField = new JTextField("#####", 20);
    passwordField = new JPasswordField("#####", 20);
    contentPane = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image image = new ImageIcon(getClass().getResource("background.jpg")).getImage();
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
      }
    };

    contentPane.setLayout(new BorderLayout());
    contentPane.setOpaque(false);
    setContentPane(contentPane);
    JButton connectButton = new JButton("Connect");
    connectButton.setPreferredSize(new Dimension(130, 30));
    JButton registerButton = new JButton("Register");
    registerButton.setPreferredSize(new Dimension(130, 30));
    JButton reassignButton = new JButton("Forgot Password");
    reassignButton.setPreferredSize(new Dimension(150, 30));
    Font font = new Font("Arial", Font.BOLD, 18);
    titleLabel.setForeground(Color.WHITE);
    titleLabel.setFont(font);

    showPasswordCheckBox.addItemListener(new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
          passwordField.setEchoChar((char) 0);
        } else {
          passwordField.setEchoChar('*');
        }
      }
    });

    reassignButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String emailED = JOptionPane.showInputDialog(null, "Enter email:");
        String usernameED = JOptionPane.showInputDialog(null, "Enter username:");
        if (emailED == null || emailED.isEmpty() || usernameED == null || usernameED.isEmpty()) {
          JOptionPane.showMessageDialog(null, "Please valid enter email and username!");
          return;
        }
        final String gemail = "#####";
        final String gemailPassword = "#####";
        String smtpServer = "smtp.gmail.com";
        int smtpPort = 587;
        String verificationCode = generateVerificationCode();
        String subject = "Password Reset Verification Code";
        String message = "Your verification code is: " + verificationCode;
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtpServer);
        props.put("mail.smtp.port", smtpPort);
        Session session = Session.getInstance(props, new Authenticator() {
          protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(gemail, gemailPassword);
          }
        });
        Message msg = new MimeMessage(session);
        try {
          Class.forName("com.mysql.jdbc.Driver");
          Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
          msg.setFrom(new InternetAddress(gemail));
        } catch (MessagingException ex) {
          Logger.getLogger(RealEstateGUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
          Logger.getLogger(RealEstateGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
          msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailED));
        } catch (MessagingException ex) {
          Logger.getLogger(RealEstateGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
          msg.setSubject(subject);
        } catch (MessagingException ex) {
          Logger.getLogger(RealEstateGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
          msg.setText(message);
        } catch (MessagingException ex) {
          Logger.getLogger(RealEstateGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
          Transport.send(msg);
        } catch (MessagingException ex) {
          Logger.getLogger(RealEstateGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        String enteredCode = JOptionPane.showInputDialog(null, "Enter Code sent to " + emailED + ":");
        if (enteredCode.equals(verificationCode)) {
          try {
            JOptionPane.showMessageDialog(null, "Verification successful. Please enter your new password!");
            String newPassword = JOptionPane.showInputDialog(null, "Enter new password:");
            String surl = databasePathField.getText();
            String susername = "masteruser";
            String spassword = "modernMAM";

            Connection conn = DriverManager.getConnection(surl, susername, spassword);
            String dbms = conn.getMetaData().getDatabaseProductName();
            String sql = "";
            if (dbms.equals("Microsoft SQL Server")) {
              sql = "ALTER LOGIN [" + usernameED + "] WITH PASSWORD = '" + newPassword + "';";
            } else if (dbms.equals("MySQL")) {
              sql = "ALTER USER '" + usernameED + "'@'%' IDENTIFIED BY '" + newPassword + "';";
            }
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            JOptionPane.showMessageDialog(null, "Password reset successful.");
          } catch (SQLException ex) {
            Logger.getLogger(RealEstateGUI.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "ERROR!");
          }
        } else {
          JOptionPane.showMessageDialog(null, "Verification failed. Please try again.");
        }
      }
    });

    registerButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String usernameReg = JOptionPane.showInputDialog(null, "Enter username:");
        if (usernameReg != null) {
          char[] passwordReg = JOptionPane.showInputDialog(null, "Enter password:").toCharArray();
          if (passwordReg != null) {
            try {
              Class.forName("com.mysql.jdbc.Driver");
              Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
              String url = databasePathField.getText();
              String databaseName = url.substring(url.lastIndexOf("/") + 1);
              String username = usernameField.getText();
              String password = new String(passwordField.getPassword());
              conn = DriverManager.getConnection(url, username, password);
              Statement stmt = conn.createStatement();
              String sql;
              if (url.startsWith("jdbc:mysql:")) {
                sql = "CREATE USER '" + usernameReg + "'@'%' IDENTIFIED BY '" + new String(passwordReg) + "'";
                stmt.executeUpdate(sql);
                sql = "GRANT SELECT, INSERT, UPDATE, DELETE ON " + databaseName + ".* TO  '" + usernameReg + "'@'%'";
                stmt.executeUpdate(sql);
              } else if (url.startsWith("jdbc:sqlserver:")) {
                sql = "CREATE LOGIN " + usernameReg + " WITH PASSWORD = '" + new String(passwordReg) + "', CHECK_POLICY = OFF, CHECK_EXPIRATION = OFF";
                stmt.executeUpdate(sql);
                sql = "EXEC sp_addsrvrolemember @loginame = '" + usernameReg + "', @rolename = 'sysadmin'";
              } else {
                throw new SQLException("Unsupported database type");
              }
              stmt.executeUpdate(sql);
              JOptionPane.showMessageDialog(null, "User created successfully");
            } catch (SQLException ex) {
              JOptionPane.showMessageDialog(null, ex.getMessage());
            } catch (ClassNotFoundException ex) {
              Logger.getLogger(RealEstateGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
          }
        }
      }
    });

    connectButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          Class.forName("com.mysql.jdbc.Driver");
          Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
          String url = databasePathField.getText();
          String username = usernameField.getText();
          String password = new String(passwordField.getPassword());
          conn = DriverManager.getConnection(url, username, password);
          JOptionPane.showMessageDialog(null, "Connection successful!");
          MainFrame frame = new MainFrame(conn); // Pass the connection object to the MainFrame constructor
          frame.setVisible(true);
          RealEstateGUI.this.dispose();
        } catch (SQLException ex) {
          JOptionPane.showMessageDialog(null, "Connection failed: " + ex.getMessage());
        } catch (ClassNotFoundException ex) {
          Logger.getLogger(RealEstateGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    });

    connectButton.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseExited(MouseEvent e) {
        System.out.println("Mouse exited");
      }
      public void mouseEntered(MouseEvent e) {
        System.out.println("Mouse entered");
      }
    });

    JPanel inputPanel = new JPanel(new GridLayout(4, 2));
    inputPanel.add(new JLabel("Database Path:"));
    inputPanel.add(databasePathField);
    inputPanel.add(new JLabel("Username:"));
    inputPanel.add(usernameField);
    inputPanel.add(new JLabel("Password:"));
    inputPanel.add(passwordField);
    inputPanel.add(new JLabel(""));
    inputPanel.add(showPasswordCheckBox);

    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(titleLabel, BorderLayout.NORTH);
    getContentPane().add(inputPanel, BorderLayout.CENTER);
    getContentPane().add(connectButton, BorderLayout.SOUTH);
    contentPane.add(titleLabel, BorderLayout.NORTH);
    contentPane.add(inputPanel, BorderLayout.CENTER);
    contentPane.add(connectButton, BorderLayout.SOUTH);
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    buttonPanel.add(connectButton);
    buttonPanel.add(registerButton);
    buttonPanel.add(reassignButton);

    getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    contentPane.add(buttonPanel, BorderLayout.SOUTH);

    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });

    setSize(600, 180);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setResizable(false);
    setVisible(true);

  }

  public static void main(String[] args) {
    new RealEstateGUI();
  }
  // end of code
}