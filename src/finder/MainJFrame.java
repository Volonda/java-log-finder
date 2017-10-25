package finder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;

public class MainJFrame extends JFrame {

    private JFileChooser fc = new JFileChooser();
    private Label label1v1 = new Label("Директория не выбрана");
    private JTextField search = new JTextField();
    private JTextField fileExtension = new JTextField("log");
    private File selectedFile;
    private int width = 600;
    private int height = 600;
    private JTextArea result = new JTextArea(width, height);
   
    private class JFileChooserActionListener implements ActionListener {
        public void actionPerformed(ActionEvent ae)
        {
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fc.setCurrentDirectory(new File("/var/log"));

            int response = fc.showOpenDialog(MainJFrame.this);

            switch (response)
            {
                case JFileChooser.APPROVE_OPTION:
                    label1v1.setText(fc.getSelectedFile().toString());
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

        Box box = Box.createVerticalBox();
        box.setBorder(new EmptyBorder(10, 10, 10, 10));

        box.add(new Label("Что искать?"));
        search.setMaximumSize(new Dimension(width, 25));
        box.add(search);

        box.add(new Label("Где искать?"));
        box.add(label1v1);

        JButton button = new JButton("Выбрать директорию");
        button.setMaximumSize(new Dimension(200, 20));
        ActionListener al = new JFileChooserActionListener();
        button.addActionListener(al);
        box.add(button);

        box.add(new Label("Расширение файлов"));
        fileExtension.setMaximumSize(new Dimension(width, 25));
        box.add(fileExtension);
        box.add(Box.createVerticalStrut(10));

        JButton findButton = new JButton("Поиск");
        findButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {

                if (selectedFile != null) {
                    try {
                        FileAnalyzer analyzer = new FileAnalyzer();
                        List<SearchEntry> entryCollection = analyzer.tree(
                            selectedFile.toString(),
                            fileExtension.getText(),
                            search.getText()
                        );
                        renderEntryCollection(entryCollection);
                    } catch (Exception e) {
                        result.setText("Ошибка:" + e.getMessage());
                        box.validate();
                        box.repaint();
                    }
                }
            }
        });
        box.add(findButton);

        box.add(new Label("Результат поиска"));
        box.add(Box.createVerticalGlue());
        result.setEditable(false);

        JPanel resultPanel = new JPanel();
        JScrollPane scrollPanel = new JScrollPane(resultPanel);
        resultPanel.add(result);
        box.add(scrollPanel);
        box.add(Box.createVerticalGlue());
        setContentPane(box);
        setSize(width, height);
    }

    private void renderEntryCollection(List<SearchEntry> collection)
    {
        String rowPattern = "Файл: %s \n Строка: %s \n Вхождение: %s\n\n";
        result.setText("");

        for(SearchEntry entry : collection) {
            String text = String.format(rowPattern, entry.getFile().toString(), entry.getLineNum(), entry.getLineText());

            result.append(text);
        }
    }
}