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
    private boolean isFileLoaded = false;
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
        JPanel tabPanel = new JPanel();
        tabPanel.setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        tabPanel.add(contentPanel);

        try {
            FileAnalyzer analyzer = new FileAnalyzer();
            List<File> entryCollection = analyzer.tree(
                    path,
                    extension,
                    search
            );

            jTreeModel model = new jTreeModel(path, entryCollection);
            JTree tree = new JTree(model);
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
                                //reset panel
                                text.setText("");
                                contentPanel.removeAll();
                                currentPage = 0;
                                isFileLoaded = false;
                                currentFile = file;

                                try {

                                    DefaultCaret caret = (DefaultCaret) text.getCaret();
                                    caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);

                                    JScrollPane scrollPane = new JScrollPane(text);

                                    AdjustmentListener adjustmentListener = new AdjustmentListener() {
                                        public void adjustmentValueChanged(AdjustmentEvent adjustmentEvent) {

                                            if (
                                                (scrollPane.getViewport().getHeight() + adjustmentEvent.getAdjustable().getValue())
                                                    == adjustmentEvent.getAdjustable().getMaximum()
                                            ) {
                                                try {
                                                    loadNextPage();
                                                }catch ( Exception e){
                                                    handleException(contentPanel, e);
                                                }
                                            }
                                        }
                                    };
                                    scrollPane.getVerticalScrollBar().addAdjustmentListener(adjustmentListener);
                                    contentPanel.add(scrollPane);

                                    tabPanel.validate();
                                    tabPanel.repaint();

                                } catch (Exception e) {
                                    handleException(contentPanel, e);
                                }

                            }
                        }
                    }
                }

            });

            tabPanel.add(tree, BorderLayout.WEST);

        } catch (Exception e) {
          handleException(tabPanel, e);
        }
        tabbedPane.add(tabPanel);
        tabbedPane.setTabComponentAt(tabbedPane.indexOfComponent(tabPanel), JTabbedPaneWithCloseButton.getTitlePanel(tabbedPane, tabPanel, "Результат"));
    }

    private void loadNextPage() throws Exception
    {
       if (!isFileLoaded) {
           List<String> lines = finder.FileReader.readPage(currentFile, currentPage);

           if (lines.size() > 0) {
               for (String line : lines) {
                   text.append(line + "\n");
               }
               currentPage++;
           } else {
               isFileLoaded = true;
           }
       }
    }

    private void handleException(JComponent hBox,Exception e)
    {
        hBox.add(new JLabel("Ошибка:" + e.getMessage()));
    }
}