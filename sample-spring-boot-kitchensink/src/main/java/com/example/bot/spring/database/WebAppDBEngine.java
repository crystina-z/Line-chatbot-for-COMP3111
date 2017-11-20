package com.example.bot.spring.database;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;

import com.example.bot.spring.webapplication.domain.*;

/**
 * this is the DBEngine that display the information on the website page
 * @author jsongaf
 *
 */
public class WebAppDBEngine extends DBEngine {
	
	static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	private Connection connection;
	/**
	 * get the customers information from the databse
	 * @return get hte customers information
	 * @throws Exception if database connection is failed
	 */
	public LinkedList<Customer> getAllCustomerInfo() throws Exception{
		connection = this.getConnection();
		LinkedList<Customer> allCus = new LinkedList<Customer>();
		PreparedStatement nstmt;
		nstmt = connection.prepareStatement(
				"SELECT * "
				+ " FROM customer_info");
		ResultSet rs = nstmt.executeQuery();
		int idx = 1;
		while(rs.next()) {
			Customer cus = new Customer();
			String name = rs.getString(2);
			String phone = rs.getString(3);
			String bootableid = rs.getString(5);
			int adults = rs.getInt(6);
			int children = rs.getInt(7);
			int toddler = rs.getInt(8);
			double totalPrice = rs.getDouble(9);
			double pricePaid = rs.getDouble(10);
			String special = rs.getString(11);
			cus.setIdx(idx);
			cus.setAdults(adults);
			cus.setBootableId(bootableid);
			cus.setChildren(children);
			cus.setName(name);
			cus.setPhone(phone);
			cus.setSpecial(special);
			cus.setToddler(toddler);
			cus.setPricePaid(pricePaid);
			cus.setTotalPrice(totalPrice);
			allCus.add(cus);
			idx++;
		}
		nstmt.close();
		rs.close();
		connection.close();
		connection = null;
		return allCus;
	}
	/**
	 * get the tours information from the database
	 * @return get the tour information
	 * @throws Exception if database connection if failed
	 */
	public LinkedList<Tour> getAllTourInfo() throws Exception{
		connection = this.getConnection();
		LinkedList<Tour> allTours = new LinkedList<Tour>();
		PreparedStatement nstmt;
			nstmt = connection.prepareStatement(
					"SELECT  b.bootableid, i.tour_name, b.tourdate, b.tourguideid, b.hotel, b.tourcapcity, b.registerednum, b.mintourist"
					+ " FROM booking_table b, tour_touroffer_relation o, tour_info i "
					+ " WHERE b.bootableid = o.bootableid"
					+ " AND i.tourid = o.tourid");
			ResultSet rs = nstmt.executeQuery();
			while(rs.next()) {
				Tour tour = new Tour();
				String tourId = rs.getString(1);
				String tourName = rs.getString(2);
				String tourDate = rs.getString(3);
				int tourGuideId = rs.getInt(4);
				String nameOfHotel = rs.getString(5);
				int tourCapacity = rs.getInt(6);
				int registeredNum = rs.getInt(7);
				int minTourist = rs.getInt(8);
				tour.setNameOfHotel(nameOfHotel);
				tour.setRegisteredNum(registeredNum);
				tour.setTourCapacity(tourCapacity);
				tour.setTourDate(tourDate);
				tour.setTourGuideId(tourGuideId);
				tour.setTourId(tourId);
				tour.setTourName(tourName);
				tour.setMinTourist(minTourist);
				allTours.add(tour);
			}
			nstmt.close();
			rs.close();
			connection.close();
			connection = null;
		return allTours;
	}
	/**
	 * add a new customer in the website if needed
	 * @param customer users
	 * @throws Exception if database connection is failed
	 */
	public void addNewCustomer(Customer customer) throws Exception {
		connection = this.getConnection();
		String name = customer.getName();
		String phone = customer.getPhone();
		String bootableid = customer.getBootableId();
		int adults = customer.getAdults();
		int children = customer.getChildren();
		int toddler = customer.getToddler();
		String special = customer.getSpecial();
		PreparedStatement nstmt = null;
		double price = 0;
		String field = null;
		String ts = bootableid.substring(5);
		String id = bootableid.substring(0, 5);
		int year = Integer.parseInt(ts.substring(0, 4));
		int month = Integer.parseInt(ts.substring(4, 6));
		int day = Integer.parseInt(ts.substring(6, 8));
		Calendar cal = new GregorianCalendar(year, month - 1, day);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		if(Calendar.SUNDAY == dayOfWeek || Calendar.SATURDAY == dayOfWeek) {
			field = "weekend_price";
		}else {
		   	field = "weekday_price";
		}
		nstmt = connection.prepareStatement(
				"SELECT "+field
				+ " FROM tour_price"
				+ " WHERE tourid = ?");
		nstmt.setString(1, id);
		ResultSet rs = nstmt.executeQuery();
		while(rs.next()) {
			price = rs.getDouble(1);
		}
		rs.close();
		nstmt.close();
		double totalPrice = price*adults+price*0.8*children;
		nstmt = connection.prepareStatement(
				"SELECT");
		nstmt = connection.prepareStatement(
				"INSERT INTO customer_info"
				+ " VALUES (0,?,?,0,?,?,?,?,?,0,?) ");
		nstmt.setString(1, name);
		nstmt.setString(2, phone);
		nstmt.setString(3,bootableid);
		nstmt.setInt(4, adults);
		nstmt.setInt(5, children);
		nstmt.setInt(6, toddler);
		nstmt.setDouble(7, totalPrice);
		nstmt.setString(8, special);
		nstmt.execute();
		nstmt.close();
		connection.close();
		connection = null;
	}
	/**
	 * get unanswered questions about the database to display it on the website
	 * @return unanswered questions list
	 * @throws Exception if database connection is failed
	 */
	public LinkedList<UQ>getUQs() throws Exception{
		connection = this.getConnection();
		PreparedStatement stmt;
		ResultSet rs;
		LinkedList<UQ> result = new LinkedList<UQ>(); 
		
		String statement = "SELECT * FROM unanswered_question ";
		stmt = connection.prepareStatement(statement);
		rs = stmt.executeQuery();	
		int idx = 1;
		while (rs.next()) {
			String id = rs.getString(1); 
			String question = rs.getString(2);
			boolean answered = rs.getBoolean(3);
			if(!answered) {
				UQ uq = new UQ();
				uq.setIdx(idx);
				uq.setId(id);
				uq.setQuestion(question);
				result.add(uq);
				idx++;
			}
		}
		
		rs.close();
		stmt.close();
		connection.close();
		connection = null;
		return result;
	}
	/**
	 * answer the unanswered question from the website
	 * @param question unanswered questions
	 * @param id user id
	 * @param answer answer
	 * @throws Exception if database connection is failed
	 */
	public void answerUQ(String question, String id, String answer) throws Exception {
		connection = this.getConnection();
		PreparedStatement nstmt;
		nstmt = connection.prepareStatement(
				"UPDATE unanswered_question"
				+ " SET answer = ?, answered_or_not = true"
				+ " WHERE id = ? AND question = ?");
	
		nstmt.setString(1, answer);
		nstmt.setString(2, id);
		nstmt.setString(3, question);
		nstmt.executeUpdate();
		nstmt.close();
		connection.close();
		connection = null;
	}
	/**
	 * get the general tour information from the database and display
	 * @return tour informaiton including length and date
	 * @throws Exception if database connection is failed
	 */
	public LinkedList<Tour> getGeneralTourInfo() throws Exception {
		connection = this.getConnection();
		PreparedStatement stmt;
		ResultSet rs;
		LinkedList<Tour> result = new LinkedList<Tour>(); 
		String statement = "SELECT DISTINCT tourid, tour_name FROM tour_info";
		stmt = connection.prepareStatement(statement);
		rs = stmt.executeQuery();			
		while (rs.next()) {
			String tourId = rs.getString(1); 
			String name = rs.getString(2);
			Tour tour = new Tour();
			tour.setTourId(tourId);
			tour.setTourName(name);
			result.add(tour);
		}
		
		rs.close();
		stmt.close();
		connection.close();
		connection = null;
		return result;
	}
	/**
	 * add a new tour in the webstie
	 * @param tour tour 
	 * @throws Exception if database connection is failed
	 */
	public void addNewTour(Tour tour) throws Exception{
		connection = this.getConnection();
		PreparedStatement nstmt = connection.prepareStatement(
				"INSERT INTO booking_table"
				+ " VALUES (?,?,?,?,?,?,0,'unconfirmed',0)");
		String tourId = tour.getTourId();
		String date = tour.getTourDate();
		
		String yyyy = date.substring(0, 4);
		String mm = date.substring(5,7);
		String dd = date.substring(8);
		String bootableid = tourId +yyyy+mm+dd;
		int tourGuideId = tour.getTourGuideId();
		String nameOfHotel = tour.getNameOfHotel();
		int tourCapacity = tour.getTourCapacity();
		int minTourist = tour.getMinTourist();
		nstmt.setString(1, bootableid);
		nstmt.setDate(2, new java.sql.Date(df.parse(date).getTime()));
		nstmt.setInt(3,tourGuideId);
		nstmt.setString(4,nameOfHotel);
		nstmt.setInt(5, tourCapacity);
		nstmt.setInt(6, minTourist);
		nstmt.execute();
		nstmt.close();
		nstmt = connection.prepareStatement(
				"INSERT INTO tour_touroffer_relation"
				+ " VALUES (?,?)");
		nstmt.setString(1, tourId);
		nstmt.setString(2, bootableid);
		nstmt.execute();
		nstmt.close();
		connection.close();
		connection = null;
	}
	/**
	 * get the discount activities for Double Eleven
	 * @return discount activity
	 * @throws Exception if database connection is failed
	 */
	public LinkedList<Activity> getAllActivities() throws Exception{
		// TODO Auto-generated method stub
		connection = this.getConnection();
		PreparedStatement stmt;
		ResultSet rs;
		LinkedList<Activity> result = new LinkedList<Activity>(); 
		String statement = "SELECT * FROM double11";
		stmt = connection.prepareStatement(statement);
		rs = stmt.executeQuery();			
		while (rs.next()) {
			String bootableid = rs.getString(1); 
			int quota = rs.getInt(2);
			Activity activity = new Activity();
			activity.setBootableId(bootableid);
			activity.setQuota(quota);
			result.add(activity);
		}
		rs.close();
		stmt.close();
		connection.close();
		connection = null;
		return result;
	}
	/**
	 * create a discount event for Double Eleven
	 * @param act discount activity
	 * @throws Exception if database connection is failed
	 */
	public void createNewActivity(Activity act) throws Exception{
		// TODO Auto-generated method stub
		connection = this.getConnection();
		String bootableid = act.getBootableId();
		int quota = act.getQuota();
		PreparedStatement nstmt = connection.prepareStatement(
				"INSERT INTO double11"
				+ " VALUES (?,?,?,'released')");
		nstmt.setString(1, bootableid);
		nstmt.setInt(2, quota);
		nstmt.setInt(3, quota);
		nstmt.execute();
		nstmt.close();
		connection.close();
		connection = null;
	}
	/**
	 * update the Customer information on whether he could get a discount
	 * @param customer customer user
	 * @throws Exception if database connection is faield
	 */
	public void updateCustomer(Customer customer) throws Exception{
		connection = this.getConnection();
		String bootableid = customer.getBootableId();
		String name = customer.getName();
		double pricePaid = customer.getPricePaid();
		double tourFee = customer.getTotalPrice();
		int people = customer.getAdults()+customer.getChildren()+customer.getToddler();
		PreparedStatement nstmt = connection.prepareStatement(
				"UPDATE customer_info"
				+ " SET paidamount = ?"
				+ " WHERE customername = ? AND bootableid = ?");
		nstmt.setDouble(1, pricePaid);
		nstmt.setString(2, name);
		nstmt.setString(3, bootableid);
		nstmt.execute();
		nstmt.close();
		if(tourFee <= pricePaid) {
			nstmt = connection.prepareStatement(
					"UPDATE booking_table"
					+" SET paidnum = paidnum + ?"
					+" WHERE bootableid = ?");
			nstmt.setInt(1, people);
			nstmt.setString(2, bootableid);
			nstmt.execute();
			nstmt.close();
		}
		connection.close();
		connection = null;
	}
	
}
