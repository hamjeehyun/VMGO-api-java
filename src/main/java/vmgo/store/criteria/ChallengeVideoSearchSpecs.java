package vmgo.store.criteria;

import org.springframework.data.jpa.domain.Specification;
import vmgo.store.entity.Challenge;
import vmgo.store.entity.ChallengeVideo;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChallengeVideoSearchSpecs {
    public static Specification<ChallengeVideo> searchWith(Map<ChallengeVideoSearchKeys, Object> searchKeyword) {
        return (Specification<ChallengeVideo>) ((root, query, builder) -> {
            List<Predicate> predicate = getPredicateWithKeyword(searchKeyword, root, builder);

            query.orderBy(builder.asc(root.get("video")));
            return builder.and(predicate.toArray(new Predicate[0]));
        });
    }
    public static Specification<ChallengeVideo> sumWith() {
        return (Specification<ChallengeVideo>) ((root, query, builder) -> {
            query
                    .select(root.get("videoId"))
                    .groupBy(root.get("challengeId"));

            return query.getGroupRestriction();
        });
    }

    private static List<Predicate> getPredicateWithKeyword(Map<ChallengeVideoSearchKeys, Object> searchKeyword, Root<ChallengeVideo> root, CriteriaBuilder builder) {
        List<Predicate> predicate = new ArrayList<>();
        for (ChallengeVideoSearchKeys key : searchKeyword.keySet()) {
            switch (key) {
                case CHALLENGE:
                    predicate.add(builder.equal(
                            root.get(key.getValue()),searchKeyword.get(key)
                    ));
                    break;
            }
        }
        return predicate;
    }
}
