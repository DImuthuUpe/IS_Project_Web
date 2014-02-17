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
import javax.servlet.http.HttpSession;

import com.dimuthuupeksha.is.beans.Advertisement;

/**
 * Servlet implementation class details
 */
@WebServlet("/details")
public class details extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public details() {
		super();
		// TODO Auto-generated constructor stub
	}

	private List<Advertisement> getSuggestions(String id) {
		try {
			Class.forName("com.mysql.jdbc.Driver");

			Connection connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/ikman_adds", "root", "fun123");
			String sql = "SELECT * FROM selection_graph t1 left join (advertisement t2) on t1.item2=t2.id where item1="+id+" order by count desc limit 0,5";
			PreparedStatement preparedStatement = connection
					.prepareStatement(sql);

			ResultSet rst = preparedStatement.executeQuery();
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
	
	
	private List<Advertisement> getCategories(String id) {
		try {
			Class.forName("com.mysql.jdbc.Driver");

			Connection connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/ikman_adds", "root", "fun123");
			
			String sql = "SELECT * FROM advertisement where id="+id;
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			
			ResultSet rst = preparedStatement.executeQuery();
			rst.next();
			String cat= rst.getString("category");
			sql = "SELECT * FROM advertisement where category like '%"+cat+"%' limit 0,5";
			preparedStatement = connection.prepareStatement(sql);

			rst = preparedStatement.executeQuery();
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

	private Advertisement getAdvertisement(String id) {
		try {
			Class.forName("com.mysql.jdbc.Driver");

			Connection connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/ikman_adds", "root", "fun123");
			String sql = "select * from advertisement where id=" + id;
			PreparedStatement preparedStatement = connection
					.prepareStatement(sql);

			ResultSet rst = preparedStatement.executeQuery();
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
				return add;
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private void updateList(String id1, String id2) {
		try {
			Class.forName("com.mysql.jdbc.Driver");

			Connection connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/ikman_adds", "root", "fun123");
			String sql = "select * from selection_graph where item1=" + id1
					+ " and item2=" + id2;
			PreparedStatement preparedStatement = connection
					.prepareStatement(sql);

			ResultSet rst = preparedStatement.executeQuery();
			if (rst.next()) {
				sql = "UPDATE selection_graph SET count= ? WHERE item1=? and item2=?";
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setInt(1,
						Integer.parseInt(rst.getString("count")) + 1);
				preparedStatement.setInt(2, Integer.parseInt(id1));
				preparedStatement.setInt(3, Integer.parseInt(id2));
				preparedStatement.executeUpdate();

			} else {
				sql = "INSERT INTO selection_graph(item1,item2,count)"
						+ "VALUES (?, ?, ?)";
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setInt(1, Integer.parseInt(id1));

				preparedStatement.setInt(2, Integer.parseInt(id2));

				preparedStatement.setInt(3, Integer.parseInt("1"));
				preparedStatement.executeUpdate();
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		PrintWriter writer = response.getWriter();

		String id = request.getParameter("id");

		HttpSession session = request.getSession();
		List<String> visitList;
		if (session.getAttribute("visit_list") != null) {
			visitList = (List<String>) session.getAttribute("visit_list");
		} else {
			visitList = new ArrayList<>();
		}
		boolean hasInList = false;
		for (String string : visitList) {
			if (id.equals(string)) {
				hasInList = true;
			} else {
				updateList(id, string);
				updateList(string, id);
			}
		}
		if (!hasInList) {
			visitList.add(id);
		}
		session.setAttribute("visit_list", visitList);
		for (String string : visitList) {
			writer.print(string + "<br/>");
		}
		writer.println();
		List<Advertisement> suggestinList = getSuggestions(id);
		for (Advertisement advertisement : suggestinList) {
			writer.print(advertisement.getId()+ " "+advertisement.getTitle() );
			writer.println();
		}
		
		List<Advertisement> catList = getCategories(id);
		
		Advertisement add = getAdvertisement(id);
		request.setAttribute("add", add);
		request.setAttribute("suggestions", suggestinList);

		request.setAttribute("categories", catList);
		 RequestDispatcher disp = request.getRequestDispatcher("details.jsp");
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
