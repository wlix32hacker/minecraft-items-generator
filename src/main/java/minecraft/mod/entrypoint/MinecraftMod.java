package minecraft.mod.entrypoint;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class MinecraftMod {

  private JPanel panel;
  private JComboBox minecraftVersion;
  private JTextField targetQtdIpt;
  private JComboBox targetItemTypeSlc;
  private JComboBox sourceItemTypeSlc;
  private JTextField sourceQtdIpt;
  private JButton findAndChangeButton;

  public void run() {
    SwingUtilities.invokeLater(() -> {
      System.out.println("running");
      JFrame frame = new JFrame("Minecraft Mod v1.0");
      frame.setLocationRelativeTo(null);
      frame.setContentPane(this.panel);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.pack();
      frame.setVisible(true);
    });
  }

  public static void main(String[] args) throws InterruptedException {
    new MinecraftMod().run();
    Thread.currentThread().join();
  }
}
