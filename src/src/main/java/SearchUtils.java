import com.opencsv.CSVWriter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SearchUtils {

    public ArrayList<String> ebay = new ArrayList<String>();
    public ArrayList<String> amazon = new ArrayList<String>();

    public SearchUtils() {
        try {
            initAmazon();
            initEbay();
        }
        catch (Exception e) {
            System.err.println("Error importing files");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void initEbay() throws Exception{
        URI resource = this.getClass().getClassLoader().getResource("META-INF/Ebay.txt").toURI();
        Scanner in = new Scanner(new File(resource));
        while (in.hasNextLine()) {
            ebay.add(in.nextLine());
        }
    }

    public void initAmazon() throws Exception{
        URI resource = this.getClass().getClassLoader().getResource("META-INF/Amazon.txt").toURI();
        Scanner in = new Scanner(new File(resource));
        while (in.hasNextLine()) {
            amazon.add(in.nextLine());
        }
    }

    public Product getData(Product product) {
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.SEVERE);
        WebDriver webClient = new HtmlUnitDriver();
        webClient.get(product.getAmazonUrl());
        for (String tag : amazon) {
            try {
                WebElement info = webClient.findElement(By.xpath(tag));
                String data = info.getText();
                product.addPrice("Amazon", data);
                break;
            }
            catch (Exception e) {
                continue;
            }
        }
        webClient.get(product.getEbayUrl());
        for (String tag : ebay) {
            try {
                WebElement info = webClient.findElement(By.xpath(tag));
                String data = info.getText();
                product.addPrice("Ebay", data);
                break;
            }
            catch (Exception e) {
                continue;
            }
        }
        if (!product.getPrice().containsKey("Amazon")) {
            product.addPrice("Amazon", "No data found");
        }
        if (!product.getPrice().containsKey("Ebay")) {
            product.addPrice("Ebay", "No data found");
        }
        return product;
    }

    public void createCSV(List<Product> products) throws IOException{
        CSVWriter writer = new CSVWriter(new FileWriter("output.csv"), ',');
        String csv[] = new String[5];
        for (Product product : products) {
            csv[0] = product.getBrand();
            csv[1] = product.getProduct();
            csv[2] = "Amazon";
            csv[3] = Double.toString(product.getCost());
            csv[4] = product.getPrice().get("Amazon");
            writer.writeNext(csv);
            csv[0] = product.getBrand();
            csv[1] = product.getProduct();
            csv[2] = "Ebay";
            csv[3] = Double.toString(product.getCost());
            csv[4] = product.getPrice().get("Ebay");
            writer.writeNext(csv);
        }
        writer.close();
    }
}
