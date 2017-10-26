package tree;

import javax.swing.tree.DefaultMutableTreeNode;

public class MXMTree {

    MXMNode root;
    MXMNode commonRoot;

    public MXMTree( MXMNode root ) {
        this.root = root;
        commonRoot = null;
    }

    public void addElement( String elementValue ) {
        String[] list = elementValue.split("/");
        root.addElement(root.incrementalPath, list);
    }

    public DefaultMutableTreeNode printTree() {
        getCommonRoot();

        DefaultMutableTreeNode node = new DefaultMutableTreeNode(root.incrementalPath);
        node = commonRoot.printNode(node);

        return node;
    }

    public MXMNode getCommonRoot() {
        if ( commonRoot != null) {
            return commonRoot;
        }
        else {
            MXMNode current = root;
            while ( current.leafs.size() <= 0 ) {
                current = current.childs.get(0);
            }
            commonRoot = current;
            return commonRoot;
        }

    }


}