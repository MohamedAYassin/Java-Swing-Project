package GUI;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

public class CaptchaFrame extends JFrame {

  private String text;
  private JLabel captchaLabel;
  private JTextField captchaField;

  public CaptchaFrame() {
    setTitle("reCAPTCHA Verification");
    setSize(400, 150);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JPanel panel = new JPanel();
    panel.setLayout(new FlowLayout());

    Properties props = new Properties();
    props.put(Constants.KAPTCHA_PRODUCER_IMPL, "com.google.code.kaptcha.impl.DefaultKaptcha");
    props.put(Constants.KAPTCHA_BORDER, "no");
    props.put(Constants.KAPTCHA_TEXTPRODUCER_FONT_SIZE, "28");
    props.put(Constants.KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "6");
    props.put(Constants.KAPTCHA_TEXTPRODUCER_FONT_COLOR, "black");
    props.put(Constants.KAPTCHA_IMAGE_WIDTH, "250");
    props.put(Constants.KAPTCHA_IMAGE_HEIGHT, "80");
    Config config = new Config(props);
    final DefaultKaptcha kaptcha = new DefaultKaptcha();
    kaptcha.setConfig(config);
    text = kaptcha.createText();
    captchaLabel = new JLabel();
    captchaLabel.setIcon(new ImageIcon(kaptcha.createImage(text)));
    panel.add(captchaLabel);

    captchaField = new JTextField(10);
    panel.add(captchaField);

    JButton verifyButton = new JButton("Verify");
    verifyButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String enteredText = captchaField.getText();
        if (enteredText.equalsIgnoreCase(text)) {
          JOptionPane.showMessageDialog(null, "Captcha verification successful");
          RealEstateGUI realEstateGUI = new RealEstateGUI();
          realEstateGUI.setVisible(true);
          dispose();
        } else {
          JOptionPane.showMessageDialog(null, "Captcha verification failed");
        }
      }
    });

    panel.add(verifyButton);

    JButton regenerateButton = new JButton("Regenerate");
    regenerateButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        text = kaptcha.createText();
        captchaLabel.setIcon(new ImageIcon(kaptcha.createImage(text)));
        captchaField.setText("");
      }
    });
    panel.add(regenerateButton);

    getContentPane().add(panel);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setResizable(false);
    setVisible(true);
  }

  public static void main(String[] args) {

    CaptchaFrame frame = new CaptchaFrame();
    frame.setVisible(true);

  }
}