package xibox.test;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class AB extends JFrame implements ActionListener,Runnable{

    private static final long serialVersionUID = 5579051307457688419L;
    private int i=0;
    private JProgressBar jpBar;
    public AB(){
        JPanel jp=new JPanel();
        jpBar=new JProgressBar(0, 100);
        JButton button=new JButton("开始");
        jp.add(jpBar);
        add(jp, BorderLayout.CENTER);
        add(button,BorderLayout.SOUTH);
        this.setSize(300, 200);
        this.setVisible(true);
        button.addActionListener(this);
        jpBar.setValue(0);
        jpBar.setStringPainted(true);
    }

    public static void main(String[] args) {
        new AB();
    }

    public void actionPerformed(ActionEvent e) {
        Thread thread=new Thread(this);
        thread.start();
    }

    public void run(){
        while(i<=99){
            jpBar.setValue(++i);
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}