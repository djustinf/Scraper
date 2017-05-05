import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptExecutor;
import com.opencsv.CSVWriter;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class SearchUtils {

    public ArrayList<String> ebay = new ArrayList<String>();
    public ArrayList<String> amazon = new ArrayList<String>();
    public ArrayList<String> jet = new ArrayList<String>();

    public SearchUtils() {
        try {
            initAmazon();
            initEbay();
            initJet();
        }
        catch (Exception e) {
            System.err.println("Error importing files");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void initEbay() throws Exception{
        InputStream resource = this.getClass().getClassLoader().getResourceAsStream("META-INF/Ebay.txt");
        Scanner in = new Scanner(resource);
        while (in.hasNextLine()) {
            ebay.add(in.nextLine());
        }
    }

    public void initJet() throws Exception{
        InputStream resource = this.getClass().getClassLoader().getResourceAsStream("META-INF/Jet.txt");
        Scanner in = new Scanner(resource);
        while (in.hasNextLine()) {
            jet.add(in.nextLine());
        }
    }

    public void initAmazon() throws Exception{
        InputStream resource = this.getClass().getClassLoader().getResourceAsStream("META-INF/Amazon.txt");
        Scanner in = new Scanner(resource);
        while (in.hasNextLine()) {
            amazon.add(in.nextLine());
        }
    }

    public void waitForReady() {
    }

    public Product getData(Product product) {
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.SEVERE);
        ExpectedCondition<Boolean> expectation = new
                ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver driver) {
                        return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete");
                    }
                };
        WebDriver webClient = new HtmlUnitDriver(true);
        WebDriverWait wait = new WebDriverWait(webClient, 10);
        webClient.get(product.getAmazonUrl());
        wait.until(expectation);
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
        wait.until(expectation);
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
        webClient.get(product.getJetUrl());
        wait.until(expectation);
        for (String tag : jet) {
            try {
                WebElement info = webClient.findElement(By.xpath(tag));
                String data = info.getText();
                product.addPrice("Jet", data);
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
        if (!product.getPrice().containsKey("Jet")) {
            product.addPrice("Jet", "No data found");
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
            csv[4] = product.getPrice().get("Amazon").replaceAll("[^0-9.]", "");
            writer.writeNext(csv);
            csv[0] = product.getBrand();
            csv[1] = product.getProduct();
            csv[2] = "Ebay";
            csv[3] = Double.toString(product.getCost());
            csv[4] = product.getPrice().get("Ebay").replaceAll("[^0-9.]", "");
            writer.writeNext(csv);
            csv[0] = product.getBrand();
            csv[1] = product.getProduct();
            csv[2] = "Jet";
            csv[3] = Double.toString(product.getCost());
            csv[4] = product.getPrice().get("Jet").replaceAll("[^0-9.]", "");
            writer.writeNext(csv);
        }
        writer.close();
    }
}
