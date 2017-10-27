package finder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class MainJFrame extends JFrame {

    private JFileChooser fc = new JFileChooser();
    private JLabel fcValue = new JLabel("Директория не выбрана");
    private JTextField search = new JTextField();
    private JTextField fileExtension = new JTextField("log");
    private JLabel fileExtensionLabel = new JLabel("Расширение файлов");
    private File selectedFile;
    private int width = 800;
    private int height = 600;
    private JLabel message = new JLabel();
    private JLabel searchLabel = new JLabel("Что искать?");
    private JButton findButton = new JButton("Поиск");
    private JButton button = new JButton("Выбрать директорию");
    private JLabel labelButton = new JLabel("Где искать?");
    private JTabbedPane tabbedPane = new JTabbedPane();

    private class JFileChooserActionListener implements ActionListener {
        public void actionPerformed(ActionEvent ae)
        {
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fc.setCurrentDirectory(new File("/var/log"));

            int response = fc.showOpenDialog(MainJFrame.this);

            switch (response)
            {
                case JFileChooser.APPROVE_OPTION:
                    fcValue.setText(fc.getSelectedFile().toString());
                    selectedFile = fc.getSelectedFile();
                    break;

                case JFileChooser.CANCEL_OPTION:

                    break;

                case JFileChooser.ERROR_OPTION:

                    break;
            }
        }
    };

    MainJFrame(){
        super();
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        GroupLayout layout = new GroupLayout(getContentPane());

        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);


        message.setMaximumSize(new Dimension(width, 25));
        search.setMaximumSize(new Dimension(width, 20));
        button.setMaximumSize(new Dimension(width /2, 20));
        ActionListener al = new JFileChooserActionListener();
        button.addActionListener(al);
        fileExtension.setMaximumSize(new Dimension(width, 25));
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        findButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {

                if (selectedFile != null) {

                    Thread thread = new AnalyzerThread(
                        search.getText(),
                        selectedFile.toString(),
                        fileExtension.getText(),
                        tabbedPane,
                            width,
                            height
                    );

                    thread.start();
                }
            }
        });

        layout.setHorizontalGroup(
            layout.createParallelGroup()
                .addGroup(
                    layout.createSequentialGroup()
                        .addComponent(searchLabel)
                        .addGap(20)
                        .addComponent(search)
                ).addGroup(
                    layout.createSequentialGroup()
                        .addComponent(labelButton)
                        .addGap(20)
                        .addComponent(fcValue)
                        .addComponent(button)
                ).addGroup(
                    layout.createSequentialGroup()
                        .addComponent(fileExtensionLabel)
                        .addGap(20)
                        .addComponent(fileExtension)
                        .addComponent(findButton)
                ).addGroup(
                    layout.createSequentialGroup()
                        .addComponent(tabbedPane)
                )
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup().addGroup(
                    layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addComponent(searchLabel)
                        .addComponent(search)
                ).addGroup(
                    layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addComponent(labelButton)
                        .addComponent(fcValue)
                        .addComponent(button)
                ).addGroup(
                    layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addComponent(fileExtensionLabel)
                        .addComponent(fileExtension)
                        .addComponent(findButton)
                )
                .addGroup(
                    layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addComponent(tabbedPane)
                )
        )
        ;

        setSize(width, height);
    }
}