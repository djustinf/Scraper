/*  Daniel Justin Foxhoven
    June 2017
    Josh Taylor Senior Project
 */
import com.opencsv.CSVWriter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*  This class encompasses all of the web crawling functionality behind the application.
    In addition, it contains support for exporting the results of the crawl as a CSV.
 */
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

    /*  Initialize ebay resources
     */
    public void initEbay() throws Exception{
        InputStream resource = this.getClass().getClassLoader().getResourceAsStream("META-INF/Ebay.txt");
        Scanner in = new Scanner(resource);
        while (in.hasNextLine()) {
            ebay.add(in.nextLine());
        }
    }

    /*  Initialize amazon resources
     */
    public void initAmazon() throws Exception{
        InputStream resource = this.getClass().getClassLoader().getResourceAsStream("META-INF/Amazon.txt");
        Scanner in = new Scanner(resource);
        while (in.hasNextLine()) {
            amazon.add(in.nextLine());
        }
    }

    /*  Crawls the provided urls and retrieves the data specified by the ebay and amazon resources
     */
    public Product getData(Product product, List<String> tags, String URL, String site) {
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.SEVERE);
        WebDriver webClient = new HtmlUnitDriver(false);
        webClient.get(URL);

        for (String tag : tags) {
            try {
                WebElement info = webClient.findElement(By.xpath(tag));
                String data = info.getText();
                product.addPrice(site, data);
                break;
            } catch (Exception e) {
                continue;
            }
        }
        if (!product.getPrice().containsKey(site) || product.getPrice().get(site).equals("")) {
            product.addPrice(site, "No data found");
        }
        webClient.close();
        return product;
    }

    /*  Generates a CSV of the output data
     */
    public void createCSV(List<Product> products) throws IOException{
        CSVWriter writer = new CSVWriter(new FileWriter("output.csv"), ',');
        String csv[] = new String[5];
        for (Product product : products) {
            if (product.getPrice().containsKey("Amazon")) {
                csv[0] = product.getBrand();
                csv[1] = product.getProduct();
                csv[2] = "Amazon";
                csv[3] = Double.toString(product.getCost());
                csv[4] = product.getPrice().get("Amazon").replaceAll("[^0-9.]", "");
                writer.writeNext(csv);
            }
            if (product.getPrice().containsKey("Ebay")) {
                csv[0] = product.getBrand();
                csv[1] = product.getProduct();
                csv[2] = "Ebay";
                csv[3] = Double.toString(product.getCost());
                csv[4] = product.getPrice().get("Ebay").replaceAll("[^0-9.]", "");
                writer.writeNext(csv);
            }
        }
        writer.close();
    }
}
