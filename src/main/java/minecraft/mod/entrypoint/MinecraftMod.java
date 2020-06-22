package minecraft.mod.entrypoint;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MinecraftMod {

  private JPanel panel;
  private JComboBox minecraftVersion;
  private JTextField targetQtdIpt;
  private JComboBox targetItemTypeSlc;
  private JComboBox sourceItemTypeSlc;
  private JTextField sourceQtdIpt;
  private JButton findAndChangeButton;

  public static void main(String[] args) {
    JFrame frame = new JFrame("Minecraft Mod v1.0");
    frame.setContentPane(new MinecraftMod().panel);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
  }
}
