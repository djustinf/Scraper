import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SearchModule {

    private String product;
    private String siteName;
    private String pageURL;
    private ArrayList<String[]> data = new ArrayList<String[]>(); // [description of data, data]

    public SearchModule(String product) {
	    this.product = product;
    }
    
    public String getProduct() {
	return this.product;
    }
    
    public String getSiteName() {
	return this.siteName;
    }
    
    public String getPageURL() {
	return this.pageURL;
    }
    
    public void setURL(String URL) {
	this.pageURL = URL;
    }
    
    public void setSiteName(String name) {
	this.siteName = name;
    }
    
    public void addData(String tuple[]) {
	data.add(tuple);
    }
    
    public ArrayList<String[]> getData () {
	return this.data;
    }

    public void generateCSV() {
        FileWriter destOut = null;
        try {
            File dest = new File(Paths.get(".").toAbsolutePath().normalize().toString()+ "\\" + getSiteName() + "_" + getProduct() + ".csv");
            if (!dest.exists()) {
                dest.createNewFile();
            }
            destOut = new FileWriter(dest, false);
            DateFormat df = new SimpleDateFormat("MM/dd/yy h:mm a, z");
            StringBuilder output = new StringBuilder("");
            output.append("\"Vendor\"");
            output.append(',');
            output.append('\"');
            output.append(getSiteName().replaceAll("\"", "\"\""));
            output.append('\"');
            output.append(',');
            output.append("\"Date\"");
            output.append(',');
            output.append('\"');
            output.append(df.format(new Date()));
            output.append('\"');
            output.append(',');
            for (String tuple[] : getData()) {
                output.append('\"');
                output.append(tuple[0].replaceAll("\"", "\"\""));
                output.append('\"');
                output.append(',');
                output.append('\"');
                output.append(tuple[1].replaceAll("\"", "\"\""));
                output.append('\"');
                output.append(',');
            }
            output.deleteCharAt(output.length()-1);
            destOut.write(output.toString());
        }
        catch (Exception e) {
            System.out.printf("File write error: %s", getSiteName() + "_" + getProduct());
            System.exit(1);
        }
        finally {
            try {
                destOut.flush();
                destOut.close();
            }
            catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
}
