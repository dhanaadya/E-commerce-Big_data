package com.controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cloudinary.Cloudinary;
import com.cloudinary.SingletonManager;
import com.cloudinary.utils.ObjectUtils;
import com.dao.Product;
import com.model.RedisSession;
import com.model.SolrSearch;

public class Search extends HttpServlet{
 
	private static final long serialVersionUID = 1L;
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
		try{		
		
		String pid = request.getParameter("pid");
		String key = request.getParameter("key");
		
		System.out.println("key :" + key);
		
	    System.out.println("pid : " + pid);
		Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
				  "cloud_name", "onlineproducts",
				  "api_key", "786449393122125",
				  "api_secret", "0ojVmg76PTkhKRz9ahcSmUjXEZY"));
		SingletonManager manager = new SingletonManager();
		manager.setCloudinary(cloudinary);
		manager.init();
		if (pid == "" && key =="")
		{
			RequestDispatcher view = request.getRequestDispatcher("errorview.jsp");
	        view.forward(request, response);
		}
	    if (pid != "")
	   {
		//CassandraSession node = new CassandraSession();
	    RedisSession rs= new RedisSession();
	    List<String> prodlst= new ArrayList<String>();
	    prodlst.add(pid);
	    //System.out.print(pid);
		List<Product> productlist= rs.cachedprod(prodlst);
		
		//System.out.println("Fetched Result from cassandra");
		//System.out.println(productlist.get(0).pdtid);
		request.setAttribute("products2", productlist);
		
	   }  
	    else if (key != "")
      {
    	  SolrSearch ss = new SolrSearch();
    	  List <Product> productlist2=ss.SolrData(key);
    	  
    	  request.setAttribute("products2", productlist2);
  		  
      }
	    
	    RequestDispatcher view = request.getRequestDispatcher("searchview.jsp");
        view.forward(request, response);
	}
	catch (Exception e) {
         e.printStackTrace();
        }
		
}
	
}
