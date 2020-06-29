package minecraft.mod.entrypoint;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import org.apache.commons.lang3.Validate;

import lombok.extern.slf4j.Slf4j;
import minecraft.mod.ItemType;
import minecraft.mod.Minecraft;
import minecraft.mod.MinecraftAttache;
import minecraft.mod.ModVersion;

@Slf4j
public class MinecraftMod {

  private JPanel panel;
  private JTextField targetQtdIpt;
  private JComboBox targetItemTypeSlc;
  private JComboBox sourceItemTypeSlc;
  private JTextField sourceQtdIpt;
  private JButton findAndChangeBtn;
  private JButton findProcessBtn;
  private JLabel foundPid;
  private JLabel messagesLbl;
  private JLabel minecraftVersionLbl;
  private JLabel aboutLbl;
  private JTabbedPane tabbedPane1;
  private JTextField xpFromIpt;
  private JTextField xpToIpt;
  private JButton changeXpBtn;
  private Minecraft minecraft;
  private ExecutorService workers;

  public MinecraftMod() {
    this.workers = Executors.newFixedThreadPool(3);
    this.findAndChangeBtn.addActionListener(e -> {
      this.changeItemType();
    });
    this.findProcessBtn.addActionListener(e -> {
      this.selectMinecraftProcess();
    });
    this.changeXpBtn.addActionListener(e -> {
      this.changeXp();
    });
    new AboutPane(this.panel, this.aboutLbl);
  }

  public static void main(String[] args) throws InterruptedException {
    checkMinecraftProcess();
    new MinecraftMod().run();
    Thread
        .currentThread()
        .join();
  }

  static void checkMinecraftProcess() {
    try {
      MinecraftAttache.checkMinecraftProcess();
    } catch (Exception e) {
      showAlert(null, e.getMessage());
      System.exit(0);
    }
  }

  public void run() {
    SwingUtilities.invokeLater(() -> {
      JFrame frame = new JFrame(String.format("Minecraft Mod v%s", ModVersion.getVersion()));
      frame.setLocationRelativeTo(null);
      frame.setContentPane(this.panel);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.pack();
      frame.setResizable(false);
      frame.setVisible(true);
      this.messagesLbl.setText("");
    });
  }

  void selectMinecraftProcess() {
    try {
      this.minecraft = MinecraftAttache
          .create()
          .findAndAttachToRunning();

      this.foundPid.setText(String.format("pid: 0x%x / %d ", minecraft.pid(), minecraft.pid()));
      this.minecraftVersionLbl.setText(
          " version: " + this.minecraft
              .classMappingsService()
              .findVersionDefs()
              .getVersion()
              .getName()
      );
      this.setItemTypes();
      this.findAndChangeBtn.setEnabled(true);
      this.changeXpBtn.setEnabled(true);
    } catch (Exception e) {
      log.warn("", e);
      this.showAlert(e.getMessage());
    }
  }

  public void changeItemType() {
    try {
      if (this.minecraft == null) {
        throw new IllegalArgumentException("Find Minecraft process id first");
      }
      final int changed = this.minecraft
          .minecraftItemScanner()
          .findAndChange(
              this.getCurrentItemType(),
              this.getCurrentQuantity(),
              this.getNewItemType(),
              this.getNewQuantity()
          );
      this.showFooterMessage(String.format("%d items changed", changed));
    } catch (Exception e) {
      log.warn(e.getMessage());
      this.showAlert(e.getMessage());
    }
  }

  void changeXp() {
    try {
      final int currentXp = this.getCurrentXp();
      final int xpTo = this.getXpTo();
      this.minecraft
          .minecraftItemScanner()
          .changeXp(currentXp, xpTo)
      ;
      this.showFooterMessage(String.format("Changed xp from %d to %d", currentXp, xpTo));
    } catch (Exception e) {
      log.warn(e.getMessage());
      this.showAlert(e.getMessage());
    }
  }

  void showFooterMessage(String msg) {
    this.messagesLbl.setText(msg);
    this.workers.submit(() -> {
      try {
        Thread.sleep(5000);
      } catch (InterruptedException e) {
      }
      this.messagesLbl.setText("");
    });
  }

  ItemType getNewItemType() {
    Validate.isTrue(this.targetItemTypeSlc.getSelectedIndex() != -1, "Select an item type");
    return ((ItemTypeComboItem) this.targetItemTypeSlc.getSelectedItem()).getItemType();
  }

