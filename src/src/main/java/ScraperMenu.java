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
        c3.anchor = GridBagConstraints.NORTHWEST;
        c3.gridx = 0;
        c3.gridy = 0;
        c3.weightx = 2;
        GridBagConstraints c4 = new GridBagConstraints();
        c4.anchor = GridBagConstraints.NORTHEAST;
        c4.gridx = 2;
        c4.gridy = 0;
        c4.weightx = 2;
        GridBagConstraints c5 = new GridBagConstraints();
        c5.anchor = GridBagConstraints.NORTH;
        c5.gridx = 1;
        c5.gridy = 0;
        c5.weightx = 1;
        c5.weighty = 2;
        GridBagConstraints c6 = new GridBagConstraints();
        c6.anchor = GridBagConstraints.SOUTHEAST;
        c6.gridx = 2;
        c6.gridy = 4;
        c6.weightx = 1;
        c6.weighty = 2;
        GridBagConstraints c7 = new GridBagConstraints();
        c7.anchor = GridBagConstraints.SOUTHWEST;
        c7.gridx = 0;
        c7.gridy = 4;
        c7.weightx = 1;
        c7.weighty = 2;
        GridBagConstraints c8 = new GridBagConstraints();
        c8.anchor = GridBagConstraints.SOUTH;
        c8.gridx = 1;
        c8.gridy = 4;
        c8.weightx = 1;
        c8.weighty = 2;
        add(createAllList(), c1);
        add(createSearchList(), c2);
        add(createAddButton(), c3);
        add(createRemoveButton(), c4);
        add(createFormButton(), c6);
        add(createSearchButton(), c5);
        add(createDeleteButton(), c7);
        add(createModifyButton(), c8);
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
        panel.add(new JLabel("Product Database"));
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
        panel.add(new JLabel("Products to Search"));
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JButton createAddButton() {
        JButton button = new JButton("Add Search ->");
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
        JButton button = new JButton("<- Remove Search");
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
                displayNewForm();
            }
        });
        return button;
    }

    private JButton createModifyButton() {
        JButton button = new JButton("Modify Product");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayModForm();
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

    private JButton createDeleteButton() {
        JButton button = new JButton("Delete Entries");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] indices = allList.getSelectedIndices();
                List<Product> selected = allList.getSelectedValuesList();
                int dialogResult = JOptionPane.showConfirmDialog (null, "Delete selected entries?","Warning!", JOptionPane.YES_NO_OPTION);
                if (dialogResult == JOptionPane.YES_OPTION) {
                    for (int i = 0; i < indices.length; i++) {
                        allListModel.remove(indices[i] - i);
                        ProductController.deleteProduct(selected.get(i).getId());
                    }
                }
            }
        });
        return button;
    }

    private void displayNewForm() {
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

    private void displayModForm() {
        int[] indices = allList.getSelectedIndices();
        List<Product> selected = allList.getSelectedValuesList();
        if (selected.size() != 1) {
            JOptionPane.showMessageDialog(null, "Please select exactly one product to modify.");
            return;
        }
        Product mod = selected.get(0);
        JTextField field1 = new JTextField(mod.getProduct());
        JTextField field2 = new JTextField(mod.getBrand());
        JTextField field3 = new JTextField(mod.getEbayUrl());
        JTextField field4 = new JTextField(mod.getAmazonUrl());
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Product"));
        panel.add(field1);
        panel.add(new JLabel("Brand"));
        panel.add(field2);
        panel.add(new JLabel("Ebay URL"));
        panel.add(field3);
        panel.add(new JLabel("Amazon URL"));
        panel.add(field4);
        int result = JOptionPane.showConfirmDialog(null, panel, "Modify a Product Search",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            mod.setProduct(field1.getText());
            mod.setBrand(field2.getText());
            mod.setEbayUrl(field3.getText());
            mod.setAmazonUrl(field4.getText());
            ProductController.modifyProduct(mod.getId(), mod);
            allListModel.remove(indices[0]);
            allListModel.addElement(mod);
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
