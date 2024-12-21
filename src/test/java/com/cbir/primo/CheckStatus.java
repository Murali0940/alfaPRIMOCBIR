package com.cbir.primo;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.io.FileHandler;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class CheckStatus {

	WebDriver driver;

	// company login
	@Test(priority = 1)
	public void alfaPRIMO_CBIRstatus() throws InterruptedException, IOException, EmailException {

		String URL = "http://13.115.237.181/cbirstatus/";

		WebDriverManager.chromedriver().clearDriverCache().setup();
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--remote-allow-origins=*");
		options.addArguments("start-maximized");

		driver = new ChromeDriver(options);
		// url
		driver.get(URL);
		System.out.println("The currrent url is : " + driver.getCurrentUrl());
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		System.out.println("The currrent Title is : " + driver.getTitle());
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));

		WebElement primo = driver.findElement(By.xpath("//span[text()='alfaPRIMO']"));
		primo.click();
		Thread.sleep(15000);

		// Get the current timestamp
		String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

		// Take a screenshot of the entire page
		File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

		// Define the destination path for the screenshot
		File destination = new File("F:\\CBIRScreenshot\\screenshot_" + timestamp + ".png");

		System.out.println("Destination path taken");

		// Copy the screenshot to the destination file
		try {
			FileHandler.copy(screenshot, destination);

			System.out.println("Screenshot saved to: " + destination.getAbsolutePath());
			System.out.println(destination.getAbsoluteFile());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Failed to save screenshot: " + e.getMessage());
		}

		System.out.println("screenshot taken");
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));

		try {
			// Create a new email object
			HtmlEmail email = new HtmlEmail();

			// Set email properties
			email.setHostName("smtp.gmail.com");
			email.setSmtpPort(587); // Use port 587 for TLS (secure connection)
			email.setStartTLSEnabled(true);
			email.setAuthentication("murali@a-tkg.co.in", "M1u2r3a4l5i6"); // Replace with your email and password
			email.setStartTLSEnabled(true);

			// Set the email details
			email.addTo("murali@a-tkg.co.in"); // Recipient's email address
			email.addTo("murali@a-tkg.co.in");
			email.setFrom("murali@a-tkg.co.in"); // Your email address
			email.setSubject("CBIR Status");
			email.setMsg("Please find today CBIR status here...");

			// Create an email attachment for the screenshot
			EmailAttachment attachmentPart = new EmailAttachment();

			String file = destination.getName();
			System.out.println("Filename: " + file);

			String attachment = "F:\\CBIRScreenshot\\" + file;

			attachmentPart.setPath(attachment); // Path of the screenshot
			attachmentPart.setDisposition(EmailAttachment.ATTACHMENT);

			// Attach the screenshot to the email
			email.attach(attachmentPart);

			// Send the email
			email.send();
			System.out.println("Email sent successfully!");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@AfterSuite
	public void teardown() {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));

		driver.quit();
	}

}
