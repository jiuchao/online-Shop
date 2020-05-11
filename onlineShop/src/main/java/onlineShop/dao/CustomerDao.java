package onlineShop.dao;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import onlineShop.model.Authorities;
import onlineShop.model.Cart;
import onlineShop.model.Customer;
import onlineShop.model.User;

@Repository
//@Repository: spring annotation, it has the same role with @component
public class CustomerDao {

	@Autowired
	//Inject all the database related sessionFactory 
	private SessionFactory sessionFactory;

	public void addCustomer(Customer customer) {
		//enable the user
		customer.getUser().setEnabled(true);

		//Set the authority of this customer
		Authorities authorities = new Authorities();
		authorities.setAuthorities("ROLE_USER");
		authorities.setEmailId(customer.getUser().getEmailId());
		
		//Create a cart
		Cart cart = new Cart();
		cart.setCustomer(customer);
		customer.setCart(cart);
		
		//Save this customer into the database
		Session session = null;		
		try {
			session = sessionFactory.openSession();		//get a session object from the session factory
			session.beginTransaction();	//begin the transaction
			session.save(authorities);
			session.save(customer);		//cart has been cascaded saved, so we did not save cart here
			session.getTransaction().commit(); //Commit the transaction
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback(); //back to the state before this transaction
		} finally {
			if (session != null) {
				session.close(); //close the session
			}
		}
	}

	public Customer getCustomerByUserName(String userName) {
		User user = null;
		//try with resources statement
		//If will auto close the session, so finally is not needed here
		//https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
		try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            //CriteriaBuilder is a framework provided by hibernate
            //construct a ctieratia query
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class); //search from user table
			//root of B-tree
			Root<User> root = criteriaQuery.from(User.class);
			//Create the query statement
			criteriaQuery.select(root).where(builder.equal(root.get("emailId"), userName));
			//create query, single result
			user = session.createQuery(criteriaQuery).getSingleResult();
            session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (user != null)
			return user.getCustomer();
		return null;
	}
}
