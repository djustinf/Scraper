import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
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
            System.exit(1);
        }
    }

    public void initEbay() throws Exception{
        URI resource = this.getClass().getClassLoader().getResource("/resources/META-INF/Ebay.txt").toURI();
        Scanner in = new Scanner(new File(resource));
        while (in.hasNextLine()) {
            ebay.add(in.nextLine());
        }
    }

    public void initAmazon() throws Exception{
        URI resource = this.getClass().getClassLoader().getResource("/resources/META-INF/Amazon.txt").toURI();
        Scanner in = new Scanner(new File(resource));
        while (in.hasNextLine()) {
            amazon.add(in.nextLine());
        }
    }

    public void getData(Product product) {
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
    }
}
