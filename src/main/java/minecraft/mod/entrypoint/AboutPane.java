package minecraft.mod.entrypoint;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;

public class AboutPane {
  public AboutPane(JPanel aboutPnl) {
    final String repoUrl = "https://github.com/wlix32hacker/minecraft-items-generator";
    JTextPane pane = new JTextPane();
    pane.setContentType("text/html"); // let the text pane know this is what you want
    pane.setText("<html> By Wlix32Hacker : <a href=\"" + repoUrl + "\">" + repoUrl + "</a></html>");
    pane.setEditable(false);
    pane.setBackground(null);
    pane.setBorder(null);
    pane.addHyperlinkListener(e1 -> {
      if (e1.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
        if (Desktop.isDesktopSupported()) {
          try {
            Desktop.
                getDesktop()
                .browse(new URI(repoUrl));
          } catch (URISyntaxException | IOException ex) {
          }
        }
      }
    });
    aboutPnl.add(pane, BorderLayout.CENTER);
  }
}
