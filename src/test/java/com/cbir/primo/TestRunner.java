package com.cbir.primo;

import org.testng.TestListenerAdapter;
import org.testng.TestNG;

public class TestRunner {
    public static void main(String[] args) {
//        TestNG testng = new TestNG();
//        testng.setTestSuites(Arrays.asList("F:\\Java_Training_Prem\\PrimoCbirStatus\\testng.xml"));  // Point to your TestNG suite XML
//        testng.run();
    	
    	TestListenerAdapter tla = new TestListenerAdapter();
    	TestNG testng = new TestNG();
    	testng.setTestClasses(new Class[] { CheckStatus.class });
    	testng.addListener(tla);
    	testng.run();
    	System.out.println("running from testng class");
    }
}
