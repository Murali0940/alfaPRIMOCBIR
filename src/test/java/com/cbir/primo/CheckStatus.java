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
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;

public class CheckStatus {

	WebDriver driver;

	// company login
	@Test(priority = 1)
	public void alfaPRIMO_CBIRstatus() throws InterruptedException, IOException, EmailException {

String URL = "http://13.115.237.181/cbirstatus/";

		// WebDriverManager.chromedriver().clearDriverCache().setup();
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless"); // for headless chrome
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
		// Thread.sleep(15000);

		By loaderLocator = By.id("loadingSpinner");

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(loaderLocator));

		System.out.println("Loader is gone, proceed further.");

		// Define the locator for the element
		By elementLocator = By.xpath("//i[@class='pi pi-spin pi-spinner']");

		// Create WebDriverWait instance
		WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(30));

		// Wait until the element is gone
		boolean isElementGone = wait1.until(ExpectedConditions.invisibilityOfElementLocated(elementLocator));

		if (isElementGone) {
			System.out.println("Element is no longer visible.");
		} else {
			System.out.println("Element is still visible.");
		}

		// Get the current timestamp
		String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

		// Define the directory path for the screenshot
		String directoryPath = "F:\\CBIRScreenshot";
		File directory = new File(directoryPath);

		// Check if the directory exists, and create it if it doesn't
		if (!directory.exists()) {
			if (directory.mkdirs()) {
				System.out.println("Directory created at: " + directoryPath);
			} else {
				System.out.println("Failed to create directory at: " + directoryPath);
				return;
			}
		}

		// Define the destination path for the screenshot
		File destination = new File("F:\\CBIRScreenshot\\screenshot_" + timestamp + ".png");

		// Take a screenshot of the entire page
		File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

		System.out.println("Destination path taken");

		// Copy the screenshot to the destination file
		try {
			FileHandler.copy(screenshot, destination);

			System.out.println("Screenshot saved to: " + destination.getAbsolutePath());
			System.out.println(destination.getAbsoluteFile());

		} catch (IOException e) {
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
