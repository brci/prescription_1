package org.openmrs.module.prescription.api.dao;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Patient;
import org.openmrs.api.db.DAOException;
import org.openmrs.api.db.hibernate.DbSession;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.prescription.Prescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository(value = "prescription.PrescriptionDao")
public class PrescriptionDao {
	
	@Autowired
	DbSessionFactory sessionFactory;
	
	private DbSession getSession() {
		return this.sessionFactory.getCurrentSession();
	}
	
	public Prescription getItemByUuid(String uuid) {
		return (Prescription) this.getSession().createCriteria((Class) Prescription.class)
		        .add((Criterion) Restrictions.eq((String) "uuid", (Object) uuid)).uniqueResult();
	}
	
	public Prescription getItemById(Integer pid) {
		return (Prescription) this.getSession().createCriteria((Class) Prescription.class)
		        .add((Criterion) Restrictions.eq("id", (Integer) pid)).uniqueResult();
	}
	
	public Prescription saveItem(Prescription item) {
		this.getSession().saveOrUpdate((Object) item);
		return item;
	}
	
	public boolean deleteItem(Prescription item) {
		System.out.println("deleting");
		try {
			this.getSession().delete((Object) item);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public List<Prescription> getAllPrescriptions(Patient patient) throws DAOException {
		List items = new ArrayList<Prescription>();
		
		try {
			String sql = "SELECT prescription_id,uuid,patient_id,drug_id,description,dose,advice,prescription_date_created,prescription_file from prescription p where p.patient_id = :p_id";
			SQLQuery query = this.sessionFactory.getCurrentSession().createSQLQuery(sql)
			        .addEntity((Class) Prescription.class);
			query.setParameter("p_id", (Object) patient.getPatientId());
			items = query.list();
			System.out.println("size " + items.size());
			return items;
		}
		catch (DAOException d) {
			d.printStackTrace();
			return items;
		}
	}
	
	public List<Prescription> getAllPrescriptionsByFile(String fileName) throws DAOException {
		List items = new ArrayList<Prescription>();
		
		try {
			String sql = "SELECT prescription_id,uuid,patient_id,drug_id,description,dose,advice,prescription_date_created,prescription_file from prescription p where p.prescription_file = :p_file ";
			sql = sql + " AND prescription_file IS NOT NULL order by prescription_id ASC";
			
			SQLQuery query = this.sessionFactory.getCurrentSession().createSQLQuery(sql)
			        .addEntity((Class) Prescription.class);
			query.setParameter("p_file", fileName);
			
			if (query.list().size() > 0)
				items = query.list();
			System.out.println("size " + items.size());
			
			return items;
		}
		catch (DAOException d) {
			d.printStackTrace();
			return items;
		}
	}
	
	public List<String> getAllPrescriptionsGrouped(Patient patient) throws DAOException {
		List items = new ArrayList<String>();
		
		try {
			String sql = "SELECT prescription_file from prescription p where p.patient_id = :p_id ";
			sql = sql
			        + " AND prescription_file IS NOT NULL group by prescription_file order by prescription_date_created DESC";
			
			SQLQuery query = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			query.setParameter("p_id", (Object) patient.getPatientId());
			List<Object> objects = query.list();
			
			for (int i = 0; i < objects.size(); i++) {
				items.add((String) objects.get(i));
			}
			
			System.out.println("size " + items.size());
			return items;
		}
		catch (DAOException d) {
			d.printStackTrace();
			return items;
		}
		
	}
	
	public boolean getPrescriptionFileInUse(String fileName) {
		
		// default true, then, the file is not deleted - must be checked manually, if so
		
		try {
			String sql = "SELECT count(*) prescription_file from prescription p where p.prescription_file = :p_file ";
			sql = sql + " AND prescription_file IS NOT NULL";
			
			SQLQuery query = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			
			query.setParameter("p_file", fileName);
			
			int count = ((Number) query.uniqueResult()).intValue();
			
			return (count > 0);
		}
		catch (DAOException d) {
			d.printStackTrace();
		}
		
		return true;
	}
	
}