  int getNewQuantity() {
    try {
      return Integer.parseInt(this.targetQtdIpt.getText());
    } catch (Exception e) {
      throw new IllegalArgumentException("Pass valid quantity");
    }
  }

  int getCurrentQuantity() {
    try {
      return Integer.parseInt(this.sourceQtdIpt.getText());
    } catch (Exception e) {
      throw new IllegalArgumentException("Pass valid quantity");
    }
  }

  ItemType getCurrentItemType() {
    Validate.isTrue(this.sourceItemTypeSlc.getSelectedIndex() != -1, "Select an item type");
    return ((ItemTypeComboItem) this.sourceItemTypeSlc.getSelectedItem()).getItemType();
  }

  int getXpTo() {
    try {
      return Integer.parseInt(this.xpToIpt.getText());
    } catch (Exception e) {
      throw new RuntimeException("Pass valid xp at 'to' field", e);
    }
  }

  int getCurrentXp() {
    try {
      return Integer.parseInt(this.xpFromIpt.getText());
    } catch (Exception e) {
      throw new RuntimeException("Pass valid xp at 'from' field", e);
    }
  }

  void setItemTypes() {
    this.sourceItemTypeSlc.removeAllItems();
    this.targetItemTypeSlc.removeAllItems();
    this.minecraft
        .minecraftItemScanner()
        .findItemTypes()
        .stream()
        .sorted(Comparator.comparing(ItemType::getName))
        .forEach(it -> {
          final ItemTypeComboItem comboItem = ItemTypeComboItem.of(it);
          this.sourceItemTypeSlc.addItem(comboItem);
          this.targetItemTypeSlc.addItem(comboItem);
        });
  }

  void showAlert(String msg) {
    showAlert(this.panel, msg);
  }

  static void showAlert(JPanel parent, String msg) {
    JOptionPane.showMessageDialog(parent, msg, "Alert!", JOptionPane.WARNING_MESSAGE);
  }

  {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
    $$$setupUI$$$();
  }

