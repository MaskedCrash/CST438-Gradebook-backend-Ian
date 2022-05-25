package com.cst438.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import com.cst438.domain.CourseDTOG;

public class RegistrationServiceREST extends RegistrationService {

	
	RestTemplate restTemplate = new RestTemplate();
	
	@Value("${registration.url}") 
	String registration_url;
	
	public RegistrationServiceREST() {
		System.out.println("REST registration service ");
	}
	
	/*
	 * 
	 * When teacher does POST to 
	 *	/course/{course_id}/finalgrades
	 *	send final grades of all students to Registration backend using CourseDTOG
	 */
	
	
	@Override
	public void sendFinalGrades(int course_id , CourseDTOG courseDTO) { 
		
		restTemplate.postForEntity(registration_url + "/course/" + courseDTO.course_id,   // URL
													courseDTO,                             // data to send
													CourseDTOG.class);                	  // return data type);
		}
}
