package server.db.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

public class UsersDao {
   
   private final SessionFactory sessionFactory;

   public UsersDao(SessionFactory sessionFactory) {
      this.sessionFactory = sessionFactory;
   }
   
   public void save(UsersDataSet usersDataSet) {
      Session session = sessionFactory.openSession();
      Transaction transaction = session.beginTransaction();
      session.save(usersDataSet);
      transaction.commit();
      session.close();
   }
   
   @SuppressWarnings("unchecked")
   public UsersDataSet read(String userName) {
      Session session = sessionFactory.openSession();
      Criteria criteria = session.createCriteria(UsersDataSet.class);
      List<UsersDataSet> udsList = criteria.add(Restrictions.eq("name", userName)).list();
      if (udsList.isEmpty()) 
         return null;
      else 
         return udsList.get(0);
   }
   
   public UsersDataSet read(int id) {
      Session session = sessionFactory.openSession();
      UsersDataSet uds = (UsersDataSet) session.get(UsersDataSet.class, id);
      return uds;
   }

}
