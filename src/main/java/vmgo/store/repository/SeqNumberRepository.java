package vmgo.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import vmgo.store.entity.SeqNumber;

@Repository
public interface SeqNumberRepository extends JpaRepository<SeqNumber, String>, JpaSpecificationExecutor<SeqNumber> {
	
}
