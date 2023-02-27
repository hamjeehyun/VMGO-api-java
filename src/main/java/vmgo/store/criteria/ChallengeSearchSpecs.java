package vmgo.store.criteria;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import vmgo.store.entity.Challenge;
import vmgo.store.logic.ChallengeStoreLogic;
import vmgo.util.JsonUtil;
import vmgo.util.StringListConverter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.boot.context.properties.bind.Bindable.listOf;

public class ChallengeSearchSpecs {
    private final static Logger LOGGER = LoggerFactory.getLogger(ChallengeSearchSpecs.class);
    private static StringListConverter stringListConverter;

    public static Specification<Challenge> searchWith(Map<ChallengeSearchKeys, Object> searchKeyword) {
        return (Specification<Challenge>) ((root, query, builder) -> {
            List<Predicate> predicate = null;
            try {
                predicate = getPredicateWithKeyword(searchKeyword, root, builder);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            query.orderBy(builder.desc(root.get("created")));
            return builder.and(predicate.toArray(new Predicate[0]));
        });
    }

    private static List<Predicate> getPredicateWithKeyword(Map<ChallengeSearchKeys, Object> searchKeyword, Root<Challenge> root, CriteriaBuilder builder) throws JsonProcessingException {
        List<Predicate> predicate = new ArrayList<>();
        for (ChallengeSearchKeys key : searchKeyword.keySet()) {
            switch (key) {
                case TAGS:
                    predicate.add(builder.and(builder.equal(
                            builder.function(
                                    "JSON_CONTAINS",
                                    String.class,
                                    root.get(key.getValue()),
                                    builder.literal(JsonUtil.toJson(searchKeyword.get(key)))
                                    ), true)
                            )
                    );
                    break;
                case FEATURED:
                case ACTIVE:
                    predicate.add(builder.equal(
                            root.get(key.getValue()),searchKeyword.get(key)
                    ));
                    break;
            }
        }

        return predicate;
    }
}
