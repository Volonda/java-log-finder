package finder;

import tree.MXMNode;
import tree.MXMTree;

import javax.swing.*;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class AnalyzerThread extends Thread{

    private String path;
    private String extension;
    private String search;
    private JTabbedPane tabbedPane;
    private int lastIndex = -1;

    public AnalyzerThread(
            String search,
            String path,
            String extension,
            JTabbedPane tabbedPane
    ) {

        this.search = search;
        this.path = path;
        this.extension = extension;
        this.tabbedPane = tabbedPane;
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

            DefaultListModel<String> listModel = new DefaultListModel<>();

            for (File entry : entryCollection) {
                listModel.addElement(entry.toString());
            }

            JList<String> list = new JList<>(listModel);
            list.addMouseListener(new java.awt.event.MouseAdapter(){
                public void mouseClicked(java.awt.event.MouseEvent mouseEvent){

                    if (!list.getCellBounds(list.getSelectedIndex(), list.getSelectedIndex()).contains(mouseEvent.getPoint())){
                        list.removeSelectionInterval(list.getSelectedIndex(), list.getSelectedIndex());
                    }
                    int index = list.getSelectedIndex();
                    if(index != lastIndex) {
                        File file = entryCollection.get(index);
                        v2Box.removeAll();

                        JTextArea text = new JTextArea();
                        try {
                            BufferedReader in = new BufferedReader(new FileReader(file));
                            String line = in.readLine();
                            while(line != null){
                                text.append(line + "\n");
                                line = in.readLine();
                            }

                            v2Box.add(text);
                            v2Box.add(Box.createVerticalGlue());
                            hBox.repaint();

                        } catch (Exception e){
                            handleException(hBox, e);
                        }
                    }
                    lastIndex = index;
                }
            });
            list.setLayoutOrientation(JList.VERTICAL);



            MXMTree mxmTree = new MXMTree(new MXMNode(path, path));
            for (File entry : entryCollection) {
                mxmTree.addElement(entry.getPath());
            }

            DefaultMutableTreeNode model = mxmTree.printTree();

            JTree tree = new JTree(model);
            v1Box.add(tree);

            v1Box.add(list);
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