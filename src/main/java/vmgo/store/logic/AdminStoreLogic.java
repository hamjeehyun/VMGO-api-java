package vmgo.store.logic;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.StoredProcedureQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import vmgo.store.AdminStore;
import vmgo.store.repository.SeqNumberRepository;

@Repository
public class AdminStoreLogic implements AdminStore {
	private final static Logger LOGGER = LoggerFactory.getLogger(AdminStoreLogic.class);
	
	@Autowired
	private SeqNumberRepository seqNumRepository;
	
	@Autowired
	private EntityManager em;

	@Override
	@Transactional(readOnly=true)
	public String getGeneratedKey(String tableName) {
		StoredProcedureQuery spq = 
				em.createNamedStoredProcedureQuery("get_generatedKey");
		spq.setParameter("tableName", tableName);
		spq.execute();
		String result = String.valueOf(spq.getOutputParameterValue("genaratedKey"));
		return result;
	}

}
