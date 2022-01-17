package GUI.Utilities;

import java.awt.Component;
import java.awt.BorderLayout;
import javax.swing.JTree;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;

public class CheckboxTreeRenderer extends JPanel implements TreeCellRenderer {

    private JCheckBox checkBox;

    public CheckboxTreeRenderer() {
        super();

        checkBox = new JCheckBox();

        setOpaque(false);
        setLayout(new BorderLayout());
        add(checkBox, BorderLayout.CENTER);
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {

        // FIXME: quando selezionato appare comunque lo sfondo colorato che è bruttino da vedere

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        Object obj = node.getUserObject();

        checkBox.setSelected(selected);
        checkBox.setText(String.valueOf(obj));
        checkBox.setOpaque(false);
        return this;
    }

}
