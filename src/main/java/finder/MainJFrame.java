package finder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;
import static javax.swing.GroupLayout.Alignment.*;

public class MainJFrame extends JFrame {

    private JFileChooser fc = new JFileChooser();
    private JLabel fcValue = new JLabel("Директория не выбрана");
    private JTextField search = new JTextField();
    private JTextField fileExtension = new JTextField("log");
    private JLabel fileExtensionLabel = new JLabel("Расширение файлов");
    private File selectedFile;
    private int width = 600;
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

        // Определение менеджера расположения
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        message.setMaximumSize(new Dimension(width, 25));
        search.setMaximumSize(new Dimension(width, 20));
        button.setMaximumSize(new Dimension(200, 20));
        ActionListener al = new JFileChooserActionListener();
        button.addActionListener(al);
        fileExtension.setMaximumSize(new Dimension(width, 25));
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        findButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {

                tabbedPane.addTab("Результат", new Label("asdasd"));

                if (selectedFile != null) {
                    try {
                        FileAnalyzer analyzer = new FileAnalyzer();
                        List<SearchEntry> entryCollection = analyzer.tree(
                            selectedFile.toString(),
                            fileExtension.getText(),
                            search.getText()
                        );
                    } catch (Exception e) {
                        message.setText("Ошибка:" + e.getMessage());
                    }
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
                    layout.createParallelGroup()
                        .addComponent(searchLabel)
                        .addComponent(search)
                ).addGroup(
                    layout.createParallelGroup()
                        .addComponent(labelButton)
                        .addComponent(fcValue)
                        .addComponent(button)
                ).addGroup(
                    layout.createParallelGroup()
                        .addComponent(fileExtensionLabel)
                        .addComponent(fileExtension)
                        .addComponent(findButton)
                )
                .addGroup(
                    layout.createParallelGroup()
                        .addComponent(tabbedPane)
                )
        )
        ;

        setSize(width, height);
    }
}