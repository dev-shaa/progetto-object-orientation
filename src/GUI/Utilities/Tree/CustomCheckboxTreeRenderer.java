package GUI.Utilities.Tree;

import java.awt.Component;
import java.awt.BorderLayout;
import javax.swing.JTree;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.tree.TreeCellRenderer;

public class CustomCheckboxTreeRenderer extends JPanel implements TreeCellRenderer {

    private JCheckBox checkBox;

    public CustomCheckboxTreeRenderer() {
        super();

        checkBox = new JCheckBox();

        setOpaque(false);
        setLayout(new BorderLayout());
        add(checkBox, BorderLayout.CENTER);
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        checkBox.setSelected(selected);
        checkBox.setText(String.valueOf(value));
        checkBox.setOpaque(false);

        return this;
    }

}