  /**
   * Method generated by IntelliJ IDEA GUI Designer
   * >>> IMPORTANT!! <<<
   * DO NOT edit this method OR call it in your code!
   *
   * @noinspection ALL
   */
  private void $$$setupUI$$$() {
    panel = new JPanel();
    panel.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
    final JPanel panel1 = new JPanel();
    panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
    panel.add(panel1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false
    ));
    messagesLbl = new JLabel();
    messagesLbl.setForeground(new Color(-15654847));
    messagesLbl.setText("some default text");
    panel1.add(messagesLbl, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
        GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false
    ));
    tabbedPane1 = new JTabbedPane();
    panel.add(tabbedPane1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200),
        null, 0, false
    ));
    final JPanel panel2 = new JPanel();
    panel2.setLayout(new GridLayoutManager(3, 1, new Insets(0, 5, 10, 5), -1, -1));
    tabbedPane1.addTab("Items", panel2);
    final JPanel panel3 = new JPanel();
    panel3.setLayout(new GridLayoutManager(2, 3, new Insets(10, 5, 10, 5), -1, -1));
    panel2.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false
    ));
    panel3.setBorder(
        BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-4473925)), "Change Item",
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
            this.$$$getFont$$$(null, -1, 14, panel3.getFont()), null
        ));
    final JLabel label1 = new JLabel();
    label1.setText("Item Type");
    panel3.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
        GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false
    ));
    final JLabel label2 = new JLabel();
    label2.setText("Quantity");
    panel3.add(label2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
        GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false
    ));
    sourceItemTypeSlc = new JComboBox();
    final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
    sourceItemTypeSlc.setModel(defaultComboBoxModel1);
    panel3.add(sourceItemTypeSlc,
        new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
            GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(250, -1), null, null,
            0, false
        )
    );
    sourceQtdIpt = new JTextField();
    panel3.add(sourceQtdIpt,
        new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
            GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 25),
            new Dimension(150, -1), null, 0, false
        )
    );
    final JPanel panel4 = new JPanel();
    panel4.setLayout(new GridLayoutManager(2, 3, new Insets(10, 5, 10, 5), -1, -1));
    panel2.add(panel4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false
    ));
    panel4.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-4473925)), "To",
        TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
        this.$$$getFont$$$(null, -1, 14, panel4.getFont()), null
    ));
    targetItemTypeSlc = new JComboBox();
    final DefaultComboBoxModel defaultComboBoxModel2 = new DefaultComboBoxModel();
    targetItemTypeSlc.setModel(defaultComboBoxModel2);
    panel4.add(targetItemTypeSlc,
        new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
            GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(250, -1), null, null,
            0, false
        )
    );
    targetQtdIpt = new JTextField();
    panel4.add(targetQtdIpt,
        new GridConstraints(1, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
            GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 26),
            new Dimension(150, -1), null, 0, false
        )
    );
    final JLabel label3 = new JLabel();
    label3.setText("Quantity");
    panel4.add(label3, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
        GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false
    ));
    final JLabel label4 = new JLabel();
    label4.setText("Item Type");
    panel4.add(label4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
        GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false
    ));
    final JPanel panel5 = new JPanel();
    panel5.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
    panel2.add(panel5, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false
    ));
    findAndChangeBtn = new JButton();
    findAndChangeBtn.setBackground(new Color(-15654847));
    findAndChangeBtn.setBorderPainted(true);
    findAndChangeBtn.setEnabled(false);
    findAndChangeBtn.setForeground(new Color(-65538));
    findAndChangeBtn.setText("find and change");
    panel5.add(findAndChangeBtn,
        new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false
        )
    );
    final Spacer spacer1 = new Spacer();
    panel5.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false
    ));
    final JPanel panel6 = new JPanel();
    panel6.setLayout(new GridLayoutManager(2, 1, new Insets(0, 5, 10, 5), -1, -1));
    tabbedPane1.addTab("Player", panel6);
    final JPanel panel7 = new JPanel();
    panel7.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 5, 0), -1, -1));
    panel6.add(panel7, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false
    ));
    panel7.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-4473925)), "XP",
        TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null
    ));
    xpFromIpt = new JTextField();
    panel7.add(xpFromIpt, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 25),
        new Dimension(150, -1), null, 0, false
    ));
    xpToIpt = new JTextField();
    panel7.add(xpToIpt, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 25),
        new Dimension(150, -1), null, 0, false
    ));
    final JLabel label5 = new JLabel();
    label5.setText("From");
    panel7.add(label5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
        GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false
    ));
    final JLabel label6 = new JLabel();
    label6.setText("To");
    panel7.add(label6, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
        GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false
    ));
    changeXpBtn = new JButton();
    changeXpBtn.setBackground(new Color(-15654847));
    changeXpBtn.setEnabled(false);
    changeXpBtn.setForeground(new Color(-65538));
    changeXpBtn.setText("change");
    panel7.add(changeXpBtn,
        new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false
        )
    );
    final Spacer spacer2 = new Spacer();
    panel6.add(spacer2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1,
        GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false
    ));
    final JPanel panel8 = new JPanel();
    panel8.setLayout(new GridLayoutManager(2, 1, new Insets(0, 5, 0, 5), -1, -1));
    panel.add(panel8, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false
    ));
    final JPanel panel9 = new JPanel();
    panel9.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
    panel8.add(panel9, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false
    ));
    minecraftVersionLbl = new JLabel();
    minecraftVersionLbl.setText("");
    panel9.add(minecraftVersionLbl,
        new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(200, -1), null, null, 0,
            false
        )
    );
    findProcessBtn = new JButton();
    findProcessBtn.setBackground(new Color(-15654847));
    findProcessBtn.setForeground(new Color(-65538));
    findProcessBtn.setText("find process");
    panel9.add(findProcessBtn,
        new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_FIXED, null, null, new Dimension(125, -1), 0, false
        )
    );
    foundPid = new JLabel();
    foundPid.setText("(no process)");
    panel9.add(foundPid, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
        GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(80, -1), null, null, 0, false
    ));
    final JPanel panel10 = new JPanel();
    panel10.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
    panel8.add(panel10, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, new Dimension(-1, 15),
        0, false
    ));
    final Spacer spacer3 = new Spacer();
    panel10.add(spacer3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false
    ));
    aboutLbl = new JLabel();
    aboutLbl.setText("about");
    panel10.add(aboutLbl, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
        GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 15), null, null, 0, false
    ));
  }

  /**
   * @noinspection ALL
   */
  private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
    if (currentFont == null) return null;
    String resultName;
    if (fontName == null) {
      resultName = currentFont.getName();
    } else {
      Font testFont = new Font(fontName, Font.PLAIN, 10);
      if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
        resultName = fontName;
      } else {
        resultName = currentFont.getName();
      }
    }
    return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
  }

  /**
   * @noinspection ALL
   */
  public JComponent $$$getRootComponent$$$() {
    return panel;
  }

}
