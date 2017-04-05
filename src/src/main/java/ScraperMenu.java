import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ScraperMenu extends JPanel {

    private JList<Product> allList;
    private JList<Product> searchList;

    public ScraperMenu() {
        super(new GridBagLayout());
        add(createAllList(), GridBagConstraints.WEST);
        add(createSearchList(), GridBagConstraints.EAST);
        add(createAddButton(), GridBagConstraints.CENTER);
        add(createRemoveButton(), GridBagConstraints.CENTER);
    }

    private JPanel createAllList() {
        DefaultListModel listModel = new DefaultListModel();
        for (Product product : ProductController.getAllProducts())
            listModel.addElement(product);
        allList = new JList(listModel);
        allList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(allList);
        scrollPane.setPreferredSize(new Dimension(400,100));
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createSearchList() {
        DefaultListModel listModel = new DefaultListModel();
        searchList = new JList(listModel);
        searchList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(searchList);
        scrollPane.setPreferredSize(new Dimension(400,100));
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JButton createAddButton() {

    }

    private JButton createRemoveButton() {

    }
}
