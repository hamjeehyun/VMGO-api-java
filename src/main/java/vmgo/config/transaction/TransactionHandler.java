package vmgo.config.transaction;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TransactionHandler {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void runInTransaction(Action action) {
        action.act();
    }

    public interface Action {
        void act();
    }
}