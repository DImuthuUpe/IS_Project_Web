package com.dimuthuupeksha.is.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimuthuupeksha.is.beans.Advertisement;

/**
 * Servlet implementation class listHandler
 */
@WebServlet("/listHandler")
public class listHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public listHandler() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	private List<Advertisement> getAddList(int start, int end) {
		try {
			Class.forName("com.mysql.jdbc.Driver");

			Connection connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/ikman_adds", "root", "fun123");
			String sql = "select * from advertisement limit "+start+","+end;
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			
			ResultSet rst=  preparedStatement.executeQuery();
			List<Advertisement> addList = new ArrayList<>();
			while (rst.next()) {
				Advertisement add = new Advertisement();
				add.setTitle(rst.getString("title"));
				add.setUrl(rst.getString("url"));
				add.setId(rst.getString("id"));
				add.setDate(rst.getString("dates"));
				add.setImage(rst.getString("image").split(";")[0]);
				add.setContent(rst.getString("content"));
				add.setPhone(rst.getString("phone"));
				add.setPrice(rst.getString("price"));
				add.setAttr(rst.getString("attr"));
				addList.add(add);			
			}
			return addList;
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		
		List<Advertisement> adds = getAddList(0, 11);
		//Advertisement add = new Advertisement();
		//add.setTitle("Add 1");
		//adds.add(add);
		request.setAttribute("adds", adds);
		PrintWriter out = response.getWriter();
		out.print("Helooo");
		RequestDispatcher disp = request.getRequestDispatcher("list.jsp");
		disp.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
