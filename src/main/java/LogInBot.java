import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.logging.Level;

/**
 * Logs into ethz moodle websites and returns them. Simulates a user.
 * optional:
 * -> change university/institution
 *
 * @author Thiemo Zaugg
 */
public class LogInBot {
    public static Document logOn(String url, String userName, String pw) {
        HtmlPage htmlPage;
        WebClient webClient;
        HtmlForm htmlForm;
        try {
            java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);

            // Create and initialize WebClient object
            webClient = new WebClient(BrowserVersion.FIREFOX_68);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setUseInsecureSSL(true);
            webClient.getCookieManager().setCookiesEnabled(true);

            //get initial page
            htmlPage = webClient.getPage(url);

            //go to login Site
            //select "ETH Z�rich"
            HtmlSelect select = (HtmlSelect) htmlPage.getElementById("idp");
            HtmlOption option = select.getOptionByValue("https://aai-logon.ethz.ch/idp/shibboleth");
            select.setSelectedAttribute(option, true);
            //click the submit btn
            htmlPage = ((DomElement) htmlPage.getFirstByXPath("//button[@type='submit']")).click();

            //Login with form
            htmlForm = htmlPage.getFirstByXPath("/html/body/section/div[2]/div[1]/div/div/div[4]/form");
            htmlForm.getInputByName("j_username").setValueAttribute(userName);
            htmlForm.getInputByName("j_password").setValueAttribute(pw);

            htmlPage = ((DomElement) htmlPage.getFirstByXPath("//button[@name='_eventId_proceed']")).click();

            System.out.println("successfully logged into: " + Jsoup.parse(htmlPage.asXml()).title());

            webClient.close();
            return Jsoup.parse(htmlPage.asXml());

        } catch (ElementNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
