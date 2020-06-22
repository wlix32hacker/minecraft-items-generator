package minecraft.mod.entrypoint;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MinecraftMod {

  private JPanel panel;
  private JComboBox minecraftVersion;
  private JTextField textField1;
  private JComboBox comboBox2;
  private JComboBox comboBox1;
  private JTextField textField2;

  public static void main(String[] args) {
    JFrame frame = new JFrame("MinecraftMod");
    frame.setContentPane(new MinecraftMod().panel);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
  }
}
