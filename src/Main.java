import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

/**
 * Created by Dustin on 5/12/14.
 */
public class Main extends JFrame {
    private JLabel label;
    private SwingWorker<Void,Void> worker;
    private String address;
    public Main(){
        super("Client");
        worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                loop();
                return null;
            }
        };
    }

    private void loop() {
        System.out.println("Loop method starting");
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    System.out.println("message should be showing up?");
                    address = JOptionPane.showInputDialog(getComponent(0), "IP Address?");

                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        try {
            Socket connection = new Socket(address,5050);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    JOptionPane.showMessageDialog(getComponent(0),"Connected Successfully");
                }
            });
            ObjectInputStream input = new ObjectInputStream(connection.getInputStream());
            while (true){
                final String received =(String) input.readObject();
                System.out.println(received);
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        label.setText(received);
                    }
                });

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args){
        final Main main = new Main();
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    main.createGui();
                    main.setVisible(true);

                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        System.out.println("starting worker");
        main.startWorker();
    }

    private void startWorker() {
        worker.execute();
    }

    private void createGui() {
        setSize(new Dimension(500,500));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        label = new JLabel("Waiting for text");
        add(label);
    }
}
