/**
 * 
 */
package com.srikanth.springboot.ui.controllers;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.srikanth.springboot.ui.domain.User;
import com.srikanth.springboot.ui.domain.pagination.DataTableRequest;
import com.srikanth.springboot.ui.domain.pagination.DataTableResults;
import com.srikanth.springboot.ui.domain.pagination.PaginationCriteria;
import com.srikanth.springboot.ui.repo.UserRepository;
import com.srikanth.springboot.ui.util.AppUtil;


@Controller
public class BaseController {

	@Autowired
	private UserRepository userRepo;
	
	/** The entity manager. */
	@PersistenceContext
	private EntityManager entityManager;
	
		
	@RequestMapping(value="/", method = RequestMethod.GET)
	public ModelAndView home(@RequestParam(value = "name", defaultValue = "World") String name) {
		ModelAndView mv = new ModelAndView("index");
		mv.addObject("userModel", new User());
		List<User> userList  = userRepo.findAll();;
		mv.addObject("userlist", userList);
		return mv;
	}
	
	@RequestMapping(value="/users", method=RequestMethod.GET)
	public String listUsers(Model model) {
		return "users";
	}
	
	@RequestMapping(value="/users/paginated", method=RequestMethod.GET)
	@ResponseBody
	public String listUsersPaginated(HttpServletRequest request, HttpServletResponse response, Model model) {
		
		DataTableRequest<User> dataTableInRQ = new DataTableRequest<User>(request);
		PaginationCriteria pagination = dataTableInRQ.getPaginationRequest();
		
		String baseQuery = "SELECT id as id, name as name, salary as salary, (SELECT COUNT(1) FROM USER) AS totalrecords  FROM USER";
		String paginatedQuery = AppUtil.buildPaginatedQuery(baseQuery, pagination);
		
		System.out.println(paginatedQuery);
		
		Query query = entityManager.createNativeQuery(paginatedQuery, User.class);
		
		@SuppressWarnings("unchecked")
		List<User> userList = query.getResultList();
		
		DataTableResults<User> dataTableResult = new DataTableResults<User>();
		dataTableResult.setDraw(dataTableInRQ.getDraw());
		dataTableResult.setListOfDataObjects(userList);
		if (!AppUtil.isObjectEmpty(userList)) {
			dataTableResult.setRecordsTotal(userList.get(0).getTotalRecords()
					.toString());
			if (dataTableInRQ.getPaginationRequest().isFilterByEmpty()) {
				dataTableResult.setRecordsFiltered(userList.get(0).getTotalRecords()
						.toString());
			} else {
				dataTableResult.setRecordsFiltered(Integer.toString(userList.size()));
			}
		}
		return new Gson().toJson(dataTableResult);
	}
	
	@RequestMapping(value="/adduser", method=RequestMethod.POST)
	public String addUser(@ModelAttribute User userModel, Model model) {
		if(null != userModel) {
			
			if(!AppUtil.isObjectEmpty(userModel.getId()) && 
					!AppUtil.isObjectEmpty(userModel.getName()) && 
					!AppUtil.isObjectEmpty(userModel.getSalary())) {
				userRepo.save(userModel);
			}
		}
		return "redirect:/";
	}

}
