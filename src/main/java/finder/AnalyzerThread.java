package finder;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

public class AnalyzerThread extends Thread{

    private String path;
    private String extension;
    private String search;
    private JTabbedPane tabbedPane;
    private File lastFile = null;
    private int width;
    private int height;

    public AnalyzerThread(
            String search,
            String path,
            String extension,
            JTabbedPane tabbedPane,
            int width,
            int height
    ) {

        this.search = search;
        this.path = path;
        this.extension = extension;
        this.tabbedPane = tabbedPane;
        this.width = width;
        this.height = height;
    }

    @Override
    public void start()
    {
        Box hBox = Box.createHorizontalBox();

        try {
            FileAnalyzer analyzer = new FileAnalyzer();
            List<File> entryCollection = analyzer.tree(
                    path,
                    extension,
                    search
            );

            Box v1Box = Box.createVerticalBox();
            Box v2Box = Box.createVerticalBox();

            jTreeModel model = new jTreeModel(path, entryCollection);
            JTree tree = new JTree(model);
            tree.setMinimumSize(new Dimension((int)(width * 0.3), height));


            tree.addMouseListener(new java.awt.event.MouseAdapter(){
                public void mouseClicked(java.awt.event.MouseEvent mouseEvent){

                    TreePath treePath = tree.getPathForLocation(mouseEvent.getX(), mouseEvent.getY());

                    if(treePath != null) {
                        String path = treePath.getLastPathComponent().toString();

                        if (path.endsWith("." + extension)) {
                            File file = new File(path);

                            if (
                                lastFile != file
                                && file.exists()
                                && file.isFile()
                            ) {
                                v2Box.removeAll();

                                JTextArea text = new JTextArea();
                                text.setPreferredSize(new Dimension(width,height));
                                try {
                                    BufferedReader in = new BufferedReader(new FileReader(file));
                                    String line = in.readLine();
                                    while (line != null) {
                                        text.append(line + "\n");
                                        line = in.readLine();
                                    }

                                    JScrollPane scrollPane = new JScrollPane(text);

                                    v2Box.add(scrollPane);
                                    v2Box.add(Box.createVerticalGlue());
                                    hBox.repaint();

                                } catch (Exception e) {
                                    handleException(hBox, e);
                                }

                                lastFile = file;
                            }
                        }
                    }
                }

            });



            v1Box.add(tree);
            v1Box.add(Box.createVerticalGlue());

            hBox.add(v1Box);
            hBox.add(v2Box);

        } catch (Exception e) {
          handleException(hBox, e);
        }

        tabbedPane.addTab("Результат", hBox);
    }

    private void handleException(Box hBox,Exception e)
    {
        hBox.add(new JLabel("Ошибка:" + e.getMessage()));
    }
}