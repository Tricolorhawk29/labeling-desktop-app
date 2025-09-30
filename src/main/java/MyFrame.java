import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class MyFrame extends JFrame implements ActionListener {

    JButton excel;
    JFrame frame = new JFrame();

    MyFrame() {
        frame.setTitle("Wielofunkcyjny program DWD");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 750);
        frame.setLayout(null);

        // App icon
        ImageIcon dwdIcon = new ImageIcon(getClass().getResource("/dwd-logo.png"));
        frame.setIconImage(dwdIcon.getImage());

        // Label
        JLabel label = new JLabel("Wybierz opcje:");
        label.setFont(new Font("Arial", Font.BOLD, 40));
        label.setBounds(50, 30, 900, 400);
        label.setIcon(dwdIcon);

        excel = new JButton("Stwórz raport");
        excel.setFont(new Font("Arial", Font.BOLD, 20));
        excel.setBackground(Color.LIGHT_GRAY);
        excel.setBounds(300, 400, 350, 80);
        excel.addActionListener(this);
        excel.setFocusable(false);

        // Add components to frame
        frame.add(label);
        frame.add(excel);

        frame.setVisible(true);
    }
   //@Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==excel){
            frame.dispose();
           DocumentationGenerate documentationGenerate1 = new DocumentationGenerate();
        }
    }
}