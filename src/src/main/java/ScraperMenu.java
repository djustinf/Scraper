import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class ScraperMenu extends JPanel {

    private JList<Product> allList;
    private JList<Product> searchList;
    private DefaultListModel allListModel;
    private DefaultListModel searchListModel;

    public ScraperMenu() {
        super(new GridBagLayout());
        GridBagConstraints c1 = new GridBagConstraints();
        c1.anchor = GridBagConstraints.WEST;
        c1.gridx = 0;
        c1.gridy = 1;
        c1.gridheight = 3;
        c1.weightx = 1;
        GridBagConstraints c2 = new GridBagConstraints();
        c2.anchor = GridBagConstraints.EAST;
        c2.gridx = 2;
        c2.gridy = 1;
        c2.gridheight = 3;
        c2.weightx = 1;
        GridBagConstraints c3 = new GridBagConstraints();
        c3.anchor = GridBagConstraints.CENTER;
        c3.gridx = 1;
        c3.gridy = 1;
        c3.weightx = 2;
        GridBagConstraints c4 = new GridBagConstraints();
        c4.anchor = GridBagConstraints.CENTER;
        c4.gridx = 1;
        c4.gridy = 2;
        c4.weightx = 2;
        GridBagConstraints c5 = new GridBagConstraints();
        c5.anchor = GridBagConstraints.SOUTH;
        c5.gridx = 1;
        c5.gridy = 3;
        c5.weightx = 2;
        c5.weighty = 2;
        GridBagConstraints c6 = new GridBagConstraints();
        c6.anchor = GridBagConstraints.SOUTH;
        c6.gridx = 1;
        c6.gridy = 4;
        c6.weightx = 2;
        c6.weighty = 2;
        add(createAllList(), c1);
        add(createSearchList(), c2);
        add(createAddButton(), c3);
        add(createRemoveButton(), c4);
        add(createFormButton(), c5);
        add(createSearchButton(), c6);
    }

    private JPanel createAllList() {
        allListModel = new DefaultListModel();
        for (Product product : ProductController.getAllProducts())
            allListModel.addElement(product);
        allList = new JList(allListModel);
        allList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(allList);
        scrollPane.setPreferredSize(new Dimension(250,750));
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createSearchList() {
        searchListModel = new DefaultListModel();
        searchList = new JList(searchListModel);
        searchList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(searchList);
        scrollPane.setPreferredSize(new Dimension(250,750));
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JButton createAddButton() {
        JButton button = new JButton(">");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int[] indices = allList.getSelectedIndices();
                List<Product> selected = allList.getSelectedValuesList();
                for (int i = 0; i < indices.length; i++) {
                    allListModel.remove(indices[i]-i);
                    searchListModel.addElement(selected.get(i));
                }
            }
        });
        return button;
    }

    private JButton createRemoveButton() {
        JButton button = new JButton("<");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int[] indices = searchList.getSelectedIndices();
                List<Product> selected = searchList.getSelectedValuesList();
                for (int i = 0; i < indices.length; i++) {
                    searchListModel.remove(indices[i]-i);
                    allListModel.addElement(selected.get(i));
                }
            }
        });
        return button;
    }

    private JButton createFormButton() {
        JButton button = new JButton("Add Product");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayForm();
            }
        });
        return button;
    }

    private JButton createSearchButton() {
        JButton button = new JButton("Run Search");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //run search
            }
        });
        return button;
    }

    private void displayForm() {
        JTextField field1 = new JTextField("<product to search>");
        JTextField field2 = new JTextField("<product brand>");
        JTextField field3 = new JTextField("www.ebay.com/<product URL>");
        JTextField field4 = new JTextField("www.amazon.com/<product URL>");
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Product"));
        panel.add(field1);
        panel.add(new JLabel("Brand"));
        panel.add(field2);
        panel.add(new JLabel("Ebay URL"));
        panel.add(field3);
        panel.add(new JLabel("Amazon URL"));
        panel.add(field4);
        int result = JOptionPane.showConfirmDialog(null, panel, "Create a New Product Search",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            Product product = new Product();
            product.setProduct(field1.getText());
            product.setBrand(field2.getText());
            product.setEbayUrl(field3.getText());
            product.setAmazonUrl(field4.getText());
            ProductController.storeProduct(product);
            allListModel.addElement(product);
        }
    }

    private static void createGUI() {
        JFrame frame = new JFrame("Web Scraper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JComponent content = new ScraperMenu();
        content.setOpaque(true);
        frame.setContentPane(content);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createGUI();
            }
        });
    }
}
