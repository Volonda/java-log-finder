package finder;



import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class jTreeModel implements TreeModel
{
    private String root;

    private List<String> nodeCollection = new ArrayList<>();

    public jTreeModel(String root, List<File> fileCollection) {
        this.root = root;

        for (File entry: fileCollection) {
            String relativePath = entry.toString().replace(root + "/","");

            String nodePath = root;

            for(String nodeName : relativePath.split("/")) {
                nodePath = nodePath + "/" + nodeName;
                if(!nodeCollection.contains(nodePath)) {

                    nodeCollection.add(nodePath);
                }
            }
        }


    }

    public Object getRoot()
    {
        return root;
    }

    public int getChildCount(Object parent) {

        List<String> collectionTmp = getChildCollection(parent.toString());

        return collectionTmp.size();
    }

    public Object getChild(Object parent, int index) {

        List<String> collectionTmp = getChildCollection(parent.toString());

        return collectionTmp.get(index);
    }

    public int getIndexOfChild(Object parent, Object child) {
        List<String> collectionTmp = getChildCollection(parent.toString());

        if(collectionTmp.contains(child.toString())){

            return 1;
        }

        return -1;
    }

    private List<String>getChildCollection(String parent)
    {
        ArrayList<String> collectionTmp = new  ArrayList<>();

        for (String entry : nodeCollection) {
            if (
                entry.startsWith(parent)
                && (entry.split("/").length - parent.split("/").length) == 1
            ) {
                collectionTmp.add(entry);
            }
        }
        return collectionTmp;
    }

    public boolean isLeaf(Object node) {
        List<String> collectionTmp = getChildCollection(node.toString());

        return collectionTmp.size() == 0;
    }

    // This method is invoked by the JTree only for editable trees.
    // This TreeModel does not allow editing, so we do not implement
    // this method.  The JTree editable property is false by default.
    public void valueForPathChanged(TreePath path, Object newvalue) {}

    // Since this is not an editable tree model, we never fire any events,
    // so we don't actually have to keep track of interested listeners
    public void addTreeModelListener(TreeModelListener l) {}
    public void removeTreeModelListener(TreeModelListener l) {}
}
