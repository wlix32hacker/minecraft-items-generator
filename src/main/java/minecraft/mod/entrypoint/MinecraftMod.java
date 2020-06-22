package minecraft.mod.entrypoint;

import com.mageddo.ramspiderjava.client.JavaRamSpider;

import minecraft.mod.ItemType;
import minecraft.mod.MinecraftScanner;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import java.util.Set;

public class MinecraftMod {

  private JPanel panel;
  private JComboBox minecraftVersion;
  private JTextField targetQtdIpt;
  private JComboBox targetItemTypeSlc;
  private JComboBox sourceItemTypeSlc;
  private JTextField sourceQtdIpt;
  private JButton findAndChangeButton;
  private MinecraftScanner minecraftScanner;

  public static void main(String[] args) throws InterruptedException {
    new MinecraftMod().run();
    Thread.currentThread().join();
  }

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

  public void selectMinecraftProcess(){
    this.minecraftScanner = JavaRamSpider.attach(0, MinecraftScanner.class);
    this.setItemTypes(minecraftScanner.findItemTypes());
  }

  public void changeItemType(){
    this.minecraftScanner.findAndChange(
        this.getCurrentItemType(), this.getCurrentQuantity(),
        this.getNewQuantity(), this.getNewItemType()
    );
  }

  ItemType getNewItemType() {
    throw new UnsupportedOperationException();
  }

  int getNewQuantity() {
    throw new UnsupportedOperationException();
  }

  int getCurrentQuantity() {
    throw new UnsupportedOperationException();
  }

  ItemType getCurrentItemType() {
    throw new UnsupportedOperationException();
  }

  void setItemTypes(Set<ItemType> itemTypes) {
    throw new UnsupportedOperationException();
  }
}
