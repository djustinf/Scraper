/*  Daniel Justin Foxhoven
    June 2017
    Josh Taylor Senior Project
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/*  This class is the GUI behind the application.
 */
public class ScraperMenu extends JPanel {

    private JList<Product> allList;
    private JList<Product> searchList;
    private DefaultListModel allListModel;
    private DefaultListModel searchListModel;

    public ScraperMenu() {
        super(new BorderLayout());
        JPanel top = new JPanel(new GridLayout());
        JPanel mid = new JPanel(new GridLayout());
        JPanel bot = new JPanel(new GridLayout());
        mid.add(createAllList());
        mid.add(createSearchList());
        top.add(createAddButton());
        top.add(createSearchButton());
        top.add(createRemoveButton());
        bot.add(createModifyButton());
        bot.add(createFormButton());
        bot.add(createDeleteButton());
        add(top, BorderLayout.NORTH);
        add(mid, BorderLayout.CENTER);
        add(bot, BorderLayout.SOUTH);
        setPreferredSize(new Dimension(500, 750));
    }

    /*  Generates the list of all products in the database
     */
    private JPanel createAllList() {
        allListModel = new DefaultListModel();
        for (Product product : ProductController.getAllProducts())
            allListModel.addElement(product);
        allList = new JList(allListModel);
        allList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(allList);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    /*  Generates the list of all products the user wants to search
     */
    private JPanel createSearchList() {
        searchListModel = new DefaultListModel();
        searchList = new JList(searchListModel);
        searchList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(searchList);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    /*  Generates the add product button
     */
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

    /*  Generates the remove product button
     */
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

    /*  Generates the create product button
     */
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

    /*  Generates the modify product button
     */
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

    /*  Generates the search button. This button kicks off the web crawling process.
     */
    private JButton createSearchButton() {
        JButton button = new JButton("Run Search");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SearchUtils searchUtils = new SearchUtils();
                ExecutorService serv = Executors.newFixedThreadPool(10);
                List<Product> done = new ArrayList<Product>();
                for (int i = 0; i < searchListModel.size(); i++) {
                    final int curVal = i;
                    if (!((Product)searchListModel.get(curVal)).getAmazonUrl().isEmpty())
                        serv.execute(() -> searchUtils.getData((Product)searchListModel.get(curVal), searchUtils.amazon, ((Product) searchListModel.get(curVal)).getAmazonUrl(), "Amazon"));
                    if (!((Product)searchListModel.get(curVal)).getEbayUrl().isEmpty())
                        serv.execute(() -> searchUtils.getData((Product)searchListModel.get(curVal), searchUtils.ebay, ((Product) searchListModel.get(curVal)).getEbayUrl(), "Ebay"));
                    done.add((Product)searchListModel.get(curVal));
                }
                serv.shutdown();
                try {
                    while (!serv.awaitTermination(10, TimeUnit.SECONDS));
                }
                catch (InterruptedException i) {
                    System.err.println("Error waiting for threads");
                }
                try {
                    searchUtils.createCSV(done);
                }
                catch (IOException a) {
                    System.err.println("Error creating output.");
                }
                JOptionPane.showMessageDialog(null, "Done!");
            }
        });
        return button;
    }

    /*  Generates the delete product form button.
     */
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

    /*  Generates the new product form.
     */
    private void displayNewForm() {
        JTextField field1 = new JTextField("<product to search>");
        JTextField field2 = new JTextField("<product brand>");
        JTextField field5 = new JTextField("<initial cost>");
        JTextField field3 = new JTextField("www.ebay.com/<product URL>");
        JTextField field4 = new JTextField("www.amazon.com/<product URL>");
        field1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (field1.getText().equals("<product to search>"))
                    field1.setText("");
            }
        });
        field2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (field2.getText().equals("<product brand>"))
                    field2.setText("");
            }
        });
        field5.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (field5.getText().equals("<initial cost>"))
                    field5.setText("");
            }
        });
        field3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (field3.getText().equals("www.ebay.com/<product URL>"))
                    field3.setText("");
            }
        });
        field4.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (field4.getText().equals("www.amazon.com/<product URL>"))
                    field4.setText("");
            }
        });
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Product"));
        panel.add(field1);
        panel.add(new JLabel("Brand"));
        panel.add(field2);
        panel.add(new JLabel("Initial Cost"));
        panel.add(field5);
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
            product.setCost(Double.parseDouble(field5.getText().replaceAll("[^0-9.]", "")));
            ProductController.storeProduct(product);
            allListModel.addElement(product);
        }
    }

    /*  Generates the modify product form.
     */
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
        JTextField field5 = new JTextField(Double.toString(mod.getCost()));
        JTextField field3 = new JTextField(mod.getEbayUrl());
        JTextField field4 = new JTextField(mod.getAmazonUrl());
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Product"));
        panel.add(field1);
        panel.add(new JLabel("Brand"));
        panel.add(field2);
        panel.add(new JLabel("Initial Cost"));
        panel.add(field5);
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
            mod.setCost(Double.parseDouble(field5.getText().replaceAll("[^0-9.]", "")));
            ProductController.modifyProduct(mod.getId(), mod);
            allListModel.remove(indices[0]);
            allListModel.addElement(mod);
        }
    }

    /*  Kicks off the whole GUI.
     */
    private static void createGUI() {
        JFrame frame = new JFrame("Web Scraper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JComponent content = new ScraperMenu();
        content.setOpaque(true);
        frame.setContentPane(content);
        frame.pack();
        frame.setVisible(true);
    }

    /*  Starts the application.
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createGUI();
            }
        });
    }
}
