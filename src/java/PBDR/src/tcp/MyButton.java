package tcp;

 
import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.SwingConstants;
 
public class MyButton extends JButton {
 
    private static final long serialVersionUID = 1L;
 
    public MyButton(String txt) {
        super(txt);
        setForeground(Color.WHITE);
        setBackground(new Color(17,35,63));
        setOpaque(true);
        setFont(new java.awt.Font(Font.SANS_SERIF,Font.BOLD,15));
         
        setBorderPainted(false); 
        setFocusPainted(false); 
         
        setHorizontalAlignment(SwingConstants.CENTER);
        setHorizontalTextPosition(SwingConstants.CENTER);
        
    }
}
