package com.teamtreehouse.contactmgr;

import com.teamtreehouse.contactmgr.model.Contact;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import java.io.Serializable;
import java.util.List;

/**
 * Created by adhyan on 5/5/16.
 */
public class Application {
    //Hold a reusable reference to a sessionFactory (since we need only one)
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        //create a StandardSeriveRegistry
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    public static void main(String[] args) {
        Contact contact = new Contact.ContactBuilder("Adhyan", "Srivastava")
                .withEmail("ad@gmail.com")
                .withPhone(8899006677L)
                .build();
        int id = save(contact);

        // Display a list of contacts before the update
        System.out.println("%n%nBefore the update %n%n");
        for (Contact c: fetchAllContacts()) {
            System.out.println(c);
        }

        //Get the persisted contact
        Contact c = findContactById(id);

        //update the contact
        c.setFirstName("Ados");

        //Persist the changes
        System.out.println("%n%nUpdating...%n%n");
        update(c);
        System.out.println("%n%nUpdate complete%n%n");


        //Display List of contacts after the update
        System.out.println("%n%nAfter update %n%n");
        for (Contact c1: fetchAllContacts()) {
            System.out.println(c1);
        }

    }

    @SuppressWarnings("unchecked")
    private static List<Contact> fetchAllContacts() {
        //open a session
        Session session = sessionFactory.openSession();

        //Create Criteria
        Criteria criteria = session.createCriteria(Contact.class);

        //Get a list of contact objects according to the Criteria object
        List<Contact> contacts = criteria.list();

        //close the session
        session.close();
        return  contacts;
    }

    private static Contact findContactById(int id) {
        //open a session
        Session session = sessionFactory.openSession();

        // Retrieve the persistent object (or null if not found)
        Contact contact = session.get(Contact.class, id);

        // close the session
        session.close();

        // Return the object
        return contact;
    }

    private static void update(Contact contact) {
        // open a session
        Session session = sessionFactory.openSession();

        // begin a transaction
        session.beginTransaction();

        //use the session to update the contact
        session.update(contact);

        //commit the transaction
        session.getTransaction().commit();

        //close the session
        session.close();
    }

    private static int save(Contact contact) {
        //open a session
        Session session = sessionFactory.openSession();

        //Begin a transaction
        session.beginTransaction();

        //Use the session to save the contact
        int id = Integer.parseInt(session.save(contact) +"");

        //commit the transaction
        session.getTransaction().commit();


        //close the session
        session.close();
        return id;
    }

}
