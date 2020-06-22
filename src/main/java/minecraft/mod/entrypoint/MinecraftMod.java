package minecraft.mod.entrypoint;

import com.mageddo.ramspiderjava.client.JavaRamSpider;

import minecraft.mod.ItemType;
import minecraft.mod.MinecraftScanner;

import org.apache.commons.lang3.Validate;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
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
  private JTextField pid;
  private JButton findProcessButton;
  private MinecraftScanner minecraftScanner;

  public MinecraftMod() {
    this.findAndChangeButton.addActionListener(e -> {
      this.changeItemType();
    });
    this.findProcessButton.addActionListener(e -> {
      this.selectMinecraftProcess(this.pid.getText());
    });
  }

  public static void main(String[] args) throws InterruptedException {
    new MinecraftMod().run();
    Thread.currentThread().join();
  }

  public void run() {
    SwingUtilities.invokeLater(() -> {
      JFrame frame = new JFrame("Minecraft Mod v1.0");
      frame.setLocationRelativeTo(null);
      frame.setContentPane(this.panel);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.pack();
      frame.setVisible(true);
    });
  }

  public void selectMinecraftProcess(String hexPid){
    try {
      this.minecraftScanner = JavaRamSpider.attach(hexPid, MinecraftScanner.class);
      this.setItemTypes(minecraftScanner.findItemTypes());
    } catch (Exception e){
      this.showAlert(e.getMessage());
    }
  }

  public void changeItemType(){
    try {
      if(this.minecraftScanner == null){
        throw new IllegalArgumentException("Find Minecraft process id first");
      }
      this.minecraftScanner.findAndChange(
          this.getCurrentItemType(), this.getCurrentQuantity(),
          this.getNewQuantity(), this.getNewItemType()
      );
    } catch (Exception e){
      this.showAlert(e.getMessage());
    }
  }

  private ItemChangeReq getItemToFind() {
    return ItemChangeReq
        .builder()
        .build();
  }

  ItemType getNewItemType() {
    Validate.isTrue(this.targetItemTypeSlc.getSelectedIndex() != -1, "Select an item type");
    return ((ItemTypeComboItem) this.targetItemTypeSlc.getSelectedItem()).getItemType();
  }

  int getNewQuantity() {
    try {
      return Integer.parseInt(this.targetQtdIpt.getText());
    } catch (Exception e){
      throw new IllegalArgumentException("Pass valid quantity");
    }
  }

  int getCurrentQuantity() {
    try {
      return Integer.parseInt(this.sourceQtdIpt.getText());
    } catch (Exception e){
      throw new IllegalArgumentException("Pass valid quantity");
    }
  }

  ItemType getCurrentItemType() {
    Validate.isTrue(this.sourceItemTypeSlc.getSelectedIndex() != -1, "Select an item type");
    return ((ItemTypeComboItem) this.sourceItemTypeSlc.getSelectedItem()).getItemType();
  }

  void setItemTypes(Set<ItemType> itemTypes) {
    itemTypes.forEach(it -> {
      final ItemTypeComboItem comboItem = ItemTypeComboItem.of(it);
      this.sourceItemTypeSlc.addItem(comboItem);
      this.targetItemTypeSlc.addItem(comboItem);
    });
//    final List<ItemTypeComboItem> itemTypeComboItems = itemTypes
//        .stream()
//        .map(ItemTypeComboItem::of)
//        .collect(Collectors.toList());
//    itemTypeComboItems.for
  }

  void showAlert(String msg){
    JOptionPane.showMessageDialog(this.panel, msg, "Alert!", JOptionPane.WARNING_MESSAGE);
  }
}
