//package minecraft.mod.entrypoint;
//
//import javax.swing.JComboBox;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//import javax.swing.JTextField;
//import javax.swing.SpringLayout;
//
//import java.awt.BorderLayout;
//import java.awt.GridBagConstraints;
//import java.awt.GridBagLayout;
//
//public class MinecraftModPanel {
//
//  private JPanel panel;
//
//  public MinecraftModPanel() {
//    this.panel = new JPanel();
//    this.panel.setLayout(new BorderLayout());
//
//    GridBagConstraints constraints = new GridBagConstraints();
//    final JPanel searchOptionsPanel = new JPanel();
//    searchOptionsPanel.setLayout(new SpringLayout());
////    final JLabel changeItemTypeLbl = new JLabel("Change Item Type");
//
//    JLabel l = new JLabel("Item Type", JLabel.TRAILING);
//    searchOptionsPanel.add(l);
//    l.setLabelFor(new JComboBox<>());
//
//    final JLabel quantityLbl = new JLabel("Quantity");
//    searchOptionsPanel.add(quantityLbl);
//    quantityLbl.setLabelFor(new JTextField());
//
//    final JLabel toLbl = new JLabel("To");
//    searchOptionsPanel.add(toLbl);
//    quantityLbl.setLabelFor(new JTextField());
//
//    SpringUtilities.makeCompactGrid(p,
//        numPairs, 2, //rows, cols
//        6, 6,        //initX, initY
//        6, 6);       //xPad, yPad
//
//    this.panel.add(searchOptionsPanel, BorderLayout.CENTER);
//
//  }
//
//  public static void main(String[] args) {
//    JFrame frame = new JFrame("MinecraftMod");
//    frame.setContentPane(new MinecraftModPanel().panel);
//    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    frame.pack();
//    frame.setVisible(true);
//  }
//}
