package finder;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.File;
import java.util.List;

public class AnalyzerThread extends Thread{

    private String path;
    private String extension;
    private String search;
    private JTabbedPane tabbedPane;
    private File currentFile = null;
    private int width;
    private int height;
    private int currentPage;
    private JTextArea text = new JTextArea();

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

                    if (treePath != null) {
                        String path = treePath.getLastPathComponent().toString();

                        if (path.endsWith("." + extension)) {
                            File file = new File(path);

                            if (
                                currentFile != file
                                && file.exists()
                                && file.isFile()
                            ) {
                                text.setText("");
                                v2Box.removeAll();
                                currentPage = 1;
                                currentFile = file;
                                try {

                                    loadNextPage();
                                    JScrollPane scrollPane = new JScrollPane(text);
                                    scrollPane.setPreferredSize(new Dimension(200,300));

                                    DefaultCaret caret = (DefaultCaret) text.getCaret();
                                    caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);

                                    AdjustmentListener adjustmentListener = new AdjustmentListener() {
                                        public void adjustmentValueChanged(AdjustmentEvent adjustmentEvent) {

                                            if (
                                                (scrollPane.getViewport().getHeight() + adjustmentEvent.getAdjustable().getValue())
                                                ==
                                                adjustmentEvent.getAdjustable().getMaximum()
                                            ) {
                                                try {
                                                    loadNextPage();
                                                }catch ( Exception e){
                                                    handleException(hBox, e);
                                                }
                                            }
                                        }
                                    };
                                    scrollPane.getVerticalScrollBar().addAdjustmentListener(adjustmentListener);

                                    v2Box.add(scrollPane);
                                    v2Box.add(Box.createVerticalGlue());
                                    hBox.repaint();

                                } catch (Exception e) {
                                    handleException(hBox, e);
                                }

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

    private void loadNextPage() throws Exception
    {
        List<String> lines = finder.FileReader.readPage(currentFile, currentPage);

        if (lines.size() > 0) {

            for (String line : lines) {
                text.append(line + "\n");
            }
            currentPage++;
        }
    }

    private void handleException(Box hBox,Exception e)
    {
        hBox.add(new JLabel("Ошибка:" + e.getMessage()));
    }
}