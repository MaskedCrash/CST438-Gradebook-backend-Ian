package com.cst438;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import com.cst438.domain.Assignment;
import com.cst438.domain.AssignmentGrade;
import com.cst438.domain.AssignmentGradeRepository;
import com.cst438.domain.AssignmentRepository;
import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentRepository;

/*
 * This example shows how to use selenium testing using the web driver 
 * with Chrome browser.
 * 
 *  - Buttons, input, and anchor elements are located using XPATH expression.
 *  - onClick( ) method is used with buttons and anchor tags.
 *  - Input fields are located and sendKeys( ) method is used to enter test data.
 *  - Spring Boot JPA is used to initialize, verify and reset the database before
 *      and after testing.
 */

@SpringBootTest
public class EndToEndTestCreateNewAssignment {

	public static final String CHROME_DRIVER_FILE_LOCATION = "C:/PleaseShootMe/chromedriver_win32/chromedriver.exe";
	//Overworked. Not gonna bother with heroku.
	//"If you and your partner are pressed for time,  you may skip the Heroku setup and use the configuration from last week
	//with both backends and database and RabbitMQ all running on your local computer. "
	public static final String URL = "http://localhost:3000";
	public static final String TEST_USER_EMAIL = "test@csumb.edu";
	public static final String TEST_INSTRUCTOR_EMAIL = "dwisneski@csumb.edu";
	public static final int SLEEP_DURATION = 1000; // 1 second.
	public static final String TEST_ASSIGNMENT_NAME = "TEST"; 
	public static final String TEST_ASSIGNMENT_DUEDATE = "2022-06-06";
	public static final int TEST_COURSE_ID = 123456;
	
	@Autowired
	EnrollmentRepository enrollmentRepository;

	@Autowired
	CourseRepository courseRepository;

	@Autowired
	AssignmentGradeRepository assignnmentGradeRepository;

	@Autowired
	AssignmentRepository assignmentRepository;

	@Test
	public void addAssignmentTest() throws Exception {

//		Database setup:  create course		
		Course c = new Course();
		c.setCourse_id(99999);
		c.setInstructor(TEST_INSTRUCTOR_EMAIL);
		c.setSemester("Fall");
		c.setYear(2021);
		c.setTitle("Test Course");


//	    add a student TEST into course 99999
		Enrollment e = new Enrollment();
		e.setCourse(c);
		e.setStudentEmail(TEST_USER_EMAIL);
		e.setStudentName("Test");

		courseRepository.save(c);
		e = enrollmentRepository.save(e);

		AssignmentGrade ag = null;

		// set the driver location and start driver
		//@formatter:off
		// browser	property name 				Java Driver Class
		// edge 	webdriver.edge.driver 		EdgeDriver
		// FireFox 	webdriver.firefox.driver 	FirefoxDriver
		// IE 		webdriver.ie.driver 		InternetExplorerDriver
		//@formatter:on

		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
		WebDriver driver = new ChromeDriver();
		// Puts an Implicit wait for 10 seconds before throwing exception
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		driver.get(URL);
		Thread.sleep(SLEEP_DURATION);

		
		
		try {
			// locate input element to submit Assignment- couldn't do this without renaming stuff, so I went with the quick and dirty solution of just selecting the last one.
			WebElement we = driver.findElement(By.xpath("//a[last()]"));
		 	we.click();

			// Change the names, then hit go.
		 	we.findElement(By.xpath("input[@name='assignmentName']")).sendKeys(TEST_ASSIGNMENT_NAME);
		    we.findElement(By.xpath("input[@name='dueDate']")).sendKeys(TEST_ASSIGNMENT_DUEDATE);
		    we.findElement(By.xpath("input[@name='courseId']")).sendKeys(String.valueOf(TEST_COURSE_ID));
			//Searching for go by the name submit.
		    driver.findElement(By.xpath("input[@type='submit']")).click();
			Thread.sleep(SLEEP_DURATION);

			
			Assignment a = assignmentRepository.findByAssignmentNameAndCourseID(TEST_ASSIGNMENT_NAME, TEST_COURSE_ID);
			assertNotNull(a); 
			
			
		} catch (Exception ex) {
			throw ex;
		} finally {

			// clean up database.
			Assignment cleanUp = assignmentRepository.findByAssignmentNameAndCourseID(TEST_ASSIGNMENT_NAME, TEST_COURSE_ID);
			if (cleanUp!=null) assignmentRepository.delete(cleanUp);

			driver.quit();
		}

	}
}
